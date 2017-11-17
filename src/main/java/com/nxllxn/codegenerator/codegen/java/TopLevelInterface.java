package com.nxllxn.codegenerator.codegen.java;

import java.util.List;
import java.util.Set;

/**
 * 顶层接口实体类,用于描述一个Java文件中最外层的接口
 *
 * @author wenchao
 */
public class TopLevelInterface extends AbstractCompilationUnit {
    public TopLevelInterface() {
        innerCompilationUnit = new InnerInterface();
    }

    @Override
    public boolean isClass() {
        return false;
    }

    @Override
    public boolean isInterface() {
        return true;
    }

    @Override
    public boolean isEnumeration() {
        return false;
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        return super.getFormattedContent(indentLevel);
    }

    public List<Field> getFields() {
        return ((InnerInterface)innerCompilationUnit).fields;
    }

    public void setFields(List<Field> fields) {
        ((InnerInterface)innerCompilationUnit).fields = fields;
    }

    public List<Method> getMethods() {
        return ((InnerInterface)innerCompilationUnit).methods;
    }

    public void setMethods(List<Method> methods) {
        ((InnerInterface)innerCompilationUnit).methods = methods;
    }

    public InnerInterface getInnerInterface() {
        return ((InnerInterface)innerCompilationUnit);
    }

    public void setInnerInterface(InnerInterface innerInterface) {
        this.innerCompilationUnit = innerInterface;
    }

    public void addField(Field field){
        ((InnerInterface)innerCompilationUnit).addField(field);
    }

    public void addFields(List<Field> fields){
        ((InnerInterface)innerCompilationUnit).addFields(fields);
    }

    public void addMethod(Method method){
        ((InnerInterface)innerCompilationUnit).addMethod(method);
    }

    public void addMethods(List<Method> methods){
        ((InnerInterface)innerCompilationUnit).addMethods(methods);
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
        return "TopLevelInterface{" +
                "innerInterface=" + innerCompilationUnit +
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
