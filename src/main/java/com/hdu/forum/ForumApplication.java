package com.hdu.forum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hdu.forum.mapper")
public class ForumApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
        System.out.println("====================================");
        System.out.println("HDU论坛系统启动成功！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("API接口: http://localhost:8080/api");
        System.out.println("====================================");
    }
}
