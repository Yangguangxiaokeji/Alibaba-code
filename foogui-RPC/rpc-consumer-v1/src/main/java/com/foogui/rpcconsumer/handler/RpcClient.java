package com.foogui.rpcconsumer.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.ExecutionException;

public class RpcClient {
    private EventLoopGroup group;

    private Channel channel;

    private String ip;

    private int port;


    private  RpcClientHandler rpcClientHandler=new RpcClientHandler();



    public RpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        initClient();
    }

    /**
     * 初始化客户端-连接Netty服务端
     */
    private void initClient() {
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                // 设置为true时，TCP协议就会定期发送空闲数据包，以保持连接的存活状态,
                // 防止客户端被关闭
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                // 设置连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(rpcClientHandler);
                    }
                });
        // 连接Netty服务端，ChannelFuture包含了连接的结果
        ChannelFuture channelFuture = bootstrap.connect(ip, port);
        // 添加监听器
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                // 连接成功
                channel = future.channel();
                // TODO: 处理业务逻辑
            } else {
                // 连接失败
                destroy();
                // TODO: 处理异常
            }
        });

    }

    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    /**
     * 提供消息发送的方法
     */
    public Object send(String msg) throws ExecutionException, InterruptedException {
        return rpcClientHandler.send(msg);
    }
}
