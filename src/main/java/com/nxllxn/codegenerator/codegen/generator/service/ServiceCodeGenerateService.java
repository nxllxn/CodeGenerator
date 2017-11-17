package com.nxllxn.codegenerator.codegen.generator.service;

import com.nxllxn.codegenerator.codegen.java.Method;
import com.nxllxn.codegenerator.codegen.java.TopLevelInterface;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.List;

/**
 * Service层代码生成服务
 *
 * @author wenchao
 */
public interface ServiceCodeGenerateService {
    /**
     * 按照表结构信息组装一个ServiceInterface
     * @param introspectedTable 当前表结构信息
     * @param targetPackage service接口所在包名称
     * @return serviceInterface
     */
    TopLevelInterface generateServiceInterface(IntrospectedTable introspectedTable, String targetPackage);

    /**
     * 按照表结构信息组装一个添加一条新记录的接口
     * @param introspectedTable 当前表结构信息
     * @return 添加一条记录的service接口
     */
    Method generateAddNewRecordInterface(IntrospectedTable introspectedTable);

    /**
     * 按照表结构信息组装一个批量添加新记录的接口
     * @param introspectedTable 当前表结构信息
     * @return 批量添加多条记录的service接口
     */
    Method generateAddRecordBatchInterface(IntrospectedTable introspectedTable);

    /**
     * 按照表结构信息组装一个根据唯一主键（一般是自增Id）来删除指定记录的接口
     * @param introspectedTable 当前表结构信息
     * @return 根据唯一主键Id删除指定记录的Service接口
     */
    Method generateRemoveRecordInterface(IntrospectedTable introspectedTable);

    /**
     * 根据当前表结构信息组装多个基于UniqueIndex删除指定记录的接口
     * @param introspectedTable 当前表结构信息
     * @return 根据uniqueIndex删除指定记录的service接口，可能多个
     */
    List<Method> generateRemoveRecordByUniqueIndexInterface(IntrospectedTable introspectedTable);

    /**
     * 根据当前表结构信息组装一个根据唯一主键修改指定记录的接口
     * @param introspectedTable 当前表结构信息
     * @return 根据唯一主键修改指定记录的service接口
     */
    Method generateModifyRecordInterface(IntrospectedTable introspectedTable);

    /**
     * 根据当前表结构信息组装一个根据唯一主键查询指定记录的接口
     * @param introspectedTable 当前表结构信息
     * @return 根据唯一主键信息查询指定记录的接口
     */
    Method generateQueryRecordInterface(IntrospectedTable introspectedTable);

    /**
     * 根据当前表结构信息组装一个根据唯一主键查询指定记录（包含blob列）的接口
     * @param introspectedTable 当前表结构信息
     * @return 根据唯一主键查询指定记录（包含blob列）的接口
     */
    Method generateQueryRecordWithBlobInterface(IntrospectedTable introspectedTable);

    /**
     * 根据当前表结构信息组装一个根据唯一主键查询指定记录（仅包含bloc列）的接口
     * @param introspectedTable 当前表结构信息
     * @return 根据唯一主键查询指定记录（仅包含bloc列）的接口
     */
    Method generateQueryRecordOnlyBlobInterface(IntrospectedTable introspectedTable);

    /**
     * 根据当前表结构信息组装一个查询全部记录（不包含blob列）的接口
     * @param introspectedTable 当前表结构信息
     * @return 查询全部记录（不包含blob列）的接口
     */
    Method generateQueryAllRecordInterface(IntrospectedTable introspectedTable);

    /**
     * 根据当前表结构信息组装一个查询全部记录（不包含blob列）且包含分页功能的接口
     * @param introspectedTable 当前表结构信息
     * @return 查询全部记录（不包含blob列）且包含分页功能的接口
     */
    Method generateQueryAllRecordWithPageInterface(IntrospectedTable introspectedTable);

    /**
     * 根据当前表结构信息按照包含的UniqueIndex组装ifSpecRecordExists接口
     * @param introspectedTable 当前表结构信息
     * @return ifSpecRecordExists接口，可能多个
     */
    List<Method> generateIfExistsInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构信息组装基于双向关系表的级联查询接口
     * @param introspectedTable 当前表结构信息
     * @return 基于双向关系表的级联查询接口，可能多个
     */
    List<Method> generateBinaryCascadeQueryInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构信息组装基于直接外健依赖的级联查询接口
     * @param introspectedTable 当前表结构信息
     * @return 基于直接外健依赖的级联查询接口
     */
    List<Method> generateSingleCascadeQueryInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构信息组装基于表列non null属性的完整性校验接口
     * @param introspectedTable 当前表结构信息
     * @return 基于表列non null属性的完整性校验接口
     */
    Method generateIntegrityCheckInterface(IntrospectedTable introspectedTable);
}
