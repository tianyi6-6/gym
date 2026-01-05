package com.gym.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        logger.error("系统异常: ", e);
        
        if (e instanceof DataAccessException) {
            // 数据库访问异常
            return Result.error("数据库操作失败: " + e.getMessage());
        } else if (e instanceof SQLException) {
            // SQL异常
            return Result.error("SQL操作失败: " + e.getMessage());
        } else {
            // 其他异常
            return Result.error("系统异常: " + e.getMessage());
        }
    }
}