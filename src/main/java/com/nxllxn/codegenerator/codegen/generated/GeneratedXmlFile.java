package com.nxllxn.codegenerator.codegen.generated;

import com.nxllxn.codegenerator.codegen.xml.XmlDocument;

/**
 * 代码生成的xml文件描述类
 *
 * @author wenchao
 */
public class GeneratedXmlFile extends AbstractGeneratedFile {
    /**
     * xml文件名称，通常是modelName + Mapper
     */
    private String fileName;

    /**
     * xml文档内容
     */
    private XmlDocument xmlDocument;

    /**
     * xml文件的扩展名
     */
    private static final String XML_FILE_EXTENSION = ".xml";

    @Override
    public String getFormattedContent() {
        return xmlDocument.getFormattedContent();
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getFileExtension() {
        return XML_FILE_EXTENSION;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public XmlDocument getXmlDocument() {
        return xmlDocument;
    }

    public void setXmlDocument(XmlDocument xmlDocument) {
        this.xmlDocument = xmlDocument;
    }
}
