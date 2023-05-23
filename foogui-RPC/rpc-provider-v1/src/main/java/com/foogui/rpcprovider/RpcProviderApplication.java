package com.foogui.rpcprovider;

import com.foogui.rpcprovider.handler.RpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcProviderApplication implements CommandLineRunner {

    @Autowired
    private RpcServer rpcServer;

    public static void main(String[] args) {
        SpringApplication.run(RpcProviderApplication.class, args);
    }

    /**
     * CommandLineRunner本身就一个异步线程执行的任务
     *
     * @param args arg游戏
     * @throws Exception 异常
     */
    @Override
    public void run(String... args) throws Exception {
        // 开启异步线程去连接，避免因为连不上导致boot启动失败
        new Thread(()->{
            rpcServer.startRpcServer("127.0.0.1", 8899);
        }).start();
    }
}
