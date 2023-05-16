package com.foogui.rpcspringbootautoconfiguration.handler;


import cn.hutool.json.JSONUtil;
import com.foogui.rpcspringbootautoconfiguration.model.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 空闲状态触发器处理器
 *
 * @author Foogui
 * @date 2023/05/16
 */
public class IdleStateTriggerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        ctx.channel().writeAndFlush(s);
    }

    // 读写超时并且添加IdleStateHandler时，会触发这个方法
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 获取超时对象，读超时，写超时还是读写超时
            IdleState state = ((IdleStateEvent) evt).state();
            // 如果客户端的连接处于读写空闲，发送心跳消息检查客户端状态
            if (state.equals(IdleState.ALL_IDLE)) {
                System.out.println("客户端发送心跳");
                // 给服务端发送字符为（HeartBeat-req）的心跳请求
                String s = JSONUtil.toJsonStr(RpcRequest.createHeartRequest());
                ctx.channel().writeAndFlush(s);
            }
        }
    }
}
