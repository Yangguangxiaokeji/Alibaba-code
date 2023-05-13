package com.foogui.rpcapi.dto;


import lombok.Data;

/**
 * 封装的响应对象
 */
@Data
public class RpcResponse {

    /**
     * 响应的请求的ID
     */
    private String rpcRequestId;

    /**
     * 返回的结果
     */
    private Object returnValue;

}
