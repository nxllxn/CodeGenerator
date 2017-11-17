package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Properties;

/**
 * 包含包名称
 */
public class PositionTypeHolder extends TypeHolder {
    /**
     * 文件存储的目标目录
     */
    protected String targetDirectory;

    /**
     * 文件所在包名称
     */
    protected String targetPackage;

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public PositionTypeHolder(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }

    @Override
    public void fromXmlNode(Node element) {
        super.fromXmlNode(element);

        Properties properties = parseProperties(element);

        this.targetDirectory = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_TARGET_DIRECTORY);
        this.targetPackage = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_TARGET_PACKAGE);
    }

    @Override
    public Element toXmlNode() {
        return null;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    @Override
    public String toString() {
        return "PositionTypeHolder{" +
                "type='" + type + '\'' +
                ", targetDirectory='" + targetDirectory + '\'' +
                ", targetPackage='" + targetPackage + '\'' +
                '}';
    }
}
