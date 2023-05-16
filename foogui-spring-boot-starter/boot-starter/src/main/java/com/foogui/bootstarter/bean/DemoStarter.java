package com.foogui.bootstarter.bean;

import lombok.Data;

import java.util.List;


@Data
public class DemoStarter  {

    private int id;
    private String name;
    private List<String> list;
}
