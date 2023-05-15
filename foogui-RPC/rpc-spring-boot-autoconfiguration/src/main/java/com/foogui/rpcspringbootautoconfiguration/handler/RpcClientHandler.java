package com.foogui.rpcspringbootautoconfiguration.handler;

import cn.hutool.json.JSONUtil;
import com.foogui.rpcspringbootautoconfiguration.constant.ConstantPool;
import com.foogui.rpcspringbootautoconfiguration.model.RpcFuture;
import com.foogui.rpcspringbootautoconfiguration.model.RpcFuturePool;
import com.foogui.rpcspringbootautoconfiguration.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * rpc客户端处理器
 *
 * @author Foogui
 * @date 2023/05/15
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        System.out.println("resp:" + msg);

        RpcResponse rpcResponse = JSONUtil.toBean(msg, RpcResponse.class);

        // 判断响应是否是验证心跳的响应
        if (ConstantPool.HEART_BEAT.equals(rpcResponse.getRpcRequestId())) {
            System.out.println("客户端接收到心跳");
            return;
        }
        // 从缓存中取出RpcFuture，设置响应值
        RpcFuture<RpcResponse> future = RpcFuturePool.get(rpcResponse.getRpcRequestId());
        future.setResponse(rpcResponse);
    }
}
