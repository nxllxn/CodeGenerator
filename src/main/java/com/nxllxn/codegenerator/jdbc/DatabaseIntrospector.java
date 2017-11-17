package com.nxllxn.codegenerator.jdbc;

import com.mysql.jdbc.MySQLConnection;
import com.nxllxn.codegenerator.codegen.generator.util.CodeGenerateUtil;
import com.nxllxn.codegenerator.codegen.java.FullyQualifiedJavaType;
import com.nxllxn.codegenerator.config.*;
import com.nxllxn.codegenerator.jdbc.entities.ForeignKey;
import com.nxllxn.codegenerator.jdbc.entities.PrimaryKey;
import com.nxllxn.codegenerator.jdbc.entities.UniqueIndex;
import com.nxllxn.codegenerator.jdbc.java.JavaReservedWords;
import com.nxllxn.codegenerator.jdbc.java.JavaTypeResolver;
import com.nxllxn.codegenerator.jdbc.java.SqlReservedWords;
import com.nxllxn.codegenerator.utils.Inflector;
import org.apache.commons.lang3.StringUtils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据库表信息，列信息读取实现类
 */
public class DatabaseIntrospector {
    /**
     * 数据库元数据操作类
     */
    private DatabaseMetaData databaseMetaData;

    /**
     * java类型解析器
     */
    private JavaTypeResolver javaTypeResolver;

    /**
     * 上下文对象
     */
    private Context context;

    public DatabaseIntrospector(DatabaseMetaData databaseMetaData, JavaTypeResolver javaTypeResolver, Context context) {
        this.databaseMetaData = databaseMetaData;
        this.javaTypeResolver = javaTypeResolver;
        this.context = context;
    }

    /**
     * 读取数据库中全部的表结构基本信息并组装出完成的表结构信息
     *
     * @return 完整的表结构信息，包括主键列，基本信息列，blob列，主键限制，外健限制，唯一索引限制
     * @throws SQLException 数据库连接失败或sql执行失败抛出此异常
     */
    public List<IntrospectedTable> introspectedTables() throws SQLException {
        //1）读取全部的数据库表信息，构造tableNameToTableMap
        Map<String, IntrospectedTable> tableNameToTableMap = introspectedAllTables();

        //2）读取全部的数据库列信息，构造tableNameToColumnsMap
        Map<String, List<IntrospectedColumn>> tableNameToColumnsMap = introspectAllColumns();

        //3）读取全部的主键信息，构造tableNameToPrimaryKeyMap
        Map<String, PrimaryKey> tableNameToPrimaryMap = introspectPrimaryKey(tableNameToTableMap.keySet());

        //4）读取全部的外健信息，构造tableNameToForeignKeyMap
        Map<String, List<ForeignKey>> tableNameToForeignKeyMap =
                introspectForeignKey(tableNameToTableMap.keySet(), tableNameToTableMap);

        //4.5)读取全部的被引用作为外健的信息
        Map<String, List<ForeignKey>> tableNameToForeignKeyBeenReferencedMap =
                introspectForeignKeyBeenReferenced(tableNameToTableMap.keySet(), tableNameToTableMap);

        //5）读取全部的UniqueIndex，构造tableNameToUniqueIndexMap
        Map<String, List<UniqueIndex>> tableNameToUniqueIndexMap = introspectUniqueIndex(tableNameToTableMap.keySet());

        //6）根据tableRenamingRule计算runTimeTableName
        calculateRunTimeTableName(tableNameToTableMap, context.getTableConfigurations());

        //7）根据tableConfiguration计算runTimeTableName对应的EntityName
        calculateEntityName(tableNameToTableMap);

        //8）根据ColumnIgnore移除不需要的列
        removeIgnoredColumns(tableNameToColumnsMap, context.getTableConfigurations());

        //9）根据ColumnRenamingRule计算runTimeColumnName
        calculateRunTimeColumnName(tableNameToColumnsMap, context.getTableConfigurations());

        //10）根据ColumnOverride计算propertyName以及TypeHandler
        applyColumnOverride(tableNameToColumnsMap, context.getTableConfigurations());

        //11)组装Table
        List<IntrospectedTable> introspectedTables = assembleIntrospectedTables(tableNameToTableMap, tableNameToColumnsMap,
                tableNameToPrimaryMap, tableNameToForeignKeyMap,
                tableNameToForeignKeyBeenReferencedMap, tableNameToUniqueIndexMap);

        Map<String,IntrospectedTable> map = new HashMap<>();
        for (IntrospectedTable introspectedTable:introspectedTables){
            map.put(introspectedTable.getPrimitiveTableName(),introspectedTable);
        }

        //12)检查纯粹的关系表，并对关系表的外键依赖进行展开
        extractPureRelationTable(map,introspectedTables);

        return introspectedTables;
    }

    /**
     * 如果当前表是一张纯粹的关系表，那么此处会对其外键依赖进行展开
     *
     * 比如 A（aid），B（bid），C（referAidByC，referBidByC），扩展为A（aid ），B（bid）
     *
     * @param tableNameToTableMap 表名称到表结构映射
     * @param introspectedTables 表结构集合
     */
    private void extractPureRelationTable(Map<String, IntrospectedTable> tableNameToTableMap, List<IntrospectedTable> introspectedTables) {
        List<ForeignKey> foreignKeys;
        for (IntrospectedTable currentTable:tableNameToTableMap.values()){
            foreignKeys = currentTable.getForeignKeyColumns();

            //如果当前这张表纯粹是一张联系表（ER Entity-Relation 中的Entity）
            if (isPureRelationTable(foreignKeys, currentTable)) {

                if (foreignKeys.size() == 2) {
                    ForeignKey cToAForeignKey = foreignKeys.get(0);
                    ForeignKey cToBForeignKey = foreignKeys.get(1);

                    ForeignKey aToCForeignKey = revert(foreignKeys.get(0));
                    ForeignKey bToCForeignKey = revert(foreignKeys.get(1));

                    IntrospectedTable tableA = tableNameToTableMap.get(cToAForeignKey.getReferToTableName());
                    IntrospectedTable tableB = tableNameToTableMap.get(cToBForeignKey.getReferToTableName());

                    if (tableA != null && tableB != null) {
                        //将已知的外键依赖进行展开，比如 A（aid），B（bid），C（referAidByC，referBidByC），扩展为A（aid ），B（bid）
                        //展开前引用关系：A referAs：C->A;B referAs: C->B;C refer:C->A,C->B
                        //展开后引用关系：A refer：A->B（A->C & C->B）;B refer: B->A（B->C & C->A）;C None(不存在了)
                        // update :新的方案，A referAs：B->A（B->C & C->A）;B referAs: A->B（A->C & C->B）;C None(不存在了)
                        ForeignKey atoBForeignKey = new ForeignKey();
                        ForeignKey bToAForeignKey = new ForeignKey();

                        atoBForeignKey.setReferFromTableName(tableA.getRuntimeTableName());
                        atoBForeignKey.setReferFromColumnName(cToBForeignKey.getReferFromColumn().getRuntimeColumnName());
                        atoBForeignKey.setReferToTableName(cToBForeignKey.getReferToTable().getRuntimeTableName());
                        atoBForeignKey.setReferToColumnName(cToBForeignKey.getReferToColumn().getRuntimeColumnName());
                        atoBForeignKey.setReferFromTable(tableA);
                        atoBForeignKey.setReferFromColumn(cToBForeignKey.getReferFromColumn());
                        atoBForeignKey.setReferToTable(cToBForeignKey.getReferToTable());
                        atoBForeignKey.setReferToColumn(cToBForeignKey.getReferToColumn());

                        atoBForeignKey.setInnerFromForeignKey(aToCForeignKey);
                        atoBForeignKey.setInnerToForeignKey(cToBForeignKey);

                        //调整前：`tableA.getForeignKeyColumns().add(atoBForeignKey)`
                        tableA.getReferAsForeignKeyColumns().add(bToAForeignKey);

                        //删除表A中 aToC（a 被 C 引用）的外键依赖
                        tableA.getReferAsForeignKeyColumns().remove(cToAForeignKey);


                        bToAForeignKey.setReferFromTableName(tableB.getRuntimeTableName());
                        bToAForeignKey.setReferFromColumnName(cToAForeignKey.getReferFromColumn().getRuntimeColumnName());
                        bToAForeignKey.setReferToTableName(cToAForeignKey.getReferToTable().getRuntimeTableName());
                        bToAForeignKey.setReferToColumnName(cToAForeignKey.getReferToColumn().getRuntimeColumnName());
                        bToAForeignKey.setReferFromTable(tableB);
                        bToAForeignKey.setReferFromColumn(cToAForeignKey.getReferFromColumn());
                        bToAForeignKey.setReferToTable(cToAForeignKey.getReferToTable());
                        bToAForeignKey.setReferToColumn(cToAForeignKey.getReferToColumn());

                        bToAForeignKey.setInnerFromForeignKey(bToCForeignKey);
                        bToAForeignKey.setInnerToForeignKey(cToAForeignKey);

                        //调整前：`tableB.getForeignKeyColumns().add(bToAForeignKey)`
                        tableB.getReferAsForeignKeyColumns().add(atoBForeignKey);

                        //删除表B中 bToC（b 被 C 引用）的外键依赖
                        tableB.getReferAsForeignKeyColumns().remove(cToBForeignKey);

                        //从表集合中移除当前表，联系表，我们将不为其产生任何代码
                        introspectedTables.remove(currentTable);
                    }
                }
            }
        }
    }

    /**
     * 从数据库中读取全部的表结构基本信息，并构造tableName 到 表结构实体的映射
     *
     * @return tableName 到 表结构实体的映射
     * @throws SQLException 数据库连接失败或sql执行失败抛出此异常
     */
    private Map<String, IntrospectedTable> introspectedAllTables() throws SQLException {
        Map<String, IntrospectedTable> introspectedTableMap = new HashMap<>();

        ResultSet resultSet = databaseMetaData.getTables(null, null, "%", new String[]{SqlConstantRegistry.TABLE_TYPE_TABLE});

        IntrospectedTable introspectedTable;
        String tableName;
        while (resultSet.next()) {
            tableName = resultSet.getString(SqlConstantRegistry.TABLE_NAME);

            introspectedTable = new IntrospectedTable();

            introspectedTable.setPrimitiveTableName(tableName);
            introspectedTable.setTableType(resultSet.getString(SqlConstantRegistry.TABLE_TYPE));
            introspectedTable.setRemarks(resultSet.getString(SqlConstantRegistry.TABLE_REMARKS));

            introspectedTableMap.put(tableName, introspectedTable);
        }

        closeResultSet(resultSet);

        return introspectedTableMap;
    }

    /**
     * 关闭结果集
     *
     * @param resultSet 待关闭结果集
     */
    private void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet == null) {
                return;
            }

            resultSet.close();
        } catch (SQLException ok) {
            //it is ok,no need for extra code for exception handle
        }
    }

    /**
     * 从数据库中读取所有列信息，并构造tableName 到 列结构实体集合的映射
     *
     * @return tableName 到 列结构实体集合的映射
     * @throws SQLException 数据库连接失败或sql执行失败抛出此异常
     */
    private Map<String, List<IntrospectedColumn>> introspectAllColumns() throws SQLException {
        Map<String, List<IntrospectedColumn>> tableNameToColumnsMap = new HashMap<>();

        ResultSet resultSet = databaseMetaData.getColumns(null, null, "%", "%");

        IntrospectedColumn introspectedColumn;
        String tableName;
        List<IntrospectedColumn> introspectedColumns;
        while (resultSet.next()) {
            tableName = resultSet.getString(SqlConstantRegistry.COLUMN_ATTACHED_TABLE_NAME);

            //初始化用于保存列的集合
            if (!tableNameToColumnsMap.containsKey(tableName)) {
                tableNameToColumnsMap.put(tableName, new ArrayList<IntrospectedColumn>());
            }

            introspectedColumn = new IntrospectedColumn();

            introspectedColumn.setAttachedTableName(tableName);
            introspectedColumn.setPrimitiveColumnName(resultSet.getString(SqlConstantRegistry.COLUMN_NAME));
            introspectedColumn.setLength(resultSet.getInt(SqlConstantRegistry.COLUMN_SIZE));
            introspectedColumn.setScale(resultSet.getInt(SqlConstantRegistry.COLUMN_DECIMAL_DIGITS));
            introspectedColumn.setJdbcType(resultSet.getInt(SqlConstantRegistry.COLUMN_DATA_TYPE));

            introspectedColumn.setNullable(resultSet.getInt(SqlConstantRegistry.COLUMN_NULLABLE)
                    == DatabaseMetaData.columnNullable);

            String autoIncrementIdentifier = resultSet.getString(SqlConstantRegistry.COLUMN_IS_AUTOINCREMENT);
            introspectedColumn.setAutoIncrement(!StringUtils.isEmpty(autoIncrementIdentifier)
                    && autoIncrementIdentifier.equals(SqlConstantRegistry.YES));

            String generatedColumnIdentifier = resultSet.getString(SqlConstantRegistry.COLUMN_IS_GENERATEDCOLUMN);
            introspectedColumn.setGeneratedColumn(!StringUtils.isEmpty(generatedColumnIdentifier)
                    && generatedColumnIdentifier.equals(SqlConstantRegistry.YES));

            introspectedColumn.setDefaultValue(resultSet.getString(SqlConstantRegistry.COLUMN_DEFAULT_VALUE));
            introspectedColumn.setRemarks(resultSet.getString(SqlConstantRegistry.COLUMN_REMARKS));

            String columnName = resultSet.getString(SqlConstantRegistry.COLUMN_NAME);
            introspectedColumn.setNeedForDelimited(JavaReservedWords.containsWord(columnName)
                    || SqlReservedWords.containsWord(columnName));

            introspectedColumn.setJdbcTypeName(javaTypeResolver.resolveJDBCTypeName(
                    resultSet.getInt(SqlConstantRegistry.COLUMN_DATA_TYPE)
            ));
            introspectedColumn.setJavaType(javaTypeResolver.resolveJavaType(
                    resultSet.getInt(SqlConstantRegistry.COLUMN_DATA_TYPE)
            ));

            introspectedColumns = tableNameToColumnsMap.get(tableName);

            introspectedColumns.add(introspectedColumn);
        }

        closeResultSet(resultSet);

        return tableNameToColumnsMap;
    }

    /**
     * 从数据库中读取全部的主键信息，并构造tableName 到 PrimaryKey的映射
     *
     * @param tableNames 待查询表名称集合
     * @return tableName 到 PrimaryKey的映射
     * @throws SQLException 数据库连接失败或sql执行失败抛出此异常
     */
    private Map<String, PrimaryKey> introspectPrimaryKey(Set<String> tableNames) throws SQLException {
        Map<String, PrimaryKey> tableNameToPrimaryKeyMap = new HashMap<>();

        for (String introspectTableName : tableNames) {
            ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, introspectTableName);

            String tableName;
            PrimaryKey primaryKey;
            List<String> onColumnNames;
            while (resultSet.next()) {
                tableName = resultSet.getString(SqlConstantRegistry.PRIMARY_KEY_TABLE_NAME);

                if (!tableNameToPrimaryKeyMap.containsKey(tableName)) {
                    primaryKey = new PrimaryKey();

                    tableNameToPrimaryKeyMap.put(tableName, primaryKey);
                }

                primaryKey = tableNameToPrimaryKeyMap.get(tableName);
                onColumnNames = primaryKey.getOnColumnNames();

                onColumnNames.add(resultSet.getString(SqlConstantRegistry.PRIMARY_KEY_COLUMN_NAME));
            }

            closeResultSet(resultSet);
        }

        return tableNameToPrimaryKeyMap;
    }

    /**
     * 从数据库中读取全部的外键信息，并构造tableName 到 ForeignKey的映射
     *
     * @return tableName 到 ForeignKey的映射
     * @throws SQLException 数据库连接失败或sql执行失败抛出此异常
     */
    private Map<String, List<ForeignKey>> introspectForeignKey(Set<String> tableNames, Map<String, IntrospectedTable> tableNameToTableMap) throws SQLException {
        Map<String, List<ForeignKey>> tableNameToForeignKeyMap = new HashMap<>();

        List<ForeignKey> foreignKeys;

        String currentDatabase = databaseMetaData.getConnection().getCatalog();

        Pattern patternForReferences = Pattern.compile("foreign key \\(`?([\\w_]+)`\\) references `?([\\w_]+)`? \\(`?([\\w_]+)`\\)");

        Statement statement = ((MySQLConnection) databaseMetaData.getConnection()).getMetadataSafeStatement();
        String showCreateTableSqlFormat = "SHOW CREATE TABLE `%s`.`%s`";
        String showCreateTableSql;
        ResultSet resultSet;
        Matcher matcher;
        String referFromColumnName;
        String referToTableName;
        String referToColumnName;
        ForeignKey foreignKey;
        for (String currentTableName : tableNames) {
            showCreateTableSql = String.format(showCreateTableSqlFormat, currentDatabase, currentTableName);

            foreignKeys = new ArrayList<>();

            resultSet = statement.executeQuery(showCreateTableSql);

            if (resultSet.next()) {
                String createTableString = resultSet.getString(2).toLowerCase();

                matcher = patternForReferences.matcher(createTableString);

                while (matcher.find()) {
                    referFromColumnName = matcher.group(1);
                    referToTableName = matcher.group(2);
                    referToColumnName = matcher.group(3);

                    foreignKey = new ForeignKey();

                    foreignKey.setReferFromTableName(currentTableName);
                    foreignKey.setReferFromColumnName(referFromColumnName);
                    foreignKey.setReferToTableName(referToTableName);
                    foreignKey.setReferToColumnName(referToColumnName);

                    foreignKeys.add(foreignKey);
                }
            }

            tableNameToForeignKeyMap.put(currentTableName, foreignKeys);
        }

        return tableNameToForeignKeyMap;
    }

    private ForeignKey revert(ForeignKey foreignKey) {
        ForeignKey revertedForeignKey = new ForeignKey();

        revertedForeignKey.setReferFromColumnName(foreignKey.getReferToColumnName());
        revertedForeignKey.setReferFromColumn(foreignKey.getReferToColumn());
        revertedForeignKey.setReferToColumnName(foreignKey.getReferFromColumnName());
        revertedForeignKey.setReferToColumn(foreignKey.getReferFromColumn());

        revertedForeignKey.setReferFromTableName(foreignKey.getReferToTableName());
        revertedForeignKey.setReferFromTable(foreignKey.getReferToTable());
        revertedForeignKey.setReferToTableName(foreignKey.getReferFromTableName());
        revertedForeignKey.setReferToTable(foreignKey.getReferFromTable());


        return revertedForeignKey;
    }

    /**
     * 如果当前表的基本列以及blob列都在外键依赖列范围内，就说明这是一个纯粹的关系表，不是实体表
     *
     * @param foreignKeys       外健依赖集合
     * @param introspectedTable 当前表表结构信息
     * @return 当前表的基本列以及blob列都在外键依赖列范围内返回true，否则返回false
     */
    private boolean isPureRelationTable(List<ForeignKey> foreignKeys, IntrospectedTable introspectedTable) {
        List<String> foreignColumnNames = new ArrayList<>();

        for (ForeignKey foreignKey : foreignKeys) {
            foreignColumnNames.add(foreignKey.getReferToColumnName());
        }

        for (IntrospectedColumn introspectedColumn : introspectedTable.getBaseColumns()) {
            if (!foreignColumnNames.contains(introspectedColumn.getPrimitiveColumnName())) {
                return false;
            }
        }

        for (IntrospectedColumn introspectedColumn : introspectedTable.getBlobColumns()) {
            if (!foreignColumnNames.contains(introspectedColumn.getPrimitiveColumnName())) {
                return false;
            }
        }

        return true;
    }

    /**
     * 从数据库中读取全部的被引用作为外健的信息，并构造tableName 到 被引用为外健的映射
     *
     * @return tableName 到 被引用为外健的映射
     * @throws SQLException 数据库连接失败或sql执行失败抛出此异常
     */
    private Map<String, List<ForeignKey>> introspectForeignKeyBeenReferenced(Set<String> tableNames, Map<String, IntrospectedTable> tableNameToTableMap) throws SQLException {
        Map<String, List<ForeignKey>> tableNameToForeignKeyMap = new HashMap<>();

        List<ForeignKey> foreignKeys;

        String currentDatabase = databaseMetaData.getConnection().getCatalog();

        Pattern patternForReferences = Pattern.compile("foreign key \\(`?([\\w_]+)`\\) references `?([\\w_]+)`? \\(`?([\\w_]+)`\\)");

        Statement statement = ((MySQLConnection) databaseMetaData.getConnection()).getMetadataSafeStatement();
        String showCreateTableSqlFormat = "SHOW CREATE TABLE `%s`.`%s`";
        String showCreateTableSql;
        ResultSet resultSet;
        Matcher matcher;
        String referFromColumnName;
        String referToTableName;
        String referToColumnName;
        ForeignKey foreignKey;
        for (String currentTableName : tableNames) {
            //这一步是为了保证所有的表都拥有一个被作为外键引用的列表
            if (!tableNameToForeignKeyMap.containsKey(currentTableName)) {
                tableNameToForeignKeyMap.put(currentTableName, new ArrayList<ForeignKey>());
            }

            showCreateTableSql = String.format(showCreateTableSqlFormat, currentDatabase, currentTableName);

            resultSet = statement.executeQuery(showCreateTableSql);

            if (resultSet.next()) {
                String createTableString = resultSet.getString(2).toLowerCase();

                matcher = patternForReferences.matcher(createTableString);

                while (matcher.find()) {
                    referFromColumnName = matcher.group(1);
                    referToTableName = matcher.group(2);
                    referToColumnName = matcher.group(3);

                    foreignKey = new ForeignKey();

                    foreignKey.setReferFromTableName(currentTableName);
                    foreignKey.setReferFromColumnName(referFromColumnName);
                    foreignKey.setReferToTableName(referToTableName);
                    foreignKey.setReferToColumnName(referToColumnName);

                    if (!tableNameToForeignKeyMap.containsKey(referToTableName)) {
                        tableNameToForeignKeyMap.put(referToTableName, new ArrayList<ForeignKey>());
                    }

                    foreignKeys = tableNameToForeignKeyMap.get(referToTableName);

                    foreignKeys.add(foreignKey);
                }
            }
        }

        return tableNameToForeignKeyMap;
    }

    /**
     * 从数据库中读取全部的唯一索引信息（之后需要去除自增，generatedKey类型的列）
     *
     * @return 唯一索引信息
     * @throws SQLException 数据库连接失败或sql执行失败抛出此异常
     */
    private Map<String, List<UniqueIndex>> introspectUniqueIndex(Set<String> tableNames) throws SQLException {
        Map<String, List<UniqueIndex>> tableNameToUniqueIndexMap = new HashMap<>();

        for (String subTableName : tableNames) {
            ResultSet resultSet = databaseMetaData.getIndexInfo(null, null, subTableName, true, false);

            Map<String, Map<String, List<String>>> tableNameToIndicesMap = new HashMap<>();

            Map<String, List<String>> indexNameToIndexColumnsMap;

            String tableName;
            List<String> uniqueIndexColumnNames;
            String indexName;
            while (resultSet.next()) {
                tableName = resultSet.getString(SqlConstantRegistry.INDEX_TABLE_NAME);

                if (!tableNameToIndicesMap.containsKey(tableName)) {
                    tableNameToIndicesMap.put(tableName, new HashMap<String, List<String>>());
                }

                indexNameToIndexColumnsMap = tableNameToIndicesMap.get(tableName);

                indexName = resultSet.getString(SqlConstantRegistry.INDEX_NAME);
                if (!indexNameToIndexColumnsMap.containsKey(indexName)) {
                    indexNameToIndexColumnsMap.put(indexName, new ArrayList<String>());
                }

                uniqueIndexColumnNames = indexNameToIndexColumnsMap.get(indexName);

                uniqueIndexColumnNames.add(resultSet.getString(SqlConstantRegistry.INDEX_COLUMN_NAME));
            }

            List<UniqueIndex> uniqueIndices;
            UniqueIndex uniqueIndex;
            for (Map.Entry<String, Map<String, List<String>>> entry : tableNameToIndicesMap.entrySet()) {
                tableName = entry.getKey();

                uniqueIndices = new ArrayList<>();
                for (List<String> indexColumnNames : entry.getValue().values()) {
                    uniqueIndex = new UniqueIndex();

                    uniqueIndex.setOnColumnNames(indexColumnNames);

                    uniqueIndices.add(uniqueIndex);
                }

                tableNameToUniqueIndexMap.put(tableName, uniqueIndices);
            }
        }

        return tableNameToUniqueIndexMap;
    }

    /**
     * 根据TableRenamingRule以及Table原始名称计算出Table运行时名称
     *
     * @param tableNameToTableMap 数据库表集合
     * @param tableConfigurations 数据库表相关配置
     */
    private void calculateRunTimeTableName(
            Map<String, IntrospectedTable> tableNameToTableMap,
            List<TableConfiguration> tableConfigurations) {
        TableConfiguration tableConfiguration;
        String currentTableName;
        String runtimeTableName;
        List<TableRenamingConfiguration> tableRenamingConfigurations;
        for (Map.Entry<String, IntrospectedTable> entry : tableNameToTableMap.entrySet()) {
            currentTableName = entry.getKey();

            tableConfiguration = getTableConfigurationForCurrentTable(tableConfigurations, currentTableName);

            tableRenamingConfigurations = tableConfiguration.getTableRenamingConfigurations();

            if (tableRenamingConfigurations == null) {
                continue;
            }

            for (TableRenamingConfiguration tableRenamingConfiguration : tableRenamingConfigurations) {
                runtimeTableName = currentTableName.replaceAll(
                        tableRenamingConfiguration.getSearchFor(),
                        tableRenamingConfiguration.getReplaceWith()
                );

                entry.getValue().setRuntimeTableName(runtimeTableName);
            }
        }
    }

    /**
     * 获取当前表结构对应的配置信息
     *
     * @param tableConfigurations 全部的表结构配置信息
     * @param currentTableName    当前表名称
     * @return 当前表结构对应的配置信息, 如果不存在当前表特定的配置，那么返回“%”对应的表结构配置
     */
    private TableConfiguration getTableConfigurationForCurrentTable(
            List<TableConfiguration> tableConfigurations, String currentTableName) {
        if (tableConfigurations == null || tableConfigurations.isEmpty()) {
            return null;
        }

        if (StringUtils.isBlank(currentTableName)) {
            return null;
        }

        //按照表名称匹配配置信息
        for (TableConfiguration tableConfiguration : tableConfigurations) {
            if (!StringUtils.isBlank(tableConfiguration.getTableName())
                    && tableConfiguration.getTableName().equals(currentTableName)) {
                return tableConfiguration;
            }
        }

        //查找默认的由通配符%指定的表配置信息
        for (TableConfiguration defaultTableConfiguration : tableConfigurations) {
            if (!StringUtils.isBlank(defaultTableConfiguration.getTableName())
                    && defaultTableConfiguration.getTableName().equals("%")) {
                return defaultTableConfiguration;
            }
        }

        return null;
    }

    /**
     * 根据Table runtime name，依据Java命名规范计算出实际使用的实体名称
     *
     * @param tableNameToTableMap 数据库表集合
     */
    private void calculateEntityName(
            Map<String, IntrospectedTable> tableNameToTableMap) {
        if (tableNameToTableMap == null) {
            return;
        }

        for (Map.Entry<String, IntrospectedTable> entry : tableNameToTableMap.entrySet()) {
            entry.getValue().setEntityName(CodeGenerateUtil.getCamelCaseString(
                    Inflector.getInstance().singularize(entry.getValue().getRuntimeTableName()), true
            ));
        }
    }

    /**
     * 移除不需要的列
     *
     * @param tableNameToColumnsMap 列集合
     * @param tableConfigurations   数据库表相关配置
     */
    private void removeIgnoredColumns(
            Map<String, List<IntrospectedColumn>> tableNameToColumnsMap,
            List<TableConfiguration> tableConfigurations) {
        String currentTableName;
        List<IntrospectedColumn> columns;
        TableConfiguration tableConfiguration;
        List<ColumnIgnoreConfiguration> columnIgnoreConfigurations;
        for (Map.Entry<String, List<IntrospectedColumn>> entry : tableNameToColumnsMap.entrySet()) {
            currentTableName = entry.getKey();
            columns = entry.getValue();

            tableConfiguration = getTableConfigurationForCurrentTable(tableConfigurations, currentTableName);

            columnIgnoreConfigurations = tableConfiguration.getColumnIgnoreConfigurations();

            for (IntrospectedColumn column : columns) {
                for (ColumnIgnoreConfiguration columnIgnoreConfiguration : columnIgnoreConfigurations) {
                    if (StringUtils.isBlank(column.getPrimitiveColumnName())) {
                        continue;
                    }

                    if (StringUtils.isBlank(columnIgnoreConfiguration.getColumn())) {
                        continue;
                    }

                    if (column.getPrimitiveColumnName().equals(columnIgnoreConfiguration.getColumn())) {
                        columns.remove(column);
                    }
                }
            }
        }
    }

    /**
     * 根据ColumnRenamingRule以及列原始名称计算出列运行时名称
     *
     * @param tableNameToColumnsMap 列集合
     * @param tableConfigurations   数据库表相关配置
     */
    private void calculateRunTimeColumnName(
            Map<String, List<IntrospectedColumn>> tableNameToColumnsMap,
            List<TableConfiguration> tableConfigurations) {
        String currentTableName;
        List<IntrospectedColumn> columns;
        TableConfiguration tableConfiguration;
        List<ColumnRenamingConfiguration> columnRenamingConfigurations;
        String runtimeColumnName;
        for (Map.Entry<String, List<IntrospectedColumn>> entry : tableNameToColumnsMap.entrySet()) {
            currentTableName = entry.getKey();
            columns = entry.getValue();

            tableConfiguration = getTableConfigurationForCurrentTable(tableConfigurations, currentTableName);

            columnRenamingConfigurations = tableConfiguration.getColumnRenamingConfigurations();

            for (IntrospectedColumn column : columns) {
                runtimeColumnName = column.getPrimitiveColumnName();

                if (StringUtils.isBlank(runtimeColumnName)) {
                    continue;
                }

                for (ColumnRenamingConfiguration columnRenamingConfiguration : columnRenamingConfigurations) {
                    runtimeColumnName = runtimeColumnName.replaceAll(
                            columnRenamingConfiguration.getSearchFor(),
                            columnRenamingConfiguration.getReplaceWith()
                    );
                }

                column.setRuntimeColumnName(runtimeColumnName);
            }
        }
    }

    /**
     * 根据Column 运行时名称按照Java命名规范计算出对应的实体名称
     *
     * @param tableNameToColumnsMap 列集合
     * @param tableConfigurations   数据库表相关配置
     */
    private void applyColumnOverride(
            Map<String, List<IntrospectedColumn>> tableNameToColumnsMap,
            List<TableConfiguration> tableConfigurations) {
        String currentTableName;
        List<IntrospectedColumn> columns;
        TableConfiguration tableConfiguration;
        List<ColumnOverrideConfiguration> columnOverrideConfigurations;
        for (Map.Entry<String, List<IntrospectedColumn>> entry : tableNameToColumnsMap.entrySet()) {
            currentTableName = entry.getKey();
            columns = entry.getValue();

            tableConfiguration = getTableConfigurationForCurrentTable(tableConfigurations, currentTableName);

            columnOverrideConfigurations = tableConfiguration.getColumnOverrideConfigurations();

            for (IntrospectedColumn column : columns) {
                column.setPropertyName(CodeGenerateUtil.getCamelCaseString(column.getRuntimeColumnName(), false));

                for (ColumnOverrideConfiguration columnOverrideConfiguration : columnOverrideConfigurations) {
                    if (column.getPrimitiveColumnName().equals(columnOverrideConfiguration.getColumn())) {
                        column.setJavaType(new FullyQualifiedJavaType(columnOverrideConfiguration.getJavaType()));
                        column.setJdbcTypeName(columnOverrideConfiguration.getJdbcType());
                        column.setPropertyName(columnOverrideConfiguration.getProperty());
                        column.setTypeHandler(columnOverrideConfiguration.getTypeHandler());
                    }
                }
            }
        }
    }

    /**
     * 组装数据库表结构
     *
     * @param tableNameToTableMap       表基本信息
     * @param tableNameToColumnsMap     列基本信息
     * @param tableNameToPrimaryMap     主键基本信息
     * @param tableNameToForeignKeyMap  外健基本信息
     * @param tableNameToUniqueIndexMap 唯一索引基本信息
     * @return 数据库表结构
     */
    private List<IntrospectedTable> assembleIntrospectedTables(
            Map<String, IntrospectedTable> tableNameToTableMap,
            Map<String, List<IntrospectedColumn>> tableNameToColumnsMap,
            Map<String, PrimaryKey> tableNameToPrimaryMap,
            Map<String, List<ForeignKey>> tableNameToForeignKeyMap,
            Map<String, List<ForeignKey>> tableNameToForeignKeyBeenReferencedMap,
            Map<String, List<UniqueIndex>> tableNameToUniqueIndexMap) {
        Set<String> tableNameSet = tableNameToTableMap.keySet();

        IntrospectedTable currentTable;
        List<IntrospectedColumn> attachedColumns;
        PrimaryKey primaryKey;
        List<String> primaryKeyColumnNames;
        List<ForeignKey> foreignKeys;
        List<ForeignKey> referAsForeignKeys;
        List<UniqueIndex> uniqueIndices;
        List<UniqueIndex> uniqueIndicesCopy;
        IntrospectedColumn uniqueIndexColumn;
        for (String tableName : tableNameSet) {
            currentTable = tableNameToTableMap.get(tableName);

            attachedColumns = tableNameToColumnsMap.get(tableName);
            primaryKey = tableNameToPrimaryMap.get(tableName);

            primaryKeyColumnNames = primaryKey == null ? new ArrayList<String>() : primaryKey.getOnColumnNames();
            for (IntrospectedColumn attachedColumn : attachedColumns) {
                //如果是主键列
                if (primaryKeyColumnNames.contains(attachedColumn.getPrimitiveColumnName())) {
                    attachedColumn.setIdentityColumn(true);

                    currentTable.appendIdentityColumn(attachedColumn);

                    continue;
                }

                //如果是Blob列
                if (attachedColumn.isBLOBColumn()) {
                    currentTable.appendBlobColumn(attachedColumn);

                    continue;
                }

                //否则判定其为基本属性列
                currentTable.appendBaseColumn(attachedColumn);
            }

            foreignKeys = tableNameToForeignKeyMap.get(tableName);
            updateTableColumnReference(tableNameToTableMap, tableNameToColumnsMap, foreignKeys);
            currentTable.setForeignKeyColumns(foreignKeys);

            referAsForeignKeys = tableNameToForeignKeyBeenReferencedMap.get(tableName);
            updateTableColumnReference(tableNameToTableMap, tableNameToColumnsMap, referAsForeignKeys);
            currentTable.setReferAsForeignKeyColumns(referAsForeignKeys);

            uniqueIndices = tableNameToUniqueIndexMap.get(tableName);
            uniqueIndicesCopy = tableNameToUniqueIndexMap.get(tableName);
            UniqueIndex uniqueIndex;
            for (int index = 0, length = uniqueIndices == null ? 0 : uniqueIndices.size();
                 uniqueIndices != null && index < length; index++) {
                uniqueIndex = uniqueIndices.get(index);

                List<IntrospectedColumn> onColumns = new ArrayList<>();

                for (String column : uniqueIndex.getOnColumnNames()) {
                    uniqueIndexColumn = findColumnByPrimitiveName(attachedColumns, column);

                    if (uniqueIndexColumn == null) {
                        uniqueIndicesCopy.remove(uniqueIndex);

                        index--;
                        length = uniqueIndices.size();

                        continue;
                    }

                    //去掉自增，generateKey类型的列
                    if (uniqueIndexColumn.isAutoIncrement()
                            || uniqueIndexColumn.isGeneratedColumn()) {
                        uniqueIndicesCopy.remove(uniqueIndex);

                        index--;
                        length = uniqueIndices.size();

                        continue;
                    }

                    onColumns.add(uniqueIndexColumn);
                }

                uniqueIndex.setOnColumns(onColumns);
            }

            if (uniqueIndices != null){
                currentTable.getUniqueIndexColumns().addAll(uniqueIndices);
            }
        }

        List<IntrospectedTable> introspectedTables = new ArrayList<>();
        introspectedTables.addAll(tableNameToTableMap.values());

        return introspectedTables;
    }

    /**
     * 更新表列引用
     *
     * @param tableNameToTableMap   表名称到表描述信息映射
     * @param tableNameToColumnsMap 表名称到表列集合映射
     * @param foreignKeys           外健依赖
     */
    private void updateTableColumnReference(Map<String, IntrospectedTable> tableNameToTableMap, Map<String, List<IntrospectedColumn>> tableNameToColumnsMap, List<ForeignKey> foreignKeys) {
        //更新表列引用
        for (ForeignKey foreignKey : foreignKeys) {
            foreignKey.setReferFromTable(tableNameToTableMap.get(foreignKey.getReferFromTableName()));
            foreignKey.setReferFromColumn(findColumnByPrimitiveName(
                    tableNameToColumnsMap.get(foreignKey.getReferFromTableName()), foreignKey.getReferFromColumnName()
            ));

            foreignKey.setReferToTable(tableNameToTableMap.get(foreignKey.getReferToTableName()));
            foreignKey.setReferToColumn(findColumnByPrimitiveName(
                    tableNameToColumnsMap.get(foreignKey.getReferToTableName()), foreignKey.getReferToColumnName()
            ));
        }
    }

    /**
     * 根据列原始名称查找指定集合中的列对象
     *
     * @param attachedColumns 指定列集合
     * @param column          指定列名称
     * @return 指定列名称对应的列对象
     */
    private IntrospectedColumn findColumnByPrimitiveName(List<IntrospectedColumn> attachedColumns, String column) {
        if (attachedColumns == null || attachedColumns.isEmpty() || StringUtils.isBlank(column)) {
            return null;
        }

        for (IntrospectedColumn attachedColumn : attachedColumns) {
            if (StringUtils.isBlank(attachedColumn.getPrimitiveColumnName())) {
                continue;
            }

            if (attachedColumn.getPrimitiveColumnName().equals(column)) {
                return attachedColumn;
            }
        }

        return null;
    }
}
