package com.offertalk.service.impl;

import com.offertalk.common.Constants;
import com.offertalk.entity.ReportRecord;
import com.offertalk.mapper.ReportRecordMapper;
import com.offertalk.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRecordMapper reportRecordMapper;

    @Override
    @Transactional
    public void createReport(Long userId, Integer contentType, Long contentId, Integer reportType, String reason) {
        ReportRecord report = new ReportRecord();
        report.setUserId(userId);
        report.setContentType(contentType);
        report.setContentId(contentId);
        report.setReportType(reportType);
        report.setReportReason(reason);
        report.setStatus(Constants.REPORT_STATUS_PENDING);
        reportRecordMapper.insert(report);
    }
}
