package com.nxllxn.codegenerator.codegen.xml;


import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import com.nxllxn.codegenerator.codegen.service.TemplateBasedCodeAssembleServiceImpl;

/**
 * XML属性，此处为了实现的一致性，我们将属性也理解为单独的节点格式
 *
 * @author
 */
public class AttributeNode extends AbstractXmlUnit {
    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性值
     */
    private String value;

    public AttributeNode() {
    }

    public AttributeNode(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder formattedContentBuilder = new StringBuilder();

        //属性没有缩进这么一说

        //属性键值代码
        formattedContentBuilder.append(codeAssembleService.assembleXmlAttributeNode(name,value));

        return formattedContentBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AttributeNode{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean hasChildNode() {
        return false;
    }
}
