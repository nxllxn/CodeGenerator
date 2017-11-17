package com.nxllxn.codegenerator.codegen.generator.entity;

import com.nxllxn.codegenerator.codegen.java.*;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.List;

/**
 * 代码生成服务
 *
 * @author wenchao
 */
public interface EntityCodeGenerateService {
    /**
     * 根据IntrospectedTable描述的表结构组装实体类
     * @param introspectedTable 表结构描述信息
     * @param entityPackageName 实体类所在包名称
     * @return 实体类
     */
    TopLevelClass generateEntityClass(IntrospectedTable introspectedTable, String entityPackageName);

    /**
     * 根据IntrospectedColumn描述的列结构组装实体的字段
     * @param introspectedTable 表结构描述信息
     * @return 当前列对应的字段
     */
    List<Field> generateFields(IntrospectedTable introspectedTable);

    /**
     * 根据IntrospectedColumn描述的列结构组装实体的字段Getter
     * @param introspectedTable 表结构描述信息
     * @return 当前字段的Getter
     */
    List<Method> generateFieldsGetter(IntrospectedTable introspectedTable);

    /**
     * 根据IntrospectedColumn描述的列结构组装实体的字段Setter
     * @param introspectedTable 表结构描述信息
     * @return 当前字段的Setter
     */
    List<Method> generateFieldsSetter(IntrospectedTable introspectedTable);

    /**
     * 根据IntrospectedTable描述的表结构组装实体的无参构造函数
     * 由于一个实体的字段可能比较多，使用带参数的构造函数需要传递很多的参数，所以此处我们采用无参构造函数的形式，
     * 具体的属性将通过setter设置进去，并且我们将提供统一的Builder用于实体的构建。
     *
     * @param introspectedTable 表结构描述信息
     * @return 当前实体的无参构造函数
     */
    Method generateConstructor(IntrospectedTable introspectedTable);

    /**
     * 根据IntrospectedTable描述的表结构组装实体的toString方法
     *
     * @param introspectedTable 表结构描述信息
     * @return 当前实体的toString方法
     */
    Method generateToStringMethod(IntrospectedTable introspectedTable);

    /**
     * 根据IntrospectedTable描述的表结组装实体的Equals方法
     * @param introspectedTable 表结构描述信息
     * @return 当前实体的equals方法
     */
    Method generateEqualsMethod(IntrospectedTable introspectedTable);

    /**
     * 根据IntrospectedTable描述的表结构组装实体的HashCode方法
     * @param introspectedTable 表结构描述信息
     * @return 当前实体的hashCode方法
     */
    Method generateHashCodeMethod(IntrospectedTable introspectedTable);

    /**
     * 根据IntrospectedTable描述的表结构组装实体的fromJson方法
     * @param introspectedTable 表结构描述信息
     * @return 当前实体的FromJson方法
     */
    Method generateFromJsonMethod(IntrospectedTable introspectedTable);

    /**
     * 根据IntrospectedTable描述的表结构组装实体的toJson方法
     * @param introspectedTable 表结构描述信息
     * @return 当前实体的ToJson方法
     */
    Method generateToJsonMethod(IntrospectedTable introspectedTable);

    /**
     * 根据IntrospectedTable描述的表结构组装实体的Builder
     * @param introspectedTable 表结构描述信息
     * @return 当前实体的Builder
     */
    InnerClass generateBuilder(IntrospectedTable introspectedTable);

    /**
     * 生成序列化反序列化基础接口类，此接口类包含一个方法签名为T fromJson（JSONObject fromJsonObj）的反序列化方法
     * 和一个方法签名为JSONObject toJson（）的序列化方法。
     * @param targetPackage 当前实体包包名
     * @return 序列化反序列化基础接口类
     */
    TopLevelInterface generateSerializerInterface(String targetPackage);

    /**
     * 生成序列化反序列化接口抽象实现
     * @param targetPackage 实体所在包包名
     * @return 序列化反序列化抽象实现类
     */
    TopLevelClass generateSerializerAbstractClass(String targetPackage);
}
