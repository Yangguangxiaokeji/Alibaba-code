package com.foogui.fooguiwebchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ChatController {
    @GetMapping("/index.html")
    public String chat() {
        return "chat";
    }
    @GetMapping("/")
    public String index() {
        return "chat";
    }
}
