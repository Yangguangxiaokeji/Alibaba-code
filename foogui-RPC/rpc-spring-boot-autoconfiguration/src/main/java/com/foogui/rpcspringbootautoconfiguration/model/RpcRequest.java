package com.foogui.rpcspringbootautoconfiguration.model;

import com.foogui.rpcspringbootautoconfiguration.constant.ConstantPool;
import lombok.Data;

import java.io.Serializable;

/**
 * 封装的请求对象
 */
@Data
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 3823843802499968722L;
    /**
     * 请求对象的ID
     */
    private String rpcRequestId;

    /**
     * 请求的服务名
     */
    private String serviceName;

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
     * 参数类型的String数组
     */
    private String[] parameterTypeStrings;
    /**
     * 入参
     */
    private Object[] parameters;

    public static RpcRequest createHeartRequest() {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRpcRequestId(ConstantPool.HEART_BEAT);
        return rpcRequest;
    }
}
