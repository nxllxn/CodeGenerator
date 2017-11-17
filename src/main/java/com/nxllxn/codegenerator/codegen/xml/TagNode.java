package com.nxllxn.codegenerator.codegen.xml;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.nxllxn.codegenerator.utils.AssembleUtil.*;

/**
 * Xml标签节点
 *
 * @author wenchao
 */
public class TagNode extends AbstractXmlUnit {
    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点属性列表
     */
    private List<XmlUnit> attributeNodes;

    /**
     * 当前节点子节点列表
     */
    private List<XmlUnit> childNodes;

    public TagNode() {
        this.attributeNodes = new ArrayList<>();
        this.childNodes = new ArrayList<>();
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        if (StringUtils.isBlank(name)) {
            return "";
        }

        StringBuilder formattedContentBuilder = new StringBuilder();

        //缩进
        indentWith(formattedContentBuilder, indentLevel);

        formattedContentBuilder.append("<");

        formattedContentBuilder.append(name);

        if (attributeNodes != null && !attributeNodes.isEmpty()) {
            formattedContentBuilder.append(" ");

            List<String> codeParts = new ArrayList<>();

            String codePart;
            for (XmlUnit attributeNode : attributeNodes) {
                codePart = attributeNode.getFormattedContent();

                if (StringUtils.isBlank(codePart)){
                    continue;
                }

                codeParts.add(codePart);
            }

            formattedContentBuilder.append(StringUtils.join(codeParts.toArray(), " "));
        }

        if (childNodes != null && !childNodes.isEmpty()) {
            if (hasOnlyOneTextNode(childNodes)){
                formattedContentBuilder.append(">");
                formattedContentBuilder.append(childNodes.get(0).getFormattedContent(DEFAULT_INDENT_LEVEL));
                formattedContentBuilder.append("</");
                formattedContentBuilder.append(name);
                formattedContentBuilder.append(">");
            } else {
                formattedContentBuilder.append(">");
                startNewLine(formattedContentBuilder);

                XmlUnit lastChild = null;
                boolean isFirst = true;
                for (XmlUnit childNode : childNodes) {
                    //每个节点中间插入一个空行
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        if (lastChild.hasChildNode()){
                            if (lastChild instanceof TagNode && ((TagNode)lastChild).hasOnlyOneTextNode(((TagNode) lastChild).getChildNodes())){
                                startNewLine(formattedContentBuilder);
                            } else {
                                extraEmptyLine(formattedContentBuilder);
                            }
                        } else {
                            startNewLine(formattedContentBuilder);
                        }
                    }

                    formattedContentBuilder.append(childNode.getFormattedContent(indentLevel + INDENT_LEVEL_INCREASED));

                    lastChild = childNode;
                }

                startNewLine(formattedContentBuilder);
                indentWith(formattedContentBuilder, indentLevel);
                formattedContentBuilder.append("</");
                formattedContentBuilder.append(name);
                formattedContentBuilder.append(">");
            }
        } else {
            formattedContentBuilder.append("/>");
        }

        return formattedContentBuilder.toString();
    }

    private boolean hasOnlyOneTextNode(List<XmlUnit> childNodes) {
        return childNodes.size() == 1 && childNodes.get(0) instanceof TextNode;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<XmlUnit> getAttributeNodes() {
        return attributeNodes;
    }

    public List<XmlUnit> getChildNodes() {
        return childNodes;
    }

    @Override
    public String toString() {
        return "TagNode{" +
                "name='" + name + '\'' +
                ", attributeNodes=" + attributeNodes +
                ", childNodes=" + childNodes +
                '}';
    }

    public void addAttributeNode(AttributeNode attributeNode) {
        if (attributeNode == null) {
            return;
        }

        this.attributeNodes.add(attributeNode);
    }

    public void addAttributeNodes(List<AttributeNode> attributeNodes) {
        if (attributeNodes == null || attributeNodes.isEmpty()) {
            return;
        }

        this.attributeNodes.addAll(attributeNodes);
    }

    public void addChildNode(TextNode xmlUnit) {
        if (xmlUnit == null) {
            return;
        }

        this.childNodes.add(xmlUnit);
    }

    public void addTextNodes(List<TextNode> xmlUnits) {
        if (xmlUnits == null || xmlUnits.isEmpty()) {
            return;
        }

        this.childNodes.addAll(xmlUnits);
    }

    public void addChildNode(TagNode xmlUnit) {
        if (xmlUnit == null) {
            return;
        }

        this.childNodes.add(xmlUnit);
    }

    public void addTagNodes(List<TagNode> xmlUnits) {
        if (xmlUnits == null || xmlUnits.isEmpty()) {
            return;
        }

        this.childNodes.addAll(xmlUnits);
    }

    @Override
    public boolean hasChildNode() {
        return !this.childNodes.isEmpty();
    }
}
