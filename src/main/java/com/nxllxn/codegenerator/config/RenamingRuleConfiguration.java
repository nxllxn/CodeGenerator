package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Properties;

/**
 * 重命名规则 内部实现使用String.replaceAll(searchFor,replaceWith)
 */
public class RenamingRuleConfiguration extends AbstractConfiguration {
    /**
     * 匹配regex
     */
    protected String searchFor;

    /**
     * 替换为字符串
     */
    protected String replaceWith;

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public RenamingRuleConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }

    @Override
    public void fromXmlNode(Node element) {
        Properties properties = parseProperties(element);

        this.searchFor = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_SEARCH_FOR);
        this.replaceWith = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_REPLACE_WITH);
    }

    @Override
    public Element toXmlNode() {
        return null;
    }

    @Override
    public void validate() {

    }

    public String getSearchFor() {
        return searchFor;
    }

    public void setSearchFor(String searchFor) {
        this.searchFor = searchFor;
    }

    public String getReplaceWith() {
        return replaceWith;
    }

    public void setReplaceWith(String replaceWith) {
        this.replaceWith = replaceWith;
    }

    @Override
    public String toString() {
        return "RenamingRuleConfiguration{" +
                "searchFor='" + searchFor + '\'' +
                ", replaceWith='" + replaceWith + '\'' +
                '}';
    }
}
