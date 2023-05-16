package com.foogui.bootstarter.config;

import com.foogui.bootstarter.bean.DemoStarter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// 默认就会创建DemoStarterProperties的Bean并交给IOC，必须配合ConfigurationProperties使用
@EnableConfigurationProperties(DemoStarterProperties.class)
@ConditionalOnBean(AutoMarker.class)
@ConditionalOnProperty(name = "open", havingValue = "true", matchIfMissing = true)
public class DemoStarterAutoConfiguration {

    static {
        System.out.println("StarterBeanAutoConfiguration init....");
    }


    @Bean
    public DemoStarter demoStarter(DemoStarterProperties demoStarterProperties) {
        // 这种方式的属性注入是常见的
        // 手动将配置类的信息注入到Bean当中，当然也可以直接将@ConfigurationProperties直接配置在DemoStarter上直接注入
        // 之所以这么做，相当于将Bean和配置信息进行了解耦
        DemoStarter starter = new DemoStarter();
        starter.setId(demoStarterProperties.getId());
        starter.setName(demoStarterProperties.getName());
        starter.setList(demoStarterProperties.getList());
        return starter;
    }


}
