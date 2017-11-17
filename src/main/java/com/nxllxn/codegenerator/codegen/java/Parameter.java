package com.nxllxn.codegenerator.codegen.java;

import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import com.nxllxn.codegenerator.codegen.service.TemplateBasedCodeAssembleServiceImpl;
import com.nxllxn.codegenerator.utils.AssembleUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 方法形参实体类
 *
 * @author
 */
public class Parameter {
    /**
     * 参数注解
     */
    private Set<String> annotations;

    /**
     * 参数类型
     */
    private FullyQualifiedJavaType type;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 是否变长参数
     */
    private boolean isVarAs;

    public Parameter() {
        this.annotations = new HashSet<>();
    }

    public Parameter(FullyQualifiedJavaType type, String name) {
        this(type, name, false);
    }

    public Parameter(FullyQualifiedJavaType type, String name, boolean isVarAs) {
        this.type = type;
        this.name = name;
        this.isVarAs = isVarAs;

        this.annotations = new HashSet<>();
    }

    public Set<String> getAnnotations() {
        return annotations;
    }

    public FullyQualifiedJavaType getType() {
        return type;
    }

    public void setType(FullyQualifiedJavaType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVarAs() {
        return isVarAs;
    }

    public void setVarAs(boolean varAs) {
        isVarAs = varAs;
    }

    public void addAnnotation(String annotation){
        this.annotations.add(annotation);
    }

    public void addAnnotations(Set<String> annotations){
        this.annotations.addAll(annotations);
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "annotations=" + annotations +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", isVarAs=" + isVarAs +
                '}';
    }

    /**
     * 获取当前类型对应的代码片段
     *
     * @return 代码片段
     */
    public String getFormattedContent() {
        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(codeAssembleService.assembleParameterAnnotation(annotations, AssembleUtil.DEFAULT_INDENT_LEVEL));

        stringBuilder.append(codeAssembleService.assembleTypeDefinition(type));

        if (isVarAs){
            //回退一个类型后面的空格
            AssembleUtil.rollbackExtraSpaceInTheEnd(stringBuilder);

            //填充 `... `
            stringBuilder.append(codeAssembleService.assembleVariableArgs(isVarAs));
        }

        stringBuilder.append(codeAssembleService.assembleParameterName(name));

        return stringBuilder.toString();
    }

    public Set<FullyQualifiedJavaType> calculateNonStaticImports() {
        Set<FullyQualifiedJavaType> nonStaticImports = new HashSet<>();

        if (type != null){
            nonStaticImports.add(type);
        }

        return nonStaticImports;
    }
}
