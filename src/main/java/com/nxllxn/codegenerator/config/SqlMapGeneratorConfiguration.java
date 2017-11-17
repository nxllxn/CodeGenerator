package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.w3c.dom.Node;

import java.util.Properties;

public class SqlMapGeneratorConfiguration extends PositionTypeHolder{
    /**
     * 命名空间包名，等同于Mapper接口所在包名
     */
    private String nameSpacePackage;

    /**
     * Xml文档使用的publicId，用于文档格式校验
     */
    private String xmlDocumentPublicId;

    /**
     * Xml文档使用的systemId，用于文档格式校验
     */
    private String xmlDocumentSystemId;

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public SqlMapGeneratorConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }

    public String getNameSpacePackage() {
        return nameSpacePackage;
    }

    public void setNameSpacePackage(String nameSpacePackage) {
        this.nameSpacePackage = nameSpacePackage;
    }

    public String getXmlDocumentPublicId() {
        return xmlDocumentPublicId;
    }

    public void setXmlDocumentPublicId(String xmlDocumentPublicId) {
        this.xmlDocumentPublicId = xmlDocumentPublicId;
    }

    public String getXmlDocumentSystemId() {
        return xmlDocumentSystemId;
    }

    public void setXmlDocumentSystemId(String xmlDocumentSystemId) {
        this.xmlDocumentSystemId = xmlDocumentSystemId;
    }

    @Override
    public void fromXmlNode(Node element) {
        super.fromXmlNode(element);

        Properties properties = parseProperties(element);

        this.xmlDocumentPublicId = properties.getProperty(XmlConstantsRegistry.XML_PUBLIC_ID);
        this.xmlDocumentSystemId = properties.getProperty(XmlConstantsRegistry.XML_SYSTEM_ID);
        this.nameSpacePackage = properties.getProperty(XmlConstantsRegistry.MYBATIS_MAPPER_XML_NAME_SPACE_PACKAGE);
    }
}
