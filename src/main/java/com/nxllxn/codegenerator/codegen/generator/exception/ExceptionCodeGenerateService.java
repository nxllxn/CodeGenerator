package com.nxllxn.codegenerator.codegen.generator.exception;

import com.nxllxn.codegenerator.codegen.java.TopLevelClass;
import com.nxllxn.codegenerator.codegen.java.TopLevelEnum;
import com.nxllxn.codegenerator.codegen.java.TopLevelInterface;

/**
 * 异常处理相关代码的代码生成服务
 *
 * @author wenchao
 */
public interface ExceptionCodeGenerateService {
    /**
     * 生成异常代码枚举定义
     * @param targetPackage 目标包名
     * @return 异常代码枚举定义枚举类
     */
    TopLevelEnum generateCodeDefinitionEnum(String targetPackage);

    /**
     * 添加异常类接口
     * @param targetPackage 目标包名
     * @return 异常类父类接口
     */
    TopLevelInterface generateWellFormedExceptionInterface(String targetPackage);

    /**
     * 生成异常类基类，提供一些默认实现
     * @param targetPackage 目标包名
     * @return 异常类基类
     */
    TopLevelClass generateAbstractExceptionClass(String targetPackage);

    /**
     * 生成服务器内部异常类代码
     * @param targetPackage 目标包名
     * @return 服务器内部异常类
     */
    TopLevelClass generateInternalServerException(String targetPackage);

    /**
     * 生成缺少参数的异常信息代码
     * @param targetPackage 目标包名
     * @return 缺少参数异常ParamMissingException
     */
    TopLevelClass generateParamMissingException(String targetPackage);

    /**
     * 生成参数错误的异常信息代码
     * @param targetPackage 目标包名
     * @return 参数错误异常ParamErrorException
     */
    TopLevelClass generateParamErrorException(String targetPackage);

    /**
     * 全局异常处理ExceptionHandler类
     * @param targetPackage 目标包名
     * @return 全局异常处理ExceptionHandler类
     */
    TopLevelClass generateGlobalExceptionHandlerClass(String targetPackage);

    TopLevelClass generateEmptyRequestBodyExceptionClass(String targetPackage);
}
