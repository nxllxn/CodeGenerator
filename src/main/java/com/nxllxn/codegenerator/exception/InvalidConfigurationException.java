package com.nxllxn.codegenerator.exception;

/**
 * 无效的配置信息异常
 */
public class InvalidConfigurationException extends RuntimeException{
    public InvalidConfigurationException(String message) {
        super(message);
    }
}
