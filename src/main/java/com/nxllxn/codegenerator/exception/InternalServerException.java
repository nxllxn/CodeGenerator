package com.nxllxn.codegenerator.exception;

public class InternalServerException extends AbstractException {
    public InternalServerException() { 
        this("系统开小差了，请稍后再试!");
    } 

    public InternalServerException(String msg) { 
        this(Code.CODE_100001,msg);
    }

    public InternalServerException(Code code, String msg) {
        super(code,msg);
    } 
} 