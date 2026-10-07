package com.opc.scaffold.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * MyBatis-Plus 代码生成器：连接 MySQL 数据库，根据表生成 entity/mapper/service/controller。
 *
 * 用法：运行前设置环境变量 DB_PASSWORD=<数据库root密码>，然后执行 main。
 * 输出目录：src/main/java（可按需修改）。
 */
public class CodeGenerator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/scaffold"
                + "?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai"
                + "&useSSL=false&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = System.getenv().getOrDefault("DB_PASSWORD", "root123");

        String projectDir = System.getProperty("user.dir");
        String javaDir = projectDir + "/src/main/java";
        String mapperXmlDir = projectDir + "/src/main/resources/mapper";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder
                        .author("OPC")
                        .outputDir(javaDir)
                        .disableOpenDir()
                        .commentDate("yyyy-MM-dd"))
                .packageConfig(builder -> builder
                        .parent("com.opc.scaffold")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .controller("controller")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, mapperXmlDir)))
                .strategyConfig(builder -> builder
                        // 这里指定要生成的表，例如 "user"，多表逗号分隔；也可留空由 addInclude 控制
                        .addInclude("user"))
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
