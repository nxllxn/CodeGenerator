package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Properties;

/**
 * TypeHolder 其内部有一个全限定java类名称
 */
public class TypeHolder extends AbstractConfiguration {
    protected String type;

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public TypeHolder(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TypeHolder{" +
                "type='" + type + '\'' +
                '}';
    }

    @Override
    public void fromXmlNode(Node element) {
        Properties properties = parseProperties(element);

        this.type = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_TYPE);
    }

    @Override
    public Element toXmlNode() {
        return null;
    }

    @Override
    public void validate() {

    }

    public AbstractCodeGenerator loadCodeGeneratorByType() {
        try {
            Class cls = TypeHolder.class.getClassLoader().loadClass(type);

            return (AbstractCodeGenerator) cls.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
