package com.offertalk.config;

import com.offertalk.common.BusinessException;
import com.offertalk.common.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        logger.warn("业务异常: {} - {}", request.getRequestURI(), e.getMessage());
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        logger.warn("参数异常: {} - {}", request.getRequestURI(), e.getMessage());
        return ApiResponse.badRequest(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e, HttpServletRequest request) {
        logger.error("系统异常: {} - ", request.getRequestURI(), e);
        return ApiResponse.error("系统繁忙，请稍后重试");
    }
}
