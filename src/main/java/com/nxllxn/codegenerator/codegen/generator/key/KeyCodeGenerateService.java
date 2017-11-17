package com.nxllxn.codegenerator.codegen.generator.key;

import com.nxllxn.codegenerator.codegen.java.Field;
import com.nxllxn.codegenerator.codegen.java.TopLevelClass;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.List;

/**
 * 实体类对应的Key字符串常量类代码生成服务接口
 *
 * @author wenchao
 */
public interface KeyCodeGenerateService {
    /**
     * 根据introspectedTable描述的表结构，生成实体属性key字符串常量定义类
     * @param introspectedTable 表结构信息
     * @param targetPackage 目标package
     * @return 构造好的字符串常量定义类
     */
    TopLevelClass generateKeyDefinitionClass(IntrospectedTable introspectedTable, String targetPackage);

    /**
     * 根据introspectedTable表结构信息组装当前类的字符串常量字段
     * @param introspectedTable 表结构信息
     * @return 字符串常量字段集合
     */
    List<Field> generateKeyDefinitions(IntrospectedTable introspectedTable);
}
