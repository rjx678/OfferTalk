package com.offertalk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offertalk.entity.Comment;

public interface CommentService {
    Page<Comment> getCommentPage(Long contentId, Integer contentType, Integer page, Integer size);
    Comment createComment(Comment comment);
    void deleteComment(Long commentId, Long userId);
    void updateLikeCount(Long commentId, Integer delta);
}
