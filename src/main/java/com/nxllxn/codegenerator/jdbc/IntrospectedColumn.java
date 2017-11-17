package com.nxllxn.codegenerator.jdbc;


import com.nxllxn.codegenerator.codegen.generator.util.CodeGenerateUtil;
import com.nxllxn.codegenerator.codegen.java.FullyQualifiedJavaType;
import com.nxllxn.codegenerator.jdbc.java.JavaReservedWords;
import com.nxllxn.codegenerator.jdbc.java.SqlReservedWords;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class IntrospectedColumn {
    /**
     * 附属的表名称
     */
    private String attachedTableName;

    /**
     * 实际的列名称
     */
    private String primitiveColumnName;

    /**
     * 运行时列名称
     */
    private String runtimeColumnName;

    /**
     * column size
     */
    private int length;

    /**
     * decimal digits
     */
    private int scale;

    /**
     * JDBC类型 参见Types
     */
    private int jdbcType;

    /**
     * JDBC 类型名称，用于xml中指定当前列使用的jdbc类型
     */
    private String jdbcTypeName;

    /**
     * 由JDBC类型经TypeResolver转换得到的Java类型
     */
    private FullyQualifiedJavaType javaType;

    /**
     * 实体类Java属性名称
     */
    private String propertyName;

    /**
     * 类型处理器，全限定类名
     */
    private String typeHandler;

    /**
     * 当前列是不是唯一标识列
     */
    private boolean isIdentityColumn;

    private boolean isSequenceColumn;

    /**
     * 当前列是否为自增列
     */
    private boolean isAutoIncrement;

    /**
     * 当前列是否为自动生成的列
     */
    private boolean isGeneratedColumn;

    /**
     * 是否需要为列名称加上分隔符，比如列名称为java，sql保留字或者包含空格
     */
    private boolean isNeedForDelimited;

    /**
     * 当前列是否可以为空
     */
    private boolean isNullable;

    /**
     * 当前列的默认值
     */
    private String defaultValue;

    /**
     * 当前列的评论信息
     */
    private String remarks;

    public IntrospectedColumn() {
    }

    public String getAttachedTableName() {
        return attachedTableName;
    }

    public void setAttachedTableName(String attachedTableName) {
        this.attachedTableName = attachedTableName;
    }

    public String getPrimitiveColumnName() {
        return primitiveColumnName;
    }

    /**
     * 获取经过分隔符处理的原始列名称，主要用于兼容Java保留字以及Sql保留字
     * @param beginningDelimiter 开始分隔符
     * @param endDelimiter 结束分隔符
     * @return 经过分隔符处理的原始列名称
     */
    public String getDelimitedPrimitiveColumnName(String beginningDelimiter,String endDelimiter) {
        if (StringUtils.isBlank(primitiveColumnName)){
            return primitiveColumnName;
        }

        if (JavaReservedWords.containsWord(primitiveColumnName)
                || SqlReservedWords.containsWord(primitiveColumnName)){
            return beginningDelimiter + primitiveColumnName + endDelimiter;
        }

        return primitiveColumnName;
    }

    public void setPrimitiveColumnName(String primitiveColumnName) {
        this.primitiveColumnName = primitiveColumnName;
    }

    public String getRuntimeColumnName() {
        return runtimeColumnName;
    }

    public void setRuntimeColumnName(String runtimeColumnName) {
        this.runtimeColumnName = runtimeColumnName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getJdbcTypeName() {
        return jdbcTypeName;
    }

    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }

    public FullyQualifiedJavaType getJavaType() {
        return javaType;
    }

    public void setJavaType(FullyQualifiedJavaType javaType) {
        this.javaType = javaType;
    }

    public String getPropertyName() {
        if (propertyName == null){
            propertyName = CodeGenerateUtil.getCamelCaseString(runtimeColumnName, false);
        }

        return propertyName;
    }

    public String getKeyConstantName(){
        return CodeGenerateUtil.assembleConstantName(propertyName);
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }

    public boolean isIdentityColumn() {
        return isIdentityColumn;
    }

    public void setIdentityColumn(boolean identityColumn) {
        isIdentityColumn = identityColumn;
    }

    public boolean isSequenceColumn() {
        return isSequenceColumn;
    }

    public void setSequenceColumn(boolean sequenceColumn) {
        isSequenceColumn = sequenceColumn;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    public boolean isGeneratedColumn() {
        return isGeneratedColumn;
    }

    public void setGeneratedColumn(boolean generatedColumn) {
        isGeneratedColumn = generatedColumn;
    }

    public boolean isNeedForDelimited() {
        return isNeedForDelimited;
    }

    public void setNeedForDelimited(boolean needForDelimited) {
        isNeedForDelimited = needForDelimited;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                attachedTableName, primitiveColumnName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }

        if (!(obj instanceof IntrospectedColumn)){
            return false;
        }

        return obj.hashCode() == this.hashCode();
    }

    /**
     * 判断当前列是否为BLOB列 此段代码摘抄自Mybatis IntrospectedColumn
     * @return 是否为BLOB列
     */
    public boolean isBLOBColumn() {
        String typeName = getJdbcTypeName();

        return "BINARY".equals(typeName) || "BLOB".equals(typeName) //$NON-NLS-1$ //$NON-NLS-2$
                || "CLOB".equals(typeName) || "LONGNVARCHAR".equals(typeName) //$NON-NLS-1$ //$NON-NLS-2$
                || "LONGVARBINARY".equals(typeName) || "LONGVARCHAR".equals(typeName) //$NON-NLS-1$ //$NON-NLS-2$
                || "NCLOB".equals(typeName) || "VARBINARY".equals(typeName); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public String toString() {
        return "IntrospectedColumn{" +
                "attachedTableName='" + attachedTableName + '\'' +
                ", primitiveColumnName='" + primitiveColumnName + '\'' +
                ", runtimeColumnName='" + runtimeColumnName + '\'' +
                ", length=" + length +
                ", scale=" + scale +
                ", jdbcType=" + jdbcType +
                ", jdbcTypeName='" + jdbcTypeName + '\'' +
                ", javaType='" + javaType + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", typeHandler='" + typeHandler + '\'' +
                ", isIdentityColumn=" + isIdentityColumn +
                ", isSequenceColumn=" + isSequenceColumn +
                ", isAutoIncrement=" + isAutoIncrement +
                ", isGeneratedColumn=" + isGeneratedColumn +
                ", isNeedForDelimited=" + isNeedForDelimited +
                ", isNullable=" + isNullable +
                ", defaultValue='" + defaultValue + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
