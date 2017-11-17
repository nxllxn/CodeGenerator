package com.nxllxn.codegenerator.exception;

public interface WellFormedException { 
    String getResponseCode();

    String getResponseMsg();

    WellFormedException fromPrimitiveException(Exception primitiveException);
} 