package com.foogui.rpcprovider.service;

import com.foogui.rpcprovider.handler.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
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
public class RpcServer implements DisposableBean {

    /**
     * 负责处理TCP/IP连接
     */
    private NioEventLoopGroup bossGroup;

    /**
     * 负责处理Channel（通道）的I/O事件
     */
    private NioEventLoopGroup workerGroup;

    @Autowired
    private RpcServerHandler rpcServerHandler;

    public void startRpcServer(String ip, String port) {
        bossGroup=new NioEventLoopGroup(1);
        workerGroup=new NioEventLoopGroup(0);
        //创建服务端启动助手
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
    }

    @Override
    public void destroy() throws Exception {

    }
}
