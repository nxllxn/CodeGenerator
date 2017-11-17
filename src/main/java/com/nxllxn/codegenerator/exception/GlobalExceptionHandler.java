package com.nxllxn.codegenerator.exception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler { 
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public String exceptionHandler(Exception exception) { 
        WellFormedException wellFormedException;
        if(exception instanceof WellFormedException){
            wellFormedException = (WellFormedException)exception;
        } else {
            wellFormedException = new AbstractException().fromPrimitiveException(exception);
        
            logger.error("{}",wellFormedException.getResponseMsg());
        }
        
        JSONObject resultJsonObj = new JSONObject();
        resultJsonObj.put("response_code",wellFormedException.getResponseCode());
        resultJsonObj.put("response_msg",wellFormedException.getResponseMsg());
        return JSONObject.toJSONString(resultJsonObj,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    } 
} 