package com.nxllxn.codegenerator.exception;

/**
 * 当前模块尚未实现异常
 */
public class NeedForImplementException extends RuntimeException {
    public NeedForImplementException() {
        super("抱歉！当前模块尚未实现！");
    }
}
