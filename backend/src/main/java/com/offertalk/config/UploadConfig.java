package com.offertalk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.upload")
public class UploadConfig {
    
    /**
     * 文件上传路径
     */
    private String path = "./uploads/";
    
    /**
     * 文件访问URL前缀
     */
    private String urlPrefix = "/uploads/";
    
    /**
     * 允许的文件类型
     */
    private String[] allowedTypes = {"image/jpeg", "image/jpg", "image/png", "image/gif"};
    
    /**
     * 允许的文件扩展名
     */
    private String[] allowedExtensions = {"jpg", "jpeg", "png", "gif"};
    
    /**
     * 最大文件大小 (字节) 默认5MB
     */
    private long maxSize = 5 * 1024 * 1024;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String[] getAllowedTypes() {
        return allowedTypes;
    }

    public void setAllowedTypes(String[] allowedTypes) {
        this.allowedTypes = allowedTypes;
    }

    public String[] getAllowedExtensions() {
        return allowedExtensions;
    }

    public void setAllowedExtensions(String[] allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }
}