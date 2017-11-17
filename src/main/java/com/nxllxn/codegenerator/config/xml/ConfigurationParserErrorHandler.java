package com.nxllxn.codegenerator.config.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 文档解析过程中异常信息处理器
 */
public class ConfigurationParserErrorHandler implements ErrorHandler{
    /**
     * 日志 SLF4J+log4j
     */
    private Logger logger = LoggerFactory.getLogger(ConfigurationParserErrorHandler.class);

    private boolean hasErrorOccurred;

    public ConfigurationParserErrorHandler() {
        this.hasErrorOccurred = false;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        logger.warn("SAXParseException：at line " + exception.getLineNumber() + " with exception message:" + exception.getMessage());

        hasErrorOccurred = true;
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        logger.error("SAXParseException：at line " + exception.getLineNumber() + " with exception message:" + exception.getMessage());

        hasErrorOccurred = true;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        logger.error("SAXParseException：at line " + exception.getLineNumber() + " with exception message:" + exception.getMessage());

        hasErrorOccurred = true;
    }

    public boolean isHasErrorOccurred() {
        return hasErrorOccurred;
    }

    public void setHasErrorOccurred(boolean hasErrorOccurred) {
        this.hasErrorOccurred = hasErrorOccurred;
    }
}
