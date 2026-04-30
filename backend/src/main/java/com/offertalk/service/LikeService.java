package com.offertalk.service;

public interface LikeService {
    boolean toggleLike(Long userId, Integer contentType, Long contentId);
    boolean hasLiked(Long userId, Integer contentType, Long contentId);
}
