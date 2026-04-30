package com.offertalk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offertalk.common.ApiResponse;
import com.offertalk.common.Constants;
import com.offertalk.common.PageResult;
import com.offertalk.entity.Comment;
import com.offertalk.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {

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

    @PostMapping("/delete/{id}")
    public ApiResponse<String> deleteComment(
            @PathVariable Long id,
            @RequestParam Long userId) {
        commentService.deleteComment(id, userId);
        return ApiResponse.success("删除成功");
    }
}
