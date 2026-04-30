package com.offertalk.service;

import com.offertalk.config.UploadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileUploadService {

    @Autowired
    private UploadConfig uploadConfig;

    /**
     * 上传用户头像
     */
    public Map<String, String> uploadAvatar(MultipartFile file) throws IOException {
        // 1. 验证文件
        validateFile(file);

        // 2. 生成文件路径和文件名
        String extension = getFileExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString() + "." + extension;
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String relativePath = "avatars/" + datePath;

        // 3. 创建目录
        String fullDirectory = uploadConfig.getPath() + relativePath;
        Path directoryPath = Paths.get(fullDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // 4. 保存文件
        Path filePath = directoryPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        // 5. 返回文件访问URL
        String fileUrl = uploadConfig.getUrlPrefix() + relativePath + "/" + filename;

        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        result.put("filename", filename);
        result.put("relativePath", relativePath + "/" + filename);

        return result;
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) throws IOException {
        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
            throw new IOException("文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > uploadConfig.getMaxSize()) {
            throw new IOException("文件大小不能超过5MB");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        boolean typeAllowed = Arrays.asList(uploadConfig.getAllowedTypes()).contains(contentType);
        if (!typeAllowed) {
            // 检查扩展名
            String extension = getFileExtension(file.getOriginalFilename());
            boolean extensionAllowed = Arrays.asList(uploadConfig.getAllowedExtensions()).contains(extension.toLowerCase());
            if (!extensionAllowed) {
                throw new IOException("仅支持JPG、PNG、GIF格式的图片");
            }
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}