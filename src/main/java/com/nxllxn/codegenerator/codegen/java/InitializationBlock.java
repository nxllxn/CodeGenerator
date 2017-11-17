package com.nxllxn.codegenerator.codegen.java;


import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import com.nxllxn.codegenerator.codegen.service.TemplateBasedCodeAssembleServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nxllxn.codegenerator.utils.AssembleUtil.*;

/**
 * 初始化代码块儿描述实体
 *
 * @author wenchao
 */
public class InitializationBlock implements Unit{
    /**
     * 代码块javaDoc注释
     */
    private List<String> blockComments;

    /**
     * 是否为静态代码块
     */
    private boolean isStatic;

    /**
     * 代码块内容
     */
    private List<String> bodyLines;

    /**
     * 额外的非静态导入
     */
    protected Set<FullyQualifiedJavaType> extraNonStaticImports;

    public InitializationBlock() {
        this.blockComments = new ArrayList<>();
        this.bodyLines = new ArrayList<>();

        this.extraNonStaticImports = new HashSet<>();
    }

    public List<String> getBlockComments() {
        return blockComments;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public List<String> getBodyLines() {
        return bodyLines;
    }

    @Override
    public String toString() {
        return "InitializationBlock{" +
                "blockComments=" + blockComments +
                ", isStatic=" + isStatic +
                ", bodyLines=" + bodyLines +
                '}';
    }

    @Override
    public boolean isClass() {
        return false;
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public boolean isEnumeration() {
        return false;
    }

    public void addBlockComment(String blockComment){
        this.blockComments.add(blockComment);
    }

    public void addBlockComments(List<String> blockComments){
        if (blockComments == null || blockComments.isEmpty()){
            return;
        }

        this.blockComments.addAll(blockComments);
    }

    public void addBodyLine(String bodyLine){
        this.bodyLines.add(bodyLine);
    }

    public void addBodyLines(List<String> bodyLines){
        if (bodyLines == null || bodyLines.isEmpty()){
            return;
        }

        this.bodyLines.addAll(bodyLines);
    }

    @Override
    public String getFormattedContent() {
        return getFormattedContent(DEFAULT_INDENT_LEVEL);
    }

    @Override
    public String getFormattedContent(int indentLevel){
        StringBuilder formattedContentBuilder = new StringBuilder();

        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        //Java Doc
        formattedContentBuilder.append(codeAssembleService.assembleJavaDoc(blockComments,indentLevel));

        if (isStatic()){
            //代码块开始block
            indentWith(formattedContentBuilder,indentLevel);
            formattedContentBuilder.append("static " + BLOCK_OPEN_IDENTIFIER);
            startNewLine(formattedContentBuilder);
        } else {
            //代码块开始block
            indentWith(formattedContentBuilder,indentLevel);
            formattedContentBuilder.append("" + BLOCK_OPEN_IDENTIFIER);
            startNewLine(formattedContentBuilder);
        }

        //代码块主block
        formattedContentBuilder.append(codeAssembleService.assembleCodeBlockBody(bodyLines,indentLevel + INDENT_LEVEL_INCREASED));

        //代码块结束block
        indentWith(formattedContentBuilder,indentLevel);
        formattedContentBuilder.append(BLOCK_CLOSE_IDENTIFIER);
        startNewLine(formattedContentBuilder);

        return formattedContentBuilder.toString();
    }

    public void addExtraNonStaticImport(FullyQualifiedJavaType nonStaticImport){
        if (nonStaticImport == null){
            return;
        }

        this.extraNonStaticImports.add(nonStaticImport);
    }

    public void addExtraNonStaticImport(Set<FullyQualifiedJavaType> nonStaticImports){
        if (nonStaticImports == null || nonStaticImports.isEmpty()){
            return;
        }

        this.extraNonStaticImports.addAll(nonStaticImports);
    }

    /**
     * 计算当前类型需要的非静态导入
     * @return 当前类型包含的非静态导入
     */
    public Set<FullyQualifiedJavaType> calculateNonStaticImports(){
        Set<FullyQualifiedJavaType> nonStaticImports = new HashSet<>();

        nonStaticImports.addAll(extraNonStaticImports);

        return nonStaticImports;
    }
}
