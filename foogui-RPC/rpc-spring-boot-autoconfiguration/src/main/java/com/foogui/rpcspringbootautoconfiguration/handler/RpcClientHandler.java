package com.foogui.rpcspringbootautoconfiguration.handler;

import cn.hutool.json.JSONUtil;
import com.foogui.rpcspringbootautoconfiguration.constant.ConstantPool;
import com.foogui.rpcspringbootautoconfiguration.model.RpcFuture;
import com.foogui.rpcspringbootautoconfiguration.model.RpcFuturePool;
import com.foogui.rpcspringbootautoconfiguration.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        System.out.println("resp:" + msg);

        RpcResponse rpcResponse = JSONUtil.toBean(msg, RpcResponse.class);

        if (ConstantPool.HEART_BEAT.equals(rpcResponse.getRpcRequestId())) {
            System.out.println("客户端接收到心跳");
            return;
        }
        rpcResponse.getRpcRequestId();
        RpcFuture<RpcResponse> future = RpcFuturePool.get(rpcResponse.getRpcRequestId());

        future.setResponse(rpcResponse);
    }
}
