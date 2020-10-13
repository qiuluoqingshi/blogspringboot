package com.qlqs.blogspringboot.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取文件的真实路径
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\";
        registry.addResourceHandler("/imageUpload/**").addResourceLocations("file:" + path + SystemConfig.uploadPath);
        registry.addResourceHandler("/userAvatarUpload/**").addResourceLocations("file:" + path + SystemConfig.avatarUploadPath);
    }
}