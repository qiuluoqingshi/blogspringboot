package com.qlqs.blogspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qlqs.blogspringboot.dao")
public class BlogspringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogspringbootApplication.class, args);
    }

}
