package com.foogui.rpcspringbootautoconfiguration.annotation;

import com.alibaba.fastjson.JSON;
import com.foogui.rpcspringbootautoconfiguration.client.NettyClient;
import com.foogui.rpcspringbootautoconfiguration.client.NettyClientBizGroup;
import com.foogui.rpcspringbootautoconfiguration.client.NettyClientGroup;
import com.foogui.rpcspringbootautoconfiguration.model.RpcRequest;
import com.foogui.rpcspringbootautoconfiguration.model.RpcResponse;
import com.foogui.rpcspringbootautoconfiguration.utils.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.UUID;

/**
 * rpc服务代理生成类
 *
 * @author Foogui
 * @date 2023/05/16
 */
public class RpcServiceProxy<T> implements InvocationHandler {

    // 这里可以维护一个缓存，存这个接口的方法抽象的对象
    private Class<T> proxyInterface;

    private NettyClientGroup nettyClientGroup;

    private String serviceName;

    /**
     * rpc服务代理
     *
     * @param proxyInterface 代理接口
     * @param serviceName    服务名称
     * @param nettyClient    Client总组
     */
    public RpcServiceProxy(Class<T> proxyInterface, String serviceName, NettyClientGroup nettyClient) {
        this.serviceName = serviceName;
        this.proxyInterface = proxyInterface;
        this.nettyClientGroup = nettyClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke");

        Map<String, NettyClientBizGroup> providers = nettyClientGroup.getProviders();

        NettyClientBizGroup nettyClientBizGroup = providers.get(serviceName);

        if (null == nettyClientBizGroup) {
            RpcResponse response = RpcResponse.createResponseOfNoService();
            return response.getReturnValue();
        }

        // 每次调用方法都会调用next访问集群中的下一个服务
        NettyClient nettyClient = nettyClientBizGroup.next();

        if (null == nettyClient) {
            RpcResponse response = RpcResponse.createResponseOfNoService();
            return response.getReturnValue();
        }

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRpcRequestId(UUID.randomUUID().toString().substring(0, 8));
        // 设置服务名称
        rpcRequest.setServiceName(serviceName);
        // 设置是哪个类
        rpcRequest.setClassName(proxyInterface.getName());
        // 设置哪个方法
        rpcRequest.setMethodName(method.getName());
        // 设置参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterTypeString = ClassUtils.class2String(parameterTypes);
        rpcRequest.setParameterTypeStrings(parameterTypeString);

        // 设置参数
        rpcRequest.setParameters(args);

        // 发送消息
        RpcResponse response = nettyClient.sendMessage(rpcRequest);

        if (response == null) {
            response = RpcResponse.createResponseOfTimeout(rpcRequest.getRpcRequestId());
        }

        // return JSONUtil.toBean(response.getReturnValue().toString(), method.getReturnType());
        return JSON.parseObject(response.getReturnValue().toString(), method.getReturnType());
    }

    /**
     * 获得代理对象
     *
     * @return {@link T}
     */
    public T getProxy() {
        return (T) Proxy.newProxyInstance(proxyInterface.getClassLoader(), new Class[]{proxyInterface}, this);
    }
}
