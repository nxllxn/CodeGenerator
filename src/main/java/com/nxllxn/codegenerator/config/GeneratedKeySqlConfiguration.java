package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Properties;

/**
 * generated key使用的sql语法配置
 */
public class GeneratedKeySqlConfiguration extends AbstractConfiguration{
    private String keyColumn;

    private String generatedKeyStatement;

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public GeneratedKeySqlConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }


    public String getGeneratedKeyStatement() {
        return generatedKeyStatement;
    }

    public void setGeneratedKeyStatement(String generatedKeyStatement) {
        this.generatedKeyStatement = generatedKeyStatement;
    }

    @Override
    public String toString() {
        return "GeneratedKeySqlConfiguration{" +
                "keyColumn='" + keyColumn + '\'' +
                ", generatedKeyStatement='" + generatedKeyStatement + '\'' +
                '}';
    }

    @Override
    public void fromXmlNode(Node element) {
        Properties properties = parseProperties(element);

        this.keyColumn = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_COLUMN);
        this.generatedKeyStatement = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_SQL_STATEMENT);
    }

    @Override
    public Element toXmlNode() {
        return null;
    }

    @Override
    public void validate() {

    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }
}
