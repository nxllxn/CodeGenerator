package com.nxllxn.codegenerator.exception;

import com.alibaba.fastjson.JSONException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class AbstractException extends RuntimeException implements WellFormedException {
    private Code responseCode;

    private String responseMsg;

    public AbstractException() {}

    public AbstractException(Code responseCode, String responseMsg) {
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
    }

    public String getResponseCode() {
        return this.responseCode.toString();
    }

    public String getResponseMsg() {
        return this.responseMsg;
    }

    public WellFormedException fromPrimitiveException(Exception primitiveException) {
        if(primitiveException instanceof WellFormedException){
            return (WellFormedException)primitiveException;
        } else if (primitiveException instanceof HttpMessageNotReadableException){
            return new EmptyRequestBodyException();
        } else if (primitiveException instanceof MissingServletRequestParameterException){
            return new ParamMissingException(primitiveException.getMessage());
        } else if (primitiveException instanceof HttpRequestMethodNotSupportedException){
            return new ParamErrorException(primitiveException.getMessage());
        } else if (primitiveException instanceof MethodArgumentTypeMismatchException){
            return new ParamErrorException("抱歉，参数类型不匹配！");
        } else if (primitiveException instanceof NullPointerException){
            return new ParamErrorException("抱歉，未查询到数据！");
        } else if (primitiveException instanceof JSONException){
            return new ParamErrorException("抱歉，请求参数不是一个有效的Json字符串！");
        } else if (primitiveException instanceof NumberFormatException){
            return new ParamErrorException("抱歉，请求参数不是一个有效的数值属性！");
        } else {
            return new InternalServerException();
        }
    } 
} 