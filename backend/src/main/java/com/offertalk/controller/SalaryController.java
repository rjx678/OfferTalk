package com.offertalk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offertalk.common.ApiResponse;
import com.offertalk.common.PageResult;
import com.offertalk.entity.SalaryDisclosure;
import com.offertalk.service.SalaryDisclosureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary")
public class SalaryController {

    @Autowired
    private SalaryDisclosureService salaryDisclosureService;

    @GetMapping("/list")
    public ApiResponse<PageResult<List<SalaryDisclosure>>> getSalaryList(
            @RequestParam(required = false) Integer recruitType,
            @RequestParam(required = false) Integer companyId,
            @RequestParam(defaultValue = "salary") String sortBy,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<SalaryDisclosure> pageResult = salaryDisclosureService.getSalaryPage(companyId,recruitType, sortBy, keyword, page, size);
        return ApiResponse.success(PageResult.of(
                pageResult.getTotal(),
                pageResult.getCurrent(),
                pageResult.getSize(),
                pageResult.getRecords()
        ));
    }

    @GetMapping("/detail/{id}")
    public ApiResponse<SalaryDisclosure> getSalaryDetail(@PathVariable Long id) {
        SalaryDisclosure salary = salaryDisclosureService.getSalaryDetail(id);
        salaryDisclosureService.incrementViewCount(id);
        return ApiResponse.success(salary);
    }

    @PostMapping("/create")
    public ApiResponse<Long> createSalary(@RequestBody SalaryDisclosure salary) {
        Long id = salaryDisclosureService.createSalary(salary);
        return ApiResponse.success("发布成功，请等待审核", id);
    }

    @GetMapping("/top")
    public ApiResponse<List<SalaryDisclosure>> getTopSalaries(
            @RequestParam(defaultValue = "10") Integer limit) {
        List<SalaryDisclosure> topList = salaryDisclosureService.getTopSalaries(limit);
        return ApiResponse.success(topList);
    }
}
