package com.nxllxn.codegenerator.codegen.generator.application;


import com.nxllxn.codegenerator.codegen.java.Method;
import com.nxllxn.codegenerator.codegen.java.TopLevelClass;

/**
 * Application 代码生成服务
 *
 * @author wenchao
 */
public interface ApplicationCodeGenerateService {
    /**
     * 生成Spring boot Application主类
     * @return Spring boot Application主类
     */
    TopLevelClass generateApplicationClass(String packageName);

    /**
     * 生生成主Main方法
     * @return Spring boot Application主类main方法
     */
    Method generateMainMethod();
}
