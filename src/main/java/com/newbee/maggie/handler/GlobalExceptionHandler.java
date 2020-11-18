package com.newbee.maggie.handler;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice    //跟前端做交互
public class GlobalExceptionHandler {

    private Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class) //处理所有的异常
    @ResponseBody   //不是返回http页面  而是返回错误信息 所以直接用ResponseBody这个标签

    //编写异常处理方法
    public Map<String, Object> exceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        logger.info("出现了异常");
        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("errorCode", 1);
        modelMap.put("errorMsg", e.getMessage());
        logger.info("异常返回信息：" + modelMap);
        return modelMap;
    }

}