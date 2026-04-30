package com.offertalk.service;

public interface CollectService {
    boolean toggleCollect(Long userId, Integer contentType, Long contentId);
    boolean hasCollected(Long userId, Integer contentType, Long contentId);
}
