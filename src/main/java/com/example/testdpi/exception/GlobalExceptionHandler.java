package com.example.testdpi.exception;

import com.example.testdpi.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//捕获com.example.controller包中所有controller的异常
@ControllerAdvice(basePackages = "com.example.testdpi.controller")
public class GlobalExceptionHandler {

    private static final Logger log= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    //    统一异常处理@ExceptionHandler,主要用于Exception
    //    捕获除了自定义异常以外的所有异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(HttpServletRequest request, Exception e){
        log.error("异常信息：",e);
        return Result.error("系统异常");
    }

    //    捕获自定义异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result customError(HttpServletRequest request,CustomException e) {
        return Result.error(e.getMsg());
    }
}
