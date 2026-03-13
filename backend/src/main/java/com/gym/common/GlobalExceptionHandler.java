package com.gym.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        logger.error("系统异常: ", e);
        
        if (e instanceof DataAccessException) {
            // 数据库访问异常
            logger.error("数据库操作异常: {}", e.getMessage(), e);
            return Result.error("数据库操作失败: " + e.getMessage());
        } else if (e instanceof SQLException) {
            // SQL异常
            logger.error("SQL操作异常: {}", e.getMessage(), e);
            return Result.error("SQL操作失败: " + e.getMessage());
        } else if (e instanceof NoHandlerFoundException) {
            // 404异常
            logger.warn("请求路径不存在: {}", e.getMessage());
            return Result.error("请求路径不存在");
        } else if (e instanceof ExecutionException) {
            // 异步执行异常
            logger.error("异步执行异常: {}", e.getMessage(), e);
            return Result.error("系统处理异常: " + e.getMessage());
        } else if (e instanceof MethodArgumentNotValidException) {
            // 数据验证异常
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;
            String errorMessage = validException.getBindingResult().getFieldError().getDefaultMessage();
            logger.warn("数据验证失败: {}", errorMessage);
            return Result.error("数据验证失败: " + errorMessage);
        } else {
            // 其他异常
            logger.error("未知系统异常: {}", e.getMessage(), e);
            return Result.error("系统异常: " + e.getMessage());
        }
    }
}