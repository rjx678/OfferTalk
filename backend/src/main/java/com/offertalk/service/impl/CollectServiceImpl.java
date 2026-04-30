package com.offertalk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.offertalk.common.Constants;
import com.offertalk.entity.CollectRecord;
import com.offertalk.mapper.CollectRecordMapper;
import com.offertalk.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectRecordMapper collectRecordMapper;

    @Override
    @Transactional
    public boolean toggleCollect(Long userId, Integer contentType, Long contentId) {
        LambdaQueryWrapper<CollectRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollectRecord::getUserId, userId)
                .eq(CollectRecord::getContentType, contentType)
                .eq(CollectRecord::getContentId, contentId);
        CollectRecord record = collectRecordMapper.selectOne(wrapper);

        boolean hasCollected;
        if (record == null) {
            record = new CollectRecord();
            record.setUserId(userId);
            record.setContentType(contentType);
            record.setContentId(contentId);
            record.setStatus(Constants.COLLECT_STATUS_ACTIVE);
            collectRecordMapper.insert(record);
            hasCollected = true;
        } else if (record.getStatus().equals(Constants.COLLECT_STATUS_ACTIVE)) {
            record.setStatus(Constants.COLLECT_STATUS_CANCELLED);
            collectRecordMapper.updateById(record);
            hasCollected = false;
        } else {
            record.setStatus(Constants.COLLECT_STATUS_ACTIVE);
            collectRecordMapper.updateById(record);
            hasCollected = true;
        }

        return hasCollected;
    }

    @Override
    public boolean hasCollected(Long userId, Integer contentType, Long contentId) {
        LambdaQueryWrapper<CollectRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollectRecord::getUserId, userId)
                .eq(CollectRecord::getContentType, contentType)
                .eq(CollectRecord::getContentId, contentId)
                .eq(CollectRecord::getStatus, Constants.COLLECT_STATUS_ACTIVE);
        return collectRecordMapper.selectCount(wrapper) > 0;
    }
}
