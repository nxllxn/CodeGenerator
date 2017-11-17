package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Properties;

/**
 * 项目基本信息相关配置
 *
 * @author wenchao
 */
public class ProjectBaseInfoConfiguration extends AbstractConfiguration {
    /**
     * 项目名称
     */
    private String projectName;

    private static final String DEFAULT_PROJECT_NAME = "Test";

    /**
     * 源文件目录
     */
    private String sourceDirectory;
    private static final String DEFAULT_SOURCE_DIRECTORY = "src/main/java/";

    /**
     * 资源文件目录
     */
    private String resourceDirectory;
    private static final String DEFAULT_RESOURCE_DIRECTORY = "src/main/resources/";

    /**
     * 根包名称
     */
    private String rootPackage;
    private static final String DEFAULT_ROOT_PACKAGE = "com.foo.bar";

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public ProjectBaseInfoConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }

    @Override
    public void fromXmlNode(Node element) {
        Properties properties = parseProperties(element);

        this.projectName = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_PROJECT_NAME);
        this.sourceDirectory = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_SOURCE_DIRECTORY);
        this.resourceDirectory = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_RESOURCE_DIRECTORY);
        this.rootPackage = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_ROOT_PACKAGE);
    }

    @Override
    public Element toXmlNode() {
        return null;
    }

    @Override
    public void validate() {

    }

    public String getProjectName() {
        if (StringUtils.isBlank(projectName)){
            return DEFAULT_PROJECT_NAME;
        }

        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSourceDirectory() {
        if (StringUtils.isBlank(sourceDirectory)){
            return DEFAULT_SOURCE_DIRECTORY;
        }

        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public String getResourceDirectory() {
        if(StringUtils.isBlank(resourceDirectory)){
            return DEFAULT_RESOURCE_DIRECTORY;
        }

        return resourceDirectory;
    }

    public void setResourceDirectory(String resourceDirectory) {
        this.resourceDirectory = resourceDirectory;
    }

    public String getRootPackage() {
        if (StringUtils.isBlank(rootPackage)){
            return DEFAULT_ROOT_PACKAGE;
        }

        return rootPackage;
    }

    public void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    @Override
    public String toString() {
        return "ProjectBaseInfoConfiguration{" +
                "projectName='" + projectName + '\'' +
                ", sourceDirectory='" + sourceDirectory + '\'' +
                ", resourceDirectory='" + resourceDirectory + '\'' +
                ", rootPackage='" + rootPackage + '\'' +
                '}';
    }
}
