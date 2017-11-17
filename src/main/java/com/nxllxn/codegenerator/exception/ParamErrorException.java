package com.nxllxn.codegenerator.exception;

public class ParamErrorException extends AbstractException {
    public ParamErrorException() { 
        this("抱歉，参数类型不匹配！");
    } 

    public ParamErrorException(String msg) { 
        this(Code.CODE_100003,msg);
    }

    public ParamErrorException(Code code, String msg) {
        super(code,msg);
    } 
} 