package com.foogui.rpcconsumer;

import com.foogui.rpcapi.service.RemoteUserService;
import com.foogui.rpcconsumer.handler.RpcClientProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcConsumerApplication.class, args);
        System.out.println("开始测试了！！");
        RemoteUserService proxy = (RemoteUserService) RpcClientProxy.createProxy(RemoteUserService.class);
        System.out.println(proxy.getById(2));
    }

}
