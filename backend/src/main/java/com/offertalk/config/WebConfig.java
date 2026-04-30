package com.offertalk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UploadConfig uploadConfig;
    
    private String absoluteUploadPath;

    @PostConstruct
    public void init() {
        // 获取绝对路径
        File uploadDir = new File(uploadConfig.getPath());
        this.absoluteUploadPath = uploadDir.getAbsolutePath();
        // 确保目录存在
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置文件上传路径的静态资源访问
        // 使用绝对路径确保正确映射
        registry.addResourceHandler(uploadConfig.getUrlPrefix() + "**")
                .addResourceLocations("file:" + absoluteUploadPath + "/");
    }
}