package com.nxllxn.codegenerator.codegen.java;

import java.util.List;
import java.util.Set;

/**
 * 顶层类实体类，用于描述一个Java代码中处于最外层的类
 *
 * @author wenchao
 */
public class TopLevelClass extends AbstractCompilationUnit {
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

    public TopLevelClass() {
        innerCompilationUnit = new InnerClass();
    }

    public TopLevelClass(String fullyQualifiedTypeName){
        innerCompilationUnit = new InnerClass();
        this.getInnerClass().type = new FullyQualifiedJavaType(fullyQualifiedTypeName);
    }

    public boolean isAbstract() {
        return ((InnerClass)innerCompilationUnit).isAbstract();
    }

    public void setAbstract(boolean isAbstract) {
        ((InnerClass)innerCompilationUnit).isAbstract = isAbstract;
    }

    public List<Field> getFields() {
        return ((InnerClass)innerCompilationUnit).fields;
    }

    public void setFields(List<Field> fields) {
        ((InnerClass)innerCompilationUnit).fields = fields;
    }

    public List<Method> getMethods() {
        return ((InnerClass)innerCompilationUnit).methods;
    }

    public void setMethods(List<Method> methods) {
        ((InnerClass)innerCompilationUnit).methods = methods;
    }

    public List<InnerClass> getInnerClasses() {
        return ((InnerClass)innerCompilationUnit).innerClasses;
    }

    public void setInnerClasses(List<InnerClass> innerClasses) {
        ((InnerClass)innerCompilationUnit).innerClasses = innerClasses;
    }

    public List<InnerInterface> getInnerInterfaces() {
        return ((InnerClass)innerCompilationUnit).innerInterfaces;
    }

    public void setInnerInterfaces(List<InnerInterface> innerInterfaces) {
        ((InnerClass)innerCompilationUnit).innerInterfaces = innerInterfaces;
    }

    public List<InnerEnum> getInnerEnums() {
        return ((InnerClass)innerCompilationUnit).innerEnums;
    }

    public void setInnerEnums(List<InnerEnum> innerEnums) {
        ((InnerClass)innerCompilationUnit).innerEnums = innerEnums;
    }

    public List<TypeParameter> getTypeParameters() {
        return ((InnerClass)innerCompilationUnit).typeParameters;
    }

    public void setTypeParameters(List<TypeParameter> typeParameters) {
        ((InnerClass)innerCompilationUnit).typeParameters = typeParameters;
    }

    public List<InitializationBlock> getInitializationBlocks() {
        return ((InnerClass)innerCompilationUnit).initializationBlocks;
    }

    public void setInitializationBlocks(List<InitializationBlock> initializationBlocks) {
        ((InnerClass)innerCompilationUnit).initializationBlocks = initializationBlocks;
    }

    public InnerClass getInnerClass() {
        return ((InnerClass)innerCompilationUnit);
    }

    public void setInnerClass(InnerClass innerClass) {
        this.innerCompilationUnit = innerClass;
    }

    public void addField(Field field){
        ((InnerClass)innerCompilationUnit).addField(field);
    }

    public void addFields(List<Field> fields){
        ((InnerClass)innerCompilationUnit).addFields(fields);
    }

    public void addMethod(Method method){
        ((InnerClass)innerCompilationUnit).addMethod(method);
    }

    public void addMethods(List<Method> methods){
        ((InnerClass)innerCompilationUnit).addMethods(methods);
    }

    public void addInnerClass(InnerClass innerClass){
        ((InnerClass)innerCompilationUnit).addInnerClass(innerClass);
    }

    public void addInnerClasses(List<InnerClass> innerClasses){
        ((InnerClass)innerCompilationUnit).addInnerClasses(innerClasses);
    }

    public void addInnerEnum(InnerEnum innerEnum){
        ((InnerClass)innerCompilationUnit).addInnerEnum(innerEnum);
    }

    public void addInnerEnums(List<InnerEnum> innerEnums){
        ((InnerClass)innerCompilationUnit).addInnerEnums(innerEnums);
    }

    public void addTypeParameter(TypeParameter typeParameter){
        ((InnerClass)innerCompilationUnit).addTypeParameter(typeParameter);
    }

    public void addTypeParameters(List<TypeParameter> typeParameters){
        ((InnerClass)innerCompilationUnit).addTypeParameters(typeParameters);
    }

    public void addInitializationBlock(InitializationBlock initializationBlock){
        ((InnerClass)innerCompilationUnit).addInitializationBlock(initializationBlock);
    }

    public void addInitializationBlocks(List<InitializationBlock> initializationBlocks){
        ((InnerClass)innerCompilationUnit).addInitializationBlocks(initializationBlocks);
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

    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        if (superInterface == null){
            return;
        }

        this.innerCompilationUnit.superInterfaces.add(superInterface);
    }

    public void addSuperInterfaces(List<FullyQualifiedJavaType> superInterfaces) {
        if (superInterfaces == null || superInterfaces.isEmpty()) {
            return;
        }

        this.innerCompilationUnit.superInterfaces.addAll(superInterfaces);
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
        return "TopLevelClass{" +
                "innerClass=" + innerCompilationUnit +
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
