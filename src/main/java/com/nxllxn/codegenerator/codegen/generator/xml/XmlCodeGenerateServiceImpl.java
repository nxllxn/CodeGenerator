package com.nxllxn.codegenerator.codegen.generator.xml;

import com.nxllxn.codegenerator.codegen.generator.util.CodeGenerateUtil;
import com.nxllxn.codegenerator.codegen.java.FullyQualifiedJavaType;
import com.nxllxn.codegenerator.codegen.xml.AttributeNode;
import com.nxllxn.codegenerator.codegen.xml.TagNode;
import com.nxllxn.codegenerator.codegen.xml.TextNode;
import com.nxllxn.codegenerator.codegen.xml.XmlDocument;
import com.nxllxn.codegenerator.config.SqlMapGeneratorConfiguration;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import com.nxllxn.codegenerator.jdbc.entities.ForeignKey;
import com.nxllxn.codegenerator.jdbc.entities.UniqueIndex;
import com.nxllxn.codegenerator.utils.AssembleUtil;
import com.nxllxn.codegenerator.utils.Inflector;

import java.util.ArrayList;
import java.util.List;

/**
 * XML代码生成服务接口实现
 *
 * @author wenchao
 */
public class XmlCodeGenerateServiceImpl implements XmlCodeGenerateService {
    /**
     * 不包含blob列的resultMap
     */
    private static final String RESULT_MAP_BASE = "resultMapBase";

    /**
     * 包含blob列的resultMap
     */
    private static final String RESULT_MAP_WITH_BLOB = "resultMapWithBlob";

    /**
     * 只包含blob列的resultMap
     */
    private static final String RESULT_MAP_WITH_ONLY_BLOB = "resultMapWithOnlyBlob";

    private String startDelimiter;
    private String endDelimiter;

    public XmlCodeGenerateServiceImpl(String startDelimiter, String endDelimiter) {
        this.startDelimiter = startDelimiter;
        this.endDelimiter = endDelimiter;
    }

    @Override
    public XmlDocument generateMapperXmlDocument(IntrospectedTable introspectedTable, SqlMapGeneratorConfiguration configuration) {
        XmlDocument mapperXmlDocument = new XmlDocument();

        mapperXmlDocument.setPublicId(configuration.getXmlDocumentPublicId());
        mapperXmlDocument.setSystemId(configuration.getXmlDocumentSystemId());

        return mapperXmlDocument;
    }

    @Override
    public TagNode generateMapperXMlRootNode(IntrospectedTable introspectedTable, SqlMapGeneratorConfiguration configuration) {
        TagNode rootNode = new TagNode();

        rootNode.setName("mapper");
        rootNode.addAttributeNode(new AttributeNode("namespace",
                configuration.getNameSpacePackage() + AssembleUtil.PACKAGE_SEPERATOR + introspectedTable.getEntityName() + "Mapper"));

        return rootNode;
    }

    @Override
    public List<TagNode> generateResultMaps(IntrospectedTable introspectedTable) {
        IntrospectedColumn theOnlyPrimaryColumn = introspectedTable.getOnlyPrimaryColumn();

        List<TagNode> resultMapNodes = new ArrayList<>();

        List<TagNode> primaryColumnResultNode = getPrimaryResultTagNode(introspectedTable, theOnlyPrimaryColumn);

        TagNode resultTagNode;
        List<TagNode> baseColumnResultNode = new ArrayList<>();
        for (IntrospectedColumn currentColumn : introspectedTable.getBaseColumns()) {
            resultTagNode = CodeGenerateUtil.generateGeneralResultNode(currentColumn);

            baseColumnResultNode.add(resultTagNode);
        }

        List<TagNode> blobColumnResultNode = new ArrayList<>();
        for (IntrospectedColumn currentColumn : introspectedTable.getBlobColumns()) {
            resultTagNode = CodeGenerateUtil.generateGeneralResultNode(currentColumn);

            blobColumnResultNode.add(resultTagNode);
        }

        TagNode resultMapNode = new TagNode();
        resultMapNode.setName("resultMap");
        resultMapNode.addAttributeNode(new AttributeNode("id", "resultMapBase"));
        resultMapNode.addAttributeNode(new AttributeNode("type",
                introspectedTable.generateFullyQualifiedJavaType().getFullyQualifiedName()));
        resultMapNode.addTagNodes(primaryColumnResultNode);
        resultMapNode.addTagNodes(baseColumnResultNode);
        resultMapNodes.add(resultMapNode);

        //如果当前表有blob属性列
        if (introspectedTable.hasBlobColumns()) {
            TagNode resultMapWithBlobNode = new TagNode();

            resultMapWithBlobNode.setName("resultMap");
            resultMapWithBlobNode.addAttributeNode(new AttributeNode("id", "resultMapWithBlob"));
            resultMapWithBlobNode.addAttributeNode(new AttributeNode("type",
                    introspectedTable.generateFullyQualifiedJavaType().getFullyQualifiedName()));
            resultMapWithBlobNode.addTagNodes(primaryColumnResultNode);
            resultMapWithBlobNode.addTagNodes(baseColumnResultNode);
            resultMapWithBlobNode.addTagNodes(blobColumnResultNode);

            resultMapNodes.add(resultMapWithBlobNode);


            TagNode resultMapWithOnlyBlobNode = new TagNode();

            resultMapWithOnlyBlobNode.setName("resultMap");
            resultMapWithOnlyBlobNode.addAttributeNode(new AttributeNode("id", "resultMapWithOnlyBlob"));
            resultMapWithOnlyBlobNode.addAttributeNode(new AttributeNode("type",
                    introspectedTable.generateFullyQualifiedJavaType().getFullyQualifiedName()));
            resultMapWithOnlyBlobNode.addTagNodes(blobColumnResultNode);

            resultMapNodes.add(resultMapWithOnlyBlobNode);
        }

        return resultMapNodes;
    }

    @Override
    public TagNode generateAddRecordImplement(IntrospectedTable introspectedTable) {
        TagNode addNewRecordNode = new TagNode();

        //添加insert节点
        addNewRecordNode.setName("insert");
        addNewRecordNode.addAttributeNode(
                new AttributeNode("id", CodeGenerateUtil.generateAddRecordMethodName(introspectedTable.getEntityName())));
        addNewRecordNode.addAttributeNode(
                new AttributeNode("parameterType", introspectedTable.generateFullyQualifiedJavaType().getFullyQualifiedName()));

        //如果存在唯一主键（如自增Id）那么开启useGeneratedKeys
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn != null) {
            addNewRecordNode.addAttributeNode(new AttributeNode("useGeneratedKeys", "true"));
            addNewRecordNode.addAttributeNode(new AttributeNode("keyProperty", primaryKeyColumn.getPropertyName()));
        }

        //第一行 `INSERT INTO `
        TextNode insertTextNode = new TextNode("INSERT INTO ");

        //第二行 `tableName(columnOne,columnTwo...) `
        StringBuilder tableAndColumnsBuilder = new StringBuilder();
        tableAndColumnsBuilder.append("    ");
        tableAndColumnsBuilder.append(introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
        tableAndColumnsBuilder.append("(");

        tableAndColumnsBuilder.append(CodeGenerateUtil.generateColumnList(introspectedTable, true,false,false,startDelimiter,endDelimiter));

        tableAndColumnsBuilder.append(") ");
        TextNode tableAndColumnTextNode = new TextNode(tableAndColumnsBuilder.toString());

        //第三行，`VALUES (valueOne,valueTwo...)`
        String simpleInstanceObjName = CodeGenerateUtil.getSimpleInstanceObjName(introspectedTable.getEntityName());
        StringBuilder valuesStringBuilder = new StringBuilder();
        valuesStringBuilder.append("VALUES ");
        valuesStringBuilder.append(CodeGenerateUtil.generateValuesList(introspectedTable, simpleInstanceObjName, true));
        TextNode valueNode = new TextNode(valuesStringBuilder.toString());

        addNewRecordNode.addChildNode(insertTextNode);
        addNewRecordNode.addChildNode(tableAndColumnTextNode);
        addNewRecordNode.addChildNode(valueNode);

        return addNewRecordNode;
    }

    @Override
    public TagNode generateAddRecordBatchImplement(IntrospectedTable introspectedTable) {
        TagNode addNewRecordBatchNode = new TagNode();

        //添加insert节点
        addNewRecordBatchNode.setName("insert");
        addNewRecordBatchNode.addAttributeNode(
                new AttributeNode("id", CodeGenerateUtil.generateAddRecordBatchMethodName(introspectedTable.getEntityName())));
        addNewRecordBatchNode.addAttributeNode(
                new AttributeNode("parameterType", FullyQualifiedJavaType.getNewListInstance().getFullyQualifiedName()));

        //如果存在唯一主键（如自增Id）那么开启useGeneratedKeys
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn != null) {
            addNewRecordBatchNode.addAttributeNode(new AttributeNode("useGeneratedKeys", "true"));
            addNewRecordBatchNode.addAttributeNode(new AttributeNode("keyProperty", primaryKeyColumn.getPropertyName()));
        }

        //第一行，`INSERT INTO `
        TextNode insertTextNode = new TextNode("INSERT INTO ");

        //第二行，`tableName(columnOne,columnTwo...) `
        StringBuilder tableAndColumnsBuilder = new StringBuilder();
        tableAndColumnsBuilder.append("    ");
        tableAndColumnsBuilder.append(introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
        tableAndColumnsBuilder.append("(");
        tableAndColumnsBuilder.append(CodeGenerateUtil.generateColumnList(introspectedTable, true,false,false,startDelimiter,endDelimiter));
        tableAndColumnsBuilder.append(") ");
        TextNode tableAndColumnTextNode = new TextNode(tableAndColumnsBuilder.toString());

        //第三行。`VALUES `
        StringBuilder valuesStatementBuilder = new StringBuilder();
        valuesStatementBuilder.append("VALUES ");
        TextNode valueStatementNode = new TextNode(valuesStatementBuilder.toString());

        //后面再跟一个ForEach TagNode
        String simpleInstanceObjName = CodeGenerateUtil.getSimpleInstanceObjName(introspectedTable.getEntityName());
        TagNode valuesDetailNode = CodeGenerateUtil.generateForEachNode("list", simpleInstanceObjName, "index", ",");
        valuesDetailNode.addChildNode(new TextNode(CodeGenerateUtil.generateValuesList(introspectedTable, simpleInstanceObjName, true)));

        addNewRecordBatchNode.addChildNode(insertTextNode);
        addNewRecordBatchNode.addChildNode(tableAndColumnTextNode);
        addNewRecordBatchNode.addChildNode(valueStatementNode);
        addNewRecordBatchNode.addChildNode(valuesDetailNode);

        return addNewRecordBatchNode;
    }

    @Override
    public TagNode generateRemoveRecordImplement(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        TagNode removeRecordTagNode = new TagNode();

        //添加一个delete标签
        removeRecordTagNode.setName("delete");
        removeRecordTagNode.addAttributeNode(
                new AttributeNode("id", CodeGenerateUtil.generateRemoveRecordMethod(introspectedTable.getEntityName(), primaryKeyColumn.getPropertyName())));
        removeRecordTagNode.addAttributeNode(
                new AttributeNode("parameterType", primaryKeyColumn.getJavaType().getFullyQualifiedName()));

        //第一行，`delete from `
        TextNode deleteFromTextNode = new TextNode("delete from ");

        //第二行，`tableName `
        TextNode tableNameTextNode = new TextNode("    " + introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter) + " ");

        //第三行，`where primaryColumn.columnName = primaryColumn.propertyName`
        StringBuilder whereClauseBuilder = new StringBuilder();
        whereClauseBuilder.append("where ");
        whereClauseBuilder.append(primaryKeyColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
        whereClauseBuilder.append("=");
        whereClauseBuilder.append("#{");
        whereClauseBuilder.append(primaryKeyColumn.getPropertyName());
        whereClauseBuilder.append("}");
        TextNode whereClauseTextNode = new TextNode(whereClauseBuilder.toString());

        removeRecordTagNode.addChildNode(deleteFromTextNode);
        removeRecordTagNode.addChildNode(tableNameTextNode);
        removeRecordTagNode.addChildNode(whereClauseTextNode);

        return removeRecordTagNode;
    }

    @Override
    public List<TagNode> generateRemoveRecordByUniqueIndexImplement(IntrospectedTable introspectedTable) {
        List<TagNode> removeRecordNodes = new ArrayList<>();

        TagNode removeRecordNode;
        for (UniqueIndex uniqueIndex : introspectedTable.getUniqueIndexColumns()) {
            removeRecordNode = new TagNode();

            //添加一个delete标签
            removeRecordNode.setName("delete");
            removeRecordNode.addAttributeNode(
                    new AttributeNode("id",
                            CodeGenerateUtil.generateRemoveRecordMethodNameByUniqueIndex(introspectedTable.getEntityName(),uniqueIndex)));

            //第一行，`delete from `
            TextNode deleteFromTextNode = new TextNode("delete from ");

            //第二行，`tableName `
            TextNode tableNameTextNode = new TextNode("    " + introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter) + " ");

            //第三行，`where uniqueIndex.onColumn.columnName = uniqueIndex.onColumn.propertyName
            //       and uniqueIndex.onColumn1.columnName = uniqueIndex.onColumn1.propertyName`
            StringBuilder whereClauseBuilder = new StringBuilder();
            whereClauseBuilder.append("where ");

            boolean isFirst = true;
            for (IntrospectedColumn onColumn:uniqueIndex.getOnColumns()){
                if (isFirst){
                    isFirst = false;
                } else {
                    whereClauseBuilder.append(" and ");
                }

                whereClauseBuilder.append(onColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
                whereClauseBuilder.append("=");
                whereClauseBuilder.append("#{");
                whereClauseBuilder.append(onColumn.getPropertyName());
                whereClauseBuilder.append("}");
            }

            TextNode whereClauseTextNode = new TextNode(whereClauseBuilder.toString());

            removeRecordNode.addChildNode(deleteFromTextNode);
            removeRecordNode.addChildNode(tableNameTextNode);
            removeRecordNode.addChildNode(whereClauseTextNode);

            removeRecordNodes.add(removeRecordNode);
        }

        return removeRecordNodes;
    }

    @Override
    public TagNode generateModifyRecordImplement(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        TagNode modifyRecordNode = new TagNode();

        //添加一个update标签
        modifyRecordNode.setName("update");
        modifyRecordNode.addAttributeNode(
                new AttributeNode("id",CodeGenerateUtil.generateModifyRecordMethodName(
                        introspectedTable.getEntityName(),primaryKeyColumn.getPropertyName())));

        //第一行 `update `
        TextNode updateTextNode = new TextNode("update " + introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter) + " ");

        //第二行 `set(columnOne=#{propertyOne},columnTwo=#{propertyTwo}...)`
        StringBuilder columnToValueBuilder = new StringBuilder();

        columnToValueBuilder.append("set ");
        String simpleInstanceObjName = "new" + introspectedTable.getEntityName();
        boolean isFirst = true;
        for (IntrospectedColumn introspectedColumn:introspectedTable.getAllColumns()){
            if (introspectedColumn.isAutoIncrement() || introspectedColumn.isGeneratedColumn()){
                continue;
            }

            if (isFirst){
                isFirst = false;
            } else {
                columnToValueBuilder.append(",");
            }

            columnToValueBuilder.append(introspectedColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            columnToValueBuilder.append("=");
            columnToValueBuilder.append("#{");
            columnToValueBuilder.append(simpleInstanceObjName);
            columnToValueBuilder.append(".");
            columnToValueBuilder.append(introspectedColumn.getPropertyName());
            columnToValueBuilder.append("}");
        }
        columnToValueBuilder.append(" ");
        TextNode tableAndColumnsTextNode = new TextNode(columnToValueBuilder.toString());

        //第三行 `where primaryColumnName=#{primaryPropertyName)`
        StringBuilder whereClauseBuilder = new StringBuilder();
        whereClauseBuilder.append("where ");
        whereClauseBuilder.append(primaryKeyColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
        whereClauseBuilder.append("=");
        whereClauseBuilder.append("#{");
        whereClauseBuilder.append("old" + CodeGenerateUtil.uppercaseFistCharacter(primaryKeyColumn.getPropertyName()));
        whereClauseBuilder.append("}");

        TextNode whereClauseTextNode = new TextNode(whereClauseBuilder.toString());

        modifyRecordNode.addChildNode(updateTextNode);
        modifyRecordNode.addChildNode(tableAndColumnsTextNode);
        modifyRecordNode.addChildNode(whereClauseTextNode);

        return modifyRecordNode;
    }

    @Override
    public TagNode generateQueryRecordImplement(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        TagNode queryRecordNode = new TagNode();

        //添加一个select标签
        queryRecordNode.setName("select");
        queryRecordNode.addAttributeNode(
                new AttributeNode("id",CodeGenerateUtil.generateQueryRecordMethodName(
                        introspectedTable.getEntityName(),primaryKeyColumn.getPropertyName()
                )));
        queryRecordNode.addAttributeNode(
                new AttributeNode("parameterType",primaryKeyColumn.getJavaType().getFullyQualifiedName()));
        queryRecordNode.addAttributeNode(
                new AttributeNode("resultMap",RESULT_MAP_BASE));

        //第一行 `select columnOne,columnTwo...`
        TextNode selectColumnsTextNode = new TextNode("select " +
                CodeGenerateUtil.generateColumnList(introspectedTable,false,false,true,startDelimiter,endDelimiter) + " ");

        //第二行 `from tableName `
        TextNode fromTableTextNode = new TextNode("from " + introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter) + " ");

        //第三行，`where primaryColumn.columnName = primaryColumn.propertyName`
        StringBuilder whereClauseBuilder = new StringBuilder();
        whereClauseBuilder.append("where ");
        whereClauseBuilder.append(primaryKeyColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
        whereClauseBuilder.append("=");
        whereClauseBuilder.append("#{");
        whereClauseBuilder.append(primaryKeyColumn.getPropertyName());
        whereClauseBuilder.append("}");
        TextNode whereClauseTextNode = new TextNode(whereClauseBuilder.toString());

        queryRecordNode.addChildNode(selectColumnsTextNode);
        queryRecordNode.addChildNode(fromTableTextNode);
        queryRecordNode.addChildNode(whereClauseTextNode);

        return queryRecordNode;
    }

    @Override
    public TagNode generateQueryRecordWithBlobImplement(IntrospectedTable introspectedTable) {
        if (!introspectedTable.hasBlobColumns()){
            return null;
        }

        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        TagNode queryRecordWithBlobNode = new TagNode();

        //添加一个select标签
        queryRecordWithBlobNode.setName("select");
        queryRecordWithBlobNode.addAttributeNode(
                new AttributeNode("id",CodeGenerateUtil.generateQueryRecordWithBlobMethodName(
                        introspectedTable.getEntityName(),primaryKeyColumn.getPropertyName()
                )));
        queryRecordWithBlobNode.addAttributeNode(
                new AttributeNode("parameterType",primaryKeyColumn.getJavaType().getFullyQualifiedName()));
        queryRecordWithBlobNode.addAttributeNode(
                new AttributeNode("resultMap",RESULT_MAP_WITH_BLOB));

        //第一行 `select columnOne,columnTwo...`
        TextNode selectColumnsTextNode = new TextNode("select " +
                CodeGenerateUtil.generateColumnList(introspectedTable,false,false,false,startDelimiter,endDelimiter) + " ");

        //第二行 `from tableName `
        TextNode fromTableTextNode = new TextNode("from " + introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter) + " ");

        //第三行，`where primaryColumn.columnName = primaryColumn.propertyName`
        StringBuilder whereClauseBuilder = new StringBuilder();
        whereClauseBuilder.append("where ");
        whereClauseBuilder.append(primaryKeyColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
        whereClauseBuilder.append("=");
        whereClauseBuilder.append("#{");
        whereClauseBuilder.append(primaryKeyColumn.getPropertyName());
        whereClauseBuilder.append("}");
        TextNode whereClauseTextNode = new TextNode(whereClauseBuilder.toString());

        queryRecordWithBlobNode.addChildNode(selectColumnsTextNode);
        queryRecordWithBlobNode.addChildNode(fromTableTextNode);
        queryRecordWithBlobNode.addChildNode(whereClauseTextNode);

        return queryRecordWithBlobNode;
    }

    @Override
    public TagNode generateQueryRecordOnlyBlobImplement(IntrospectedTable introspectedTable) {
        if (!introspectedTable.hasBlobColumns()){
            return null;
        }

        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        TagNode queryRecordWithOnlyBlobNode = new TagNode();

        //添加一个select标签
        queryRecordWithOnlyBlobNode.setName("select");
        queryRecordWithOnlyBlobNode.addAttributeNode(
                new AttributeNode("id",CodeGenerateUtil.generateQueryRecordWithOnlyBlobMethodName(
                        introspectedTable.getEntityName(),primaryKeyColumn.getPropertyName()
                )));
        queryRecordWithOnlyBlobNode.addAttributeNode(
                new AttributeNode("parameterType",primaryKeyColumn.getJavaType().getFullyQualifiedName()));
        queryRecordWithOnlyBlobNode.addAttributeNode(
                new AttributeNode("resultMap",RESULT_MAP_WITH_ONLY_BLOB));

        //第一行 `select columnOne,columnTwo...`
        TextNode selectColumnsTextNode = new TextNode("select " +
                CodeGenerateUtil.generateColumnList(introspectedTable,true,true,false,startDelimiter,endDelimiter) + " ");

        //第二行 `from tableName `
        TextNode fromTableTextNode = new TextNode("from " + introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter) + " ");

        //第三行，`where primaryColumn.columnName = primaryColumn.propertyName`
        StringBuilder whereClauseBuilder = new StringBuilder();
        whereClauseBuilder.append("where ");
        whereClauseBuilder.append(primaryKeyColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
        whereClauseBuilder.append("=");
        whereClauseBuilder.append("#{");
        whereClauseBuilder.append(primaryKeyColumn.getPropertyName());
        whereClauseBuilder.append("}");
        TextNode whereClauseTextNode = new TextNode(whereClauseBuilder.toString());

        queryRecordWithOnlyBlobNode.addChildNode(selectColumnsTextNode);
        queryRecordWithOnlyBlobNode.addChildNode(fromTableTextNode);
        queryRecordWithOnlyBlobNode.addChildNode(whereClauseTextNode);

        return queryRecordWithOnlyBlobNode;
    }

    @Override
    public TagNode generateQueryAllRecordImplement(IntrospectedTable introspectedTable) {
        TagNode queryAllRecordNode = new TagNode();

        //插入一个select标签
        queryAllRecordNode.setName("select");
        queryAllRecordNode.addAttributeNode(
                new AttributeNode("id",CodeGenerateUtil.generateQueryAllRecordMethodName(introspectedTable.getEntityName())));
        queryAllRecordNode.addAttributeNode(
                new AttributeNode("resultMap",RESULT_MAP_BASE));

        //第一行 `select columnOne,columnTwo...`
        TextNode selectColumnsTextNode = new TextNode("select " +
                CodeGenerateUtil.generateColumnList(introspectedTable,false,false,true,startDelimiter,endDelimiter) + " ");

        //第二行 `from tableName `
        TextNode fromTableTextNode = new TextNode("from " + introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter) + " ");

        queryAllRecordNode.addChildNode(selectColumnsTextNode);
        queryAllRecordNode.addChildNode(fromTableTextNode);

        return queryAllRecordNode;
    }

    @Override
    public TagNode generateQueryAllRecordWithPageImplement(IntrospectedTable introspectedTable) {
        TagNode queryAllRecordWithPageNode = new TagNode();

        //插入一个select标签
        queryAllRecordWithPageNode.setName("select");
        queryAllRecordWithPageNode.addAttributeNode(
                new AttributeNode("id",CodeGenerateUtil.generateQueryAllRecordWithPageMethodName(introspectedTable.getEntityName())));
        queryAllRecordWithPageNode.addAttributeNode(
                new AttributeNode("resultMap",RESULT_MAP_BASE));

        //第一行 `select columnOne,columnTwo...`
        TextNode selectColumnsTextNode = new TextNode("select " +
                CodeGenerateUtil.generateColumnList(introspectedTable,false,false,true,startDelimiter,endDelimiter) + " ");

        //第二行 `from tableName `
        TextNode fromTableTextNode = new TextNode("from " + introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter) + " ");

        //第三行
        TextNode limitTextNode = new TextNode("limit #{offset},#{limit}");

        queryAllRecordWithPageNode.addChildNode(selectColumnsTextNode);
        queryAllRecordWithPageNode.addChildNode(fromTableTextNode);
        queryAllRecordWithPageNode.addChildNode(limitTextNode);

        return queryAllRecordWithPageNode;
    }

    @Override
    public List<TagNode> generateQueryCountImplement(IntrospectedTable introspectedTable) {
        List<TagNode> queryCountTagNodes = new ArrayList<>();

        TagNode queryCountTagNode;
        for (UniqueIndex uniqueIndex:introspectedTable.getUniqueIndexColumns()){
            queryCountTagNode = new TagNode();

            //添加一个select标签
            queryCountTagNode.setName("select");
            queryCountTagNode.addAttributeNode(
                    new AttributeNode("id",CodeGenerateUtil.generateQueryCountMethodNameByUniqueIndex(
                            introspectedTable.getEntityName(),uniqueIndex)));
            queryCountTagNode.addAttributeNode(
                    new AttributeNode("resultType", FullyQualifiedJavaType.getIntegerInstance().getFullyQualifiedName()));

            //第一行 `select count(1) `
            TextNode selectCountNode = new TextNode("select count(1) ");

            //第二行 `from tableName `
            TextNode fromTableNode = new TextNode("from " + introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter) + " ");

            //第三行 `where uniqueIndex.column.columnName = #{uniqueIndex.column.propertyName} and ...`
            StringBuilder whereClauseBuilder = new StringBuilder();
            whereClauseBuilder.append("where ");
            boolean isFirst = true;
            for (IntrospectedColumn onColumn:uniqueIndex.getOnColumns()){
                if (isFirst){
                    isFirst = false;
                } else {
                    whereClauseBuilder.append(" AND ");
                }

                whereClauseBuilder.append(onColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
                whereClauseBuilder.append("=");
                whereClauseBuilder.append("#{");
                whereClauseBuilder.append(onColumn.getPropertyName());
                whereClauseBuilder.append("}");
            }
            TextNode whereClauseTextNode = new TextNode(whereClauseBuilder.toString());

            queryCountTagNode.addChildNode(selectCountNode);
            queryCountTagNode.addChildNode(fromTableNode);
            queryCountTagNode.addChildNode(whereClauseTextNode);

            queryCountTagNodes.add(queryCountTagNode);
        }

        return queryCountTagNodes;
    }

    @Override
    public List<TagNode> generateCascadeQueryImplement(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null){
            return null;
        }

        List<TagNode> cascadeQueryNodes = new ArrayList<>();

        TagNode cascadeQueryNode;
        for (ForeignKey foreignKey:introspectedTable.getForeignKeyColumns()){
            //associate
            cascadeQueryNode = new TagNode();

            IntrospectedTable referFromTable = foreignKey.getReferFromTable();
            IntrospectedColumn referFromColumn = foreignKey.getReferFromColumn();
            IntrospectedTable referToTable = foreignKey.getReferToTable();
            IntrospectedColumn referToColumn = foreignKey.getReferToColumn();


            //添加一个select标签
            cascadeQueryNode.setName("select");
            cascadeQueryNode.addAttributeNode(
                    new AttributeNode("id",CodeGenerateUtil.generateQueryRecordWithAssociationMethodName(introspectedTable, referToTable)));
            cascadeQueryNode.addAttributeNode(
                    new AttributeNode("resultMap",generateResultMapName(foreignKey)));

            //第一行 `select columnOne,columnTwo...`
            StringBuilder selectColumnsBuilder = new StringBuilder();
            selectColumnsBuilder.append("select ");
            selectColumnsBuilder.append(CodeGenerateUtil.generateColumnListWithTableName(
                    introspectedTable,false,false,false,startDelimiter,endDelimiter));
            selectColumnsBuilder.append(",");
            selectColumnsBuilder.append(CodeGenerateUtil.generateColumnListWithTableName(
                    referFromTable,false,false,false,startDelimiter,endDelimiter));
            selectColumnsBuilder.append(" ");
            TextNode selectColumnsTextNode = new TextNode(selectColumnsBuilder.toString());

            //第二行 `from tableOne `
            StringBuilder fromTableBuilder = new StringBuilder();
            fromTableBuilder.append("from ");
            fromTableBuilder.append(referFromTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            TextNode fromTableNode = new TextNode(fromTableBuilder.toString());

            //第三行 `left join tableTwo on tableOne.columnName=tableTwo.columnName`
            StringBuilder leftJoinBuilder = new StringBuilder();
            leftJoinBuilder.append("left join ");
            leftJoinBuilder.append(referToTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(" on ");
            leftJoinBuilder.append(referToTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(".");
            leftJoinBuilder.append(referToColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            leftJoinBuilder.append("=");
            leftJoinBuilder.append(referFromTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(".");
            leftJoinBuilder.append(referFromColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(" ");
            TextNode leftJoinNode = new TextNode(leftJoinBuilder.toString());

            cascadeQueryNode.addChildNode(selectColumnsTextNode);
            cascadeQueryNode.addChildNode(fromTableNode);
            cascadeQueryNode.addChildNode(leftJoinNode);

            cascadeQueryNodes.add(cascadeQueryNode);
        }

        for (ForeignKey referAsForeignKey:introspectedTable.getReferAsForeignKeyColumns()){
            if (referAsForeignKey.getInnerToForeignKey() == null
                    || referAsForeignKey.getInnerFromForeignKey() == null){
                //single join 只需要一层Join

                cascadeQueryNode = new TagNode();

                IntrospectedTable referFromTable = referAsForeignKey.getReferFromTable();
                IntrospectedColumn referFromColumn = referAsForeignKey.getReferFromColumn();
                IntrospectedTable referToTable = referAsForeignKey.getReferToTable();
                IntrospectedColumn referToColumn = referAsForeignKey.getReferToColumn();


                //添加一个select标签
                cascadeQueryNode.setName("select");
                cascadeQueryNode.addAttributeNode(
                        new AttributeNode("id",CodeGenerateUtil.generateQueryRecordWithCollectionMethodName(introspectedTable, referFromTable)));
                cascadeQueryNode.addAttributeNode(
                        new AttributeNode("resultMap",generateResultMapNameRevert(referAsForeignKey)));

                //第一行 `select columnOne,columnTwo...`
                StringBuilder selectColumnsBuilder = new StringBuilder();
                selectColumnsBuilder.append("select ");
                selectColumnsBuilder.append(CodeGenerateUtil.generateColumnListWithTableName(
                        introspectedTable,false,false,false,startDelimiter,endDelimiter));
                selectColumnsBuilder.append(",");
                selectColumnsBuilder.append(CodeGenerateUtil.generateColumnListWithTableName(
                        referFromTable,false,false,false,startDelimiter,endDelimiter));
                selectColumnsBuilder.append(" ");
                TextNode selectColumnsTextNode = new TextNode(selectColumnsBuilder.toString());

                //第二行 `from tableOne `
                StringBuilder fromTableBuilder = new StringBuilder();
                fromTableBuilder.append("from ");
                fromTableBuilder.append(referToTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
                TextNode fromTableNode = new TextNode(fromTableBuilder.toString());

                //第三行 `left join tableTwo on tableOne.columnName=tableTwo.columnName`
                StringBuilder leftJoinBuilder = new StringBuilder();
                leftJoinBuilder.append("left join ");
                leftJoinBuilder.append(referFromTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
                leftJoinBuilder.append(" on ");
                leftJoinBuilder.append(referFromTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
                leftJoinBuilder.append(".");
                leftJoinBuilder.append(referFromColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
                leftJoinBuilder.append("=");
                leftJoinBuilder.append(referToTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
                leftJoinBuilder.append(".");
                leftJoinBuilder.append(referToColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
                leftJoinBuilder.append(" ");
                TextNode leftJoinNode = new TextNode(leftJoinBuilder.toString());

                cascadeQueryNode.addChildNode(selectColumnsTextNode);
                cascadeQueryNode.addChildNode(fromTableNode);
                cascadeQueryNode.addChildNode(leftJoinNode);

                cascadeQueryNodes.add(cascadeQueryNode);

                continue;
            }

            ForeignKey innerFromForeignKey = referAsForeignKey.getInnerFromForeignKey();
            ForeignKey innerToForeignKey = referAsForeignKey.getInnerToForeignKey();

            IntrospectedTable referFromTable = referAsForeignKey.getReferFromTable();

            cascadeQueryNode = new TagNode();

            //添加一个select标签
            cascadeQueryNode.setName("select");
            cascadeQueryNode.addAttributeNode(
                    new AttributeNode("id",CodeGenerateUtil.generateQueryRecordWithCollectionMethodName(introspectedTable, referFromTable)));
            cascadeQueryNode.addAttributeNode(
                    new AttributeNode("resultMap",generateResultMapNameForCascade(referAsForeignKey)));

            //第一行 `select columnOne,columnTwo...`
            StringBuilder selectColumnsBuilder = new StringBuilder();
            selectColumnsBuilder.append("select ");
            selectColumnsBuilder.append(CodeGenerateUtil.generateColumnListWithTableName(
                    introspectedTable,false,false,false,startDelimiter,endDelimiter));
            selectColumnsBuilder.append(",");
            selectColumnsBuilder.append(CodeGenerateUtil.generateColumnListWithTableName(
                    referFromTable,false,false,false,startDelimiter,endDelimiter));
            selectColumnsBuilder.append(" ");
            TextNode selectColumnsTextNode = new TextNode(selectColumnsBuilder.toString());

            //第二行 `from tableOne `
            StringBuilder fromTableBuilder = new StringBuilder();
            fromTableBuilder.append("from ");
            fromTableBuilder.append(innerToForeignKey.getReferToTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            TextNode fromTableNode = new TextNode(fromTableBuilder.toString());

            //第三行 `left join tableTwo on tableOne.columnName=tableTwo.columnName`
            StringBuilder leftJoinBuilder = new StringBuilder();
            leftJoinBuilder.append("left join ");
            leftJoinBuilder.append(innerToForeignKey.getReferFromTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(" on ");
            leftJoinBuilder.append(innerToForeignKey.getReferToTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(".");
            leftJoinBuilder.append(innerToForeignKey.getReferToColumn().getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            leftJoinBuilder.append("=");
            leftJoinBuilder.append(innerToForeignKey.getReferFromTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(".");
            leftJoinBuilder.append(innerToForeignKey.getReferFromColumn().getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(" ");
            TextNode firstLeftJoinNode = new TextNode(leftJoinBuilder.toString());

            //第四行 `left join tableThree on tableTwo.columnName=tableThree.columnName`
            leftJoinBuilder.setLength(0);
            leftJoinBuilder.append("left join ");
            leftJoinBuilder.append(innerFromForeignKey.getReferFromTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(" on ");
            leftJoinBuilder.append(innerFromForeignKey.getReferToTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(".");
            leftJoinBuilder.append(innerFromForeignKey.getReferToColumn().getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            leftJoinBuilder.append("=");
            leftJoinBuilder.append(innerFromForeignKey.getReferFromTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(".");
            leftJoinBuilder.append(innerFromForeignKey.getReferFromColumn().getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(" ");
            TextNode secondLeftJoinNode = new TextNode(leftJoinBuilder.toString());

            //第五行，`where tableOne.primaryKeyColumn=propertyName'
            StringBuilder whereClauseBuilder = new StringBuilder();
            whereClauseBuilder.append("where ");
            whereClauseBuilder.append(introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            whereClauseBuilder.append(".");
            whereClauseBuilder.append(primaryKeyColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            whereClauseBuilder.append("=");
            whereClauseBuilder.append("#{");
            whereClauseBuilder.append(primaryKeyColumn.getPropertyName());
            whereClauseBuilder.append("}");
            TextNode whereClauseNode = new TextNode(whereClauseBuilder.toString());

            cascadeQueryNode.addChildNode(selectColumnsTextNode);
            cascadeQueryNode.addChildNode(fromTableNode);
            cascadeQueryNode.addChildNode(firstLeftJoinNode);
            cascadeQueryNode.addChildNode(secondLeftJoinNode);
            cascadeQueryNode.addChildNode(whereClauseNode);

            cascadeQueryNodes.add(cascadeQueryNode);
        }

        return cascadeQueryNodes;
    }

    @Override
    public List<TagNode> generateCascadeResultMap(IntrospectedTable introspectedTable) {
        IntrospectedColumn theOnlyPrimaryColumn = introspectedTable.getOnlyPrimaryColumn();

        List<TagNode> resultMapTagNodes = new ArrayList<>();

        List<TagNode> primaryColumnResultNode = getPrimaryResultTagNode(introspectedTable, theOnlyPrimaryColumn);

        List<TagNode> baseColumnResultNode = getBaseResultTagNode(introspectedTable);

        TagNode resultMapTagNode;
        for (ForeignKey foreignKey:introspectedTable.getForeignKeyColumns()){
            resultMapTagNode = new TagNode();
            resultMapTagNode.setName("resultMap");
            resultMapTagNode.addAttributeNode(
                    new AttributeNode("id", generateResultMapName(foreignKey)));
            resultMapTagNode.addAttributeNode(
                    new AttributeNode("type",introspectedTable.generateFullyQualifiedJavaType().getFullyQualifiedName()));

            resultMapTagNode.addTagNodes(primaryColumnResultNode);
            resultMapTagNode.addTagNodes(baseColumnResultNode);

            IntrospectedTable referToTable = foreignKey.getReferToTable();

            TagNode associationTagNode = new TagNode();
            associationTagNode.setName("association");
            associationTagNode.addAttributeNode(
                    new AttributeNode("property", Inflector.getInstance().pluralize(CodeGenerateUtil.getSimpleInstanceObjName(referToTable.getEntityName()))));
            associationTagNode.addAttributeNode(
                    new AttributeNode("javaType",referToTable.generateFullyQualifiedJavaType().getFullyQualifiedName()));

            List<TagNode> primaryColumnResultNodeForAssociationTable = getPrimaryResultTagNode(
                    referToTable, referToTable.getOnlyPrimaryColumn()
            );
            List<TagNode> baseColumnResultNodeForAssociationTable = getBaseResultTagNode(referToTable);

            associationTagNode.addTagNodes(primaryColumnResultNodeForAssociationTable);
            associationTagNode.addTagNodes(baseColumnResultNodeForAssociationTable);

            resultMapTagNode.addChildNode(associationTagNode);

            resultMapTagNodes.add(resultMapTagNode);
        }

        for (ForeignKey referAsForeignKey:introspectedTable.getReferAsForeignKeyColumns()){
            IntrospectedTable innerFromTable;
            String resultMapName;
            if (referAsForeignKey.getInnerToForeignKey() != null
                    && referAsForeignKey.getInnerFromForeignKey() != null){
                ForeignKey innerFromForeignKey = referAsForeignKey.getInnerFromForeignKey();
                innerFromTable = innerFromForeignKey.getReferFromTable();

                resultMapName = generateResultMapNameForCascade(referAsForeignKey);
            } else {
                innerFromTable = referAsForeignKey.getReferFromTable();

                resultMapName = generateResultMapNameRevert(referAsForeignKey);
            }

            resultMapTagNode = new TagNode();
            resultMapTagNode.setName("resultMap");
            resultMapTagNode.addAttributeNode(
                    new AttributeNode("id", resultMapName));
            resultMapTagNode.addAttributeNode(
                    new AttributeNode("type",introspectedTable.generateFullyQualifiedJavaType().getFullyQualifiedName()));

            resultMapTagNode.addTagNodes(primaryColumnResultNode);
            resultMapTagNode.addTagNodes(baseColumnResultNode);

            TagNode collectionTagNode = new TagNode();
            collectionTagNode.setName("collection");
            collectionTagNode.addAttributeNode(
                    new AttributeNode("property", Inflector.getInstance().pluralize(CodeGenerateUtil.getSimpleInstanceObjName(innerFromTable.getEntityName()))));
            collectionTagNode.addAttributeNode(
                    new AttributeNode("ofType",innerFromTable.generateFullyQualifiedJavaType().getFullyQualifiedName()));

            List<TagNode> primaryColumnResultNodeForInnerTable = getPrimaryResultTagNode(
                    innerFromTable, innerFromTable.getOnlyPrimaryColumn()
            );
            List<TagNode> baseColumnResultNodeForInnerTable = getBaseResultTagNode(innerFromTable);

            collectionTagNode.addTagNodes(primaryColumnResultNodeForInnerTable);
            collectionTagNode.addTagNodes(baseColumnResultNodeForInnerTable);

            resultMapTagNode.addChildNode(collectionTagNode);

            resultMapTagNodes.add(resultMapTagNode);
        }

        return resultMapTagNodes;
    }

    @Override
    public List<TagNode> generateSingleCascadeQueryInterface(IntrospectedTable introspectedTable) {
        List<TagNode> queryAllByRelationNodes = new ArrayList<>();

        TagNode queryAllByRelationNode;
        for (ForeignKey referAsForeignKey:introspectedTable.getReferAsForeignKeyColumns()){
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null){
                continue;
            }

            ForeignKey innerFromForeignKey = referAsForeignKey.getInnerFromForeignKey();
            ForeignKey innerToForeignKey = referAsForeignKey.getInnerToForeignKey();

            queryAllByRelationNode = new TagNode();

            //添加一个select标签
            queryAllByRelationNode.setName("select");
            queryAllByRelationNode.addAttributeNode(
                    new AttributeNode("id",CodeGenerateUtil.generateQueryRecordByForeignColumn(
                            introspectedTable, innerFromForeignKey.getReferFromColumn())));
            queryAllByRelationNode.addAttributeNode(
                    new AttributeNode("resultMap","resultMapBase"));

            //第一行 `select columnOne,columnTwo...`
            StringBuilder selectColumnsBuilder = new StringBuilder();
            selectColumnsBuilder.append("select ");
            selectColumnsBuilder.append(CodeGenerateUtil.generateColumnListWithTableName(
                    introspectedTable,false,false,false,startDelimiter,endDelimiter));
            selectColumnsBuilder.append(" ");
            TextNode selectColumnsTextNode = new TextNode(selectColumnsBuilder.toString());

            //第二行 `from tableTwo `
            StringBuilder fromTableBuilder = new StringBuilder();
            fromTableBuilder.append("from ");
            fromTableBuilder.append(innerToForeignKey.getReferFromTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            TextNode fromTableNode = new TextNode(fromTableBuilder.toString());

            //第三行 `left join tableTwo on tableOne.columnName=tableTwo.columnName`
            StringBuilder leftJoinBuilder = new StringBuilder();
            leftJoinBuilder.append("left join ");
            leftJoinBuilder.append(innerToForeignKey.getReferToTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(" on ");
            leftJoinBuilder.append(innerToForeignKey.getReferToTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(".");
            leftJoinBuilder.append(innerToForeignKey.getReferToColumn().getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            leftJoinBuilder.append("=");
            leftJoinBuilder.append(innerToForeignKey.getReferFromTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(".");
            leftJoinBuilder.append(innerToForeignKey.getReferFromColumn().getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            leftJoinBuilder.append(" ");
            TextNode leftJoinNode = new TextNode(leftJoinBuilder.toString());

            //第五行，`where tableOne.primaryKeyColumn=propertyName'
            StringBuilder whereClauseBuilder = new StringBuilder();
            whereClauseBuilder.append("where ");
            whereClauseBuilder.append(innerFromForeignKey.getReferToTable().getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            whereClauseBuilder.append(".");
            whereClauseBuilder.append(innerFromForeignKey.getReferToColumn().getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            whereClauseBuilder.append("=");
            whereClauseBuilder.append("#{");
            whereClauseBuilder.append(innerFromForeignKey.getReferToColumn().getPropertyName());
            whereClauseBuilder.append("}");
            TextNode whereClauseNode = new TextNode(whereClauseBuilder.toString());

            queryAllByRelationNode.addChildNode(selectColumnsTextNode);
            queryAllByRelationNode.addChildNode(fromTableNode);
            queryAllByRelationNode.addChildNode(leftJoinNode);
            queryAllByRelationNode.addChildNode(whereClauseNode);

            queryAllByRelationNodes.add(queryAllByRelationNode);
        }

        return queryAllByRelationNodes;
    }

    private String generateResultMapName(ForeignKey referAsForeignKey) {
        String fromTableName = referAsForeignKey.getReferFromTable().getEntityName();

        String toTableName = referAsForeignKey.getReferToTable().getEntityName();

        return "resultMap" + fromTableName + "With" + toTableName;
    }

    private String generateResultMapNameRevert(ForeignKey referAsForeignKey) {
        String fromTableName = referAsForeignKey.getReferFromTable().getEntityName();

        String toTableName = referAsForeignKey.getReferToTable().getEntityName();

        return "resultMap" + toTableName + "With" + Inflector.getInstance().pluralize(fromTableName);
    }

    private List<TagNode> getPrimaryResultTagNode(IntrospectedTable introspectedTable, IntrospectedColumn theOnlyPrimaryColumn) {
        List<TagNode> primaryColumnResultNode = new ArrayList<>();
        TagNode resultTagNode;
        for (IntrospectedColumn currentColumn : introspectedTable.getIdentityColumns()) {
            if (theOnlyPrimaryColumn != null && currentColumn.equals(theOnlyPrimaryColumn)) {
                resultTagNode = CodeGenerateUtil.generateIdResultNode(currentColumn);
            } else {
                resultTagNode = CodeGenerateUtil.generateGeneralResultNode(currentColumn);
            }

            primaryColumnResultNode.add(resultTagNode);
        }
        return primaryColumnResultNode;
    }

    private List<TagNode> getBaseResultTagNode(IntrospectedTable introspectedTable) {
        TagNode resultTagNode;
        List<TagNode> baseColumnResultNode = new ArrayList<>();
        for (IntrospectedColumn currentColumn : introspectedTable.getBaseColumns()) {
            resultTagNode = CodeGenerateUtil.generateGeneralResultNode(currentColumn);

            baseColumnResultNode.add(resultTagNode);
        }
        return baseColumnResultNode;
    }

    private String generateResultMapNameForCascade(ForeignKey foreignKey) {
        String fromTableName = foreignKey.getInnerFromForeignKey()
                .getReferFromTable().getEntityName();

        String toTableName = foreignKey.getInnerToForeignKey()
                .getReferToTable().getEntityName();


        return "resultMap" + toTableName + "With" + Inflector.getInstance().pluralize(fromTableName);
    }

    @Override
    public List<TagNode> generateRemoveRelationsByForeignKey(IntrospectedTable introspectedTable) {
        List<TagNode> removeRelationTagNodes = new ArrayList<>();

        IntrospectedTable referFromTable;
        //IntrospectedColumn referFromColumn;
        IntrospectedTable throughTable;

        IntrospectedColumn throughColumn;
        //IntrospectedTable referToTable;
        IntrospectedColumn referToColumn;
        TagNode removeRelationTagNode;
        for (ForeignKey referAsForeignKey:introspectedTable.getReferAsForeignKeyColumns()){
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null){
                continue;
            }

            referFromTable = referAsForeignKey.getInnerFromForeignKey().getReferFromTable();    //B
            //referFromColumn= referAsForeignKey.getInnerFromForeignKey().getReferFromColumn();    //B column
            throughTable = referAsForeignKey.getInnerFromForeignKey().getReferToTable();    //C

            throughColumn = referAsForeignKey.getInnerToForeignKey().getReferFromColumn();    //c a column
            //referToTable = referAsForeignKey.getInnerToForeignKey().getReferToTable();    //A
            referToColumn = referAsForeignKey.getInnerToForeignKey().getReferToColumn();    //a column

            //组装方法名称 removeBRelationsByAColumn
            removeRelationTagNode = new TagNode();
            removeRelationTagNode.setName("delete");
            removeRelationTagNode.addAttributeNode(
                    new AttributeNode("id",CodeGenerateUtil.generateRemoveRelationsMethodName(referFromTable,referToColumn)));
            removeRelationTagNode.addAttributeNode(
                    new AttributeNode("parameterType",referToColumn.getJavaType().getFullyQualifiedName()));

            //第一行 `delete from `
            removeRelationTagNode.addChildNode(new TextNode("delete from "));

            //第二行 `tableName `
            removeRelationTagNode.addChildNode(new TextNode(throughTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter)));

            //第三行，`where aid = #{aid}`
            StringBuilder whereClauseBuilder = new StringBuilder();
            whereClauseBuilder.append("where ");
            whereClauseBuilder.append(throughColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
            whereClauseBuilder.append("=");
            whereClauseBuilder.append("#{");
            whereClauseBuilder.append(referToColumn.getPropertyName());
            whereClauseBuilder.append("}");
            removeRelationTagNode.addChildNode(new TextNode(whereClauseBuilder.toString()));

            removeRelationTagNodes.add(removeRelationTagNode);
        }

        return removeRelationTagNodes;
    }

    @Override
    public List<TagNode> generateQueryRecordByUniqueIndex(IntrospectedTable introspectedTable) {
        List<TagNode> queryRecordByUniqueIndexTagNodes = new ArrayList<>();

        TagNode queryRecordByUniqueIndexTagNode;
        for (UniqueIndex uniqueIndex:introspectedTable.getUniqueIndexColumns()){
            queryRecordByUniqueIndexTagNode = new TagNode();

            //添加一个select标签
            queryRecordByUniqueIndexTagNode.setName("select");
            queryRecordByUniqueIndexTagNode.addAttributeNode(
                    new AttributeNode("id",CodeGenerateUtil.generateQueryRecordByUniqueIndexMethodName(
                            introspectedTable.getEntityName(),uniqueIndex)));
            queryRecordByUniqueIndexTagNode.addAttributeNode(
                    new AttributeNode("resultMap",RESULT_MAP_BASE));

            //第一行 `select columnOne,columnTwo...`
            TextNode selectColumnsTextNode = new TextNode("select " +
                    CodeGenerateUtil.generateColumnList(introspectedTable,false,false,true,startDelimiter,endDelimiter) + " ");

            //第二行 `from tableName `
            TextNode fromTableTextNode = new TextNode("from " + introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter) + " ");

            //第三行 `where uniqueIndex.column.columnName = #{uniqueIndex.column.propertyName} and ...`
            StringBuilder whereClauseBuilder = new StringBuilder();
            whereClauseBuilder.append("where ");
            boolean isFirst = true;
            for (IntrospectedColumn onColumn:uniqueIndex.getOnColumns()){
                if (isFirst){
                    isFirst = false;
                } else {
                    whereClauseBuilder.append(" AND ");
                }

                whereClauseBuilder.append(onColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
                whereClauseBuilder.append("=");
                whereClauseBuilder.append("#{");
                whereClauseBuilder.append(onColumn.getPropertyName());
                whereClauseBuilder.append("}");
            }
            TextNode whereClauseTextNode = new TextNode(whereClauseBuilder.toString());

            queryRecordByUniqueIndexTagNode.addChildNode(selectColumnsTextNode);
            queryRecordByUniqueIndexTagNode.addChildNode(fromTableTextNode);
            queryRecordByUniqueIndexTagNode.addChildNode(whereClauseTextNode);

            queryRecordByUniqueIndexTagNodes.add(queryRecordByUniqueIndexTagNode);
        }

        return queryRecordByUniqueIndexTagNodes;
    }
}
