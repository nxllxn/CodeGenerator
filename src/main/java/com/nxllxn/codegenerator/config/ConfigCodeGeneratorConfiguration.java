package com.nxllxn.codegenerator.config;

import java.util.Properties;

public class ConfigCodeGeneratorConfiguration extends PositionTypeHolder {
    private String devDataSourceHost;
    private String devDataSourceDBName;
    private String devDataSourceUserName;
    private String devDataSourcePassword;

    private String testDataSourceHost;
    private String testDataSourceDBName;
    private String testDataSourceUserName;
    private String testDataSourcePassword;

    private String proDataSourceHost;
    private String proDataSourceDBName;
    private String proDataSourceUserName;
    private String proDataSourcePassword;

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public ConfigCodeGeneratorConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }

    public String getDevDataSourceHost() {
        return devDataSourceHost;
    }

    public void setDevDataSourceHost(String devDataSourceHost) {
        this.devDataSourceHost = devDataSourceHost;
    }

    public String getDevDataSourceDBName() {
        return devDataSourceDBName;
    }

    public void setDevDataSourceDBName(String devDataSourceDBName) {
        this.devDataSourceDBName = devDataSourceDBName;
    }

    public String getDevDataSourceUserName() {
        return devDataSourceUserName;
    }

    public void setDevDataSourceUserName(String devDataSourceUserName) {
        this.devDataSourceUserName = devDataSourceUserName;
    }

    public String getDevDataSourcePassword() {
        return devDataSourcePassword;
    }

    public void setDevDataSourcePassword(String devDataSourcePassword) {
        this.devDataSourcePassword = devDataSourcePassword;
    }

    public String getTestDataSourceHost() {
        return testDataSourceHost;
    }

    public void setTestDataSourceHost(String testDataSourceHost) {
        this.testDataSourceHost = testDataSourceHost;
    }

    public String getTestDataSourceDBName() {
        return testDataSourceDBName;
    }

    public void setTestDataSourceDBName(String testDataSourceDBName) {
        this.testDataSourceDBName = testDataSourceDBName;
    }

    public String getTestDataSourceUserName() {
        return testDataSourceUserName;
    }

    public void setTestDataSourceUserName(String testDataSourceUserName) {
        this.testDataSourceUserName = testDataSourceUserName;
    }

    public String getTestDataSourcePassword() {
        return testDataSourcePassword;
    }

    public void setTestDataSourcePassword(String testDataSourcePassword) {
        this.testDataSourcePassword = testDataSourcePassword;
    }

    public String getProDataSourceHost() {
        return proDataSourceHost;
    }

    public void setProDataSourceHost(String proDataSourceHost) {
        this.proDataSourceHost = proDataSourceHost;
    }

    public String getProDataSourceDBName() {
        return proDataSourceDBName;
    }

    public void setProDataSourceDBName(String proDataSourceDBName) {
        this.proDataSourceDBName = proDataSourceDBName;
    }

    public String getProDataSourceUserName() {
        return proDataSourceUserName;
    }

    public void setProDataSourceUserName(String proDataSourceUserName) {
        this.proDataSourceUserName = proDataSourceUserName;
    }

    public String getProDataSourcePassword() {
        return proDataSourcePassword;
    }

    public void setProDataSourcePassword(String proDataSourcePassword) {
        this.proDataSourcePassword = proDataSourcePassword;
    }

    @Override
    public String toString() {
        return "ConfigCodeGeneratorConfiguration{" +
                "devDataSourceHost='" + devDataSourceHost + '\'' +
                ", devDataSourceDBName='" + devDataSourceDBName + '\'' +
                ", devDataSourceUserName='" + devDataSourceUserName + '\'' +
                ", devDataSourcePassword='" + devDataSourcePassword + '\'' +
                ", testDataSourceHost='" + testDataSourceHost + '\'' +
                ", testDataSourceDBName='" + testDataSourceDBName + '\'' +
                ", testDataSourceUserName='" + testDataSourceUserName + '\'' +
                ", testDataSourcePassword='" + testDataSourcePassword + '\'' +
                ", proDataSourceHost='" + proDataSourceHost + '\'' +
                ", proDataSourceDBName='" + proDataSourceDBName + '\'' +
                ", proDataSourceUserName='" + proDataSourceUserName + '\'' +
                ", proDataSourcePassword='" + proDataSourcePassword + '\'' +
                '}';
    }
}
