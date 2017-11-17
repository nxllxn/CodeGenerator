package com.nxllxn.codegenerator.codegen.java;

import java.util.List;
import java.util.Set;

/**
 * Java内部编译单元（如内部类，内部接口，内部枚举等）接口
 *
 * @author wenchao
 */
public interface InnerCompilationUnit extends Unit {
    /**
     * 为当前类型定义的类型评论信息，如果是一个外部类，则此信息用于生成统一的文件头
     * @return 当前类型定义的类型评论信息
     */
    List<String> getTypeComments();

    /**
     * 获取当前类型的注解
     * @return 当前已添加的类型注解
     */
    Set<String> getTypeAnnotations();

    /**
     * 用于获取当前类型可见性
     * @see Visibility
     * @return 当前类型可见性，参见Visibility
     */
    Visibility getVisibility();

    /**
     * 用于获取当前类型是否是静态类型
     * @return 如果当前类型是静态类型，返回true
     */
    boolean isStatic();

    /**
     * 用于获取当前类型是否是final的
     * @return 如果当前类型由final进行修饰，返回true
     */
    boolean isFinal();

    /**
     * 获取当前类型的全限定定义类型
     * @return 当前类型的全限定定义类型
     */
    FullyQualifiedJavaType getType();

    /**
     * 获取当前类型继承的父类
     * @return 当前类型继承的父类
     */
    FullyQualifiedJavaType getSuperClass();

    /**
     * 获取当前类型实现的父类接口
     * @return 当前类型实现的全部父类接口
     */
    Set<FullyQualifiedJavaType> getSuperInterfaces();

    /**
     * 为当前类型添加一行类型描述
     * @param typeComment 待添加类型描述
     */
    void addTypeComment(String typeComment);

    /**
     * 为当前类型添加多行类型描述信息
     * @param typeComments 待添加类型评论信息
     */
    void addTypeComments(Set<String> typeComments);

    /**
     * 为当前类型添加一个类型注解
     * @param annotation 待添加的类型注解
     */
    void addAnnotation(String annotation);

    /**
     * 为当前类型批量添加多个类型注解
     * @param annotations 待添加的类型注解集合
     */
    void addAnnotations(Set<String> annotations);

    /**
     * 为当前类型添加一个继承接口
     * @param superInterface 待添加接口类型
     */
    void addSuperInterface(FullyQualifiedJavaType superInterface);

    /**
     * 为当前类型批量添加继承接口
     * @param superInterfaces 待添加继承接口集合
     */
    void addSuperInterfaces(List<FullyQualifiedJavaType> superInterfaces);

    /**
     * 计算当前类型需要的非静态导入
     * @return 当前类型包含的非静态导入
     */
    Set<FullyQualifiedJavaType> calculateNonStaticImports();
}
