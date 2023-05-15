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
        if (ConstantPool.HEART_BEAT.equals(requestBean.getRpcRequestId())) {
            System.out.println("服务端接收到心跳");
            RpcResponse rpcResponse = RpcResponse.createResponseOfHeartBeat();
            String s = JSONUtil.toJsonStr(rpcResponse);
            ctx.channel().writeAndFlush(s);
            return;
        }

        try {
            String[] parameterTypeStrings = requestBean.getParameterTypeStrings();
            Class<?>[] parameterTypes = ClassUtils.string2Class(parameterTypeStrings);
            requestBean.setParameterTypes(parameterTypes);

            String className = requestBean.getClassName();
            Class<?> aClass = Class.forName(className);
            Object bean = applicationContext.getBean(aClass);
            Method method =
                    aClass.getMethod(requestBean.getMethodName(), requestBean.getParameterTypes());

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
