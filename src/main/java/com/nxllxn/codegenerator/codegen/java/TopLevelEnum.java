package com.nxllxn.codegenerator.codegen.java;

import java.util.List;
import java.util.Set;

/**
 * 顶层枚举类型描述类
 *
 * @author wenchao
 */
public class TopLevelEnum extends AbstractCompilationUnit {
    public TopLevelEnum() {
        innerCompilationUnit = new InnerEnum();
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

    public List<Field> getFields() {
        return ((InnerEnum)innerCompilationUnit).fields;
    }

    public void setFields(List<Field> fields) {
        ((InnerEnum)innerCompilationUnit).fields = fields;
    }

    public List<InnerClass> getInnerClasses() {
        return ((InnerEnum)innerCompilationUnit).innerClasses;
    }

    public void setInnerClasses(List<InnerClass> innerClasses) {
        ((InnerEnum)innerCompilationUnit).innerClasses = innerClasses;
    }

    public List<InnerEnum> getInnerEnums() {
        return ((InnerEnum)innerCompilationUnit).innerEnums;
    }

    public void setInnerEnums(List<InnerEnum> innerEnums) {
        ((InnerEnum)innerCompilationUnit).innerEnums = innerEnums;
    }

    public List<Method> getMethods() {
        return ((InnerEnum)innerCompilationUnit).methods;
    }

    public void setMethods(List<Method> methods) {
        ((InnerEnum)innerCompilationUnit).methods = methods;
    }

    public List<EnumConstant> getEnumConstants() {
        return ((InnerEnum)innerCompilationUnit).enumConstants;
    }

    public void setEnumConstants(List<EnumConstant> enumConstants) {
        ((InnerEnum)innerCompilationUnit).enumConstants = enumConstants;
    }

    public InnerEnum getInnerEnum() {
        return ((InnerEnum)innerCompilationUnit);
    }

    public void setInnerEnum(InnerEnum innerEnum) {
        this.innerCompilationUnit = innerEnum;
    }

    public void addField(Field field){
        ((InnerEnum)innerCompilationUnit).addField(field);
    }

    public void addFields(List<Field> fields){
        ((InnerEnum)innerCompilationUnit).addFields(fields);
    }

    public void addMethod(Method method){
        ((InnerEnum)innerCompilationUnit).addMethod(method);
    }

    public void addMethods(List<Method> methods){
        ((InnerEnum)innerCompilationUnit).addMethods(methods);
    }

    public void addInnerClass(InnerClass innerClass){
        ((InnerEnum)innerCompilationUnit).addInnerClass(innerClass);
    }

    public void addInnerClasses(List<InnerClass> innerClasses){
        ((InnerEnum)innerCompilationUnit).addInnerClasses(innerClasses);
    }

    public void addInnerEnum(InnerEnum innerEnum){
        ((InnerEnum)innerCompilationUnit).addInnerEnum(innerEnum);
    }

    public void addInnerEnums(List<InnerEnum> innerEnums){
        ((InnerEnum)innerCompilationUnit).addInnerEnums(innerEnums);
    }

    public void addEnumConstant(EnumConstant enumConstant){
        ((InnerEnum)innerCompilationUnit).addEnumConstant(enumConstant);
    }

    public void addEnumConstants(List<EnumConstant> enumConstants){
        ((InnerEnum)innerCompilationUnit).addEnumConstants(enumConstants);
    }

    public void addAnnotation(String annotation) {
        innerCompilationUnit.addAnnotation(annotation);
    }

    public void addAnnotations(Set<String> annotations) {
        innerCompilationUnit.addAnnotations(annotations);
    }

    public void addTypeComment(String typeComment) {
        innerCompilationUnit.addTypeComment(typeComment);
    }

    public void addTypeComments(Set<String> typeComments) {
        innerCompilationUnit.addTypeComments(typeComments);
    }

    @Override
    public FullyQualifiedJavaType getType() {
        return innerCompilationUnit.getType();
    }

    public void addExtraNonStaticImport(FullyQualifiedJavaType nonStaticImport){
        this.innerCompilationUnit.addExtraNonStaticImport(nonStaticImport);
    }

    public void addExtraNonStaticImports(Set<FullyQualifiedJavaType> nonStaticImports){
        this.innerCompilationUnit.addExtraNonStaticImports(nonStaticImports);
    }


    @Override
    public String toString() {
        return "TopLevelEnum{" +
                "innerEnum=" + innerCompilationUnit +
                ", packageName='" + packageName + '\'' +
                ", typeComments=" + innerCompilationUnit.typeComments +
                ", imports=" + imports +
                ", typeAnnotations=" + innerCompilationUnit.typeAnnotations +
                ", staticImports=" + staticImports +
                ", visibility=" + innerCompilationUnit.visibility +
                ", isStatic=" + innerCompilationUnit.isStatic +
                ", isFinal=" + innerCompilationUnit.isFinal +
                ", type=" + innerCompilationUnit.type +
                ", superClass=" + innerCompilationUnit.superClass +
                ", superInterfaces=" + innerCompilationUnit.superInterfaces +
                '}';
    }
}
