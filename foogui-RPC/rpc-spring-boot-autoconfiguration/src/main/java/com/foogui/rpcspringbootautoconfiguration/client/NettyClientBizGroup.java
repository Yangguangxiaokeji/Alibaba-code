package com.foogui.rpcspringbootautoconfiguration.client;


import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务的业务组，包含了 NettyClient
 * 这里需要考虑轮训机制
 * @author Foogui
 * @date 2023/05/15
 */
@Data
public class NettyClientBizGroup {

    /**
     * 下一个下标
     */
    private AtomicInteger index = new AtomicInteger(0);

    /**
     * 对应的服务名
     */
    private String providerName;

    /**
     * 服务提供者列表
     */
    List<NettyClient> providerList = new ArrayList<>();

    /**
     * key = 服务提供者ip:port
     * value = NettyClient
     */
    Map<String, NettyClient> providerMap = new HashMap<>();

    /**
     * 网
     *
     * @param providerName       服务名
     * @param providerStringList 该服务名下对应的服务实例的路径集合(ip:host形式)
     */
    public NettyClientBizGroup(String providerName, List<String> providerStringList) {
        this.providerName = providerName;

        for (String s : providerStringList) {
            System.out.println(s);
            String[] split = s.split(":");
            String ip = split[0];
            Integer port = Integer.parseInt(split[1]);
            // 创建客户端并与服务器建立连接
            NettyClient nettyClient = new NettyClient(ip, port);
            nettyClient.startAsync();

            providerList.add(nettyClient);
            providerMap.put(s, nettyClient);
        }
    }

    /**
     * 获取业务组中下一个客户端，轮询操作
     *
     * @return
     */
    public NettyClient next() {
        if (providerList.size() == 0) {
            return null;
        }
        // 环形队列循环
        return providerList.get(index.getAndIncrement() % providerList.size());
    }
}
