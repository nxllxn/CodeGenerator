package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.w3c.dom.Node;

import java.util.Properties;

/**
 * Maven pom文件生成相关配置
 */
public class PomGeneratorConfiguration extends PositionTypeHolder {
    private String publicId;
    private String systemId;

    private String namespace;
    private String xsiNamespace;

    public static final String PACKAGE_TYPE_JAR = "jar";
    public static final String PACKAGE_TYPE_WAR = "war";

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public PomGeneratorConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }

    @Override
    public void fromXmlNode(Node element) {
        super.fromXmlNode(element);

        Properties properties = parseProperties(element);

        publicId = properties.getProperty(XmlConstantsRegistry.XML_PUBLIC_ID);
        systemId = properties.getProperty(XmlConstantsRegistry.XML_SYSTEM_ID);
        namespace = properties.getProperty(XmlConstantsRegistry.XML_NAMESPACE);
        xsiNamespace = properties.getProperty(XmlConstantsRegistry.XML_XSI_NAMESPACE);
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getXsiNamespace() {
        return xsiNamespace;
    }

    public void setXsiNamespace(String xsiNamespace) {
        this.xsiNamespace = xsiNamespace;
    }

    @Override
    public String toString() {
        return "PomGeneratorConfiguration{" +
                "publicId='" + publicId + '\'' +
                ", systemId='" + systemId + '\'' +
                ", type='" + type + '\'' +
                ", targetDirectory='" + targetDirectory + '\'' +
                ", targetPackage='" + targetPackage + '\'' +
                '}';
    }
}
