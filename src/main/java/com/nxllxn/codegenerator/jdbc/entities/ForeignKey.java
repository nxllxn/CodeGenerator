package com.nxllxn.codegenerator.jdbc.entities;

import com.alibaba.fastjson.annotation.JSONField;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.Objects;

/**
 * 表外健描述实体类
 */
public class ForeignKey {
    /**
     * 当前table外健依赖列
     */
    @JSONField(serialize=false)
    private IntrospectedTable referFromTable;

    /**
     * 当前table外健依赖列
     */
    @JSONField(serialize=false)
    private IntrospectedColumn referFromColumn;

    /**
     * 外健依赖的表
     */
    @JSONField(serialize=false)
    private IntrospectedTable referToTable;

    /**
     * 外健依赖的列
     */
    @JSONField(serialize=false)
    private IntrospectedColumn referToColumn;

    /**
     * 内部外键依赖，如果a 经过关系表c 间接外键依赖于b，那么此外健表示a to c
     */
    private ForeignKey innerFromForeignKey;

    /**
     * 内部外键依赖 如果a 经过关系表c 间接外键依赖于b，那么此外健表示b to c
     */
    private ForeignKey innerToForeignKey;

    private String referFromTableName;

    private String referFromColumnName;

    private String referToTableName;

    private String referToColumnName;

    public IntrospectedColumn getReferFromColumn() {
        return referFromColumn;
    }

    public void setReferFromColumn(IntrospectedColumn referFromColumn) {
        this.referFromColumn = referFromColumn;
    }

    public IntrospectedTable getReferToTable() {
        return referToTable;
    }

    public void setReferToTable(IntrospectedTable referToTable) {
        this.referToTable = referToTable;
    }

    public IntrospectedColumn getReferToColumn() {
        return referToColumn;
    }

    public void setReferToColumn(IntrospectedColumn referToColumn) {
        this.referToColumn = referToColumn;
    }

    public IntrospectedTable getReferFromTable() {
        return referFromTable;
    }

    public void setReferFromTable(IntrospectedTable referFromTable) {
        this.referFromTable = referFromTable;
    }

    public String getReferFromTableName() {
        return referFromTableName;
    }

    public void setReferFromTableName(String referFromTableName) {
        this.referFromTableName = referFromTableName;
    }

    public String getReferFromColumnName() {
        return referFromColumnName;
    }

    public void setReferFromColumnName(String referFromColumnName) {
        this.referFromColumnName = referFromColumnName;
    }

    public String getReferToTableName() {
        return referToTableName;
    }

    public void setReferToTableName(String referToTableName) {
        this.referToTableName = referToTableName;
    }

    public String getReferToColumnName() {
        return referToColumnName;
    }

    public void setReferToColumnName(String referToColumnName) {
        this.referToColumnName = referToColumnName;
    }

    public ForeignKey getInnerFromForeignKey() {
        return innerFromForeignKey;
    }

    public void setInnerFromForeignKey(ForeignKey innerFromForeignKey) {
        this.innerFromForeignKey = innerFromForeignKey;
    }

    public ForeignKey getInnerToForeignKey() {
        return innerToForeignKey;
    }

    public void setInnerToForeignKey(ForeignKey innerToForeignKey) {
        this.innerToForeignKey = innerToForeignKey;
    }

    @Override
    public int hashCode() {
        return Objects.hash(referFromTableName,referFromColumnName,referToTableName,referToColumnName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }

        if (!(obj instanceof ForeignKey)){
            return false;
        }

        return obj.hashCode() == this.hashCode();
    }

    @Override
    public String toString() {
        return "ForeignKey{" +
                "referFromTableName='" + referFromTableName + '\'' +
                ", referFromColumnName='" + referFromColumnName + '\'' +
                ", referToTableName='" + referToTableName + '\'' +
                ", referToColumnName='" + referToColumnName + '\'' +
                '}';
    }
}
