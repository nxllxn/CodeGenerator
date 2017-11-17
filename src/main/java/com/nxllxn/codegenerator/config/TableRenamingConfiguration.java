package com.nxllxn.codegenerator.config;

import java.util.Properties;

/**
 * 数据库表的重命名规则
 */
public class TableRenamingConfiguration extends RenamingRuleConfiguration {

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public TableRenamingConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }
}
