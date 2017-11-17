package com.nxllxn.codegenerator.codegen.generator.serviceimpl;

import com.nxllxn.codegenerator.codegen.java.Field;
import com.nxllxn.codegenerator.codegen.java.Method;
import com.nxllxn.codegenerator.codegen.java.TopLevelClass;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.List;

/**
 * service实现层代码生成服务
 *
 * @author wenchao
 */
public interface ServiceImplCodeGenerateService {
    /**
     * 生成一个service层的接口实现类
     * @param introspectedTable 当前表结构信息
     * @param targetPackage 目标package，即service.impl包所在目录
     * @return service层接口实现类
     */
    TopLevelClass generateServiceImplClass(IntrospectedTable introspectedTable, String targetPackage);

    /**
     * 为当前service实现类添加依赖的Mapper组件
     * @param introspectedTable 当前表结构信息
     * @return 依赖的mapper组件结合
     */
    List<Field> generateDependentMapperField(IntrospectedTable introspectedTable);

    /**
     * 为当前service实现类添加自动注入Mapper的构造函数
     * @param introspectedTable 当前表结构信息
     * @return 构造函数
     */
    Method generateAutowiredConstructor(IntrospectedTable introspectedTable);

    /**
     * 为ServiceImpl添加一个添加一条记录的接口实现
     * @param introspectedTable 当前表结构信息
     * @return 添加一条记录的接口实现
     */
    Method generateAddNewRecordImpl(IntrospectedTable introspectedTable);

    /**
     * 为ServiceImpl添加一个批量添加记录的接口实现
     * @param introspectedTable 当前表结构信息
     * @return 批量添加记录的接口实现
     */
    Method generateAddRecordBatchImpl(IntrospectedTable introspectedTable);

    /**
     * 为ServiceImpl添加删除指定标识符对应记录的接口实现
     * @param introspectedTable 当前表结构信息
     * @return 删除指定标识符对应记录的接口实现
     */
    Method generateRemoveRecordImpl(IntrospectedTable introspectedTable);

    /**
     * 为ServiceImpl添加按照当前表包含的UniqueIndex删除指定记录接口实现，注意UniqueIndex可能多个
     * @param introspectedTable 当前表结构信息
     * @return 按照当前表包含的UniqueIndex删除指定记录接口实现
     */
    List<Method> generateRemoveRecordByUniqueIndexImpl(IntrospectedTable introspectedTable);

    /**
     * 为ServiceImpl添加按照指定标识符修改记录的接口实现
     * @param introspectedTable 当前表结构信息
     * @return 按照指定标识符修改记录的接口实现
     */
    Method generateModifyRecordImpl(IntrospectedTable introspectedTable);

    /**
     * 为ServiceImpl添加按照指定标识符查询指定记录的接口实现（只包含主键列和基础列）
     * @param introspectedTable 当前表结构信息
     * @return 按照指定标识符查询指定记录的接口实现（只包含主键列和基础列）
     */
    Method generateQueryRecordImpl(IntrospectedTable introspectedTable);

    /**
     * 为ServiceImpl添加按照指定标识符查询指定记录的接口实现（包括主键列，基础列，blob列）
     * @param introspectedTable 当前表结构信息
     * @return 按照指定标识符查询指定记录的接口实现（包括主键列，基础列，blob列）
     */
    Method generateQueryRecordWithBlobImpl(IntrospectedTable introspectedTable);

    /**
     * 为ServiceImpl添加按照指定标识符查询指定记录的接口实现（仅包括blob列）
     * @param introspectedTable 当前表结构信息
     * @return 按照指定标识符查询指定记录的接口实现（仅包括blob列）
     */
    Method generateQueryRecordOnlyBlobImpl(IntrospectedTable introspectedTable);

    /**
     * 为Service添加查询全部记录的接口实现（只包含主键列和基础列）
     * @param introspectedTable 当前表结构信息
     * @return 查询全部记录的接口实现（只包含主键列和基础列）
     */
    Method generateQueryAllRecordImpl(IntrospectedTable introspectedTable);

    /**
     * 为ServiceImpl添加查询全部记录的接口实现，此接口提供分页
     * @param introspectedTable 当前表结构信息
     * @return 查询全部记录的接口实现，此接口提供分页
     */
    Method generateQueryAllRecordWithPageImpl(IntrospectedTable introspectedTable);

    /**
     * 为ServiceImpl添加查询指定UniqueIndex对应的记录是否存在的接口实现，注意UniqueIndex可能多个
     * @param introspectedTable 当前表结构信息
     * @return 查询指定UniqueIndex对应的记录是否存在的接口实现
     */
    List<Method> generateIfExistsImpl(IntrospectedTable introspectedTable);

    /**
     * 按照当前列表依赖的外键依赖 利用join进行级联查询,注意外键依赖可能多个，目前实现只考虑一层的级联，可以多个表
     * @param introspectedTable 当前表结构信息
     * @return 按照relation表标识的外健依赖进行两层join操作的级联查询
     */
    List<Method> generateBinaryCascadeQueryImpl(IntrospectedTable introspectedTable);

    /**
     * 当前表包含的外键依赖，逆向查询当前实体基本属性接口实现
     * @param introspectedTable 当前表结构信息
     * @return 按照直接外键依赖进行单层join操作的级联查询
     */
    List<Method> generateSingleCascadeQueryImpl(IntrospectedTable introspectedTable);

    /**
     * 为serviceImpl添加integrityCheck的接口实现
     * @param introspectedTable 当前表结构信息
     * @return 按照non null column组装的完整性校验方法
     */
    Method generateIntegrityCheckImpl(IntrospectedTable introspectedTable);
}
