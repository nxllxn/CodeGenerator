package com.nxllxn.codegenerator.codegen.generator;


import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.List;

/**
 * 所有代码生成器的父类
 *
 * @author wenchao
 */
public abstract class AbstractCodeGenerator {
    /**
     * 数据库表结构集合
     */
    protected List<IntrospectedTable> introspectedTables;

    /**
     * 用户定义的配置上下文
     */
    protected Context context;

    public AbstractCodeGenerator() {
    }

    public List<IntrospectedTable> getIntrospectedTables() {
        return introspectedTables;
    }

    public void setIntrospectedTables(List<IntrospectedTable> introspectedTables) {
        this.introspectedTables = introspectedTables;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 代码生成接口
     * @return 生成的代码文件
     */
    public abstract List<GeneratedFile> generate();

    @Override
    public String toString() {
        return "AbstractGenerator{}";
    }
}
