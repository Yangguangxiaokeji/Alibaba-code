package com.foogui.rpcspringbootautoconfiguration.annotation;


import com.foogui.rpcspringbootautoconfiguration.client.NettyClientGroup;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 该 BeanPostProcessor 用于注入远程服务接口代理类
 */
public class RcpServiceProcessor implements InstantiationAwareBeanPostProcessor, ApplicationContextAware {

    private ApplicationContext context;

    // 这是时候的bean已经有属性的了
    // postProcessBeforeInitialization()是每一个Bean创建过程中都会执行
    // 然后是执行@PostConstruct和postProcessAfterInitialization()
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        // 判断类里是否有 @RpcService 注解
        Class<?> clazz = context.getType(beanName);
        if (Objects.isNull(clazz)) {
            return bean;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            // 找出标记了RpcReference注解的属性
            RpcReference injectService = field.getAnnotation(RpcReference.class);
            if (injectService == null) {
                continue;
            }

            // 获取想远程调用的服务名称
            String providerName = injectService.value();
            // 获取接口Class
            Class<?> fieldClass = field.getType();
            // 获取nettyClient
            NettyClientGroup nettyClientGroup = context.getBean(NettyClientGroup.class);
            // 从客户端的总集群中找到对应的微服务名，并创建对应接口的代理类
            RpcServiceProxy rpcProxy = new RpcServiceProxy(fieldClass, providerName, nettyClientGroup);
            // 每次都会创建新的代理对象，那么多个类的成员变量对应的代理对象其实不是一个，
            // 也就是说IOC中存在接口的多个代理对象
            Object proxy = rpcProxy.getProxy();

            // 当我们需要获取私有属性的属性值得时候，我们必须设置Accessible为true，然后才能获取
            field.setAccessible(true);
            try {
                // 通过field.set()重新给带有远程服务接口设置新的属性值，即代理对象
                // 例如 private RemoteUserService service;中的service最后注入的实际是代理对象
                field.set(bean, proxy);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 将bean返回交给ioc容器进行最终的实例化
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public RcpServiceProcessor() {
        System.out.println("-----RcpServiceInjectBeanPostProcessor被IOC接管了-----------");
    }
}
