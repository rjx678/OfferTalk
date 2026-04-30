package com.offertalk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.offertalk.entity.InterviewExperience;

import java.util.List;

public interface InterviewExperienceService extends IService<InterviewExperience> {
    Page<InterviewExperience> getExperiencePage(Integer companyId,Integer recruitType, String sortBy, String keyword, Integer page, Integer size);
    InterviewExperience getExperienceDetail(Long id);
    Long createExperience(InterviewExperience experience);
    void incrementViewCount(Long id);
    void updateLikeCount(Long id, Integer delta);
    void updateCommentCount(Long id, Integer delta);
    void updateTruthScore(Long id, Double score);
}
