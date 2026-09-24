package com.opc.scaffold;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OPC Java 脚手架启动类
 * 新项目 Clone 后：改包名、artifactId、应用名
 */
@SpringBootApplication
@MapperScan("com.opc.scaffold.mapper")
public class ScaffoldApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScaffoldApplication.class, args);
    }
}
