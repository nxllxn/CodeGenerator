package com.nxllxn.codegenerator.codegen.java;

import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * 类型字段实体类
 *
 * @author wenchao
 */
public class Field extends AbstractElement {
    /**
     * 字段是否为transient,用于控制序列化
     */
    private boolean isTransient;

    /**
     * 字段是否是volatile的，用于控制多线程的内存可见性
     */
    private boolean isVolatile;

    /**
     * 字段类型
     */
    private FullyQualifiedJavaType type;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段初始化字符串字面值
     */
    private String initStrValue;

    public Field() {
        super();
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        if (StringUtils.isBlank(name)){
            return "";
        }

        return super.getFormattedContent(indentLevel);
    }

    @Override
    protected void assembleModifiers(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel) {
        super.assembleModifiers(formattedContentBuilder, codeAssembleService, indentLevel);

        formattedContentBuilder.append(codeAssembleService.assembleTransientModifier(isTransient));
        formattedContentBuilder.append(codeAssembleService.assembleVolatileModifier(isVolatile));
    }

    @Override
    protected void assembleMainBlock(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel) {
        formattedContentBuilder.append(codeAssembleService.assembleTypeDefinition(type));

        formattedContentBuilder.append(codeAssembleService.assembleFieldName(name));

        //如果有字符串初始化字面值，那么需要添加属性初始化值
        if (!StringUtils.isBlank(initStrValue)){
            formattedContentBuilder.append(" = ");
            formattedContentBuilder.append(type.getCorrespondingValue(initStrValue));
        }

        formattedContentBuilder.append(";");
    }

    public boolean isTransient() {
        return isTransient;
    }

    public void setTransient(boolean aTransient) {
        isTransient = aTransient;
    }

    public boolean isVolatile() {
        return isVolatile;
    }

    public void setVolatile(boolean aVolatile) {
        isVolatile = aVolatile;
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

    public String getInitStrValue() {
        return initStrValue;
    }

    public void setInitStrValue(String initStrValue) {
        this.initStrValue = initStrValue;
    }

    @Override
    public String toString() {
        return "Field{" +
                "isTransient=" + isTransient +
                ", isVolatile=" + isVolatile +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", initStrValue='" + initStrValue + '\'' +
                '}';
    }

    @Override
    public Set<FullyQualifiedJavaType> calculateNonStaticImports() {
        Set<FullyQualifiedJavaType> nonStaticImports = super.calculateNonStaticImports();

        if (type != null){
            nonStaticImports.add(type);
        }

        return nonStaticImports;
    }
}
