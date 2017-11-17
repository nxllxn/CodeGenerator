package com.nxllxn.codegenerator.config.xml;

import com.nxllxn.codegenerator.config.Configuration;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.exception.XMLParseException;
import com.nxllxn.codegenerator.message.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Properties;

/**
 * 配置资源解析工具类
 */
public class ConfigurationParser {
    /**
     * 日志SLF4J
     */
    private Logger logger = LoggerFactory.getLogger(ConfigurationParser.class);

    /**
     * 系统环境变量中的属性
     */
    private Properties systemProperties;

    /**
     * 用户自定义属性
     */
    private Properties extraProperties;

    /**
     * 从配置资源中加载的属性
     */
    private Properties configProperties;

    public ConfigurationParser() {
        this(null);
    }

    public ConfigurationParser(Properties configProperties) {
        //用户自定义属性
        this.configProperties = configProperties == null ? new Properties() : configProperties;

        //系统属性
        systemProperties = System.getProperties();

        //稍后在资源解析的过程中进行加载
        extraProperties = new Properties();
    }

    public Configuration parseConfiguration(File configFile) throws IOException {
        FileReader fileReader = new FileReader(configFile);

        return parseConfiguration(fileReader);
    }

    public Configuration parseConfiguration(Reader configFileReader) throws IOException {
        InputSource inputSource = new InputSource(configFileReader);

        return parseConfiguration(inputSource);
    }

    public Configuration parseConfiguration(InputStream configInputStream) throws IOException {
        InputSource inputSource = new InputSource(configInputStream);

        return parseConfiguration(inputSource);
    }

    public Configuration parseConfiguration(InputSource configInputSource) throws IOException {
        try {
            //文档构建器工厂
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);

            //获取文档构造器并设置EntityResolver以及ErrorHandler
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setEntityResolver(new ConfigurationParserEntityResolver());
            documentBuilder.setErrorHandler(new ConfigurationParserErrorHandler());

            //解析xml得到Document对象
            Document document = documentBuilder.parse(configInputSource);

            //如果跟节点不是一个有效的元素节点，那么直接抛出异常
            Element rootNode = document.getDocumentElement();
            if (rootNode.getNodeType() != Node.ELEMENT_NODE){
                throw new XMLParseException(Messages.forMessage(Messages.MessageKey.NEED_FOR_IMPLEMENT));
            }

            //如果xml文档publicId不是我们定义的publicId,那么直接抛出异常
            DocumentType documentType = document.getDoctype();
            if (!documentType.getPublicId().equals(XmlConstantsRegistry.DEFAULT_XML_PUBLIC_ID)){
                throw new XMLParseException(Messages.forMessage(Messages.MessageKey.NEED_FOR_IMPLEMENT));
            }

            //初步校验，没有什么问题，那么进行实际的配置解析
            Configuration configuration;

            configuration = parseConfiguration(rootNode);

            return configuration;
        } catch (SAXException e) {
            logger.error("SAXException:" + e.getMessage());

            throw new XMLParseException(e.getMessage());
        }catch (ParserConfigurationException e){
            logger.error("ParserConfigurationException:" + e.getMessage());

            throw new XMLParseException(e.getMessage());
        }
    }

    /**
     * 解析指定Xml节点以及其各个子节点（递归）得到配置信息
     * @param rootNode xml节点
     * @return 配置信息
     */
    private Configuration parseConfiguration(Element rootNode) {
        Context context = new Context(systemProperties,extraProperties,configProperties);

        context.fromXmlNode(rootNode);

        return context;
    }
}
