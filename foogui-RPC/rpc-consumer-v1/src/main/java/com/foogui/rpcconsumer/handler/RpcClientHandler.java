package com.foogui.rpcconsumer.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
@ChannelHandler.Sharable
public class RpcClientHandler extends SimpleChannelInboundHandler<String>  {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    /**
     * 上下文和其他handler和pipeline交流
     */
    private ChannelHandlerContext context;

    // 发送的消息
    private String requestMsg;

    // 服务端的消息
    private String responseMsg;

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    /**
     * 通道建立起连接时调的方法
     * @param ctx ctx
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    /**
     * 通道读取服务端返回的信息
     */
    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        responseMsg = msg;
        //唤醒等待的线程
        notify();
    }

    /**
     * 发送消息到服务端
     */

    public synchronized Object send(String msg) throws ExecutionException, InterruptedException {
        executorService.submit(() -> {
            // 消息发送
            context.writeAndFlush(msg);
        });
        //线程等待服务端数据的返回响应
        wait();
        return responseMsg;
    }
}
