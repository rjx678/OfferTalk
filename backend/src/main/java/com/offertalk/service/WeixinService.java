package com.offertalk.service;

import com.offertalk.config.WeixinConfig;
import com.offertalk.common.BusinessException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class WeixinService {

    private static final Logger logger = LoggerFactory.getLogger(WeixinService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WeixinConfig weixinConfig;

    @Autowired
    private RestTemplate restTemplate;

    public String getOpenid(String code) {
        String requestId = java.util.UUID.randomUUID().toString().substring(0, 8);
        String timestamp = LocalDateTime.now().format(formatter);
        
        logger.info("========== 微信登录请求开始 ==========");
        logger.info("[{}] 请求时间: {}", requestId, timestamp);
        logger.info("[{}] 请求参数 code: {}", requestId, code != null ? code.substring(0, Math.min(10, code.length())) + "..." : "null");
        logger.info("[{}] AppId: {}", requestId, weixinConfig.getAppId());
        logger.info("[{}] AppSecret: {}", requestId, weixinConfig.getAppSecret() != null ? "******" : "null");
        logger.info("[{}] LoginUrl: {}", requestId, weixinConfig.getLoginUrl());

        try {
            String url = weixinConfig.getLoginUrl() +
                    "?appid=" + weixinConfig.getAppId() +
                    "&secret=" + weixinConfig.getAppSecret() +
                    "&js_code=" + code +
                    "&grant_type=authorization_code";

            logger.info("[{}] 完整请求URL: {}", requestId, url);
            
            long startTime = System.currentTimeMillis();
            
            // 先获取原始响应，处理 text/plain 类型
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            String responseBody = responseEntity.getBody();
            
            long endTime = System.currentTimeMillis();
            
            logger.info("[{}] 微信接口响应耗时: {}ms", requestId, (endTime - startTime));
            logger.info("[{}] HTTP状态码: {}", requestId, responseEntity.getStatusCode());
            logger.info("[{}] 响应内容类型: {}", requestId, responseEntity.getHeaders().getContentType());
            logger.info("[{}] 响应内容: {}", requestId, responseBody);

            if (responseBody == null || responseBody.isEmpty()) {
                logger.error("[{}] 微信接口返回为空", requestId);
                throw new BusinessException("微信登录失败：接口返回为空");
            }

            // 手动解析JSON字符串为Map
            Map<String, Object> response;
            try {
                response = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
            } catch (Exception e) {
                logger.error("[{}] JSON解析失败: {}", requestId, e.getMessage());
                throw new BusinessException("微信登录失败：响应解析错误");
            }

            if (response.containsKey("errcode")) {
                Object errcodeObj = response.get("errcode");
                Integer errcode = null;
                
                if (errcodeObj instanceof Integer) {
                    errcode = (Integer) errcodeObj;
                } else if (errcodeObj instanceof String) {
                    errcode = Integer.parseInt((String) errcodeObj);
                }
                
                String errmsg = (String) response.get("errmsg");
                
                if (errcode != null && errcode != 0) {
                    logger.error("[{}] 微信接口返回错误 - errcode: {}, errmsg: {}", requestId, errcode, errmsg);
                    String errorMessage = buildErrorMessage(errcode, errmsg);
                    throw new BusinessException(errorMessage);
                }
            }

            String openid = (String) response.get("openid");
            String unionid = (String) response.get("unionid");
            
            logger.info("[{}] 获取openid成功: {}", requestId, openid != null ? openid.substring(0, 10) + "..." : "null");
            logger.info("[{}] 获取unionid: {}", requestId, unionid != null ? unionid.substring(0, 10) + "..." : "null");
            logger.info("[{}] 微信登录请求成功 ==========", requestId);

            if (openid == null || openid.isEmpty()) {
                logger.error("[{}] openid为空", requestId);
                throw new BusinessException("获取openid失败");
            }

            return openid;
            
        } catch (HttpClientErrorException e) {
            logger.error("[{}] HttpClientErrorException - StatusCode: {}, ResponseBody: {}", 
                requestId, e.getStatusCode(), e.getResponseBodyAsString());
            
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new BusinessException("微信登录参数错误: " + e.getMessage());
            }
            throw new BusinessException("微信登录失败: HTTP错误 " + e.getStatusCode());
            
        } catch (ResourceAccessException e) {
            logger.error("[{}] ResourceAccessException - 网络连接异常", requestId);
            logger.error("[{}] 异常堆栈:", requestId, e);
            throw new BusinessException("微信登录失败：网络连接异常，请检查网络");
            
        } catch (BusinessException e) {
            throw e;
            
        } catch (Exception e) {
            logger.error("[{}] 未知异常", requestId);
            logger.error("[{}] 异常类型: {}", requestId, e.getClass().getName());
            logger.error("[{}] 异常消息: {}", requestId, e.getMessage());
            logger.error("[{}] 异常堆栈:", requestId, e);
            
            throw new BusinessException("微信登录失败：" + e.getMessage());
        }
    }

    private String buildErrorMessage(Integer errcode, String errmsg) {
        String message = "微信登录失败";
        
        switch (errcode) {
            case -1:
                message = "微信服务器繁忙，请稍后重试";
                break;
            case 40001:
                message = "获取access_token时AppSecret错误，或者access_token无效";
                break;
            case 40002:
                message = "不合法的凭证类型";
                break;
            case 40003:
                message = "不合法的openid";
                break;
            case 40013:
                message = "不合法的AppID";
                break;
            case 40014:
                message = "不合法的access_token";
                break;
            case 40029:
                message = "不合法的code";
                break;
            case 41001:
                message = "缺少access_token参数";
                break;
            case 41002:
                message = "缺少appid参数";
                break;
            case 41003:
                message = "缺少refresh_token参数";
                break;
            case 41004:
                message = "缺少secret参数";
                break;
            case 41005:
                message = "缺少多媒体文件数据";
                break;
            case 41006:
                message = "缺少media_id参数";
                break;
            case 42001:
                message = "access_token超时";
                break;
            case 43002:
                message = "需要用户确认授权";
                break;
            case 43003:
                message = "已拒绝授权";
                break;
            case 44003:
                message = "需要使用wx.login获取code";
                break;
            case 50001:
                message = "未绑定开放平台，请在开放平台绑定小程序";
                break;
            case 61002:
                message = "code已过期，请重新获取";
                break;
            default:
                message = "微信登录失败: " + (errmsg != null ? errmsg : "未知错误");
        }
        
        return message;
    }
}