package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import com.nxllxn.codegenerator.exception.NeedForImplementException;
import com.nxllxn.codegenerator.exception.XMLParseException;
import com.nxllxn.codegenerator.message.Messages;
import com.nxllxn.codegenerator.utils.ObjectFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 用于加载xml文件中定义的额外资源属性文件
 */
public class ExtraPropertiesConfiguration extends AbstractConfiguration implements Configuration {
    /**
     * 日志：SLF4J + Log4j
     */
    private Logger logger = LoggerFactory.getLogger(ExtraPropertiesConfiguration.class);

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public ExtraPropertiesConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }

    @Override
    public void fromXmlNode(Node element) {
        Properties properties = parseProperties(element);

        String resource = properties.getProperty(XmlConstantsRegistry.EXTRA_PROPERTIES_RESOURCE);
        String url = properties.getProperty(XmlConstantsRegistry.EXTRA_PROPERTIES_URL);

        InputStream inputStream = null;
        try {
            if (StringUtils.isBlank(resource) && StringUtils.isBlank(url)) {
                throw new XMLParseException(Messages.forMessage(Messages.MessageKey.NEED_FOR_IMPLEMENT));
            }

            //先加载资源文件
            URL resourceUrl = ObjectFactory.getResource(resource);

            //如果未加载到resource指定的资源文件，那么再加载url指定的资源
            if (resourceUrl == null) {
                resourceUrl = new URL(url);
            }

            inputStream = resourceUrl.openConnection().getInputStream();
            extraProperties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new XMLParseException(Messages.forMessage(Messages.MessageKey.NEED_FOR_IMPLEMENT));
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                //do Nothing but record a warn log
                logger.warn("加载配置资源文件，输入流关闭失败！");
            }
        }
    }

    @Override
    public Element toXmlNode() {
        throw new NeedForImplementException();
    }

    @Override
    public void validate() {
        throw new NeedForImplementException();
    }
}
