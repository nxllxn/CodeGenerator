package com.nxllxn.codegenerator.exception;

public class UnknownCorrespondingTemplateNameException extends RuntimeException{
    public UnknownCorrespondingTemplateNameException() {
        super("抱歉！当前模板没有定义对应的模板名称！");
    }
}
