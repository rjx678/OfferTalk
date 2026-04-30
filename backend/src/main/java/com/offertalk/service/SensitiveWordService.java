package com.offertalk.service;

public interface SensitiveWordService {
    String filterSensitiveWords(String content);
    boolean containsSensitiveWords(String content);
    boolean shouldBlock(String content);
}
