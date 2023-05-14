package com.foogui.rpcconsumerv2.controller;


import com.foogui.rpcapi.service.RemoteUserService;
import com.foogui.rpcspringbootautoconfiguration.annotation.RpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // 自定义注解，其中value为服务提供者名称，类似OpenFeign的使用
    @RpcReference("provider01")
    private RemoteUserService remoteUserService;

    @GetMapping("/index/{id}")
    public Object hello(@PathVariable Integer id) {
        return remoteUserService.getById(id);
    }
}
