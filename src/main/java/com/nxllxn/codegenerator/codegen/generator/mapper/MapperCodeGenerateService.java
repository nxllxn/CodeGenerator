package com.nxllxn.codegenerator.codegen.generator.mapper;

import com.nxllxn.codegenerator.codegen.java.Method;
import com.nxllxn.codegenerator.codegen.java.TopLevelInterface;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.List;

/**
 * Mapper 代码生成服务接口
 *
 * @author wemchao
 */
public interface MapperCodeGenerateService {
    /**
     * 按照当前表结构组装Mapper接口
     * @param introspectedTable 当前表结构信息
     * @param targetPackage 目标包名称是指Mapper接口所在包名
     * @return Mapper接口
     */
    TopLevelInterface generateMapperInterface(IntrospectedTable introspectedTable, String targetPackage);

    /**
     * 按照当前表结构组装增加一条记录的接口
     * @param introspectedTable 当前表结构信息
     * @return 添加一条记录的Mapper接口
     */
    Method generateAddRecordInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构组装批量添加记录接口
     * @param introspectedTable 当前表结构信息
     * @return 批量添加记录的Mapper接口
     */
    Method generateAddRecordBatchInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构组装删除指定标识符对应记录的接口
     * @param introspectedTable 当前表结构信息
     * @return 删除一条记录的Mapper接口
     */
    Method generateRemoveRecordInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构组装按照指定标识符修改记录的接口
     * @param introspectedTable 当前表结构信息
     * @return 修改一条记录的Mapper接口
     */
    Method generateModifyRecordInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构组装按照指定标识符查询指定记录的接口（只包含主键列和基础列）
     * @param introspectedTable 当前表结构信息
     * @return 根据唯一主键查询一条记录的接口
     */
    Method generateQueryRecordInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构组装按照指定标识符查询指定记录的接口（包括主键列，基础列，blob列）
     * @param introspectedTable 当前表结构信息
     * @return 根据唯一主键查询一条记录（包含blob）的接口
     */
    Method generateQueryRecordWithBlobInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构组装按照指定标识符查询指定记录的接口（仅包括blob列）
     * @param introspectedTable 当前表结构信息
     * @return 根据唯一主键查询指定记录（仅包含blob）的接口
     */
    Method generateQueryRecordOnlyBlobInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构组装查询全部记录的接口（只包含主键列和基础列）
     * @param introspectedTable 当前表结构信息
     * @return 查询全部记录（不包含blob）的接口
     */
    Method generateQueryAllRecordInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表包含的外键依赖，利用join关键字级联查询当前实体基本属性以及关联的属性
     * 比如A(aid)，B(bid)，C(cid)三张表，A，B为实体表，C为联系表，那么对于A表，此接口相当于A Left Join C on C.aid = A.aid Left Join B on c.bid = B.bib where aid = ？
     * @param introspectedTable 当前表结构信息
     * @return 基于关系表的外健依赖关系进行两层的级联查询
     */
    List<Method> generateBinaryCascadeQueryInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表包含的外键依赖，逆向查询当前实体基本属性
     * 比如A(aid)，B(bid)，C(cid)三张表，A，B为实体表，C为联系表，那么对于A表，此接口相当于C，Join A on C.aid = A.aid where bid = ？
     *
     * @param introspectedTable 当前表结构信息 当前表结构信息
     * @return 基于直接的外健依赖进行单层的级联查询的Mapper接口
     */
    List<Method> generateSingleCascadeQueryInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构组装查询全部记录的接口，此接口提供分页
     * @param introspectedTable 当前表结构信息
     * @return 查询全部记录提供分页功能的Mapper接口
     */
    Method generateQueryAllRecordWithPageInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表接口组装查询指定UniqueIndex对应的记录数目的接口，注意UniqueIndex可能多个
     * @param introspectedTable 当前表结构信息
     * @return 查询指定uniqueIndex对应的记录数目的接口
     */
    List<Method> generateQueryCountInterface(IntrospectedTable introspectedTable);

    /**
     * 按照当前表结构组装按照当前表包含的UniqueIndex删除指定记录接口
     * @param introspectedTable 当前表结构信息
     * @return 按照UniqueIndex删除指定记录的接口
     */
    List<Method> generateRemoveRecordByUniqueIndexInterface(IntrospectedTable introspectedTable);

    /**
     * 为Mapper添加删除指定关系表中和当前外键关联的关系条目的接口
     * @param introspectedTable 当前表结构信息
     * @return 删除指定关系表中和当前外键关联的关系条目的接口，注意外键可能多个
     */
    List<Method> generateRemoveRelationsByForeignKey(IntrospectedTable introspectedTable);

    /**
     * 为Mapper添加根据指定唯一索引查询记录的接口
     * @param introspectedTable 当前表结构信息
     * @return 按照唯一索引查询指定记录的接口集合
     */
    List<Method> generateQueryRecordByUniqueIndex(IntrospectedTable introspectedTable);
}
