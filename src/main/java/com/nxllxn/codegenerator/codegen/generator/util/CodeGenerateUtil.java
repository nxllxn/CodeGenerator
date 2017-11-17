package com.nxllxn.codegenerator.codegen.generator.util;

import com.nxllxn.codegenerator.codegen.java.*;
import com.nxllxn.codegenerator.codegen.xml.AttributeNode;
import com.nxllxn.codegenerator.codegen.xml.TagNode;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import com.nxllxn.codegenerator.jdbc.entities.UniqueIndex;
import com.nxllxn.codegenerator.utils.Inflector;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

/**
 * 代码生成工具类，包含一些工具方法实现
 *
 * @author wenchao
 */
public class CodeGenerateUtil {
    public static String assembleGetterMethodName(String propertyName, FullyQualifiedJavaType fullyQualifiedJavaType) {
        StringBuilder sb = new StringBuilder();

        sb.append(propertyName);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }

        if (fullyQualifiedJavaType.equals(FullyQualifiedJavaType
                .getBooleanPrimitiveInstance())) {
            sb.insert(0, "is");
        } else {
            sb.insert(0, "get");
        }

        return sb.toString();
    }

    public static String assembleSetterMethodName(String propertyName) {
        StringBuilder sb = new StringBuilder();

        sb.append(propertyName);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }

        sb.insert(0, "set");

        return sb.toString();
    }

    public static String getCamelCaseString(String inputString,
                                            boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);

            switch (c) {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                case '/':
                case '&':
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;

                default:
                    if (nextUpperCase) {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                    break;
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    }

    /**
     * 将一个字符串转换为下划线的形式,由于算法是按照大写字母进行的转换，所以对于标准的驼峰格式的字符串，此方法可以正常转换，但是其他情况此方法只能尽力转换。
     *
     * @param inputString 输入字符串
     * @return 下划线格式字符串
     */
    public static String getUnderScoreString(String inputString) {
        StringBuilder underScoreBuilder = new StringBuilder();

        Character currentCharacter = null;
        Character lastCharacter;
        for (int index = 0, length = inputString.length(); index < length; index++) {
            lastCharacter = currentCharacter;

            currentCharacter = inputString.charAt(index);

            if (currentCharacter >= 'A' && currentCharacter <= 'Z') {
                if (lastCharacter != null && lastCharacter >= 'A' && lastCharacter <= 'Z') {
                    underScoreBuilder.append(Character.toLowerCase(currentCharacter));

                    continue;
                }

                if (index != 0 && lastCharacter != '_') {
                    underScoreBuilder.append("_");
                }

                underScoreBuilder.append(Character.toLowerCase(currentCharacter));

                continue;
            }

            underScoreBuilder.append(currentCharacter);
        }

        return underScoreBuilder.toString();
    }

    public static String getValidPropertyName(String inputString) {
        String answer;

        if (inputString == null) {
            answer = null;
        } else if (inputString.length() < 2) {
            answer = inputString.toLowerCase(Locale.US);
        } else {
            if (Character.isUpperCase(inputString.charAt(0))
                    && !Character.isUpperCase(inputString.charAt(1))) {
                answer = inputString.substring(0, 1).toLowerCase(Locale.US)
                        + inputString.substring(1);
            } else {
                answer = inputString;
            }
        }

        return answer;
    }

    public static Method getJavaBeansGetter(IntrospectedColumn introspectedColumn,
                                            IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType fqjt = introspectedColumn
                .getJavaType();
        String property = introspectedColumn.getPropertyName();

        Method method = new Method();
        method.setVisibility(Visibility.PUBLIC);
        method.setReturnType(fqjt);
        method.setName(assembleGetterMethodName(property, fqjt));

        StringBuilder sb = new StringBuilder();
        sb.append("return ");
        sb.append(property);
        sb.append(';');
        method.addBodyLine(sb.toString());

        return method;
    }

    public static Field getJavaBeansField(IntrospectedColumn introspectedColumn,
                                          IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType fqjt = introspectedColumn.getJavaType();
        String property = introspectedColumn.getPropertyName();

        Field field = new Field();
        field.setVisibility(Visibility.PRIVATE);
        field.setType(fqjt);
        field.setName(property);

        return field;
    }

    public static Method getJavaBeansSetter(IntrospectedColumn introspectedColumn,
                                            IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType fqjt = introspectedColumn.getJavaType();
        String property = introspectedColumn.getPropertyName();

        Method method = new Method();
        method.setVisibility(Visibility.PUBLIC);
        method.setName(assembleSetterMethodName(property));
        method.addParameter(new Parameter(fqjt, property));

        StringBuilder sb = new StringBuilder();

        sb.append("this.");
        sb.append(property);
        sb.append(" = ");
        sb.append(property);
        sb.append(';');

        method.addBodyLine(sb.toString());

        return method;
    }

    public static String assembleWithMethod(String propertyName) {
        StringBuilder sb = new StringBuilder();

        sb.append(propertyName);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }

        sb.insert(0, "with");

        return sb.toString();
    }

    public static String generateInstantiateStatement(String entityName) {
        if (StringUtils.isBlank(entityName)) {
            return entityName;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(entityName);
        stringBuilder.append(" ");
        stringBuilder.append(getSimpleInstanceObjName(entityName));
        stringBuilder.append(" = new ");
        stringBuilder.append(entityName);
        stringBuilder.append("();");

        return stringBuilder.toString();
    }

    /**
     * 获取当前类的实例对象名称，简单的把首字母小写
     *
     * @param entityName 实例类SimpleName
     * @return 实例对象名称
     */
    public static String getSimpleInstanceObjName(String entityName) {
        if (StringUtils.isBlank(entityName)) {
            return entityName;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(entityName);
        if (Character.isUpperCase(stringBuilder.charAt(0))) {
            stringBuilder.setCharAt(0, Character.toLowerCase(stringBuilder.charAt(0)));
        }
        return stringBuilder.toString();
    }

    /**
     * 简单的将一个属性名称首字母大写，这里假定这个propertyName是一个规范的驼峰格式
     *
     * @param propertyName 属性名称
     * @return 首字母大写后的属性名称，类似于实体名称
     */
    public static String uppercaseFistCharacter(String propertyName) {
        if (StringUtils.isBlank(propertyName)) {
            return propertyName;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(propertyName);
        if (Character.isLowerCase(stringBuilder.charAt(0))) {
            if (stringBuilder.length() == 1 || !Character.isUpperCase(stringBuilder.charAt(1))) {
                stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 根据指定字符串组装常量名称，常量命名规范：下划线分隔字母均大写
     *
     * @param str 参考字符串
     * @return 组装后的常量名称
     */
    public static String assembleConstantName(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        return getUnderScoreString(str).toUpperCase();
    }


    /**
     * 组装添加一条记录的方法名称
     *
     * @param entityName 当前记录实体名称
     * @return 添加一条记录的方法名称，比如addNewRecord
     */
    public static String generateAddRecordMethodName(String entityName) {
        return "addNew" + entityName;
    }

    /**
     * 组装批量添加记录的方法名称
     *
     * @param entityName 当前记录实体名称
     * @return 批量添加记录的方法名称，比如addNewRecordBatch
     */
    public static String generateAddRecordBatchMethodName(String entityName) {
        return "addNew" + entityName + "Batch";
    }

    /**
     * 组装按照唯一主键（比如自增的Id）来删除记录的方法名称
     *
     * @param entityName             当前记录实体名称
     * @param primaryKeyPropertyName 主键所在列属性名称
     * @return 删除记录的方法名称，比如removeRecordById
     */
    public static String generateRemoveRecordMethod(String entityName, String primaryKeyPropertyName) {
        return "remove" + entityName + "By"
                + CodeGenerateUtil.uppercaseFistCharacter(primaryKeyPropertyName);
    }

    /**
     * 组装按照唯一主键（比如自增的Id）来修改记录的方法名称
     *
     * @param entityName             当前记录实体名称
     * @param primaryKeyPropertyName 主键所在列属性名称
     * @return 修改记录方法名称，比如modifyRecordById
     */
    public static String generateModifyRecordMethodName(String entityName, String primaryKeyPropertyName) {
        return "modify" + entityName + "By"
                + CodeGenerateUtil.uppercaseFistCharacter(primaryKeyPropertyName);
    }

    /**
     * 组装按照唯一主键（比如自增的Id）来查询记录的方法名称
     *
     * @param entityName             当前记录实体名称
     * @param primaryKeyPropertyName 主键所在列属性名称
     * @return 查询记录的方法名称, 比如queryRecordById
     */
    public static String generateQueryRecordMethodName(String entityName, String primaryKeyPropertyName) {
        return "query"
                + entityName
                + "By"
                + CodeGenerateUtil.uppercaseFistCharacter(primaryKeyPropertyName);
    }

    /**
     * 组装按照唯一主键（比如自增的Id）查询带BLOB属性的记录的方法名称
     *
     * @param entityName             当前记录实体名称
     * @param primaryKeyPropertyName 主键所在列属性名称
     * @return 查询带BLOB属性的记录的方法名称，比如queryRecordByIdWithBlob
     */
    public static String generateQueryRecordWithBlobMethodName(String entityName, String primaryKeyPropertyName) {
        return "query"
                + entityName
                + "By"
                + CodeGenerateUtil.uppercaseFistCharacter(primaryKeyPropertyName)
                + "WithBlob";
    }

    /**
     * 组装按照唯一主键（比如自增Id）查询仅包含BLOB属性的记录的方法名称
     *
     * @param entityName             当前记录实体名称
     * @param primaryKeyPropertyName 主键所在列属性名称
     * @return 查询仅包含BLOB属性的记录的方法名称，比如queryRecordByIdWithOnlyBlob
     */
    public static String generateQueryRecordWithOnlyBlobMethodName(String entityName, String primaryKeyPropertyName) {
        return "query"
                + entityName
                + "By"
                + CodeGenerateUtil.uppercaseFistCharacter(primaryKeyPropertyName)
                + "WithOnlyBlob";
    }

    /**
     * 组装查询全部记录的方法名称
     *
     * @param entityName 当前记录实体名称
     * @return 查询全部记录的方法名称，比如queryAllRecords
     */
    public static String generateQueryAllRecordMethodName(String entityName) {
        return "queryAll"
                + Inflector.getInstance().pluralize(entityName);
    }

    /**
     * 组装查询全部记录且包含分页功能的方法名称
     *
     * @param entityName 当前记录实体名称
     * @return 查询全部记录且包含分页功能的方法名称，比如queryAllRecordsWithPage
     */
    public static String generateQueryAllRecordWithPageMethodName(String entityName) {
        return "queryAll"
                + Inflector.getInstance().pluralize(entityName) + "WithPage";
    }

    /**
     * 按照UniqueIndex依赖的列名来组装queryCount函数的名称
     *
     * @param entityName  当前实体类名称
     * @param uniqueIndex uniqueIndex实例
     * @return 对应的queryCount函数的名称，比如queryRecordCountByEnName。
     */
    public static String generateQueryCountMethodNameByUniqueIndex(String entityName, UniqueIndex uniqueIndex) {
        String[] columnNamesArray = new String[uniqueIndex.getOnColumnNames().size()];
        int index = 0;
        for (String columnName : uniqueIndex.getOnColumnNames()) {
            columnNamesArray[index++] = CodeGenerateUtil.getCamelCaseString(columnName, true);
        }

        return "query" + entityName + "CountBy" + StringUtils.join(columnNamesArray, "And");
    }

    /**
     * 按照唯一索引组装RemoveRecord方法名称
     *
     * @param entityName  实体名称
     * @param uniqueIndex 当前UniqueIndex对象
     * @return 组装好的函数名称，比如removeRecordByEnName
     */
    public static String generateRemoveRecordMethodNameByUniqueIndex(String entityName, UniqueIndex uniqueIndex) {
        String[] columnNamesArray = new String[uniqueIndex.getOnColumnNames().size()];
        int index = 0;
        for (String columnName : uniqueIndex.getOnColumnNames()) {
            columnNamesArray[index++] = CodeGenerateUtil.getCamelCaseString(columnName, true);
        }

        return "remove" + entityName + "By" + StringUtils.join(columnNamesArray, "And");
    }

    /**
     * 获取有切仅有一列的主键列
     *
     * @param introspectedTable 当前表结构信息
     * @return 唯一的一列主键列
     */
    public static IntrospectedColumn getOnlyPrimaryColumn(IntrospectedTable introspectedTable) {
        //如果当前表结构包含不止一个主键列或者没有主键标识列，那么不生成此方法
        List<IntrospectedColumn> identityColumns = introspectedTable.getIdentityColumns();
        if (identityColumns.size() != 1) {
            return null;
        }

        //如果当前表包含的唯一主键列不是自增列或者不是auto_generated的列，那么不生成此方法
        IntrospectedColumn primaryKeyColumn = identityColumns.get(0);

        //primaryKeyColumn.isGeneratedColumn() 总是返回false
        if (!primaryKeyColumn.isAutoIncrement()) {
            return null;
        }

        return primaryKeyColumn;
    }

    /**
     * 根据主键列生成id result节点
     *
     * @param primaryColumn 主键列
     * @return id 类型的result节点
     */
    public static TagNode generateIdResultNode(IntrospectedColumn primaryColumn) {
        TagNode resultTagNode;
        resultTagNode = new TagNode();

        resultTagNode.setName("id");

        resultTagNode.addAttributeNode(new AttributeNode("column", primaryColumn.getPrimitiveColumnName()));
        resultTagNode.addAttributeNode(new AttributeNode("jdbcType", primaryColumn.getJdbcTypeName()));
        resultTagNode.addAttributeNode(new AttributeNode("property", primaryColumn.getPropertyName()));
        resultTagNode.addAttributeNode(new AttributeNode("javaType", primaryColumn.getJavaType().getFullyQualifiedName()));

        if (!StringUtils.isBlank(primaryColumn.getTypeHandler())) {
            resultTagNode.addAttributeNode(new AttributeNode("typeHandler", primaryColumn.getTypeHandler()));
        }

        return resultTagNode;
    }

    /**
     * 根据列结构信息生成一般的result节点
     *
     * @param generalColumn 非唯一主键列
     * @return result类型的result节点
     */
    public static TagNode generateGeneralResultNode(IntrospectedColumn generalColumn) {
        TagNode resultTagNode;
        resultTagNode = new TagNode();

        resultTagNode.setName("result");

        resultTagNode.addAttributeNode(new AttributeNode("column", generalColumn.getPrimitiveColumnName()));
        resultTagNode.addAttributeNode(new AttributeNode("jdbcType", generalColumn.getJdbcTypeName()));
        resultTagNode.addAttributeNode(new AttributeNode("property", generalColumn.getPropertyName()));
        resultTagNode.addAttributeNode(new AttributeNode("javaType", generalColumn.getJavaType().getFullyQualifiedName()));

        if (!StringUtils.isBlank(generalColumn.getTypeHandler())) {
            resultTagNode.addAttributeNode(new AttributeNode("typeHandler", generalColumn.getTypeHandler()));
        }

        return resultTagNode;
    }

    /**
     * 按照表结构组装values列表
     *
     * @param introspectedTable       当前表结构信息
     * @param simpleInstanceObjName   当前表实体类对应的简单实体名称（简单的将实体类名首字母小写）
     * @param excludePrimaryKeyColumn 是否exclude掉唯一主键所在的列
     * @return values列表，比如(#{simpleInstanceObjName.propertyOne},#{simpleInstanceObjName.propertyTwo}...)
     */
    public static String generateValuesList(IntrospectedTable introspectedTable, String simpleInstanceObjName, boolean excludePrimaryKeyColumn) {
        StringBuilder valuesStringBuilder = new StringBuilder();

        valuesStringBuilder.append("(");

        boolean isFirst = true;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            if (excludePrimaryKeyColumn) {
                if (introspectedColumn.isGeneratedColumn() || introspectedColumn.isAutoIncrement()) {
                    continue;
                }
            }

            if (isFirst) {
                isFirst = false;
            } else {
                valuesStringBuilder.append(",");
            }

            valuesStringBuilder.append("#{");
            valuesStringBuilder.append(simpleInstanceObjName);
            valuesStringBuilder.append(".");
            valuesStringBuilder.append(introspectedColumn.getPropertyName());
            valuesStringBuilder.append("}");
        }

        valuesStringBuilder.append(")");

        return valuesStringBuilder.toString();
    }

    /**
     * 按照表结构信息组装column列表
     *
     * @param introspectedTable         表结构信息
     * @param isExcludePrimaryKeyColumn 是否exclude掉唯一主键列，比如插入操作就没必要插入主键Id，主键Id将在后面有generatedKey进行填充
     * @param isExcludeBaseColumn       是否exclude基本信息列
     * @param isExcludeBlobColumn       是否exclude掉blob列
     * @return column列表
     */
    public static String generateColumnList(
            IntrospectedTable introspectedTable,
            boolean isExcludePrimaryKeyColumn,
            boolean isExcludeBaseColumn,
            boolean isExcludeBlobColumn,
            String startDelimiter,
            String endDelimiter) {
        StringBuilder columnsBuilder = new StringBuilder();

        List<IntrospectedColumn> allColumns;

        if (isExcludeBlobColumn) {
            if (isExcludeBaseColumn) {
                allColumns = introspectedTable.getIdentityColumns();
            } else {
                allColumns = introspectedTable.getAllNonBlobColumns();
            }
        } else {
            if (isExcludeBaseColumn) {
                allColumns = introspectedTable.getAllNonBaseColumns();
            } else {
                allColumns = introspectedTable.getAllColumns();
            }
        }

        boolean isFirst = true;
        for (IntrospectedColumn introspectedColumn : allColumns) {
            //添加操作不需要添加主键
            if (isExcludePrimaryKeyColumn) {
                if (introspectedColumn.isAutoIncrement() || introspectedColumn.isGeneratedColumn()) {
                    continue;
                }
            }

            if (isFirst) {
                isFirst = false;
            } else {
                columnsBuilder.append(",");
            }

            columnsBuilder.append(introspectedColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
        }

        return columnsBuilder.toString();
    }

    public static String generateColumnListWithTableName(
            IntrospectedTable introspectedTable,
            boolean isExcludePrimaryKeyColumn,
            boolean isExcludeBaseColumn,
            boolean isExcludeBlobColumn,
            String startDelimiter,String endDelimiter) {
        StringBuilder columnsBuilder = new StringBuilder();

        List<IntrospectedColumn> allColumns;

        if (isExcludeBlobColumn) {
            if (isExcludeBaseColumn) {
                allColumns = introspectedTable.getIdentityColumns();
            } else {
                allColumns = introspectedTable.getAllNonBlobColumns();
            }
        } else {
            if (isExcludeBaseColumn) {
                allColumns = introspectedTable.getAllNonBaseColumns();
            } else {
                allColumns = introspectedTable.getAllColumns();
            }
        }

        boolean isFirst = true;
        for (IntrospectedColumn introspectedColumn : allColumns) {
            //添加操作不需要添加主键
            if (isExcludePrimaryKeyColumn) {
                if (introspectedColumn.isAutoIncrement() || introspectedColumn.isGeneratedColumn()) {
                    continue;
                }
            }

            if (isFirst) {
                isFirst = false;
            } else {
                columnsBuilder.append(",");
            }

            columnsBuilder.append(introspectedTable.getDelimitedPrimitiveTableName(startDelimiter,endDelimiter));
            columnsBuilder.append(".");
            columnsBuilder.append(introspectedColumn.getDelimitedPrimitiveColumnName(startDelimiter,endDelimiter));
        }

        return columnsBuilder.toString();
    }

    /**
     * 组装一个ForEach Tag Node
     *
     * @param collection for each 操作遍历的集合
     * @param item       引用每一个条目的实例名称
     * @param index      索引
     * @param separator  分隔符
     * @return ForEach Node
     */
    public static TagNode generateForEachNode(String collection, String item, String index, String separator) {
        TagNode forEachTagNode = new TagNode();

        forEachTagNode.setName("foreach");
        forEachTagNode.addAttributeNode(new AttributeNode("collection", collection));
        forEachTagNode.addAttributeNode(new AttributeNode("item", item));
        forEachTagNode.addAttributeNode(new AttributeNode("index", index));
        forEachTagNode.addAttributeNode(new AttributeNode("separator", separator));

        return forEachTagNode;
    }

    /**
     * 按照级联外健组装queryRecordByForeignColumnName方法名
     *
     * @param introspectedTable 当前表结构信息
     * @param referFromColumn   依赖表的外健列信息
     * @return queryRecord方法名，比如A(aid),b(bid),c(aid,bid) 那么组装好的方法名为queryAsByBid
     */
    public static String generateQueryRecordByForeignColumn(IntrospectedTable introspectedTable, IntrospectedColumn referFromColumn) {
        return "query"
                + Inflector.getInstance().pluralize(introspectedTable.getEntityName())
                + "By"
                + CodeGenerateUtil.uppercaseFistCharacter(referFromColumn.getPropertyName());
    }

    /**
     * 按照级联外键组装queryRecordWithAnotherRecords方法名
     *
     * @param introspectedTable 当前表结构信息
     * @param referFromTable    依赖的表结构信息
     * @return queryRecord方法名，比如A(aid),b(bid),c(aid,bid) 那么组装好的方法名为queryAWithBs
     */
    public static String generateQueryRecordWithCollectionMethodName(IntrospectedTable introspectedTable, IntrospectedTable referFromTable) {
        return "query"
                + introspectedTable.getEntityName()
                + "With"
                + Inflector.getInstance().pluralize(referFromTable.getEntityName());
    }

    /**
     * 按照直接外键依赖组装queryRecordWithAnotherRecord方法名
     *
     * @param introspectedTable 当前表结构信息
     * @param referFromTable    依赖的表结构信息
     * @return queryRecord方法名，比如A(aid),b(bid),c(aid,bid) 那么组装好的方法名为queryAWithB
     */
    public static String generateQueryRecordWithAssociationMethodName(IntrospectedTable introspectedTable, IntrospectedTable referFromTable) {
        return "query"
                + introspectedTable.getEntityName()
                + "With"
                + referFromTable.getEntityName();
    }

    /**
     * 根据UniqueIndex组装isSpecRecordWithXXXAndYYYExists
     *
     * @param entityName  实体名称
     * @param uniqueIndex 唯一索引信息
     * @return 方法名
     */
    public static String generateIsSpecRecordExistsMethodName(String entityName, UniqueIndex uniqueIndex) {
        String[] columnNamesArray = new String[uniqueIndex.getOnColumnNames().size()];
        int index = 0;
        for (String columnName : uniqueIndex.getOnColumnNames()) {
            columnNamesArray[index++] = CodeGenerateUtil.getCamelCaseString(columnName, true);
        }

        return "isSpec" + entityName + "With" + StringUtils.join(columnNamesArray, "And") + "Exists";
    }

    /**
     * 根据relation表中外键依赖组装removeRelations方法名，比如A（aid），B(bid),C(aid,bid)
     *
     * @param referFromTable 主动外键依赖的表 B
     * @param referToColumn  依赖到的列 aid
     * @return removeRelations方法名, 比如removeBRelationsByAid
     */
    public static String generateRemoveRelationsMethodName(IntrospectedTable referFromTable, IntrospectedColumn referToColumn) {
        return "remove" + referFromTable.getEntityName() + "RelationsBy" + uppercaseFistCharacter(referToColumn.getPropertyName());
    }

    public static String generateQueryRecordByUniqueIndexMethodName(String entityName, UniqueIndex uniqueIndex) {
        String[] columnNamesArray = new String[uniqueIndex.getOnColumnNames().size()];
        int index = 0;
        for (String columnName : uniqueIndex.getOnColumnNames()) {
            columnNamesArray[index++] = CodeGenerateUtil.getCamelCaseString(columnName, true);
        }

        return "query" + entityName + "By" + StringUtils.join(columnNamesArray, "And");
    }
}
