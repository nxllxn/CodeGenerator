package com.nxllxn.codegenerator.codegen.java;

/**
 * 用于定义Java类型的可见性
 * @author wenchao
 */
public enum Visibility {
    PRIVATE("private"),
    DEFAULT(""),
    PROTECTED("protected"),
    PUBLIC("public");

    private String visibility;

    Visibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return this.visibility;
    }
}
