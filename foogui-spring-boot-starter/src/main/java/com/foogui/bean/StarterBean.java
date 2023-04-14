package com.foogui.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;



@EnableConfigurationProperties(StarterBean.class)
@ConfigurationProperties(prefix = "starter.bean")
@Data
public class StarterBean {
    private int id;
    private String name;
}
