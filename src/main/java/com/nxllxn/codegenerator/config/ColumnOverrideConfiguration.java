package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Properties;

/**
 * 表列覆盖相关设置
 */
public class ColumnOverrideConfiguration extends AbstractConfiguration{
    /**
     * 待覆盖的列名称
     */
    private String column;

    /**
     * 产生的实体属性名称
     */
    private String property;

    /**
     * 属性对应的Java类型
     */
    private String javaType;

    /**
     * 列对应的jdbc类型
     */
    private String jdbcType;

    /**
     * 关联的类型处理器
     */
    private String typeHandler;

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public ColumnOverrideConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }

    @Override
    public void fromXmlNode(Node element) {
        Properties properties = parseProperties(element);
        this.column = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_COLUMN);

        Properties customProperties = parseCustomProperties(element);
        this.property = customProperties.getProperty(XmlConstantsRegistry.ATTR_NAME_PROPERTY);
        this.javaType = customProperties.getProperty(XmlConstantsRegistry.ATTR_NAME_JAVA_TYPE);
        this.jdbcType = customProperties.getProperty(XmlConstantsRegistry.ATTR_NAME_JDBC_TYPE);
        this.typeHandler = customProperties.getProperty(XmlConstantsRegistry.ATTR_NAME_TYPE_HANDLER);
    }

    @Override
    public Element toXmlNode() {
        return null;
    }

    @Override
    public void validate() {

    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }

    @Override
    public String toString() {
        return "ColumnOverrideConfiguration{" +
                "column='" + column + '\'' +
                ", property='" + property + '\'' +
                ", javaType='" + javaType + '\'' +
                ", jdbcType='" + jdbcType + '\'' +
                ", typeHandler='" + typeHandler + '\'' +
                '}';
    }
}
