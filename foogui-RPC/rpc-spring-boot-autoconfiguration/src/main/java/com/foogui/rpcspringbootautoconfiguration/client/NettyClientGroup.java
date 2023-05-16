package com.foogui.rpcspringbootautoconfiguration.client;

import com.foogui.rpcspringbootautoconfiguration.config.RpcClientProperties;
import com.foogui.rpcspringbootautoconfiguration.config.RpcProperties;
import com.foogui.rpcspringbootautoconfiguration.utils.ZKServer;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户端组
 * NettyClientGroup 含 NettyClientBizGroup，NettyClientBizGroup 含 NettyClient
 * @author Foogui
 * @date 2023/05/15
 */
@Data
public class NettyClientGroup implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private ZKServer zkServer;
    /**
     * client属性
     */
    private RpcClientProperties rpcClientProperties;
    /**
     * 全局属性
     */
    private RpcProperties rpcProperties;

    private boolean hasInit = false;

    /**
     * 服务消费者名称
     */
    private String consumerName;
    /**
     * key = 某一类业务的服务提供者名称
     * value = 某一类业务的服务提供者集群
     */
    Map<String, NettyClientBizGroup> providers;

    @PostConstruct
    public void postConstruct() throws Exception {
        // 连接NettyServer
        connectProviders();
        System.out.println("_______________________________________________________________");
        System.out.println(providers);
    }

    /**
     * 更新服务
     */
    public void refreshProviders() throws Exception {
        connectProviders();
        System.out.println("_____________________________refreshProviders");
        System.out.println(providers);
    }

    /**
     * 连接NettyServer
     */
    private void connectProviders() throws Exception {
        // 只有当成员变量初始化完毕后才可以继续执行，避免值还没设置就去使用而导致的NPE
        // 实际上，ApplicationContextAware接口的回调方法会先于@PostConstruct注解的方法执行。
        if (!hasInit) {
            return;
        }
        // 确保更新服务时，旧的服务都被剔除了
        providers = new HashMap<>();
        // providerPath 是 "/rpc/provider"
        String providerPath = rpcProperties.getPath() + rpcProperties.getProviderPath();
        // 获取服务提供者们的路径，一类一类的服务，一个服务下可能对应还有很多实例
        // 这里的providerPaths获取的是/rpc/provider下的多个文件夹的名
        List<String> providerPaths = zkServer.getChild(providerPath);

        for (String path : providerPaths) {
            // 获取具体的服务实例的地址，因为存在集群的情况
            // 获取的是服务对应的文件夹下的微服务的名，
            // 例如providerInstancePaths的值可能/rpc/provider/user下的127.0.0.1:8080
            List<String> providerInstancePaths = zkServer.getChild(providerPath + "/" + path);
            // 该服务对应的文件夹没有实例，就遍历下一个服务的文件夹
            if (CollectionUtils.isEmpty(providerInstancePaths)) {
                continue;
            }
            // path对应的是服务名(文件夹名),providerInstancePaths对应是具体的服务的路径(ip:端口)
            // 例如 rpc/provider/user,path对应就是user，user目录下还有很多实例
            NettyClientBizGroup nettyClientBizGroup = new NettyClientBizGroup(path, providerInstancePaths);

            this.providers.put(path, nettyClientBizGroup);
        }
    }

    /**
     * 给容器填充属性
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.zkServer = applicationContext.getBean(ZKServer.class);
        this.rpcClientProperties = applicationContext.getBean(RpcClientProperties.class);
        this.rpcProperties = applicationContext.getBean(RpcProperties.class);
        this.consumerName = rpcClientProperties.getConsumerName();
        this.hasInit = true;
    }
}
