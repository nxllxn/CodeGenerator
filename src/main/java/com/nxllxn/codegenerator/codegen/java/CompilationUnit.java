package com.nxllxn.codegenerator.codegen.java;

import java.util.Set;

/**
 * 用于描述所有Java类，接口，枚举类型的基本接口
 *
 * @author wenchao
 */
public interface CompilationUnit extends Unit {
    /**
     * 获取当前类型包名称
     * @return 当前类型包名
     */
    String getPackageName();

    /**
     * 获取当前类型的非静态引用
     * @return 当前类型的非静态引用
     */
    Set<FullyQualifiedJavaType> getImports();

    /**
     * 获取当前类型的静态引用
     * @return 当前类型的静态引用
     */
    Set<String> getStaticImports();

    /**
     * 为当前类型添加一个非静态导入
     * @param nonStaticImport 待导入类型全限定类型描述
     */
    void addImport(FullyQualifiedJavaType nonStaticImport);

    /**
     * 为当前类型批量添加多个非静态导入集合
     * @param nonStaticImports 待导入类型全限定类型描述集合
     */
    void addImports(Set<FullyQualifiedJavaType> nonStaticImports);

    /**
     * 为当前类型添加一个静态导入
     * @param staticImport 待添加的静态导入
     */
    void addStaticImport(String staticImport);

    /**
     * 为当前类型批量添加多个静态导入
     * @param staticImports 待添加静态导入
     */
    void addStaticImports(Set<String> staticImports);

    /**
     * 获取当前类型的全限定类型
     *
     * @return 当前类型的全限定类型
     */
    FullyQualifiedJavaType getType();
}
