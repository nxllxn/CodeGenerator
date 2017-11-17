package com.nxllxn.codegenerator.jdbc.entities;

import com.alibaba.fastjson.annotation.JSONField;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;

import java.util.List;

/**
 * 索引描述实体类
 */
public class UniqueIndex {
    /**
     * 索引依赖的列
     */
    @JSONField(serialize=false)
    private List<IntrospectedColumn> onColumns;

    private List<String> onColumnNames;

    public List<IntrospectedColumn> getOnColumns() {
        return onColumns;
    }

    public void setOnColumns(List<IntrospectedColumn> onColumns) {
        this.onColumns = onColumns;
    }

    public List<String> getOnColumnNames() {
        return onColumnNames;
    }

    public void setOnColumnNames(List<String> onColumnNames) {
        this.onColumnNames = onColumnNames;
    }

    @Override
    public String toString() {
        return "UniqueIndex{" +
                "onColumnNames=" + onColumnNames +
                '}';
    }
}
