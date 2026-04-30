package com.offertalk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offertalk.common.ApiResponse;
import com.offertalk.common.PageResult;
import com.offertalk.entity.InterviewExperience;
import com.offertalk.service.InterviewExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private InterviewExperienceService interviewExperienceService;

    @GetMapping("/list")
    public ApiResponse<PageResult<List<?>>> getPostList(
            @RequestParam(required = false) Integer recruitType,
            @RequestParam(required = false) Integer companyId,
            @RequestParam(defaultValue = "new") String sortBy,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

            Page<InterviewExperience> pageResult = interviewExperienceService.getExperiencePage(companyId,recruitType, sortBy, keyword, page, size);
            return ApiResponse.success(PageResult.of(
                    pageResult.getTotal(),
                    pageResult.getCurrent(),
                    pageResult.getSize(),
                    pageResult.getRecords()
            ));

    }

    @GetMapping("/interview/{id}")
    public ApiResponse<InterviewExperience> getInterviewDetail(@PathVariable Long id) {
        InterviewExperience experience = interviewExperienceService.getExperienceDetail(id);
        interviewExperienceService.incrementViewCount(id);
        return ApiResponse.success(experience);
    }

    @PostMapping("/interview/create")
    public ApiResponse<Map<String, Long>> createInterview(@RequestBody InterviewExperience experience) {
        Long id = interviewExperienceService.createExperience(experience);
        Map<String, Long> result = new HashMap<>();
        result.put("id", id);
        return ApiResponse.success("发布成功，请等待审核", result);
    }
}