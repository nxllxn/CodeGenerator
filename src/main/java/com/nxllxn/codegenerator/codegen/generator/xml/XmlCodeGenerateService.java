package com.nxllxn.codegenerator.codegen.generator.xml;

import com.nxllxn.codegenerator.codegen.xml.TagNode;
import com.nxllxn.codegenerator.codegen.xml.XmlDocument;
import com.nxllxn.codegenerator.config.SqlMapGeneratorConfiguration;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.List;

/**
 * XML代码生成服务接口
 *
 * @author wenchao
 */
public interface XmlCodeGenerateService {
    /**
     * 生成Mapper xml文档对象，初始化文件头 publicId systemId
     * @param introspectedTable 当前表结构信息
     * @param configuration xml文档生成相关配置信息
     * @return XmlDocument对象
     */
    XmlDocument generateMapperXmlDocument(IntrospectedTable introspectedTable, SqlMapGeneratorConfiguration configuration);

    /**
     * 生成xml文档mapper根节点
     * @param introspectedTable 当前表结构信息
     * @param configuration xml文档生成相关配置信息
     * @return xml文档根节点 mapper节点
     */
    TagNode generateMapperXMlRootNode(IntrospectedTable introspectedTable, SqlMapGeneratorConfiguration configuration);

    /**
     * 生成无blob列，有blob列，仅blob列对应的resultMap，如果没有blob类型的列，那么不添加
     * @param introspectedTable 当前表结构信息
     * @return xml mybatis resultMap 节点集合，注意可能多个
     */
    List<TagNode> generateResultMaps(IntrospectedTable introspectedTable);

    /**
     * 添加增加一条记录接口实现
     * @param introspectedTable 当前表结构信息
     * @return 添加一条记录的xml节点
     */
    TagNode generateAddRecordImplement(IntrospectedTable introspectedTable);

    /**
     * 添加批量增加记录的接口实现
     * @param introspectedTable 当前表结构信息
     * @return 批量增加记录的xml sql insert 节点
     */
    TagNode generateAddRecordBatchImplement(IntrospectedTable introspectedTable);

    /**
     * 添加根据唯一主键（比如自增Id）删除指定记录的实现
     * @param introspectedTable 当前表结构信息
     * @return 根据唯一主键（比如自增Id）删除指定记录xml sql delete 节点
     */
    TagNode generateRemoveRecordImplement(IntrospectedTable introspectedTable);

    /**
     * 添加根据唯一索引删除指定记录的实现
     * @param introspectedTable 当前表结构信息
     * @return 据唯一索引删除指定记录的xml sql delete 节点
     */
    List<TagNode> generateRemoveRecordByUniqueIndexImplement(IntrospectedTable introspectedTable);

    /**
     * 添加根据唯一主键修改指定记录的实现
     * @param introspectedTable 当前表结构信息
     * @return 根据唯一主键修改指定记录的xml sql update节点
     */
    TagNode generateModifyRecordImplement(IntrospectedTable introspectedTable);

    /**
     * 添加根据唯一主键查询指定记录的实现
     * @param introspectedTable 当前表结构信息
     * @return 查询指定记录的xml sql select节点
     */
    TagNode generateQueryRecordImplement(IntrospectedTable introspectedTable);

    /**
     * 添加根据唯一主键查询指定记录包括blob列的实现
     * @param introspectedTable 当前表结构信息
     * @return 查询指定记录，包括blob列的xml sql select节点
     */
    TagNode generateQueryRecordWithBlobImplement(IntrospectedTable introspectedTable);

    /**
     * 添加根据唯一主键查询指定记录仅Blob列的实现
     * @param introspectedTable 当前表结构信息
     * @return 查询指定记录，仅包括Blob列的xml sql select节点
     */
    TagNode generateQueryRecordOnlyBlobImplement(IntrospectedTable introspectedTable);

    /**
     * 添加查询全部记录的实现，查询全部记录时，考虑到数据库负荷已经网络等关系，默认不会查询Blob列
     * @param introspectedTable 当前表结构信息
     * @return 查询全部记录的xml sql select节点
     */
    TagNode generateQueryAllRecordImplement(IntrospectedTable introspectedTable);

    /**
     * 添加查询全部记录且提供分页的接口实现，考虑到数据库负荷已经网络等关系，默认不会查询Blob列
     * @param introspectedTable 当前表结构信息
     * @return 查询全部记录的xml sql select节点
     */
    TagNode generateQueryAllRecordWithPageImplement(IntrospectedTable introspectedTable);

    /**
     * 添加根据当前表结构包含的UniqueIndex查询对应记录总数的接口实现，此实现用于为isSpecRecordExists实现提供支持
     * @param introspectedTable 当前表结构信息
     * @return 查询指定UniqueIndex对应记录数目的xml sql select节点
     */
    List<TagNode> generateQueryCountImplement(IntrospectedTable introspectedTable);

    /**
     * 添加根据当前表结构包含的外键依赖关系利用join关键字进行级联查询的接口实现
     * @param introspectedTable 当前表结构信息
     * @return 根据当前表结构包含的外键依赖关系利用join关键字进行级联查询的xml sql select节点
     */
    List<TagNode> generateCascadeQueryImplement(IntrospectedTable introspectedTable);

    /**
     * 按照当前表包含的外健依赖关系 组装级联的resultMap属性
     * @param introspectedTable 当前表结构信息
     * @return 级联的ResultMap集合
     */
    List<TagNode> generateCascadeResultMap(IntrospectedTable introspectedTable);

    /**
     * 为XML添加删除指定关系表中和当前外键关联的关系条目的接口实现
     * @param introspectedTable 当前表结构信息
     * @return 删除指定关系条目的xml sql delete节点
     */
    List<TagNode> generateRemoveRelationsByForeignKey(IntrospectedTable introspectedTable);

    /**
     * 按照当前表包含的基于关系表的外键依赖，逆向查询当前实体基本属性，比如表结构A（aid），B(bid)，c（aid，bid），那么组装好的方法为queryAByBid
     * @param introspectedTable 当前表结构信息
     * @return 基于关系表的外键依赖得到的queryAll接口
     */
    List<TagNode> generateSingleCascadeQueryInterface(IntrospectedTable introspectedTable);

    /**
     * 组装按照指定UniqueIndex格式查找指定记录的接口实现
     * @param introspectedTable 当前表结构信息
     * @return 按照指定UniqueIndex格式查找指定记录的接口实现
     */
    List<TagNode> generateQueryRecordByUniqueIndex(IntrospectedTable introspectedTable);
}
