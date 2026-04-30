package com.offertalk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.path:./uploads/}")
    private String uploadPath;
    
    @Value("${app.upload.url-prefix:/api/uploads/}")
    private String urlPrefix;
    
    private String absoluteUploadPath;

    @PostConstruct
    public void init() {
        File uploadDir = new File(uploadPath);
        this.absoluteUploadPath = uploadDir.getAbsolutePath();
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        System.out.println("========== 文件上传配置 ==========");
        System.out.println("上传路径配置: " + uploadPath);
        System.out.println("上传路径绝对路径: " + absoluteUploadPath);
        System.out.println("URL前缀: " + urlPrefix);
        System.out.println("===================================");
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
        String resourcePath = "file:" + absoluteUploadPath + File.separator;
        
        System.out.println("========== 静态资源映射配置 ==========");
        System.out.println("请求路径: " + urlPrefix + "**");
        System.out.println("文件路径: " + resourcePath);
        System.out.println("===================================");
        
        registry.addResourceHandler(urlPrefix + "**")
                .addResourceLocations(resourcePath)
                .setCachePeriod(3600);
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourcePath)
                .setCachePeriod(3600);
    }
}
