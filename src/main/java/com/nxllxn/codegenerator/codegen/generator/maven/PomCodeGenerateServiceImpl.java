package com.nxllxn.codegenerator.codegen.generator.maven;

import com.nxllxn.codegenerator.codegen.xml.AttributeNode;
import com.nxllxn.codegenerator.codegen.xml.TagNode;
import com.nxllxn.codegenerator.codegen.xml.TextNode;
import com.nxllxn.codegenerator.codegen.xml.XmlDocument;
import com.nxllxn.codegenerator.config.PomGeneratorConfiguration;
import com.nxllxn.codegenerator.utils.AssembleUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * maven pom 文件生成服务实现类
 */
public class PomCodeGenerateServiceImpl implements PomCodeGenerateService {

    @Override
    public XmlDocument generatePomXmlDocument(String publicId, String systemId) {
        XmlDocument pomXmlDocument = new XmlDocument();

        pomXmlDocument.setSystemId(systemId);
        pomXmlDocument.setPublicId(publicId);

        return pomXmlDocument;
    }

    @Override
    public TagNode generatePomRootNode(PomGeneratorConfiguration pomGeneratorConfiguration) {
        TagNode rootNode = new TagNode();

        rootNode.setName("project");
        rootNode.addAttributeNode(new AttributeNode("xmlns", pomGeneratorConfiguration.getNamespace()));
        rootNode.addAttributeNode(new AttributeNode("xmlns:xsi", pomGeneratorConfiguration.getXsiNamespace()));
        rootNode.addAttributeNode(new AttributeNode("xsi:schemaLocation",
                pomGeneratorConfiguration.getPublicId() + " " + pomGeneratorConfiguration.getSystemId()));

        return rootNode;
    }

    @Override
    public TagNode generateModelVersionNode() {
        return generateNameValueNode("modelVersion", "4.0.0");
    }

    @Override
    public List<TagNode> generateMavenCoordinate(String groupId, String artifactId, String version, String packageType, String projectName) {
        List<TagNode> coordinateTagNodes = new ArrayList<>();


        groupId = StringUtils.isBlank(groupId) ? "groupId" : groupId;
        artifactId = StringUtils.isBlank(artifactId) ? "artifactId" : artifactId;
        version = StringUtils.isBlank(version) ? "1.0-SNAPSHOT" : version;
        packageType = StringUtils.isBlank(packageType) ? PomGeneratorConfiguration.PACKAGE_TYPE_JAR : packageType;
        projectName = StringUtils.isBlank(projectName) ? "GeneratedProject" : projectName;

        coordinateTagNodes.add(generateNameValueNode("groupId", groupId));
        coordinateTagNodes.add(generateNameValueNode("artifactId", artifactId));
        coordinateTagNodes.add(generateNameValueNode("version", version));
        coordinateTagNodes.add(generateNameValueNode("packaging", packageType));
        coordinateTagNodes.add(generateNameValueNode("name", projectName));

        return coordinateTagNodes;
    }

    @Override
    public TagNode generatePropertiesNode() {
        TagNode propertiesNode = new TagNode();

        propertiesNode.setName("properties");

        return propertiesNode;
    }

    @Override
    public TagNode generateParentNode() {
        TagNode parentNode = new TagNode();

        parentNode.setName("parent");
        parentNode.addChildNode(generateNameValueNode("groupId", "org.springframework.boot"));
        parentNode.addChildNode(generateNameValueNode("artifactId", "spring-boot-starter-parent"));
        parentNode.addChildNode(generateNameValueNode("version", "1.5.4.RELEASE"));

        return parentNode;
    }

    @Override
    public TagNode generateDependenciesNode() {
        TagNode dependenciesNode = new TagNode();

        dependenciesNode.setName("dependencies");

        return dependenciesNode;
    }

    @Override
    public List<TagNode> generateDependencyNodes(TagNode propertiesNode) {
        List<TagNode> dependencyNodes = new ArrayList<>();

        String[][] dependencies = new String[][]{
                {"org.apache.commons", "commons-lang3", "3.4"},
                {"com.alibaba", "fastjson", "1.2.37"},
                {"org.springframework.boot", "spring-boot-starter", "", "org.springframework.boot", "spring-boot-starter-logging", ""},
                {"org.springframework.boot", "spring-boot-starter-web",""},
                {"org.springframework.boot", "spring-boot-starter-log4j2", ""},
                {"com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", ""},
                {"org.springframework.boot", "spring-boot-starter-test", ""},
                {"junit", "junit", "4.12"},
                {"org.springframework.boot", "spring-boot-starter-jdbc", ""},
                {"mysql", "mysql-connector-java", "5.1.40"},
                {"com.alibaba", "druid", "1.0.29"},
                {"org.mybatis.spring.boot", "mybatis-spring-boot-starter", "1.3.0"}
        };

        TagNode dependencyNode;
        String propertyName;
        for (String[] dependency : dependencies) {
            dependencyNode = new TagNode();
            dependencyNode.setName("dependency");

            TagNode exclusionsNode = null;
            for (int index = 0; index < dependency.length / 3; index++) {
                if (index == 0) {
                    dependencyNode.addChildNode(generateNameValueNode("groupId", dependency[index * 3]));
                    dependencyNode.addChildNode(generateNameValueNode("artifactId", dependency[index * 3 + 1]));

                    if (!StringUtils.isBlank(dependency[index * 3 + 2])) {
                        propertyName = dependency[index * 3 + 1] + ".version";

                        propertiesNode.addChildNode(generateNameValueNode(propertyName,dependency[index * 3 + 2]));

                        dependencyNode.addChildNode(generateNameValueNode("version", "${" + propertyName + "}"));
                    }

                    continue;
                }

                if (exclusionsNode == null){
                    exclusionsNode = new TagNode();
                }

                TagNode exclusionNode = new TagNode();
                exclusionNode.addChildNode(generateNameValueNode("groupId", dependency[index * 3]));
                exclusionNode.addChildNode(generateNameValueNode("artifactId", dependency[index * 3 + 1]));
                if (!StringUtils.isBlank(dependency[index * 3 + 2])) {
                    propertyName = dependency[index * 3 + 1] + ".version";

                    propertiesNode.addChildNode(generateNameValueNode(propertyName,dependency[index * 3 + 2]));

                    dependencyNode.addChildNode(generateNameValueNode("version", "${" + propertyName + "}"));
                }
                exclusionsNode.addChildNode(exclusionNode);
            }

            if (exclusionsNode != null){
                dependencyNode.addChildNode(exclusionsNode);
            }

            dependencyNodes.add(dependencyNode);
        }

        return dependencyNodes;
    }

    @Override
    public TagNode generateBuildNode() {
        TagNode buildNode = new TagNode();

        buildNode.setName("build");

        return buildNode;
    }

    @Override
    public TagNode generateFinalNameNode(String projectName) {
        return generateNameValueNode("finalName",projectName);
    }

    @Override
    public TagNode generatePluginsNode(String applicationClassPackageName) {
        TagNode pluginsNode = new TagNode();
        pluginsNode.setName("plugins");

        //maven compile Plugin
        TagNode compilePluginNode = new TagNode();
        compilePluginNode.setName("plugin");
        compilePluginNode.addChildNode(generateNameValueNode("groupId","org.apache.maven.plugins"));
        compilePluginNode.addChildNode(generateNameValueNode("artifactId","maven-compiler-plugin"));
        compilePluginNode.addChildNode(generateNameValueNode("version","3.3"));
        TagNode configurationNode = new TagNode();
        configurationNode.setName("configuration");
        configurationNode.addChildNode(generateNameValueNode("encoding","utf-8"));
        configurationNode.addChildNode(generateNameValueNode("source","1.7"));
        configurationNode.addChildNode(generateNameValueNode("target","1.7"));
        compilePluginNode.addChildNode(configurationNode);
        pluginsNode.addChildNode(compilePluginNode);

        //spring-boot maven plugin
        TagNode springBootMavenPluginNode = new TagNode();
        springBootMavenPluginNode.setName("plugin");
        springBootMavenPluginNode.addChildNode(generateNameValueNode("groupId","org.springframework.boot"));
        springBootMavenPluginNode.addChildNode(generateNameValueNode("artifactId","spring-boot-maven-plugin"));
        configurationNode = new TagNode();
        configurationNode.setName("configuration");
        configurationNode.addChildNode(generateNameValueNode("mainClass",
                applicationClassPackageName + AssembleUtil.PACKAGE_SEPERATOR + "Application"));
        pluginsNode.addChildNode(springBootMavenPluginNode);

        return pluginsNode;
    }

    private TagNode generateNameValueNode(String nodeName, String nodeValue) {
        TagNode nameValueNode = new TagNode();
        nameValueNode.setName(nodeName);
        nameValueNode.addChildNode(new TextNode(nodeValue));
        return nameValueNode;
    }

    @Override
    public TagNode generateResourceFilterNode(String resourcesDir,String sourceDir) {
        TagNode resourcesNode = new TagNode();

        resourcesNode.setName("resources");

        //通用资源文件打包配置
        TagNode commonResourceFilterNode = new TagNode();
        commonResourceFilterNode.setName("resource");
        commonResourceFilterNode.addChildNode(generateNameValueNode("directory",resourcesDir));

        TagNode includesNode = new TagNode();
        includesNode.setName("includes");
        includesNode.addChildNode(generateNameValueNode("include","*.properties"));
        includesNode.addChildNode(generateNameValueNode("include","*.yml"));
        commonResourceFilterNode.addChildNode(includesNode);

        TagNode filteringNode = generateNameValueNode("filtering","false");
        commonResourceFilterNode.addChildNode(filteringNode);

        resourcesNode.addChildNode(commonResourceFilterNode);

        //mapper文件打包配置
        TagNode mapperResourceFilterNode = new TagNode();
        mapperResourceFilterNode.setName("resource");
        mapperResourceFilterNode.addChildNode(generateNameValueNode("directory",sourceDir));

        includesNode = new TagNode();
        includesNode.setName("includes");
        includesNode.addChildNode(generateNameValueNode("include","*.xml"));
        mapperResourceFilterNode.addChildNode(includesNode);

        filteringNode = generateNameValueNode("filtering","false");
        mapperResourceFilterNode.addChildNode(filteringNode);

        resourcesNode.addChildNode(mapperResourceFilterNode);

        return resourcesNode;
    }
}
