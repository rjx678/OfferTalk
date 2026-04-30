package com.offertalk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.offertalk.entity.SalaryDisclosure;

import java.util.List;

public interface SalaryDisclosureService extends IService<SalaryDisclosure> {
    Page<SalaryDisclosure> getSalaryPage(Integer companyId,Integer recruitType, String sortBy, String keyword, Integer page, Integer size);
    SalaryDisclosure getSalaryDetail(Long id);
    Long createSalary(SalaryDisclosure salary);
    void incrementViewCount(Long id);
    void updateCommentCount(Long id, Integer delta);
    void updateLikeCount(Long id, Integer delta);
    void updateTruthScore(Long id, Double score);
    List<SalaryDisclosure> getTopSalaries(Integer limit);
}
