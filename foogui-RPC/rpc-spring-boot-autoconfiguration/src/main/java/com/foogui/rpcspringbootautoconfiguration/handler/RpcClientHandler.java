package com.foogui.rpcspringbootautoconfiguration.handler;

import cn.hutool.json.JSONUtil;
import com.foogui.rpcspringbootautoconfiguration.constant.ConstantPool;
import com.foogui.rpcspringbootautoconfiguration.model.RpcFuture;
import com.foogui.rpcspringbootautoconfiguration.model.RpcFuturePool;
import com.foogui.rpcspringbootautoconfiguration.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * rpc客户端响应处理器
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
            System.out.println("客户端接收到心跳,表明客户端还活着");
            return;
        }
        // 从缓存中取出RpcFuture，设置响应值
        // 需要注意消息channelRead0方法既然已经被回调了，就说明服务端已经响应完了
        RpcFuture<RpcResponse> future = RpcFuturePool.get(rpcResponse.getRpcRequestId());
        // 将响应值设置到future，并触发了异步的回调相当于解除了NettyClient调用RpcFuture的get()时发生的阻塞
        future.setResponse(rpcResponse);
    }
}
