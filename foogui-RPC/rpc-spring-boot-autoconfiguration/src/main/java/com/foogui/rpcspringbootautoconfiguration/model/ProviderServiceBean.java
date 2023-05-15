package com.foogui.rpcspringbootautoconfiguration.model;

import lombok.Data;

@Data
public class ProviderServiceBean {
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务地址，格式：ip:port
     */
    private String address;
    /**
     * 权重，越大优先级越高
     */
    private Integer weight;
}
