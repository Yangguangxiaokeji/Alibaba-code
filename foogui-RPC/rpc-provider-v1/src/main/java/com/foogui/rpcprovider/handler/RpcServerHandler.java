package com.foogui.rpcprovider.handler;

import com.alibaba.fastjson.JSON;
import com.foogui.rpcapi.dto.RpcRequest;
import com.foogui.rpcapi.dto.RpcResponse;
import com.foogui.rpcprovider.annotation.RpcService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * rpc服务处理器
 * 真正去处理请求的地方
 *
 * @author Foogui
 * @date 2023/05/13
 */
@Slf4j
@Component
// 表示该ChannelHandler实现类可以被共享使用，即可以被多个Channel共用，而不必每次都创建一个新的实例。这样可以节省资源，提高性能
@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<String> implements ApplicationContextAware {

    /**
     * 缓存服务实现
     */
    private static final Map<String, Object> SERVICE_INSTANCE_MAP = new ConcurrentHashMap<>();

    // 在Netty应用程序中，通常会使用ChannelHandlerContext来与ChannelPipeline进行交互，进行消息的出站
    // 而使用SimpleChannelInboundHandler来处理入站消息。
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取客户端发来的消息
        RpcRequest request = JSON.parseObject(msg, RpcRequest.class);
        RpcResponse response = new RpcResponse();
        response.setRpcRequestId(request.getRpcRequestId());
        try {
            response.setReturnValue(handle(request));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 将response转成Json返回给消费端
        ctx.writeAndFlush(JSON.toJSONString(response));
    }

    /**
     * 处理请求，从缓存中拿到对应的实现类，并执行相关逻辑返回结果
     *
     * @param request 请求
     * @return {@link Object}
     */
    private Object handle(RpcRequest request) throws InvocationTargetException {

        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        String interfaceClassName = request.getInterfaceClassName();
        // 缓存是以实现的第一个接口为key的
        Object bean = SERVICE_INSTANCE_MAP.get(interfaceClassName);
        if (bean == null) {
            throw new RuntimeException("根据beanName找不到服务,beanName:" + request.getInterfaceClassName());
        }

        // 反射执行bean的目标方法- CGLIB反射调用
        // 在这个项目里，这个bean就是UserServiceImpl
        Class<?> beanClass = bean.getClass();
        // Method method = beanClass.getMethod(methodName, parameterTypes);

        // FastClass是一个关键类，用来创建Class对象，利用索引避免了反射调用带来的性能损失。在Spring中主要用于实现AOP功能
        // FastClass只能用于调用公共方法，不能用于调用私有方法
        // 在需要频繁调用某个公共方法时使用FastClass来提高程序的性能
        FastClass fastClass = FastClass.create(beanClass);
        FastMethod method = fastClass.getMethod(methodName, parameterTypes);

        return method.invoke(bean, parameters);
    }

    /**
     * 将服务端所有标记了@RpcService的Bean提前放入缓存存起来
     *
     * @param applicationContext 应用程序上下文
     * @throws BeansException 豆子例外
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);
        beans.forEach((k, v) -> {
            // 确保加@RpcService的Bean都是某个接口的实现类
            if (v.getClass().getInterfaces().length == 0) {
                throw new RuntimeException("服务必须实现接口才能对外服务，否则不应该加@RpcService注解");
            }
            // 默认取服务Bean实现的第一个接口作为缓存的key
            SERVICE_INSTANCE_MAP.put(v.getClass().getInterfaces()[0].getName(), v);
        });
    }
}
