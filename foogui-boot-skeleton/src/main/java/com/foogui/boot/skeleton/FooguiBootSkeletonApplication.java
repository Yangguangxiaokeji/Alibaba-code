package com.foogui.boot.skeleton;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.foogui.boot.skeleton.modules.*.mapper")
public class FooguiBootSkeletonApplication {

    public static void main(String[] args) {
        SpringApplication.run(FooguiBootSkeletonApplication.class, args);
    }

}
