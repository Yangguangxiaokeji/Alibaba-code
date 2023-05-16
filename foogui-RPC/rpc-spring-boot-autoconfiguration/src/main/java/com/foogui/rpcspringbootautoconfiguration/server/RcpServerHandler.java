package com.foogui.rpcspringbootautoconfiguration.server;

import cn.hutool.json.JSONUtil;
import com.foogui.rpcspringbootautoconfiguration.constant.ConstantPool;
import com.foogui.rpcspringbootautoconfiguration.model.RpcRequest;
import com.foogui.rpcspringbootautoconfiguration.model.RpcResponse;
import com.foogui.rpcspringbootautoconfiguration.utils.ClassUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

@Slf4j
public class RcpServerHandler extends SimpleChannelInboundHandler<String> {
    private ApplicationContext applicationContext;

    public RcpServerHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("msg is" + msg);
        RpcRequest requestBean = JSONUtil.toBean(msg, RpcRequest.class);
        // 心跳逻辑
        // 如果接受到的是客户端心跳处理器IdleStateTriggerHandler发来的心跳检测请求
        if (ConstantPool.HEART_BEAT.equals(requestBean.getRpcRequestId())) {
            System.out.println("服务端接收到心跳");
            RpcResponse rpcResponse = RpcResponse.createResponseOfHeartBeat();
            String s = JSONUtil.toJsonStr(rpcResponse);
            // 返回给Client  response，客户端处理器对响应的id进行判断响应的用途
            ctx.channel().writeAndFlush(s);
            return;
        }
        // Client正常的请求
        try {
            String[] parameterTypeStrings = requestBean.getParameterTypeStrings();
            Class<?>[] parameterTypes = ClassUtils.string2Class(parameterTypeStrings);
            requestBean.setParameterTypes(parameterTypes);

            String className = requestBean.getClassName();
            Class<?> aClass = Class.forName(className);
            // 从容器中拿到接口的代理对象，那个这个代理对象是谁？
            // 其实就是RcpServiceProcessor在扩展点创建并注入的接口的代理对象
            // 容器中管理的代理对应有多个，拿的是谁？本类中的还是其他类的
            Object bean = applicationContext.getBean(aClass);
            Method method =aClass.getMethod(requestBean.getMethodName(), requestBean.getParameterTypes());
            // 执行代理对象的方法
            Object invoke = method.invoke(bean, requestBean.getParameters());

            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setRpcRequestId(requestBean.getRpcRequestId());
            rpcResponse.setReturnValue(invoke);

            String s = JSONUtil.toJsonStr(rpcResponse);

            ctx.channel().writeAndFlush(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("active");
        super.channelRegistered(ctx);
    }
}
