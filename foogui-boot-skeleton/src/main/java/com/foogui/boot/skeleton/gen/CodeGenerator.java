package com.foogui.boot.skeleton.gen;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3308/alibaba-code?characterEncoding=utf8&useSSL=false&serverTimezone=UTC", "root", "root")
                .globalConfig(builder -> {
                    builder.author("Foogui") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                   
                            .outputDir("D:\\Git-Github\\Alibaba-code\\foogui-boot-skeleton\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.foogui.boot.skeleton.modules") // 设置父包名
                            .moduleName("user") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\Git-Github\\Alibaba-code\\foogui-boot-skeleton\\src\\main\\resources\\mapper"));// 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("t_user")   // 设置需要生成的表名

                            .addTablePrefix("t_");  // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine())// 使用Freemarker
                .execute();
    }
}
