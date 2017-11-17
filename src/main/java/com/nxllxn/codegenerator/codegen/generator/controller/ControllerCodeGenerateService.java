package com.nxllxn.codegenerator.codegen.generator.controller;

import com.nxllxn.codegenerator.codegen.java.Field;
import com.nxllxn.codegenerator.codegen.java.Method;
import com.nxllxn.codegenerator.codegen.java.TopLevelClass;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.List;

/**
 * Controller层代码生成服务接口
 *
 * @author wenchao
 */
public interface ControllerCodeGenerateService {
    /**
     * 生成Controller web接口类
     * @param introspectedTable 当前表结构信息
     * @param targetPackage 目标package，即存放controller web接口的目录
     * @return  controller web接口类
     */
    TopLevelClass generateControllerClass(IntrospectedTable introspectedTable, String targetPackage);

    /**
     * 生成当前controller依赖的service组件
     * @param introspectedTable 当前表结构信息
     * @return 当前controller依赖的service组件
     */
    List<Field> generateDependentServiceField(IntrospectedTable introspectedTable);

    /**
     * 生成当前controller依赖的service组件的自动注入构造函数
     * @param introspectedTable 当前表结构信息
     * @return 自动注入组件的构造函数
     */
    Method generateAutowiredConstructor(IntrospectedTable introspectedTable);

    /**
     * 为Controller添加一个添加一条记录的Web接口
     * @param introspectedTable 当前表结构信息
     * @return 添加一条记录的Web接口
     */
    Method generateAddNewRecordImpl(IntrospectedTable introspectedTable);

    /**
     * 为Controller添加一个批量添加记录的Web接口
     * @param introspectedTable 当前表结构信息
     * @return 批量添加记录的Web接口
     */
    Method generateAddRecordBatchImpl(IntrospectedTable introspectedTable);

    /**
     * 为controller添加一个删除指定记录的web接口
     * @param introspectedTable 当前表结构信息
     * @return 根据唯一主键删除指定记录的Web接口
     */
    Method generateRemoveRecordImpl(IntrospectedTable introspectedTable);

    /**
     * 为Controller添加一个或多个按照当前表包含的UniqueIndex删除指定记录的Web接口
     * @param introspectedTable 当前表结构信息
     * @return 按照当前表包含的UniqueIndex删除指定记录的Web接口集合
     */
    List<Method> generateRemoveRecordByUniqueIndexImpl(IntrospectedTable introspectedTable);

    /**
     * 为Controller添加一个按照唯一主键（比如自增Id）修改指定记录的Web接口
     * @param introspectedTable 当前表结构信息
     * @return 按照唯一主键（比如自增Id）修改指定记录的Web接口
     */
    Method generateModifyRecordImpl(IntrospectedTable introspectedTable);

    /**
     * 为Controller添加一个按照唯一主键查询指定记录的Web接口
     * @param introspectedTable 当前表结构信息
     * @return 按照唯一主键查询指定记录的Web接口
     */
    Method generateQueryRecordImpl(IntrospectedTable introspectedTable);

    /**
     * 为Controller添加一个按照唯一主键查询指定记录（包括Blob）的Web接口
     * @param introspectedTable 当前表结构信息
     * @return 按照唯一主键查询指定记录（包括Blob）的Web接口
     */
    Method generateQueryRecordWithBlobImpl(IntrospectedTable introspectedTable);

    /**
     * 为Controller添加一个按照唯一主键查询指定记录（仅包括Blob列）的Web接口
     * @param introspectedTable 当前表结构信息
     * @return 按照唯一主键查询指定记录（仅包括Blob列）的Web接口
     */
    Method generateQueryRecordOnlyBlobImpl(IntrospectedTable introspectedTable);

    /**
     * 为Controller添加一个查询全部记录（不包括Blob列）的Web接口
     * @param introspectedTable 当前表结构信息
     * @return 查询全部记录（不包括Blob列）的Web接口
     */
    Method generateQueryAllRecordImpl(IntrospectedTable introspectedTable);

    /**
     * 为Controller添加一个查询全部记录（不包括Blob列）且带分页功能的Web接口
     * @param introspectedTable 当前表结构信息
     * @return 查询全部记录（不包括Blob列）且带分页功能的Web接口
     */
    Method generateQueryAllRecordWithPageImpl(IntrospectedTable introspectedTable);

    /**
     * 为Controller添加查询指定UniqueIndex对应记录是否存在的接口
     * @param introspectedTable 当前表结构信息
     * @return 查询指定UniqueIndex对应记录是否存在的接口集合
     */
    List<Method> generateIfExistsImpl(IntrospectedTable introspectedTable);

    List<Method> generateBinaryCascadeQueryImpl(IntrospectedTable introspectedTable);

    List<Method> generateSingleCascadeQueryImpl(IntrospectedTable introspectedTable);

    /**
     * 生成所有Controller Class的抽象父类AbstractController
     *
     * @param controllerPackageName 目标包名
     * @param entityPackageName 目标包名
     *
     * @return 抽象父类AbstractController，此类提供了部分默认实现
     */
    TopLevelClass generateAbstractControllerClass(String controllerPackageName, String entityPackageName);
}
