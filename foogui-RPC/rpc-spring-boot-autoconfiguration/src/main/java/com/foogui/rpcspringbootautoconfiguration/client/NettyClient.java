package com.foogui.rpcspringbootautoconfiguration.client;

import cn.hutool.json.JSONUtil;
import com.foogui.rpcspringbootautoconfiguration.handler.IdleStateTriggerHandler;
import com.foogui.rpcspringbootautoconfiguration.handler.RpcClientHandler;
import com.foogui.rpcspringbootautoconfiguration.model.RpcFuture;
import com.foogui.rpcspringbootautoconfiguration.model.RpcFuturePool;
import com.foogui.rpcspringbootautoconfiguration.model.RpcRequest;
import com.foogui.rpcspringbootautoconfiguration.model.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 具体的微服务实例
 *
 * @author Foogui
 * @date 2023/05/15
 */
public class NettyClient {

    private final EventLoopGroup group = new NioEventLoopGroup();
    private ChannelFuture channelFuture = null;

    private final String ip;
    private final Integer port;

    public NettyClient(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 异步启动 Netty 客户端
     */
    public void startAsync() {
        new Thread(this::runClient).start();
    }

    public void runClient() {
        try {
            //1. 创建客户端启动助手
            Bootstrap bootstrap = new Bootstrap();
            //2. 设置线程组,线程组对于该客户端负责处理所有的event
            bootstrap.group(group)
            //3.设置参数
                    .channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                            socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                            // 添加客户端对应的处理器，当有相应时由其进行处理
                            socketChannel.pipeline().addLast(new RpcClientHandler());
                            // 添加 Netty 自带心跳机制，如果连接空闲状态的时间超过10秒就会触发对应的处理器
                            socketChannel.pipeline().addLast(new IdleStateHandler(10, 10, 10, TimeUnit.SECONDS));
                            // 自定义处理器，心跳机制触发时会调用IdleStateTriggerHandler的userEventTriggered方法
                            socketChannel.pipeline().addLast(new IdleStateTriggerHandler());
                        }
                    });
            //4. 启动客户端,等待连接服务端,同时将异步改为同步
            channelFuture = bootstrap.connect(ip, port).sync();
            //5. 关闭通道和关闭连接池，closeFuture会导致阻塞在这，将来channel关闭时继续执行
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     * 客户端发送消息。返回响应
     *
     * @param msg 味精
     * @return {@link RpcResponse}
     */
    public RpcResponse sendMessage(RpcRequest msg) {
        // 存起来
        RpcFuture<RpcResponse> future = new RpcFuture<>();
        RpcFuturePool.put(msg.getRpcRequestId(), future);
        RpcResponse rpcResponse = null;
        try {
            String s = JSONUtil.toJsonStr(msg);
            // 通过Future获得通道，然后向服务端写json
            channelFuture.channel().writeAndFlush(s);
            // 获取响应结果，这里会阻塞住，直到服务端返回了响应
            rpcResponse = future.get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RpcFuturePool.remove(msg.getRpcRequestId());
        }
        return rpcResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NettyClient that = (NettyClient) o;
        return Objects.equals(ip, that.ip) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
