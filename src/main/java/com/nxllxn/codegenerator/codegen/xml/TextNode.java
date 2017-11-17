package com.nxllxn.codegenerator.codegen.xml;

import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import com.nxllxn.codegenerator.codegen.service.TemplateBasedCodeAssembleServiceImpl;
import com.nxllxn.codegenerator.utils.AssembleUtil;

import static com.nxllxn.codegenerator.utils.AssembleUtil.indentWith;

/**
 * 普通文本节点
 *
 * @author wenchao
 */
public class TextNode extends AbstractXmlUnit {
    /**
     * 当前节点文本内容
     */
    private String text;

    /**
     * Xml 中空行是一个以换行符为内容的文本节点
     */
    public static final TextNode EMPTY_LINE_TEXT_NODE = new TextNode("");

    public TextNode() {
    }

    public TextNode(String text) {
        this.text = text;
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        if (text == null){
            return "";
        }

        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder formattedContentBuilder = new StringBuilder();

        //缩进
        indentWith(formattedContentBuilder,indentLevel);

        //构造文本内容
        formattedContentBuilder.append(codeAssembleService.assembleTextNode(text));

        return formattedContentBuilder.toString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL);
    }

    @Override
    public boolean hasChildNode() {
        return false;
    }
}
