package com.foogui.rpcconsumer.handler;

import com.alibaba.fastjson.JSON;
import com.foogui.rpcapi.dto.RpcRequest;
import com.foogui.rpcapi.dto.RpcResponse;

import java.lang.reflect.*;
import java.util.Optional;
import java.util.UUID;

public class RpcClientProxy {

    public static Object createProxy(Class<?> remoteServiceClass) {
        return Proxy.newProxyInstance(RpcClientProxy.class.getClassLoader(), new Class[]{remoteServiceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcRequest request = new RpcRequest();
                request.setRpcRequestId(UUID.randomUUID().toString());
                // 返回的是定义该方法的类的名称,其实就是接口的Class名
                request.setClassName(method.getDeclaringClass().getName());
                // request.setClassName(remoteServiceClass.getName());
                request.setMethodName(method.getName());
                request.setParameterTypes(method.getParameterTypes());
                request.setParameters(args);
                RpcClient rpcClient=null;
                try {
                     rpcClient = new RpcClient("127.0.0.1", 8899);
                    // 发送消息
                    Object responseMsg=rpcClient.send(JSON.toJSONString(request));
                    RpcResponse rpcResponse = JSON.parseObject(responseMsg.toString(), RpcResponse.class);
                    Object result = Optional.ofNullable(rpcResponse.getReturnValue()).orElseGet(() -> {
                        try {
                            Constructor<?> constructor = method.getReturnType().getConstructor(Integer.class, String.class);
                            return JSON.toJSONString(constructor.newInstance(args[0],"没有这个id"));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    return JSON.parseObject(result.toString(), method.getReturnType());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    if (rpcClient!=null){
                        rpcClient.destroy();
                    }
                }

            }
        });
    }
}
