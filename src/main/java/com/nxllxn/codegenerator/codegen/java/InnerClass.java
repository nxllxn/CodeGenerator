package com.nxllxn.codegenerator.codegen.java;


import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import com.nxllxn.codegenerator.utils.AssembleUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.nxllxn.codegenerator.utils.AssembleUtil.extraEmptyLine;
import static com.nxllxn.codegenerator.utils.AssembleUtil.removeExtraEmptyLineInCodeBlockEnd;

/**
 * 内部类
 *
 * @author wenchao
 */
public class InnerClass extends AbstractInnerCompilationUnit {
    /**
     * 当前类的TypeParameter
     */
    protected List<TypeParameter> typeParameters;

    /**
     * 标识当前类是否为抽象类
     */
    protected boolean isAbstract;

    /**
     * 当前类的初始化代码块
     */
    protected List<InitializationBlock> initializationBlocks;

    /**
     * 当前类字段列表
     */
    protected List<Field> fields;

    /**
     * 当前类方法列表
     */
    protected List<Method> methods;

    /**
     * 当前类包含的内部类
     */
    protected List<InnerClass> innerClasses;

    /**
     * 当前类包含的内部接口
     */
    protected List<InnerInterface> innerInterfaces;

    /**
     * 当前类包含的内部枚举
     */
    protected List<InnerEnum> innerEnums;

    public InnerClass() {
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.innerClasses = new ArrayList<>();
        this.innerInterfaces = new ArrayList<>();
        this.innerEnums = new ArrayList<>();
        this.typeParameters = new ArrayList<>();
        this.initializationBlocks = new ArrayList<>();
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public boolean isEnumeration() {
        return false;
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        return super.getFormattedContent(indentLevel);
    }

    @Override
    protected void assembleModifiers(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel) {
        formattedContentBuilder.append(codeAssembleService.assembleVisibility(visibility));
        formattedContentBuilder.append(codeAssembleService.assembleAbstractModifier(isAbstract));
        formattedContentBuilder.append(codeAssembleService.assembleStaticModifier(isStatic));
        formattedContentBuilder.append(codeAssembleService.assembleFinalModifier(isFinal));
        formattedContentBuilder.append(codeAssembleService.assembleElementTypeClass());
    }

    @Override
    protected void assembleTypeParameters(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService) {
        if (typeParameters != null && !typeParameters.isEmpty()) {
            //回退当前代码块末端的空格
            AssembleUtil.rollbackExtraSpaceInTheEnd(formattedContentBuilder);

            formattedContentBuilder.append(codeAssembleService.assembleTypeParameters(typeParameters));
        }
    }

    @Override
    protected void assembleMainBlock(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel) {
        //静态初始化代码块
        for (InitializationBlock initializationBlock : initializationBlocks) {
            if (initializationBlock.isStatic()) {
                formattedContentBuilder.append(initializationBlock.getFormattedContent(indentLevel));

                //额外空行
                extraEmptyLine(formattedContentBuilder);
            }
        }

        //非静态初始化代码块
        for (InitializationBlock initializationBlock : initializationBlocks) {
            if (!initializationBlock.isStatic()) {
                formattedContentBuilder.append(initializationBlock.getFormattedContent(indentLevel));

                //额外空行
                extraEmptyLine(formattedContentBuilder);
            }
        }

        //全部字段
        for (Field field : fields) {
            formattedContentBuilder.append(field.getFormattedContent(indentLevel));

            //额外空行
            extraEmptyLine(formattedContentBuilder);
        }

        //全部方法
        for (Method method : methods) {
            formattedContentBuilder.append(method.getFormattedContent(indentLevel));

            //额外空行
            extraEmptyLine(formattedContentBuilder);
        }

        for (InnerClass innerClass : innerClasses) {
            formattedContentBuilder.append(innerClass.getFormattedContent(indentLevel));

            //额外空行
            extraEmptyLine(formattedContentBuilder);
        }

        for (InnerInterface innerInterface : innerInterfaces) {
            formattedContentBuilder.append(innerInterface.getFormattedContent(indentLevel));

            //额外空行
            extraEmptyLine(formattedContentBuilder);
        }

        for (InnerEnum innerEnum : innerEnums) {
            formattedContentBuilder.append(innerEnum.getFormattedContent(indentLevel));

            //额外空行
            extraEmptyLine(formattedContentBuilder);
        }

        //移除代码块最后面的空行
        removeExtraEmptyLineInCodeBlockEnd(formattedContentBuilder);
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<InnerClass> getInnerClasses() {
        return innerClasses;
    }

    public List<InnerInterface> getInnerInterfaces() {
        return innerInterfaces;
    }

    public List<InnerEnum> getInnerEnums() {
        return innerEnums;
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public List<InitializationBlock> getInitializationBlocks() {
        return initializationBlocks;
    }

    public void addField(Field field) {
        if (field == null){
            return;
        }

        this.fields.add(field);
    }

    public void addFields(List<Field> fields) {
        if (fields == null || fields.isEmpty()) {
            return;
        }

        this.fields.addAll(fields);
    }

    public void addMethod(Method method) {
        if (method == null){
            return;
        }

        this.methods.add(method);
    }

    public void addMethods(List<Method> methods) {
        if (methods == null || methods.isEmpty()) {
            return;
        }

        this.methods.addAll(methods);
    }

    public void addInnerClass(InnerClass innerClass) {
        if (innerClass == null){
            return;
        }

        this.innerClasses.add(innerClass);
    }

    public void addInnerClasses(List<InnerClass> innerClasses) {
        if (innerClasses == null || innerClasses.isEmpty()) {
            return;
        }

        this.innerClasses.addAll(innerClasses);
    }

    public void addInnerEnum(InnerEnum innerEnum) {
        if (innerEnum == null){
            return;
        }

        this.innerEnums.add(innerEnum);
    }

    public void addInnerEnums(List<InnerEnum> innerEnums) {
        if (innerEnums == null || innerEnums.isEmpty()) {
            return;
        }

        this.innerEnums.addAll(innerEnums);
    }

    public void addInnerInterface(InnerInterface innerInterface){
        if (innerInterface == null){
            return;
        }

        this.innerInterfaces.add(innerInterface);
    }

    public void addInnerInterfaces(List<InnerInterface> innerInterfaces){
        if (innerInterfaces == null || innerInterfaces.isEmpty()){
            return;
        }

        this.innerInterfaces.addAll(innerInterfaces);
    }

    public void addTypeParameter(TypeParameter typeParameter) {
        if (typeParameter == null){
            return;
        }

        this.typeParameters.add(typeParameter);
    }

    public void addTypeParameters(List<TypeParameter> typeParameters) {
        if (typeParameters == null || typeParameters.isEmpty()) {
            return;
        }

        this.typeParameters.addAll(typeParameters);
    }

    public void addInitializationBlock(InitializationBlock initializationBlock) {
        if (initializationBlock == null){
            return;
        }

        this.initializationBlocks.add(initializationBlock);
    }

    public void addInitializationBlocks(List<InitializationBlock> initializationBlocks) {
        if (initializationBlocks == null || initializationBlocks.isEmpty()) {
            return;
        }

        this.initializationBlocks.addAll(initializationBlocks);
    }

    @Override
    public Set<FullyQualifiedJavaType> calculateNonStaticImports() {
        Set<FullyQualifiedJavaType> nonStaticImports = super.calculateNonStaticImports();

        for (Field field:fields){
            nonStaticImports.addAll(field.calculateNonStaticImports());
        }

        for (Method method:methods){
            nonStaticImports.addAll(method.calculateNonStaticImports());
        }

        for (InnerClass innerClass:innerClasses){
            nonStaticImports.addAll(innerClass.calculateNonStaticImports());
        }

        for (InnerInterface innerInterface:innerInterfaces){
            nonStaticImports.addAll(innerInterface.calculateNonStaticImports());
        }

        for (InnerEnum innerEnum:innerEnums){
            nonStaticImports.addAll(innerEnum.calculateNonStaticImports());
        }

        for (TypeParameter typeParameter:typeParameters){
            nonStaticImports.addAll(typeParameter.calculateNonStaticImports());
        }

        for (InitializationBlock initializationBlock:initializationBlocks){
            nonStaticImports.addAll(initializationBlock.calculateNonStaticImports());
        }

        return nonStaticImports;
    }

    @Override
    public String toString() {
        return "InnerClass{" +
                "isAbstract=" + isAbstract +
                ", fields=" + fields +
                ", methods=" + methods +
                ", innerClasses=" + innerClasses +
                ", innerInterfaces=" + innerInterfaces +
                ", innerEnums=" + innerEnums +
                ", typeParameters=" + typeParameters +
                ", initializationBlocks=" + initializationBlocks +
                '}';
    }
}
