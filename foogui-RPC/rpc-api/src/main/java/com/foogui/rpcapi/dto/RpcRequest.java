package com.foogui.rpcapi.dto;

import lombok.Data;

/**
 * 封装的请求对象
 */
@Data
public class RpcRequest {
    /**
     * 请求对象的ID
     */
    private String rpcRequestId;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 入参
     */
    private Object[] parameters;
}