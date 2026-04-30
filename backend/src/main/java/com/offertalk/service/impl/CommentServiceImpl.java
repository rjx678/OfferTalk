package com.offertalk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offertalk.common.Constants;
import com.offertalk.entity.Comment;
import com.offertalk.mapper.CommentMapper;
import com.offertalk.service.CommentService;
import com.offertalk.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Override
    public Page<Comment> getCommentPage(Long contentId, Integer contentType, Integer page, Integer size) {
        Page<Comment> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getContentId, contentId)
                .eq(Comment::getContentType, contentType)
                .eq(Comment::getDeleteFlag, Constants.DELETED_NO)
                .orderByAsc(Comment::getCreateTime);
        return commentMapper.selectPage(pageParam, wrapper);
    }

    @Override
    @Transactional
    public Comment createComment(Comment comment) {
        if (sensitiveWordService.shouldBlock(comment.getContent())) {
            throw new com.offertalk.common.BusinessException("评论内容包含敏感内容，请修改后重试");
        }
        comment.setContent(sensitiveWordService.filterSensitiveWords(comment.getContent()));
        comment.setAuditStatus(Constants.AUDIT_STATUS_PASS);
        comment.setLikeCount(0);
        comment.setReportCount(0);
        comment.setDeleteFlag(Constants.DELETED_NO);

        if (comment.getParentId() != null && comment.getParentId() > 0) {
            Comment parent = commentMapper.selectById(comment.getParentId());
            if (parent != null) {
                comment.setRootId(parent.getRootId() != null ? parent.getRootId() : parent.getId());
            }
        }

        commentMapper.insert(comment);
        return comment;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new com.offertalk.common.BusinessException("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new com.offertalk.common.BusinessException("无权删除该评论");
        }
        comment.setDeleteFlag(Constants.DELETED_YES);
        commentMapper.updateById(comment);
    }

    @Override
    public void updateLikeCount(Long commentId, Integer delta) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment != null) {
            comment.setLikeCount(comment.getLikeCount() + delta);
            commentMapper.updateById(comment);
        }
    }
}
