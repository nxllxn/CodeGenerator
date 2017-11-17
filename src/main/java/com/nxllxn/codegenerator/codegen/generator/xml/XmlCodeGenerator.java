package com.nxllxn.codegenerator.codegen.generator.xml;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generated.GeneratedXmlFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.xml.TagNode;
import com.nxllxn.codegenerator.codegen.xml.XmlDocument;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.SqlMapGeneratorConfiguration;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.ArrayList;
import java.util.List;

/**
 * XML代码生成器
 */
public class XmlCodeGenerator extends AbstractCodeGenerator {
    /**
     * XML 代码生成服务
     */
    private XmlCodeGenerateService xmlCodeGenerateService;

    /**
     * XML代码生成相关配置
     */
    private SqlMapGeneratorConfiguration configuration;

    /**
     * 文件编码
     */
    private String fileEncoding;

    public XmlCodeGenerator() {
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        xmlCodeGenerateService = new XmlCodeGenerateServiceImpl(context.getBeginningDelimiter(),context.getEndingDelimiter());

        configuration = context.getSqlMapGeneratorConfiguration();

        fileEncoding = context.getFileEncoding();
    }

    @Override
    public List<GeneratedFile> generate() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();

        GeneratedXmlFile generatedXmlFile;
        for (IntrospectedTable introspectedTable:introspectedTables){
            //1.生成XmlDocument文档对象
            XmlDocument mapperXmlDocument = xmlCodeGenerateService.generateMapperXmlDocument(introspectedTable,configuration);

            //2.生成XML文件根节点mapper节点
            TagNode rootNode = xmlCodeGenerateService.generateMapperXMlRootNode(introspectedTable,configuration);

            //3.按照无blob列，有blob列，仅blob列组装resultMap，如果没有blob列，不组装
            rootNode.addTagNodes(xmlCodeGenerateService.generateResultMaps(introspectedTable));

            //added:15.按照当前表包含的外健依赖关系 组装级联的resultMap属性
            rootNode.addTagNodes(xmlCodeGenerateService.generateCascadeResultMap(introspectedTable));

            //added:16.为XML添加删除指定关系表中和当前外键关联的关系条目的接口实现
            rootNode.addTagNodes(xmlCodeGenerateService.generateRemoveRelationsByForeignKey(introspectedTable));

            //4.为XML添加增加一条记录接口实现
            rootNode.addChildNode(xmlCodeGenerateService.generateAddRecordImplement(introspectedTable));

            //5.为XML添加批量添加记录接口实现
            rootNode.addChildNode(xmlCodeGenerateService.generateAddRecordBatchImplement(introspectedTable));

            //6.为XML添加删除指定标识符对应记录的接口实现
            rootNode.addChildNode(xmlCodeGenerateService.generateRemoveRecordImplement(introspectedTable));

            //added:15.为XML添加按照当前表包含的UniqueIndex删除指定记录接口实现，注意UniqueIndex可能多个
            rootNode.addTagNodes(xmlCodeGenerateService.generateRemoveRecordByUniqueIndexImplement(introspectedTable));

            //7.为XML添加按照指定标识符修改记录的接口实现
            rootNode.addChildNode(xmlCodeGenerateService.generateModifyRecordImplement(introspectedTable));

            //8.为XML添加按照指定标识符查询指定记录的接口实现（只包含主键列和基础列）
            rootNode.addChildNode(xmlCodeGenerateService.generateQueryRecordImplement(introspectedTable));

            //9.为XML添加按照指定标识符查询指定记录的接口实现（包括主键列，基础列，blob列）
            rootNode.addChildNode(xmlCodeGenerateService.generateQueryRecordWithBlobImplement(introspectedTable));

            //10.为XML添加按照指定标识符查询指定记录的接口实现（仅包括blob列）
            rootNode.addChildNode(xmlCodeGenerateService.generateQueryRecordOnlyBlobImplement(introspectedTable));

            //11.为XML添加查询全部记录的接口实现（只包含主键列和基础列）
            rootNode.addChildNode(xmlCodeGenerateService.generateQueryAllRecordImplement(introspectedTable));

            //12.为XML添加查询全部记录的接口实现，此接口提供分页
            rootNode.addChildNode(xmlCodeGenerateService.generateQueryAllRecordWithPageImplement(introspectedTable));

            //13.为XML添加查询指定UniqueIndex对应的记录数目的接口实现，注意UniqueIndex可能多个
            rootNode.addTagNodes(xmlCodeGenerateService.generateQueryCountImplement(introspectedTable));

            //14.按照当前列表依赖的外键依赖 利用join进行级联查询,注意外键依赖可能多个，目前实现只考虑一层的级联，可以多个表
            rootNode.addTagNodes(xmlCodeGenerateService.generateCascadeQueryImplement(introspectedTable));

            //14.按照当前表包含的基于关系表的外键依赖，逆向查询当前实体基本属性
            rootNode.addTagNodes(xmlCodeGenerateService.generateSingleCascadeQueryInterface(introspectedTable));

            //15.根据当前表包含的唯一索引信息查询指定记录
            rootNode.addTagNodes(xmlCodeGenerateService.generateQueryRecordByUniqueIndex(introspectedTable));

            mapperXmlDocument.setRootNode(rootNode);

            generatedXmlFile = new GeneratedXmlFile();
            generatedXmlFile.setTargetDir(configuration.getTargetDirectory());
            generatedXmlFile.setTargetPackage(configuration.getTargetPackage());
            generatedXmlFile.setFileEncoding(fileEncoding);
            generatedXmlFile.setXmlDocument(mapperXmlDocument);
            generatedXmlFile.setFileName(introspectedTable.getEntityName() + "Mapper");
            generatedFiles.add(generatedXmlFile);
        }

        return generatedFiles;
    }
}
