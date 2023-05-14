package com.foogui.rpcspringbootautoconfiguration.model;

import lombok.Data;

import java.util.concurrent.*;

@Data
public class RpcFuture<T> implements Future<T> {

    private T response;

    /**
     * 因为请求和响应是一一对应的，所以这里是1
     */
    private final CountDownLatch COUNTDOWNLATCH = new CountDownLatch(1);

    /**
     * Future的请求时间，用于计算Future是否超时
     */
    private final long BEGIN_TIME = System.currentTimeMillis();

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        if (response != null){
            return true;
        }
        return false;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        COUNTDOWNLATCH.await();
        return response;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        // await如果返回true就表示计数到0了，false就表示等待超时了
        if (COUNTDOWNLATCH.await(timeout, unit)) {
            return response;
        }
        return null;
    }
    public void setResponse(T response) {
        this.response = response;
        COUNTDOWNLATCH.countDown();
    }

    public long getBeginTime() {
        return BEGIN_TIME;
    }
}
