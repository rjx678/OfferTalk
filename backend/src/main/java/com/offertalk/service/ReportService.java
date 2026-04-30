package com.offertalk.service;

public interface ReportService {
    void createReport(Long userId, Integer contentType, Long contentId, Integer reportType, String reason);
}
