package com.nxllxn.codegenerator.codegen.xml;

import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import com.nxllxn.codegenerator.codegen.service.TemplateBasedCodeAssembleServiceImpl;
import org.apache.commons.lang3.StringUtils;

import static com.nxllxn.codegenerator.utils.AssembleUtil.extraEmptyLine;
import static com.nxllxn.codegenerator.utils.AssembleUtil.startNewLine;

/**
 * Xml文档描述实体类
 *
 * @author wenchao
 */
public class XmlDocument extends AbstractXmlUnit {
    /**
     * xml文档的public id
     */
    private String publicId;

    /**
     * xml文档的systemId
     */
    private String systemId;

    /**
     * xml文档的根节点
     */
    private TagNode rootNode;

    @Override
    public String getFormattedContent(int indentLevel) {
        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder formattedContentBuilder = new StringBuilder();

        //XML文档Header
        formattedContentBuilder.append(codeAssembleService.assembleXmlHeader());

        //换行
        startNewLine(formattedContentBuilder);

        if (rootNode == null || StringUtils.isBlank(rootNode.getName())){
            return formattedContentBuilder.toString();
        }

        //public Id & system Id
        formattedContentBuilder.append(codeAssembleService.assemblePublicIdAndSystemId(rootNode.getName(),publicId,systemId));

        //添加一个额外的空行
        extraEmptyLine(formattedContentBuilder);

        //根节点内容
        formattedContentBuilder.append(rootNode.getFormattedContent(indentLevel));

        return formattedContentBuilder.toString();
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public TagNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TagNode rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    public String toString() {
        return "XmlDocument{" +
                "publicId='" + publicId + '\'' +
                ", systemId='" + systemId + '\'' +
                ", rootNode=" + rootNode +
                '}';
    }

    @Override
    public boolean hasChildNode() {
        return false;
    }
}
