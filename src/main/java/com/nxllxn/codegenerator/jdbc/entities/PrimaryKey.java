package com.nxllxn.codegenerator.jdbc.entities;

import com.alibaba.fastjson.annotation.JSONField;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * 表主键实体类
 */
public class PrimaryKey {
    /**
     * 主键依赖的列名
     */
    private List<String> onColumnNames;

    /**
     * 主键依赖的列
     */
    @JSONField(serialize=false)
    private List<IntrospectedColumn> onColumns;

    public PrimaryKey() {
        this.onColumnNames = new ArrayList<>();
        this.onColumns = new ArrayList<>();
    }

    public List<IntrospectedColumn> getOnColumns() {
        return onColumns;
    }

    public List<String> getOnColumnNames() {
        return onColumnNames;
    }

    public void setOnColumnNames(List<String> onColumnNames) {
        this.onColumnNames = onColumnNames;
    }

    public void setOnColumns(List<IntrospectedColumn> onColumns) {
        this.onColumns = onColumns;
    }

    @Override
    public String toString() {
        return "PrimaryKey{" +
                "onColumnNames=" + onColumnNames +
                '}';
    }
}
