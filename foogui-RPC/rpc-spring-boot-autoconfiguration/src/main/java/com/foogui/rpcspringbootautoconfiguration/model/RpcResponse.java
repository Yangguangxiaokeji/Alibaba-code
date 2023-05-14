package com.foogui.rpcspringbootautoconfiguration.model;


import com.foogui.rpcspringbootautoconfiguration.constant.ConstantPool;
import lombok.Data;

import java.io.Serializable;

/**
 * 封装的响应对象
 */
@Data
public class RpcResponse implements Serializable {


    private static final long serialVersionUID = 4507002047348827923L;
    /**
     * 响应的请求的ID
     */
    private String rpcRequestId;

    /**
     * 返回的结果
     */
    private Object returnValue;

    /**
     * 没有服务提供者
     *
     * @return
     */
    public static RpcResponse answerNoService() {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRpcRequestId(ConstantPool.NO_SERVICE);
        rpcResponse.setReturnValue("没有服务提供者");
        return rpcResponse;
    }
    /**
     * 超时TimeOut
     *
     * @return
     */
    public static RpcResponse answerTimeout(String requestId) {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRpcRequestId(requestId);
        rpcResponse.setReturnValue("超时TimeOut");
        return rpcResponse;
    }
    /**
     * 心跳
     *
     * @return
     */
    public static RpcResponse answerHeart() {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRpcRequestId(ConstantPool.HEART_BEAT);
        return rpcResponse;
    }

}
