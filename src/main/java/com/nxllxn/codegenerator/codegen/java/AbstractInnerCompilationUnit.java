package com.nxllxn.codegenerator.codegen.java;

import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import com.nxllxn.codegenerator.codegen.service.TemplateBasedCodeAssembleServiceImpl;
import com.nxllxn.codegenerator.utils.AssembleUtil;
import com.nxllxn.codegenerator.utils.RegExpUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import static com.nxllxn.codegenerator.utils.AssembleUtil.DEFAULT_INDENT_LEVEL;
import static com.nxllxn.codegenerator.utils.AssembleUtil.indentWith;
import static com.nxllxn.codegenerator.utils.AssembleUtil.startNewLine;

/**
 * 内部编译单元的抽象实现，用于提供必要的属性以及方法
 *
 * @author wenchao
 */
public abstract class AbstractInnerCompilationUnit implements InnerCompilationUnit {
    /**
     * 当前类型的类型评论信息
     */
    protected List<String> typeComments;

    /**
     * 当前类型的类型注解信息
     */
    protected Set<String> typeAnnotations;

    /**
     * 当前类型的可见性
     */
    protected Visibility visibility;

    /**
     * 当前类型是否为static类型
     */
    protected boolean isStatic;

    /**
     * 当前类型是否为Final类型
     */
    protected boolean isFinal;

    /**
     * 当前类型全限定基本信息
     */
    protected FullyQualifiedJavaType type;

    /**
     * 当前类型继承的超类
     */
    protected FullyQualifiedJavaType superClass;

    /**
     * 当前类型实现的接口
     */
    protected Set<FullyQualifiedJavaType> superInterfaces;

    /**
     * 额外的非静态导入
     */
    protected Set<FullyQualifiedJavaType> extraNonStaticImports;

    public AbstractInnerCompilationUnit() {
        this.typeComments = new ArrayList<>();
        this.typeAnnotations = new HashSet<>();
        this.superInterfaces = new HashSet<>();

        this.extraNonStaticImports = new HashSet<>();

        //默认为Default可见性
        this.visibility = Visibility.DEFAULT;
    }

    @Override
    public void addAnnotation(String annotation) {
        this.typeAnnotations.add(annotation);
    }

    @Override
    public void addAnnotations(Set<String> annotations) {
        if (annotations == null){
            return;
        }

        this.typeAnnotations.addAll(annotations);
    }

    @Override
    public void addTypeComment(String typeComment) {
        this.typeComments.add(typeComment);
    }

    @Override
    public void addTypeComments(Set<String> typeComments) {
        if (typeComments == null){
            return;
        }

        this.typeComments.addAll(typeComments);
    }

    @Override
    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        this.superInterfaces.add(superInterface);
    }

    @Override
    public void addSuperInterfaces(List<FullyQualifiedJavaType> superInterfaces) {
        if (superInterfaces == null || superInterfaces.isEmpty()) {
            return;
        }

        this.superInterfaces.addAll(superInterfaces);
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public FullyQualifiedJavaType getType() {
        return type;
    }

    @Override
    public List<String> getTypeComments() {
        return typeComments;
    }

    @Override
    public Set<String> getTypeAnnotations() {
        return typeAnnotations;
    }

    @Override
    public FullyQualifiedJavaType getSuperClass() {
        return superClass;
    }

    @Override
    public Set<FullyQualifiedJavaType> getSuperInterfaces() {
        return superInterfaces;
    }

    @Override
    public String getFormattedContent() {
        return getFormattedContent(DEFAULT_INDENT_LEVEL);
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        if (type == null){
            return "";
        }

        StringBuilder formattedContentBuilder = new StringBuilder();

        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        //类评论
        formattedContentBuilder.append(codeAssembleService.assembleJavaDoc(typeComments,indentLevel));

        //注解
        formattedContentBuilder.append(codeAssembleService.assembleClassAnnotation(typeAnnotations,indentLevel));

        //类声明前面加缩进
        indentWith(formattedContentBuilder,indentLevel);

        //修饰符
        assembleModifiers(formattedContentBuilder, codeAssembleService,indentLevel);

        //类型名称
        formattedContentBuilder.append(codeAssembleService.assembleTypeDefinition(type));

        //泛型参数
        assembleTypeParameters(formattedContentBuilder,codeAssembleService);

        //类继承
        formattedContentBuilder.append(codeAssembleService.assembleClassExtension(superClass));

        //接口实现
        formattedContentBuilder.append(codeAssembleService.assembleInterfaceImplements(superInterfaces));

        //代码块开始标识符
        formattedContentBuilder.append(AssembleUtil.BLOCK_OPEN_IDENTIFIER);

        //换行
        startNewLine(formattedContentBuilder);

        //类，接口，枚举的主体代码
        assembleMainBlock(formattedContentBuilder,codeAssembleService,indentLevel + AssembleUtil.INDENT_LEVEL_INCREASED);

        //类代码块结束标识符}前面加缩进
        indentWith(formattedContentBuilder,indentLevel);

        //如果代码块没有代码，那么取消开始标识符{与结束标识符}之间的空白字符
        Matcher invalidSpaceMatcher = RegExpUtil.PATTERN_FOR_CODE_BLOCK_OPEN_IDENTIFIER.matcher(formattedContentBuilder.toString());
        if (invalidSpaceMatcher.find()){
            formattedContentBuilder.setLength(formattedContentBuilder.length() - invalidSpaceMatcher.group(1).length());
        }

        //代码块结束标识符
        formattedContentBuilder.append(AssembleUtil.BLOCK_CLOSE_IDENTIFIER);

        return formattedContentBuilder.toString();
    }

    /**
     * 组装泛型参数
     * @param formattedContentBuilder 代码Builder
     * @param codeAssembleService 代码组装服务
     */
    protected abstract void assembleTypeParameters(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService);

    /**
     * 组装类，接口，枚举的主体代码
     * @param formattedContentBuilder 代码builder
     * @param codeAssembleService 代码组装服务
     * @param indentLevel 缩进等级
     */
    protected abstract void assembleMainBlock(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService,int indentLevel);

    /**
     * 组装当前元素的修饰符号
     * @param formattedContentBuilder StringBuilder对喜爱
     * @param codeAssembleService 代码组装服务
     * @param indentLevel 缩进等级
     */
    protected void assembleModifiers(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService,int indentLevel) {
        formattedContentBuilder.append(codeAssembleService.assembleVisibility(visibility));
        formattedContentBuilder.append(codeAssembleService.assembleStaticModifier(isStatic));
        formattedContentBuilder.append(codeAssembleService.assembleFinalModifier(isFinal));
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public void setType(FullyQualifiedJavaType type) {
        this.type = type;
    }

    public void setSuperClass(FullyQualifiedJavaType superClass) {
        this.superClass = superClass;
    }

    @Override
    public Set<FullyQualifiedJavaType> calculateNonStaticImports() {
        Set<FullyQualifiedJavaType> nonStaticImports = new HashSet<>();

        if(superClass != null){
            nonStaticImports.add(superClass);
        }

        nonStaticImports.addAll(superInterfaces);
        nonStaticImports.addAll(extraNonStaticImports);

        return nonStaticImports;
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
}
