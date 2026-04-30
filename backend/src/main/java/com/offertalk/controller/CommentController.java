package com.offertalk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offertalk.common.ApiResponse;
import com.offertalk.common.Constants;
import com.offertalk.common.PageResult;
import com.offertalk.entity.Comment;
import com.offertalk.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    public ApiResponse<PageResult<List<Comment>>> getCommentList(
            @RequestParam Long contentId,
            @RequestParam Integer contentType,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        Page<Comment> pageResult = commentService.getCommentPage(contentId, contentType, page, size);
        return ApiResponse.success(PageResult.of(
                pageResult.getTotal(),
                pageResult.getCurrent(),
                pageResult.getSize(),
                pageResult.getRecords()
        ));
    }

    @PostMapping("/create")
    public ApiResponse<Comment> createComment(@RequestBody Comment comment) {
        if (comment.getUserId() == null) {
            return ApiResponse.badRequest("用户ID不能为空");
        }
        Comment created = commentService.createComment(comment);
        return ApiResponse.success("评论成功", created);
    }

    @PostMapping("/like")
    public ApiResponse<Map<String, Boolean>> likeComment(@RequestBody Map<String, Object> params) {
        try {
            Long commentId = params.containsKey("commentId") ? Long.parseLong(params.get("commentId").toString()) : null;
            Long userId = params.containsKey("userId") ? Long.parseLong(params.get("userId").toString()) : null;

            logger.info("评论点赞请求 - commentId: {}, userId: {}", commentId, userId);

            if (commentId == null) {
                logger.warn("评论点赞参数缺失 - commentId: {}", commentId);
                return ApiResponse.error(400, "评论ID不能为空");
            }

            commentService.updateLikeCount(commentId, 1);

            Map<String, Boolean> result = new HashMap<>();
            result.put("hasLiked", true);
            return ApiResponse.success("点赞成功", result);

        } catch (NumberFormatException e) {
            logger.error("评论点赞参数格式错误", e);
            return ApiResponse.error(400, "参数格式错误");
        } catch (Exception e) {
            logger.error("评论点赞失败", e);
            return ApiResponse.error(500, "点赞失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public ApiResponse<String> deleteComment(
            @PathVariable Long id,
            @RequestParam Long userId) {
        commentService.deleteComment(id, userId);
        return ApiResponse.success("删除成功");
    }
}
