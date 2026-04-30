package com.offertalk.service;

import com.offertalk.config.WeixinConfig;
import com.offertalk.common.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@Service
public class WeixinService {

    @Autowired
    private WeixinConfig weixinConfig;

    @Autowired
    private RestTemplate restTemplate;

    public String getOpenid(String code) {
        try {
            String url = weixinConfig.getLoginUrl() +
                    "?appid=" + weixinConfig.getAppId() +
                    "&secret=" + weixinConfig.getAppSecret() +
                    "&js_code=" + code +
                    "&grant_type=authorization_code";

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null) {
                throw new BusinessException("微信登录失败");
            }

            if (response.containsKey("errcode")) {
                Integer errcode = (Integer) response.get("errcode");
                if (errcode != 0) {
                    throw new BusinessException("微信登录失败: " + response.get("errmsg"));
                }
            }

            String openid = (String) response.get("openid");
            if (openid == null || openid.isEmpty()) {
                throw new BusinessException("获取openid失败");
            }

            return openid;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new BusinessException("微信登录参数错误");
            }
            throw new BusinessException("微信登录失败");
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                throw e;
            }
            throw new BusinessException("微信登录失败");
        }
    }
}