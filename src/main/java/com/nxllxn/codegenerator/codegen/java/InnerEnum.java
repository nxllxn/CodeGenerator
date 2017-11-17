package com.nxllxn.codegenerator.codegen.java;

import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.nxllxn.codegenerator.utils.AssembleUtil.*;

/**
 * 内部枚举类型描述实体类
 *
 * @author wenchao
 */
public class InnerEnum extends AbstractInnerCompilationUnit {
    /**
     * 枚举包含的字段列表
     */
    protected List<Field> fields;

    /**
     * 枚举包含的内部类
     */
    protected List<InnerClass> innerClasses;

    /**
     * 枚举包含的内部枚举
     */
    protected List<InnerEnum> innerEnums;

    /**
     * 枚举包含的内部方法
     */
    protected List<Method> methods;

    /**
     * 枚举包含的枚举常量
     */
    protected List<EnumConstant> enumConstants;

    public InnerEnum() {
        fields = new ArrayList<>();
        innerClasses = new ArrayList<>();
        innerEnums = new ArrayList<>();
        methods = new ArrayList<>();
        enumConstants = new ArrayList<>();
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
        return true;
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        return super.getFormattedContent(indentLevel);
    }

    @Override
    protected void assembleMainBlock(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel) {
        if (enumConstants != null && !enumConstants.isEmpty()) {
            boolean isFirst = true;
            for (EnumConstant enumConstant : enumConstants) {
                if (isFirst){
                    isFirst = false;
                } else {
                    //枚举常量后面的逗号
                    formattedContentBuilder.append(",");
                    startNewLine(formattedContentBuilder);
                }

                formattedContentBuilder.append(enumConstant.getFormattedContent(indentLevel, fields));
            }

            //最后一行枚举常量后面是逗号
            formattedContentBuilder.append(";");

            extraEmptyLine(formattedContentBuilder);
        }

        //如果没有枚举常量，也必须添加一个空白的分号，并且提供相应的缩进
        if (enumConstants == null || enumConstants.isEmpty()){
            //添加缩进
            indentWith(formattedContentBuilder,indentLevel);
            //添加一个分号
            formattedContentBuilder.append(";");

            extraEmptyLine(formattedContentBuilder);
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


        for (InnerEnum innerEnum : innerEnums) {
            formattedContentBuilder.append(innerEnum.getFormattedContent(indentLevel));

            //额外空行
            extraEmptyLine(formattedContentBuilder);
        }

        //移除代码块最后面的空行
        removeExtraEmptyLineInCodeBlockEnd(formattedContentBuilder);
    }

    @Override
    protected void assembleModifiers(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel) {
        super.assembleModifiers(formattedContentBuilder, codeAssembleService, indentLevel);

        formattedContentBuilder.append(codeAssembleService.assembleElementTypeEnum());
    }

    @Override
    protected void assembleTypeParameters(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService) {
        //do nothing,枚举类型没有泛型参数
    }

    @Override
    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        //do nothing,枚举类型没有父接口
    }

    @Override
    public void addSuperInterfaces(List<FullyQualifiedJavaType> superInterfaces) {
        //do nothing,枚举类型没有父接口
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<InnerClass> getInnerClasses() {
        return innerClasses;
    }

    public void setInnerClasses(List<InnerClass> innerClasses) {
        this.innerClasses = innerClasses;
    }

    public List<InnerEnum> getInnerEnums() {
        return innerEnums;
    }

    public void setInnerEnums(List<InnerEnum> innerEnums) {
        this.innerEnums = innerEnums;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public List<EnumConstant> getEnumConstants() {
        return enumConstants;
    }

    public void setEnumConstants(List<EnumConstant> enumConstants) {
        this.enumConstants = enumConstants;
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

    public void addEnumConstant(EnumConstant enumConstant) {
        if (enumConstant == null) {
            return;
        }

        this.enumConstants.add(enumConstant);
    }

    public void addEnumConstants(List<EnumConstant> enumConstants) {
        if (enumConstants == null || enumConstants.isEmpty()) {
            return;
        }

        this.enumConstants.addAll(enumConstants);
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

        for (InnerEnum innerEnum:innerEnums){
            nonStaticImports.addAll(innerEnum.calculateNonStaticImports());
        }

        return nonStaticImports;
    }

    @Override
    public String toString() {
        return "InnerEnum{" +
                "fields=" + fields +
                ", innerClasses=" + innerClasses +
                ", innerEnums=" + innerEnums +
                ", typeComments=" + typeComments +
                ", methods=" + methods +
                ", typeAnnotations=" + typeAnnotations +
                ", enumConstants=" + enumConstants +
                ", visibility=" + visibility +
                ", isStatic=" + isStatic +
                ", isFinal=" + isFinal +
                ", type=" + type +
                ", superClass=" + superClass +
                ", superInterfaces=" + superInterfaces +
                '}';
    }
}
