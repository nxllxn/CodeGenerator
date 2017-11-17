package com.nxllxn.codegenerator.config.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * 基于DTD（Document type Definition）的文档实体校验器
 */
public class ConfigurationParserEntityResolver implements EntityResolver{
    /**
     * 日志SLF4J + log4j
     */
    private Logger logger = LoggerFactory.getLogger(ConfigurationParserEntityResolver.class);

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "com/nxllxn/codegenerator/config/xml/confguration-1.0.dtd");

        return new InputSource(is);
    }
}
