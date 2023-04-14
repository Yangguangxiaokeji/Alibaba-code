package com.foogui.fooguienter;

import com.foogui.EnableStarterBeanRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableStarterBeanRegister
public class FooguiEnterApplication {


    public static void main(String[] args) {
        SpringApplication.run(FooguiEnterApplication.class, args);
    }

}
