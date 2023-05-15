package com.foogui.rpcprovider.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * rpc服务器
 *
 * @author Foogui
 * @date 2023/05/13
 */
@Service
@Slf4j
public class RpcServer implements DisposableBean {

    /**
     * 负责处理TCP/IP连接
     */
    private NioEventLoopGroup bossGroup;

    /**
     * 负责处理Channel（通道）的I/O请求，具体干活的
     */
    private NioEventLoopGroup workerGroup;

    @Autowired
    private RpcServerHandler rpcServerHandler;

    public void startRpcServer(String ip, int port) {
        try {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup(0);
            // 创建服务端启动助手
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            // 添加String的编解码器
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(rpcServerHandler);
                        }
                    });
            // 异步变同步，确保端口绑定成功再继续执行
            ChannelFuture channelFuture = bootstrap.bind(ip, port).sync();
            log.info("服务启动成功");
            // closeFuture关闭动作阻塞，直到被唤醒才执行
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            destroy();
        }
    }

    @Override
    public void destroy() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }
}
