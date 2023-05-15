package com.foogui.rpcspringbootautoconfiguration.utils;

import cn.hutool.json.JSONUtil;
import com.foogui.rpcspringbootautoconfiguration.config.RpcProperties;
import com.foogui.rpcspringbootautoconfiguration.model.ProviderServiceBean;
import lombok.Getter;
import lombok.Setter;
import org.apache.zookeeper.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ZKServer implements ApplicationContextAware {

    private RpcProperties rpcProperties;

    private Watcher watcher;

    private String ip;
    private Integer port;
    private String path;
    private String providerPath;
    private String consumerPath;

    ZooKeeper zk;

    @PostConstruct
    public void init() {
        // 连接zookeeper
        initZookeeper();
        // 在zk上创建目录
        createRpcPath();
        // 给provider目录添加监听器
        addWatcher();
    }

    /**
     * 初始化
     */
    private void initZookeeper() {
        this.ip = rpcProperties.getRegisterAddress();
        this.port = rpcProperties.getServerPort();
        this.path = rpcProperties.getPath();
        this.providerPath = rpcProperties.getProviderPath();
        this.consumerPath = rpcProperties.getConsumerPath();
        String url = ip + ":" + port;
        try {
            zk = new ZooKeeper(url, 500000, watcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createRpcPath() {
        try {
            // 创建rpc跟目录
            createPathPermanent(path, "");
            // 创建provider目录
            createPathPermanent(this.path + providerPath, "");
            // 创建consumer目录
            createPathPermanent(this.path + consumerPath, "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addWatcher() {
        try {
            String listenProviderPath = path + providerPath;
            // 启动子节点的创建和删除的监听
            zk.getChildren(listenProviderPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createPathPermanent(String path, String data) throws InterruptedException, KeeperException {
        if (zk.exists(path, true) == null) {
            String mkPath = zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("Success create path: " + mkPath);
            return mkPath;
        } else {
            return null;
        }
    }

    public String createPathTemp(String path, String data) throws Exception {
        if (zk.exists(path, true) == null) {
            String mkPath =
                    zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("Success create path: " + mkPath);
            return mkPath;
        } else {
            return null;
        }
    }

    public List<String> getChild(String path) throws Exception {
        return zk.getChildren(path, true);
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.rpcProperties = applicationContext.getBean(RpcProperties.class);
        this.watcher = applicationContext.getBean(Watcher.class);
    }
    /**
     * 向zk注册服务提供者
     */
    public void sendProviderBeanMsg(ProviderServiceBean providerServiceBean) {

        String s = JSONUtil.toJsonStr(providerServiceBean);
    }

    /**
     * 只要ZkServer中对应的注册信息一致就表示  ==
     *
     * @param o ZkServer
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZKServer zkServer = (ZKServer) o;
        return Objects.equals(rpcProperties, zkServer.rpcProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rpcProperties);
    }
}
