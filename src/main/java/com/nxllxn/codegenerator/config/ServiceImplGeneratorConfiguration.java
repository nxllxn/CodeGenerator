package com.nxllxn.codegenerator.config;

import java.util.Properties;

/**
 * service层代码生成器相关配置
 *
 * @author wenchao
 */
public class ServiceImplGeneratorConfiguration extends PositionTypeHolder {
    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public ServiceImplGeneratorConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }
}
