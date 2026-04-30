package com.offertalk.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.offertalk.common.Constants;
import com.offertalk.entity.SensitiveWord;
import com.offertalk.mapper.SensitiveWordMapper;
import com.offertalk.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {

    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;

    private Set<String> sensitiveWords = new HashSet<>();
    private Set<String> blockWords = new HashSet<>();

    @PostConstruct
    public void init() {
        loadSensitiveWords();
    }

    private void loadSensitiveWords() {
        LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SensitiveWord::getStatus, Constants.STATUS_ENABLED);
        List<SensitiveWord> words = sensitiveWordMapper.selectList(wrapper);

        for (SensitiveWord word : words) {
            if (word.getLevel() == Constants.SENSITIVE_LEVEL_BLOCK) {
                blockWords.add(word.getWord().toLowerCase());
            }
            sensitiveWords.add(word.getWord().toLowerCase());
        }
    }

    @Override
    public String filterSensitiveWords(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        String result = content;
        for (String word : sensitiveWords) {
            if (result.toLowerCase().contains(word)) {
                LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SensitiveWord::getWord, word)
                        .eq(SensitiveWord::getStatus, Constants.STATUS_ENABLED);
                SensitiveWord sensitiveWord = sensitiveWordMapper.selectOne(wrapper);

                if (sensitiveWord != null) {
                    if (sensitiveWord.getLevel() == Constants.SENSITIVE_LEVEL_REPLACE) {
                        String replace = sensitiveWord.getReplaceWord() != null ?
                                sensitiveWord.getReplaceWord() : "***";
                        result = result.replaceAll("(?i)" + Pattern.quote(word), replace);
                    } else if (sensitiveWord.getLevel() == Constants.SENSITIVE_LEVEL_BLOCK) {
                        result = result.replaceAll("(?i)" + Pattern.quote(word), "***");
                    }
                }
            }
        }

        return result;
    }

    @Override
    public boolean containsSensitiveWords(String content) {
        if (content == null || content.isEmpty()) {
            return false;
        }

        String lowerContent = content.toLowerCase();
        for (String word : sensitiveWords) {
            if (lowerContent.contains(word)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldBlock(String content) {
        if (content == null || content.isEmpty()) {
            return false;
        }

        String lowerContent = content.toLowerCase();
        for (String word : blockWords) {
            if (lowerContent.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
