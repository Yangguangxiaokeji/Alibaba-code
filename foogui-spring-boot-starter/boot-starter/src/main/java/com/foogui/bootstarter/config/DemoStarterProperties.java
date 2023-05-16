package com.foogui.bootstarter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@Data
@ConfigurationProperties(prefix = "starter.demo")
public class DemoStarterProperties {
    private int id;
    private String name;
    private List<String> list;
}
