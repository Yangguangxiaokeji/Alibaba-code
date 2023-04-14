package com.foogui.fooguienter;

import com.foogui.bean.StarterBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FooguiEnterApplicationTests {
    @Autowired
    private StarterBean starterBean;

    @Test
    void contextLoads() {
        System.out.println(starterBean);
    }

}
