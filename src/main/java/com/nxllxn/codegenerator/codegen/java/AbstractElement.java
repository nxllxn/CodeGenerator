package com.nxllxn.codegenerator.codegen.java;

import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import com.nxllxn.codegenerator.codegen.service.TemplateBasedCodeAssembleServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nxllxn.codegenerator.utils.AssembleUtil.indentWith;

/**
 * 用于描述Java类型中字段，方法的超类
 *
 * @author wenchao
 */
public abstract class AbstractElement {
    /**
     * 元素评论
     */
    protected List<String> elementComments;

    /**
     * 元素注解
     */
    protected Set<String> annotations;

    /**
     * 元素可见性
     */
    protected Visibility visibility;

    /**
     * 是否为静态的
     */
    protected boolean isStatic;

    /**
     * 是否为final的
     */
    protected boolean isFinal;

    /**
     * 额外的非静态导入
     */
    protected Set<FullyQualifiedJavaType> extraNonStaticImports;

    public AbstractElement() {
        elementComments = new ArrayList<>();
        annotations = new HashSet<>();

        extraNonStaticImports = new HashSet<>();

        visibility = Visibility.DEFAULT;
    }

    public void addElementComment(String elementComment) {
        this.elementComments.add(elementComment);
    }

    public void addElementComments(List<String> elementComments){
        if (elementComments == null){
            return;
        }

        this.elementComments.addAll(elementComments);
    }

    public void addAnnotation(String annotation) {
        this.annotations.add(annotation);
    }

    public void addAnnotations(Set<String> annotations){
        if (annotations == null){
            return;
        }

        this.annotations.addAll(annotations);
    }

    /**
     * 用于将当前元素转换为格式化的Java代码
     *
     * @param indentLevel 缩进等级
     *
     * @return 当前元素转对应的格式化的Java代码
     */
    public String getFormattedContent(int indentLevel){
        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder formattedContentBuilder = new StringBuilder();

        //JAVA DOC
        formattedContentBuilder.append(codeAssembleService.assembleJavaDoc(elementComments,indentLevel));

        //注解
        formattedContentBuilder.append(codeAssembleService.assembleFieldAnnotation(annotations,indentLevel));

        //修饰符
        assembleModifiers(formattedContentBuilder,codeAssembleService,indentLevel);

        //mainBlock
        assembleMainBlock(formattedContentBuilder,codeAssembleService,indentLevel);

        return formattedContentBuilder.toString();
    }

    /**
     * 主代码块部分
     * @param formattedContentBuilder 代码builder
     * @param codeAssembleService 代码组装服务
     * @param indentLevel 缩进等级
     */
    protected abstract void assembleMainBlock(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel);

    /**
     * 组装修饰符
     * @param formattedContentBuilder 代码builder
     * @param codeAssembleService 代码组装服务
     * @param indentLevel 缩进等级
     */
    protected void assembleModifiers(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel) {
        indentWith(formattedContentBuilder,indentLevel);

        formattedContentBuilder.append(codeAssembleService.assembleVisibility(visibility));
        formattedContentBuilder.append(codeAssembleService.assembleStaticModifier(isStatic));
        formattedContentBuilder.append(codeAssembleService.assembleFinalModifier(isFinal));
    }

    public List<String> getElementComments() {
        return elementComments;
    }

    public Set<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<String> annotations) {
        this.annotations = annotations;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public void addExtraNonStaticImport(FullyQualifiedJavaType nonStaticImport){
        if (nonStaticImport == null){
            return;
        }

        this.extraNonStaticImports.add(nonStaticImport);
    }

    public void addExtraNonStaticImports(Set<FullyQualifiedJavaType> nonStaticImports){
        if (nonStaticImports == null || nonStaticImports.isEmpty()){
            return;
        }

        this.extraNonStaticImports.addAll(nonStaticImports);
    }

    /**
     * 计算当前元素需要的非静态导入
     * @return 当前元素包含的非静态导入
     */
    public Set<FullyQualifiedJavaType> calculateNonStaticImports(){
        Set<FullyQualifiedJavaType> nonStaticImports = new HashSet<>();

        nonStaticImports.addAll(extraNonStaticImports);

        return nonStaticImports;
    }
}
