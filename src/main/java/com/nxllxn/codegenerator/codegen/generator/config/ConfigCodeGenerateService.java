package com.nxllxn.codegenerator.codegen.generator.config;


import com.nxllxn.codegenerator.codegen.resource.ResourceUnit;
import com.nxllxn.codegenerator.config.ConfigCodeGeneratorConfiguration;

/**
 * 配置文件代码生成服务
 *
 * @author wenchao
 */
public interface ConfigCodeGenerateService {
    /**
     * 生成主配置文件代码
     * @param targetDirectory 配置文件所在目录
     * @param xmlPackageName xml文件所在目录，用于配置mybatis mapper-locations
     * @return 主配置文件代码
     */
    ResourceUnit generateMainConfigResourceUnit(String targetDirectory, String xmlPackageName);

    /**
     * 生成本地开发环境配置文件代码
     * @param configuration 配置文件生成相关配置
     * @return 开发环境配置文件代码
     */
    ResourceUnit generateDevConfigResourceUnit(ConfigCodeGeneratorConfiguration configuration);

    /**
     * 生成测试环境配置文件代码
     * @param configuration 配置文件生成相关配置
     * @return 测试环境配置文件代码
     */
    ResourceUnit generateTestConfigResourceUnit(ConfigCodeGeneratorConfiguration configuration);

    /**
     * 生成生产环境配置文件代码
     * @param configuration 配置文件生成相关配置
     * @return 生产环境配置文件代码
     */
    ResourceUnit generateProConfigResourceUnit(ConfigCodeGeneratorConfiguration configuration);
}
