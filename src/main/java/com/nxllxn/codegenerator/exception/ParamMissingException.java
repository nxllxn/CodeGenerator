package com.nxllxn.codegenerator.exception;

public class ParamMissingException extends AbstractException {
    public ParamMissingException() { 
        this("抱歉，请求参数缺失!");
    } 

    public ParamMissingException(String msg) { 
        this(Code.CODE_100002,msg);
    } 

    public ParamMissingException(Code code, String msg) {
        super(code,msg);
    } 
} 