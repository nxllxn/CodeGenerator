package com.nxllxn.codegenerator.codegen.generator.maven;


import com.nxllxn.codegenerator.codegen.xml.TagNode;
import com.nxllxn.codegenerator.codegen.xml.XmlDocument;
import com.nxllxn.codegenerator.config.PomGeneratorConfiguration;

import java.util.List;

/**
 * maven pom 文件生成服务
 *
 * @author wenchao
 */
public interface PomCodeGenerateService {
    /**
     * 生成Pom文档
     * @param publicId maven pom文件publicId
     * @param systemId maven pom文件systemId
     * @return pom文档
     */
    XmlDocument generatePomXmlDocument(String publicId, String systemId);

    /**
     * 生成Pom文档根节点
     * @return pom文档根节点
     * @param pomGeneratorConfiguration
     */
    TagNode generatePomRootNode(PomGeneratorConfiguration pomGeneratorConfiguration);

    /**
     * 为pom文档生成modelVersion节点
     * @return modelVersion节点
     */
    TagNode generateModelVersionNode();

    /**
     * 生成maven坐标
     * @param groupId groupId
     * @param artifactId artifactId
     * @param version jar包版本
     * @param packageType 打包类型
     * @param projectName 项目名称
     * @return maven坐标相关节点
     */
    List<TagNode> generateMavenCoordinate(String groupId, String artifactId, String version, String packageType, String projectName);

    /**
     * 生成属性定义节点
     * @return 属性定义节点
     */
    TagNode generatePropertiesNode();

    /**
     * 生成Spring boot start parent 节点
     * @return Spring boot start parent 节点
     */
    TagNode generateParentNode();

    /**
     * 生成空的依赖节点
     * @return maven依赖节点
     */
    TagNode generateDependenciesNode();

    /**
     * 生成常用依赖节点
     * @return 常用依赖节点集合
     * @param propertiesNode 属性节点
     */
    List<TagNode> generateDependencyNodes(TagNode propertiesNode);

    /**
     * 生成maven build节点
     * @return maven build节点
     */
    TagNode generateBuildNode();

    /**
     * 生成finalName节点
     * @param projectName 项目名称
     * @return finalName节点
     */
    TagNode generateFinalNameNode(String projectName);

    /**
     * 生成plugins节点
     * @return plugins节点
     */
    TagNode generatePluginsNode(String applicationClassPackageName);

    /**
     * 生成资源文件过滤相关配置节点
     * @param resourcesDir 资源文件目录
     * @param sourceDir 代码源文件目录
     * @return 资源文件过滤相关配置节点
     */
    TagNode generateResourceFilterNode(String resourcesDir, String sourceDir);
}
