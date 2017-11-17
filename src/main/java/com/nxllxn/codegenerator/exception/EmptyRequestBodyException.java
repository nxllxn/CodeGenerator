package com.nxllxn.codegenerator.exception;

public class EmptyRequestBodyException extends AbstractException {
    public EmptyRequestBodyException() {
        this("抱歉，当前请求请求Body为空！");
    }

    public EmptyRequestBodyException(String msg) {
        this(Code.CODE_100002,msg);
    } 

    public EmptyRequestBodyException(Code code, String msg) {
        super(code,msg);
    } 
} 