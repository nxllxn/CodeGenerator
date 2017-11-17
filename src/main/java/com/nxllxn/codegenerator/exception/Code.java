package com.nxllxn.codegenerator.exception;

public enum Code { 
    CODE_00,
    CODE_100001,
    CODE_100002,
    CODE_100003;

    public String toString() { 
        return super.toString().replace("CODE_","");
    } 
} 