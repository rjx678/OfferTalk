package com.offertalk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offertalk.common.ApiResponse;
import com.offertalk.common.PageResult;
import com.offertalk.entity.CompanyReview;
import com.offertalk.service.CompanyReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private CompanyReviewService companyReviewService;

    @GetMapping("/list")
    public ApiResponse<PageResult<List<CompanyReview>>> getReviewList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer recruitType,
            @RequestParam(required = false) Integer companyId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<CompanyReview> pageResult = companyReviewService.getReviewPage(companyId,keyword, recruitType, page, size);
        return ApiResponse.success(PageResult.of(
                pageResult.getTotal(),
                pageResult.getCurrent(),
                pageResult.getSize(),
                pageResult.getRecords()
        ));
    }

    @GetMapping("/detail/{id}")
    public ApiResponse<CompanyReview> getReviewDetail(@PathVariable Long id) {
        CompanyReview review = companyReviewService.getReviewDetail(id);
        companyReviewService.incrementViewCount(id);
        return ApiResponse.success(review);
    }

    @PostMapping("/create")
    public ApiResponse<Long> createReview(@RequestBody CompanyReview review) {
        Long id = companyReviewService.createReview(review);
        return ApiResponse.success("发布成功，请等待审核", id);
    }

    @GetMapping("/hot")
    public ApiResponse<List<CompanyReview>> getHotReviews(
            @RequestParam(defaultValue = "10") Integer limit) {
        List<CompanyReview> hotList = companyReviewService.getHotReviews(limit);
        return ApiResponse.success(hotList);
    }
}
