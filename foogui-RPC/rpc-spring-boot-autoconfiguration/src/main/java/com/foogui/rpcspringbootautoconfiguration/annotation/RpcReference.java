package com.foogui.rpcspringbootautoconfiguration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用于注入远程服务，实际注入的是远程服务接口的代理类
 */
@Target(ElementType.FIELD) // 方法注解
@Retention(RetentionPolicy.RUNTIME) // 运行时注解
public @interface RpcReference {
    /**
     * 具体引入的服务名
     * @return {@link String}
     */
    String value();
}

