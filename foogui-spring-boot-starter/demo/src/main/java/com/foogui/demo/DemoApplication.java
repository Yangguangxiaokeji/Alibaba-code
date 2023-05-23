package com.foogui.demo;

import com.foogui.bootstarter.EnableDemoStarter;
import com.foogui.bootstarter.bean.DemoStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDemoStarter
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        System.out.println(context.getBean(DemoStarter.class));
    }

}
