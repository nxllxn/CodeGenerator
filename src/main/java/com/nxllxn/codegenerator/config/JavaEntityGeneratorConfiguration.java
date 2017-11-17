package com.nxllxn.codegenerator.config;

import java.util.Properties;

/**
 * Java实体代码生成器配置类
 *
 * @author wenchao
 */
public class JavaEntityGeneratorConfiguration extends PositionTypeHolder {
    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public JavaEntityGeneratorConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }
}
