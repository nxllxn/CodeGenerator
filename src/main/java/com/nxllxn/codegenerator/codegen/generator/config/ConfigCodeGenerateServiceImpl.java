package com.nxllxn.codegenerator.codegen.generator.config;

import com.nxllxn.codegenerator.codegen.resource.ResourceUnit;
import com.nxllxn.codegenerator.codegen.resource.YamlResource;
import com.nxllxn.codegenerator.config.ConfigCodeGeneratorConfiguration;
import com.nxllxn.codegenerator.utils.PathUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 配置文件代码生成服务实现类
 *
 * @author wenchao
 */
public class ConfigCodeGenerateServiceImpl implements ConfigCodeGenerateService {
    @Override
    public ResourceUnit generateMainConfigResourceUnit(String targetPackage, String xmlPackageName) {
        YamlResource mainYamlResource = new YamlResource();
        mainYamlResource.setFileName("application");

        //使用的配置环境
        mainYamlResource.appendConfiguration("spring.profiles.active", "dev");

        //服务器端相关配置
        mainYamlResource.appendConfiguration("server.port", "8080");

        //日志
        mainYamlResource.appendConfiguration("logging.level.root", "INFO");

        if (!StringUtils.isBlank(xmlPackageName)) {
            mainYamlResource.appendConfiguration("mybatis.mapper-locations", "classpath:" + PathUtil.packageNameToPath(
                    xmlPackageName
            ) + File.separator + "*.xml");
        }

        return mainYamlResource;
    }

    @Override
    public ResourceUnit generateDevConfigResourceUnit(ConfigCodeGeneratorConfiguration configuration) {
        YamlResource devYamlResource = new YamlResource();

        devYamlResource.setFileName("application-dev");

        String host = configuration.getDevDataSourceHost();
        String dbName = configuration.getDevDataSourceDBName();
        String userName = configuration.getDevDataSourceUserName();
        String password = configuration.getDevDataSourcePassword();

        appendConfigurations(devYamlResource, host, dbName, userName, password);

        return devYamlResource;
    }

    @Override
    public ResourceUnit generateTestConfigResourceUnit(ConfigCodeGeneratorConfiguration configuration) {
        YamlResource testYamlResource = new YamlResource();

        testYamlResource.setFileName("application-test");

        String host = configuration.getTestDataSourceHost();
        String dbName = configuration.getTestDataSourceDBName();
        String userName = configuration.getTestDataSourceUserName();
        String password = configuration.getTestDataSourcePassword();

        appendConfigurations(testYamlResource, host, dbName, userName, password);

        return testYamlResource;
    }

    @Override
    public ResourceUnit generateProConfigResourceUnit(ConfigCodeGeneratorConfiguration configuration) {
        YamlResource proYamlResource = new YamlResource();

        proYamlResource.setFileName("application-pro");

        String host = configuration.getProDataSourceHost();
        String dbName = configuration.getProDataSourceDBName();
        String userName = configuration.getProDataSourceUserName();
        String password = configuration.getProDataSourcePassword();

        appendConfigurations(proYamlResource, host, dbName, userName, password);

        return proYamlResource;
    }

    private void appendConfigurations(YamlResource devYamlResource, String host, String dbName, String userName, String password) {
        host = StringUtils.isBlank(host) ? "数据库服务器主机名" : host;
        dbName = StringUtils.isBlank(dbName) ? "数据库名称" : dbName;
        userName = StringUtils.isBlank(userName) ? "用户名" : userName;
        password = StringUtils.isBlank(password) ? "密码" : password;

        devYamlResource.appendConfiguration("spring.datasource.type", "com.alibaba.druid.pool.DruidDataSource");
        devYamlResource.appendConfiguration("spring.datasource.driver-class-name", "com.mysql.jdbc.Driver");
        devYamlResource.appendConfiguration("spring.datasource.url",
                "jdbc:mysql://"
                        + host
                        +  "/"
                        + dbName + "?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true");
        devYamlResource.appendConfiguration("spring.datasource.username", userName);
        devYamlResource.appendConfiguration("spring.datasource.password", password);
        devYamlResource.appendConfiguration("spring.datasource.maxActive", "20");
        devYamlResource.appendConfiguration("spring.datasource.initialSize", "1");
        devYamlResource.appendConfiguration("spring.datasource.maxWait", "60000");
        devYamlResource.appendConfiguration("spring.datasource.poolPreparedStatements", "true");
        devYamlResource.appendConfiguration("spring.datasource.maxPoolPreparedStatementPerConnectionSize", "20");
        devYamlResource.appendConfiguration("spring.datasource.connectionProperties", "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
        devYamlResource.appendConfiguration("spring.datasource.minIdle", "1");
        devYamlResource.appendConfiguration("spring.datasource.timeBetweenEvictionRunsMillis", "60000");
        devYamlResource.appendConfiguration("spring.datasource.minEvictableIdleTimeMillis", "300000");
        devYamlResource.appendConfiguration("spring.datasource.validationQuery", "select 1 from dual");
        devYamlResource.appendConfiguration("spring.datasource.testWhileIdle", "true");
        devYamlResource.appendConfiguration("spring.datasource.testOnBorrow", "false");
        devYamlResource.appendConfiguration("spring.datasource.testOnReturn", "false");
        devYamlResource.appendConfiguration("spring.datasource.filters", "stat");
    }
}
