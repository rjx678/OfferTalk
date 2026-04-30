package com.offertalk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.offertalk.common.ApiResponse;
import com.offertalk.entity.*;
import com.offertalk.mapper.CommentMapper;
import com.offertalk.mapper.LikeRecordMapper;
import com.offertalk.mapper.CollectRecordMapper;
import com.offertalk.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private WeixinService weixinService;

    @Autowired
    private InterviewExperienceService interviewExperienceService;

    @Autowired
    private SalaryDisclosureService salaryDisclosureService;

    @Autowired
    private CompanyReviewService companyReviewService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private CollectRecordMapper collectRecordMapper;

    @Value("${app.upload.path}")
    private String uploadPath;

    @Value("${app.upload.url-prefix}")
    private String urlPrefix;

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String code = params.get("code");
        if (code == null || code.isEmpty()) {
            return ApiResponse.error(400, "缺少code参数");
        }

        try {
            String openid = weixinService.getOpenid(code);
            
            SysUser user = sysUserService.getOrCreateUserByOpenid(openid);
            user.setLastLoginTime(LocalDateTime.now());
            sysUserService.updateById(user);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("openid", user.getOpenid());
            userInfo.put("unionid", user.getUnionid());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("avatarUrl", user.getAvatarUrl());
            userInfo.put("phone", user.getPhone());
            userInfo.put("bio", user.getBio());
            userInfo.put("totalPostCount", user.getTotalPostCount());
            userInfo.put("totalLikeCount", user.getTotalLikeCount());
            userInfo.put("createTime", user.getCreateTime());

            return ApiResponse.success(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(500, "微信登录失败");
        }
    }

    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getUserInfo(@RequestParam Long userId) {
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("openid", user.getOpenid());
        userInfo.put("unionid", user.getUnionid());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("avatarUrl", user.getAvatarUrl());
        userInfo.put("phone", user.getPhone());
        userInfo.put("bio", user.getBio());
        userInfo.put("totalPostCount", user.getTotalPostCount());
        userInfo.put("totalLikeCount", user.getTotalLikeCount());
        userInfo.put("createTime", user.getCreateTime());

        return ApiResponse.success(userInfo);
    }

    @PostMapping("/avatar/upload")
    public ApiResponse<Map<String, Object>> uploadAvatar(@RequestParam Long userId, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.error(400, "请选择要上传的图片");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = java.util.UUID.randomUUID().toString() + extension;

        try {
            java.io.File uploadDir = new java.io.File(uploadPath + "/avatars");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String dateDir = java.time.LocalDate.now().toString().replace("-", "/");
            java.io.File dateDirFile = new java.io.File(uploadDir, dateDir);
            if (!dateDirFile.exists()) {
                dateDirFile.mkdirs();
            }

            java.io.File dest = new java.io.File(dateDirFile, newFilename);
            file.transferTo(dest);

            String avatarUrl = urlPrefix + "/avatars/" + dateDir + "/" + newFilename;

            SysUser user = sysUserService.getById(userId);
            if (user != null) {
                user.setAvatarUrl(avatarUrl);
                sysUserService.updateById(user);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("avatarUrl", avatarUrl);
            return ApiResponse.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.error(500, "上传失败");
        }
    }

    @GetMapping("/posts")
    public ApiResponse<List<Object>> getUserPosts(@RequestParam Long userId) {
        List<Object> posts = new ArrayList<>();

        LambdaQueryWrapper<InterviewExperience> expWrapper = new LambdaQueryWrapper<>();
        expWrapper.eq(InterviewExperience::getUserId, userId)
                .eq(InterviewExperience::getIsDeleted, 0)
                .orderByDesc(InterviewExperience::getCreateTime);
        List<InterviewExperience> experiences = interviewExperienceService.list(expWrapper);
        experiences.forEach(exp -> {
            Map<String, Object> post = new HashMap<>();
            post.put("id", exp.getId());
            post.put("title", exp.getTitle());
            post.put("content", exp.getExperienceText());
            post.put("typeName", "面经");
            post.put("type", 1);
            post.put("createTime", exp.getCreateTime());
            post.put("viewCount", exp.getViewCount());
            post.put("commentCount", exp.getCommentCount());
            post.put("likeCount", exp.getLikeCount());
            posts.add(post);
        });

        LambdaQueryWrapper<SalaryDisclosure> salaryWrapper = new LambdaQueryWrapper<>();
        salaryWrapper.eq(SalaryDisclosure::getUserId, userId)
                .eq(SalaryDisclosure::getIsDeleted, 0)
                .orderByDesc(SalaryDisclosure::getCreateTime);
        List<SalaryDisclosure> salaries = salaryDisclosureService.list(salaryWrapper);
        salaries.forEach(salary -> {
            Map<String, Object> post = new HashMap<>();
            post.put("id", salary.getId());
            post.put("title", salary.getJobCategory() + "薪资爆料");
            post.put("content", "月薪: " + salary.getMonthlyBase() + " | 总包: " + salary.getTotalPackage());
            post.put("typeName", "薪资");
            post.put("type", 2);
            post.put("createTime", salary.getCreateTime());
            post.put("viewCount", salary.getViewCount());
            post.put("commentCount", salary.getCommentCount());
            post.put("likeCount", salary.getLikeCount());
            posts.add(post);
        });

        LambdaQueryWrapper<CompanyReview> reviewWrapper = new LambdaQueryWrapper<>();
        reviewWrapper.eq(CompanyReview::getUserId, userId)
                .eq(CompanyReview::getIsDeleted, 0)
                .orderByDesc(CompanyReview::getCreateTime);
        List<CompanyReview> reviews = companyReviewService.list(reviewWrapper);
        reviews.forEach(review -> {
            Map<String, Object> post = new HashMap<>();
            post.put("id", review.getId());
            post.put("title", review.getCompanyName() + "评价");
            post.put("content", "优点: " + review.getProsText() + " | 缺点: " + review.getConsText());
            post.put("typeName", "评价");
            post.put("type", 3);
            post.put("createTime", review.getCreateTime());
            post.put("viewCount", review.getViewCount());
            post.put("commentCount", review.getCommentCount());
            post.put("likeCount", review.getLikeCount());
            posts.add(post);
        });

        posts.sort((a, b) -> {
            Object timeA = ((Map)a).get("createTime");
            Object timeB = ((Map)b).get("createTime");
            String timeAStr = timeA != null ? timeA.toString() : "";
            String timeBStr = timeB != null ? timeB.toString() : "";
            return timeBStr.compareTo(timeAStr);
        });

        return ApiResponse.success(posts);
    }

    @GetMapping("/comments")
    public ApiResponse<Map<String, Object>> getUserComments(@RequestParam Long userId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getUserId, userId)
                .orderByDesc(Comment::getCreateTime);
        List<Comment> comments = commentMapper.selectList(wrapper);

        List<Map<String, Object>> commentList = new ArrayList<>();
        comments.forEach(comment -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", comment.getId());
            item.put("content", comment.getContent());
            item.put("contentId", comment.getContentId());
            item.put("contentType", comment.getContentType());
            item.put("createTime", comment.getCreateTime());
            item.put("likeCount", comment.getLikeCount());
            commentList.add(item);
        });

        Map<String, Object> result = new HashMap<>();
        result.put("data", commentList);
        return ApiResponse.success(result);
    }

    @GetMapping("/likes")
    public ApiResponse<Map<String, Object>> getUserLikes(@RequestParam Long userId) {
        LambdaQueryWrapper<LikeRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LikeRecord::getUserId, userId)
                .orderByDesc(LikeRecord::getCreateTime);
        List<LikeRecord> likes = likeRecordMapper.selectList(wrapper);

        List<Map<String, Object>> likeList = new ArrayList<>();
        for (LikeRecord like : likes) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", like.getId());
            item.put("contentId", like.getContentId());
            item.put("contentType", like.getContentType());
            item.put("createTime", like.getCreateTime());

            if (like.getContentType() == 1) {
                InterviewExperience exp = interviewExperienceService.getById(like.getContentId());
                if (exp != null) {
                    item.put("title", exp.getTitle());
                    item.put("typeName", "面经");
                }
            } else if (like.getContentType() == 2) {
                SalaryDisclosure salary = salaryDisclosureService.getById(like.getContentId());
                if (salary != null) {
                    item.put("title", salary.getJobCategory() + "薪资爆料");
                    item.put("typeName", "薪资");
                }
            } else if (like.getContentType() == 3) {
                CompanyReview review = companyReviewService.getById(like.getContentId());
                if (review != null) {
                    item.put("title", review.getCompanyName() + "评价");
                    item.put("typeName", "评价");
                }
            }
            likeList.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", likeList);
        return ApiResponse.success(result);
    }

    @GetMapping("/collections")
    public ApiResponse<List<Object>> getUserCollections(@RequestParam Long userId) {
        LambdaQueryWrapper<CollectRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollectRecord::getUserId, userId)
                .orderByDesc(CollectRecord::getCreateTime);
        List<CollectRecord> collections = collectRecordMapper.selectList(wrapper);

        List<Object> collectList = new ArrayList<>();
        for (CollectRecord collect : collections) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", collect.getId());
            item.put("contentId", collect.getContentId());
            item.put("contentType", collect.getContentType());
            item.put("createTime", collect.getCreateTime());

            if (collect.getContentType() == 1) {
                InterviewExperience exp = interviewExperienceService.getById(collect.getContentId());
                if (exp != null) {
                    item.put("title", exp.getTitle());
                    item.put("content", exp.getExperienceText());
                    item.put("typeName", "面经");
                    item.put("type", 1);
                    item.put("viewCount", exp.getViewCount());
                    item.put("commentCount", exp.getCommentCount());
                    item.put("likeCount", exp.getLikeCount());
                }
            } else if (collect.getContentType() == 2) {
                SalaryDisclosure salary = salaryDisclosureService.getById(collect.getContentId());
                if (salary != null) {
                    item.put("title", salary.getJobCategory() + "薪资爆料");
                    item.put("content", "月薪: " + salary.getMonthlyBase() + " | 总包: " + salary.getTotalPackage());
                    item.put("typeName", "薪资");
                    item.put("type", 2);
                    item.put("viewCount", salary.getViewCount());
                    item.put("commentCount", salary.getCommentCount());
                    item.put("likeCount", salary.getLikeCount());
                }
            } else if (collect.getContentType() == 3) {
                CompanyReview review = companyReviewService.getById(collect.getContentId());
                if (review != null) {
                    item.put("title", review.getCompanyName() + "评价");
                    item.put("content", "优点: " + review.getProsText() + " | 缺点: " + review.getConsText());
                    item.put("typeName", "评价");
                    item.put("type", 3);
                    item.put("viewCount", review.getViewCount());
                    item.put("commentCount", review.getCommentCount());
                    item.put("likeCount", review.getLikeCount());
                }
            }
            collectList.add(item);
        }

        return ApiResponse.success(collectList);
    }
}