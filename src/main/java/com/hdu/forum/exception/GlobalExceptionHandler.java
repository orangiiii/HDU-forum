package com.hdu.forum.exception;

import com.hdu.forum.dto.Result;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * JSON序列化失败
     */
    @ExceptionHandler(HttpMessageNotWritableException.class)
    public Result<?> handleJsonWriteError(HttpMessageNotWritableException e) {
        e.printStackTrace();
        return Result.error(500, "JSON序列化失败: " + e.getMessage());
    }

    /**
     * 参数校验错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.error(400, message);
    }

    /**
     * 业务异常（RuntimeException）
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntime(RuntimeException e) {
        e.printStackTrace();
        return Result.error(500, e.getMessage());
    }

    /**
     * 所有未捕获的异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.error(500, "服务器内部错误: " + e.getMessage());
    }
}
