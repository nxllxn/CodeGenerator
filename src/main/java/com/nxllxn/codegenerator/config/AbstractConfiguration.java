package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.codegen.xml.AttributeNode;
import com.nxllxn.codegenerator.codegen.xml.TagNode;
import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Configuration接口的抽象实现，用于提供一些默认的实现（PS：接口內部不提供默认实现）
 */
public abstract class AbstractConfiguration implements Configuration{

    /**
     * 系统环境变量中的属性
     */
    protected Properties systemProperties;

    /**
     * 用户自定义属性
     */
    protected Properties extraProperties;

    /**
     * 从配置资源中加载的属性
     */
    protected Properties configProperties;

    /**
     * 当前配置节点自定义属性
     */
    protected Properties customProperties;

    /**
     * 构造函数
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties 用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public AbstractConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        this.systemProperties = systemProperties;
        this.extraProperties = extraProperties;
        this.configProperties = configProperties;

        this.customProperties = new Properties();
    }

    /**
     * 解析指定属性配置节点里面包含的全部属性（此部分代码摘抄自MyBatisCodeGenerator MyBatisGeneratorConfigurationParser实现）
     * @param node 指定Xml节点
     * @return 解析得到的属性条目
     */
    protected Properties parseProperties(Node node) {
        Properties attributes = new Properties();
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node attribute = nnm.item(i);
            String value = parsePropertyTokens(attribute.getNodeValue());
            attributes.put(attribute.getNodeName(), value);
        }

        return attributes;
    }

    /**
     * 循环解析由属性占位符占位的属性
     * @param attributeLiteralStr 配置属性文本字符串（未解析）
     * @return 解析后的属性值
     */
    private String parsePropertyTokens(String attributeLiteralStr) {
        final String OPEN = "${";
        final String CLOSE = "}";

        String newString = attributeLiteralStr;
        if (newString != null) {
            int start = newString.indexOf(OPEN);
            int end = newString.indexOf(CLOSE);

            while (start > -1 && end > start) {
                String prepend = newString.substring(0, start);
                String append = newString.substring(end + CLOSE.length());
                String propName = newString.substring(start + OPEN.length(),
                        end);
                String propValue = resolveProperty(propName);
                if (propValue != null) {
                    newString = prepend + propValue + append;
                }

                start = newString.indexOf(OPEN, end);
                end = newString.indexOf(CLOSE, end);
            }
        }

        return newString;
    }

    /**
     * 从已有的属性中为当前这次属性解析提供解决方案
     * @param key 指定属性Key
     * @return 指定属性key在系统属性，用户自定义属性，已解析的配置文件中的属性中的对应值,当前节点自定义属性。优先级 系统属性>用户自定义属性>配置文件中已经解析的属性>当前节点自定义属性
     */
    private String resolveProperty(String key) {
        String property;

        property = systemProperties.getProperty(key);

        if (property == null) {
            property = configProperties.getProperty(key);
        }

        if (property == null) {
            property = extraProperties.getProperty(key);
        }

        if (property == null) {
            property = customProperties.getProperty(key);
        }

        return property;
    }

    protected void parseProperty(Node node) {
        Properties attributes = parseProperties(node);

        String name = attributes.getProperty("name");
        String value = attributes.getProperty("value");

        customProperties.setProperty(name, value);
    }

    protected void addPropertyXmlElements(TagNode tagNode) {
        Enumeration<?> enumeration = customProperties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String propertyName = (String) enumeration.nextElement();

            TagNode node = new TagNode();
            node.setName("property");

            node.addAttributeNode(new AttributeNode("name", propertyName));
            node.addAttributeNode(new AttributeNode("value", customProperties.getProperty(propertyName)));

            tagNode.addChildNode(node);
        }
    }

    /**
     * 解析当前节点内部自定义的属性
     * @param node 当前节点
     * @return 当前节点内部自定义的属性
     */
    protected Properties parseCustomProperties(Node node){
        Properties properties = new Properties();

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if (StringUtils.isBlank(childNode.getNodeName())){
                continue;
            }

            if (XmlConstantsRegistry.NODE_NAME_PROPERTY.equals(childNode.getNodeName())) {
                Properties attributes = parseProperties(childNode);

                String name = attributes.getProperty("name");
                String value = attributes.getProperty("value");

                properties.put(name,value);
            }
        }

        return properties;
    }

    public Properties getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Properties systemProperties) {
        this.systemProperties = systemProperties;
    }

    public Properties getExtraProperties() {
        return extraProperties;
    }

    public void setExtraProperties(Properties extraProperties) {
        this.extraProperties = extraProperties;
    }

    public Properties getConfigProperties() {
        return configProperties;
    }

    public void setConfigProperties(Properties configProperties) {
        this.configProperties = configProperties;
    }

    public Properties getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(Properties customProperties) {
        this.customProperties = customProperties;
    }

    @Override
    public String toString() {
        return "AbstractConfiguration{" +
                ", extraProperties=" + extraProperties +
                ", configProperties=" + configProperties +
                ", customProperties=" + customProperties +
                '}';
    }
}
