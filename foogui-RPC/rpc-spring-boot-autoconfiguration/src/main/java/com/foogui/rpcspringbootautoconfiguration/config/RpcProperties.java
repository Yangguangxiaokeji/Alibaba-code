package com.foogui.rpcspringbootautoconfiguration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * rpc全局配置
 *
 * @author Foogui
 * @date 2023/05/16
 */
@ConfigurationProperties(prefix = "rpc")
@Data
public class RpcProperties {
    /**
     * 注册中心地址
     */
    private String registerAddress = "127.0.0.1";

    /**
     * 注册中心端口
     */
    private Integer serverPort = 2181;

    /**
     * 服务提供者保存根地址
     */
    private String path = "/rpc";

    /**
     * 服务消费者保存地址
     */
    private String consumerPath = "/consumer";

    /**
     * 服务提供者保存地址，下一个层级是具体的服务，路径为服务名，再下一层就ip:port的实例
     */
    private String providerPath = "/provider";
}
