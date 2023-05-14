package com.foogui.rpcspringbootautoconfiguration.model;

import java.util.HashMap;
import java.util.Map;

public class RpcFuturePool {
    /**
     * 异步结果暂存
     */
    public static final Map<String, RpcFuture<RpcResponse>> POOL = new HashMap<>();

    public static void put(String key, RpcFuture<RpcResponse> future) {
        POOL.put(key, future);
    }

    public static RpcFuture<RpcResponse> get(String key) {
        return POOL.get(key);
    }

    public static void remove(String key) {
        POOL.remove(key);
    }
}
