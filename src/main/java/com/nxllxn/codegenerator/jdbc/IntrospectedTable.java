package com.nxllxn.codegenerator.jdbc;

import com.alibaba.fastjson.annotation.JSONField;
import com.nxllxn.codegenerator.codegen.generator.util.CodeGenerateUtil;
import com.nxllxn.codegenerator.codegen.java.FullyQualifiedJavaType;
import com.nxllxn.codegenerator.jdbc.entities.ForeignKey;
import com.nxllxn.codegenerator.jdbc.entities.PrimaryKey;
import com.nxllxn.codegenerator.jdbc.entities.UniqueIndex;
import com.nxllxn.codegenerator.jdbc.java.JavaReservedWords;
import com.nxllxn.codegenerator.jdbc.java.SqlReservedWords;
import com.nxllxn.codegenerator.utils.Inflector;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表结构实体，用于描述数据库中任意表的结构，包括表名称，包含的标识列，基本信息列，blob列，以及当前表的主键限制，外健限制，唯一索引限制等
 * <p>
 * TODO generatedColumn & 如果一个Table是一个纯粹的Relation表，那么将外健Relation扩展到对应的关联表中
 *
 * @author wnchao
 */
public class IntrospectedTable {
    /**
     * 原始表名称
     */
    private String primitiveTableName;

    /**
     * 运行时表名称
     */
    private String runtimeTableName;

    /**
     * 有当前表产生的实体名称
     */
    private String entityName;

    /**
     * 实体类所在包名
     */
    private String entityPackageName;

    /**
     * 当前表类型
     */
    private String tableType;

    /**
     * 当前表评论信息
     */
    private String remarks;

    /**
     * 唯一标识列
     */
    private List<IntrospectedColumn> identityColumns;

    /**
     * 基本信息列
     */
    private List<IntrospectedColumn> baseColumns;

    /**
     * blob列
     */
    private List<IntrospectedColumn> blobColumns;

    private FullyQualifiedJavaType fullyQualifiedJavaType;

    /**
     * Mapper接口文件后缀名
     */
    public static final String MAPPER_INTERFACE_SUFFIX = "Mapper";

    /**
     * service层接口类后缀名
     */
    public static final String SERVICE_INTERFACE_SUFFIX = "Service";

    /**
     * service层实现类后缀名
     */
    public static final String SERVICE_IMPL_CLASS_SUFFIX = "ServiceImpl";

    /**
     * controller类后缀名
     */
    public static final String CONTROLLER_CLASS_SUFFIX = "Controller";

    /**
     * 字符串常量定义类后缀名
     */
    public static final String KEY_DEFINITION_CLASS_SUFFIX = "Key";

    /**
     * mapper所在包名
     */
    private String mapperPackageName;

    /**
     * Mapper接口全限定类型
     */
    private FullyQualifiedJavaType mapperClassType;

    /**
     * service interface 所在包名
     */
    private String serviceInterfacePackageName;

    /**
     * service interface 全限定类型
     */
    private FullyQualifiedJavaType serviceInterfaceType;

    /**
     * service impl所在包名
     */
    private String serviceImplPackageName;

    /**
     * service impl 全限定类型
     */
    private FullyQualifiedJavaType serviceImplClassType;

    /**
     * controller所在包名
     */
    private String controllerPackageName;

    /**
     * controller 类全限定类型
     */
    private FullyQualifiedJavaType controllerClassType;

    /**
     * key 常量定义类所在包名
     */
    private String keyPackageName;

    /**
     * key常量定义类全限定类型
     */
    private FullyQualifiedJavaType keyDefinitionClassType;


    /**
     * 代码生成的列，主要是根据外键以及被引用作为外键的依赖关系来产生
     * <p>
     * &lt;br&gt;根据外健依赖会生成以下内容：
     * &lt;br&gt;1）一个外键依赖于的表对应的实体的实例属性
     * &lt;br&gt;2）一个外键依赖于的表对应的实体的实例的主键属性（如果存在自增主键） TODO 目前尚不实现主键
     * &lt;br&gt;根据被引用作为外健会生成以下内容
     * &lt;br&gt;1）一个依赖于当前表的表实体集合
     * &lt;br&gt;2）一个依赖于当前表的表实体主键集合（如果存在自增主键） TODO 目前尚不实现主键
     * &lt;br&gt;3)一个标识上述集合大小的int字段
     * <p>
     * &lt;br&gt;eg：用户表 地址表 订单表
     * &lt;br&gt;用户表address_id外健依赖与地址表address_id
     * &lt;br&gt;订单表uid外键依赖于用户表uid
     * <p>
     * &lt;br&gt;那么user实体对应的generatedColumn包含：
     * &lt;br&gt;Long addressId;
     * &lt;br&gt;Address address;
     * &lt;br&gt;List&lt;Long&gt; orderIds;
     * &lt;br&gt;List&lt;Order&gt; orders;
     * &lt;br&gt;int orderCount;
     */
    @JSONField(serialize = false)
    private List<IntrospectedColumn> generatedColumns;

    /**
     * 当前表关联的主键信息
     */
    private List<PrimaryKey> primaryKeyColumns;

    /**
     * 当前表关联的外健限制信息
     */
    private List<ForeignKey> foreignKeyColumns;

    /**
     * 被引用为外健的列
     */
    private List<ForeignKey> referAsForeignKeyColumns;

    /**
     * 当前表关联的唯一索引信息
     */
    private List<UniqueIndex> uniqueIndexColumns;

    public IntrospectedTable() {
        this.identityColumns = new ArrayList<>();
        this.baseColumns = new ArrayList<>();
        this.blobColumns = new ArrayList<>();

        this.primaryKeyColumns = new ArrayList<>();
        this.foreignKeyColumns = new ArrayList<>();
        this.referAsForeignKeyColumns = new ArrayList<>();
        this.uniqueIndexColumns = new ArrayList<>();
    }

    public String getPrimitiveTableName() {
        return primitiveTableName;
    }

    /**
     * 获取当前表名经过分隔符处理后的名称，主要用于处理Java关键字以及Sql关键字。比如order关键字，如果sql语句中表名为order，sql将不能正常执行，需要用分隔符分隔
     * @param startDelimiter 开始分隔符如"`"
     * @param endDelimiter 结束分隔符如"`"
     * @return 分隔符处理后的表名称，如果表名不是关键字，那么默认不加分隔符
     */
    public String getDelimitedPrimitiveTableName(String startDelimiter,String endDelimiter){
        if (StringUtils.isBlank(primitiveTableName)){
            return primitiveTableName;
        }

        if (JavaReservedWords.containsWord(primitiveTableName)
                || SqlReservedWords.containsWord(primitiveTableName)){
            return startDelimiter + primitiveTableName + endDelimiter;
        }

        return primitiveTableName;
    }

    public void setPrimitiveTableName(String primitiveTableName) {
        this.primitiveTableName = primitiveTableName;
    }

    public String getRuntimeTableName() {
        return runtimeTableName;
    }

    public void setRuntimeTableName(String runtimeTableName) {
        this.runtimeTableName = runtimeTableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getInstanceName(){
        return CodeGenerateUtil.getSimpleInstanceObjName(entityName);
    }

    public String getMapperInterfaceName() {
        return entityName + MAPPER_INTERFACE_SUFFIX;
    }

    public String getServiceInterfaceName() {
        return entityName + SERVICE_INTERFACE_SUFFIX;
    }

    public String getServiceImplementClassName() {
        return entityName + SERVICE_IMPL_CLASS_SUFFIX;
    }

    public String getControllerClassName() {
        return entityName + CONTROLLER_CLASS_SUFFIX;
    }

    public String getKeyClassName() {
        return entityName + KEY_DEFINITION_CLASS_SUFFIX;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<IntrospectedColumn> getIdentityColumns() {
        return identityColumns;
    }

    public List<IntrospectedColumn> getAllColumnsIncludeGenerated() {
        List<IntrospectedColumn> allColumns = new ArrayList<>();

        allColumns.addAll(identityColumns);
        allColumns.addAll(baseColumns);
        allColumns.addAll(blobColumns);

        if (generatedColumns == null) {
            calculateGeneratedColumns();
        }

        allColumns.addAll(generatedColumns);

        return allColumns;
    }

    public List<IntrospectedColumn> getAllColumns() {
        List<IntrospectedColumn> allColumns = new ArrayList<>();

        allColumns.addAll(identityColumns);
        allColumns.addAll(baseColumns);
        allColumns.addAll(blobColumns);

        return allColumns;
    }

    public List<IntrospectedColumn> getAllNonBlobColumns() {
        List<IntrospectedColumn> allColumns = new ArrayList<>();

        allColumns.addAll(identityColumns);
        allColumns.addAll(baseColumns);

        return allColumns;
    }

    public List<IntrospectedColumn> getAllNonBaseColumns() {
        List<IntrospectedColumn> allColumns = new ArrayList<>();

        allColumns.addAll(identityColumns);
        allColumns.addAll(blobColumns);

        return allColumns;
    }

    public List<IntrospectedColumn> getAllNonPrimaryColumns() {
        List<IntrospectedColumn> allColumns = new ArrayList<>();

        allColumns.addAll(baseColumns);
        allColumns.addAll(blobColumns);

        return allColumns;
    }

    public List<IntrospectedColumn> calculateGeneratedColumns() {
        if (generatedColumns != null) {
            return generatedColumns;
        }

        generatedColumns = new ArrayList<>();

        //根据当前表包含的外健引用组装generatedColumn
        IntrospectedColumn generatedColumn;
        IntrospectedTable referToTable;
        for (ForeignKey foreignKey : foreignKeyColumns) {
            if (foreignKey.getInnerFromForeignKey() != null
                    || foreignKey.getInnerToForeignKey() != null) {
                /*referToTable = foreignKey.getReferToTable();

                //添加一个实体引用属性
                generatedColumn = new IntrospectedColumn();
                generatedColumn.setAttachedTableName(runtimeTableName);
                generatedColumn.setRemarks("Generated Column from foreign key reference");
                generatedColumn.setRuntimeColumnName(Inflector.getInstance().pluralize(referToTable.getRuntimeTableName()));

                FullyQualifiedJavaType fullyQualifiedJavaType = FullyQualifiedJavaType.getNewListInstance();
                fullyQualifiedJavaType.addTypeArgument(new FullyQualifiedJavaType(referToTable.getEntityName()));
                generatedColumn.setJavaType(fullyQualifiedJavaType);    //TODO 怎么解决导入问题

                generatedColumns.add(generatedColumn);

                //添加一个实体主键属性  TODO 暂不实现

                //添加一个表示上述集合大小的count属性
                generatedColumn = new IntrospectedColumn();
                generatedColumn.setRemarks("Generated Column from refer as foreign key reference");
                generatedColumn.setRuntimeColumnName(referToTable.getRuntimeTableName() + "_count");
                generatedColumn.setJavaType(FullyQualifiedJavaType.getIntInstance());

                generatedColumns.add(generatedColumn);*/

                //调整到了referAs中
            } else {
                referToTable = foreignKey.getReferToTable();

                //添加一个实体引用属性
                generatedColumn = new IntrospectedColumn();
                generatedColumn.setAttachedTableName(runtimeTableName);
                generatedColumn.setRemarks("Generated Column from foreign key reference");
                generatedColumn.setRuntimeColumnName(referToTable.getRuntimeTableName());
                generatedColumn.setJavaType(referToTable.generateFullyQualifiedJavaType());

                generatedColumns.add(generatedColumn);

                //添加一个实体主键属性 TODO 暂不实现
            }
        }

        IntrospectedTable referFromTable;
        for (ForeignKey referAsForeignKey : referAsForeignKeyColumns) {
            referFromTable = referAsForeignKey.getReferFromTable();

            //添加一个实体引用属性集合
            generatedColumn = new IntrospectedColumn();
            generatedColumn.setAttachedTableName(runtimeTableName);
            generatedColumn.setRemarks("Generated Column from refer as foreign key reference");
            generatedColumn.setRuntimeColumnName(Inflector.getInstance().pluralize(referFromTable.getRuntimeTableName()));

            FullyQualifiedJavaType fullyQualifiedJavaType = FullyQualifiedJavaType.getNewListInstance();
            fullyQualifiedJavaType.addTypeArgument(referFromTable.generateFullyQualifiedJavaType());

            generatedColumn.setJavaType(fullyQualifiedJavaType);

            generatedColumns.add(generatedColumn);

            //添加一个实体主键属性  TODO 暂不实现

            //添加一个表示上述集合大小的count属性
            generatedColumn = new IntrospectedColumn();
            generatedColumn.setRemarks("Generated Column from refer as foreign key reference");
            generatedColumn.setRuntimeColumnName(referFromTable.getRuntimeTableName() + "_count");
            generatedColumn.setJavaType(FullyQualifiedJavaType.getIntegerInstance());

            generatedColumns.add(generatedColumn);
        }

        return generatedColumns;
    }

    public List<IntrospectedColumn> getBaseColumns() {
        return baseColumns;
    }

    public void setBaseColumns(List<IntrospectedColumn> baseColumns) {
        this.baseColumns = baseColumns;
    }

    public List<IntrospectedColumn> getBlobColumns() {
        return blobColumns;
    }

    public List<PrimaryKey> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    public List<ForeignKey> getForeignKeyColumns() {
        return foreignKeyColumns;
    }

    public void setForeignKeyColumns(List<ForeignKey> foreignKeyColumns) {
        this.foreignKeyColumns = foreignKeyColumns;
    }

    public List<UniqueIndex> getUniqueIndexColumns() {
        return uniqueIndexColumns;
    }

    public List<ForeignKey> getReferAsForeignKeyColumns() {
        return referAsForeignKeyColumns;
    }

    public void setReferAsForeignKeyColumns(List<ForeignKey> referAsForeignKeyColumns) {
        this.referAsForeignKeyColumns = referAsForeignKeyColumns;
    }

    public void appendIdentityColumn(IntrospectedColumn attachedColumn) {
        this.getIdentityColumns().add(attachedColumn);
    }

    public void appendBlobColumn(IntrospectedColumn attachedColumn) {
        this.getBlobColumns().add(attachedColumn);
    }

    public void appendBaseColumn(IntrospectedColumn attachedColumn) {
        this.getBaseColumns().add(attachedColumn);
    }

    public void setEntityPackageName(String entityPackageName){
        this.entityPackageName = entityPackageName;

        if (this.fullyQualifiedJavaType != null){
            this.fullyQualifiedJavaType.setPackageName(entityPackageName);
        }
    }

    public void setMapperPackageName(String mapperPackageName) {
        this.mapperPackageName = mapperPackageName;

        if (this.mapperClassType != null) {
            this.mapperClassType.setPackageName(mapperPackageName);
        }
    }

    public void setServiceInterfacePackageName(String serviceInterfacePackageName) {
        this.serviceInterfacePackageName = serviceInterfacePackageName;

        if (this.serviceInterfaceType != null) {
            this.serviceInterfaceType.setPackageName(serviceInterfacePackageName);
        }
    }

    public void setServiceImplPackageName(String serviceImplPackageName) {
        this.serviceImplPackageName = serviceImplPackageName;

        if (this.serviceImplClassType != null) {
            this.serviceImplClassType.setPackageName(serviceImplPackageName);
        }
    }

    public void setControllerPackageName(String controllerPackageName) {
        this.controllerPackageName = controllerPackageName;

        if (this.controllerClassType != null) {
            controllerClassType.setPackageName(controllerPackageName);
        }
    }

    public void setKeyPackageName(String keyPackageName) {
        this.keyPackageName = keyPackageName;

        if (this.keyDefinitionClassType != null) {
            keyDefinitionClassType.setPackageName(keyPackageName);
        }
    }

    public FullyQualifiedJavaType generateMapperInterfaceType() {
        if (mapperClassType == null) {
            mapperClassType = new FullyQualifiedJavaType(
                    generateTypeByPackageNameAndClassName(mapperPackageName, getEntityName(), MAPPER_INTERFACE_SUFFIX));
        }

        return mapperClassType;
    }

    /**
     * 根据包名称和类名称组建全限定类名
     *
     * @param packageName 包名
     * @param entityName 实体名称
     * @param suffix   后缀名
     * @return 对应的全限定类型
     */
    private String generateTypeByPackageNameAndClassName(String packageName, String entityName, String suffix) {
        if (StringUtils.isBlank(entityName)) {
            return null;
        }

        StringBuilder fullyQualifiedClassNameBuilder = new StringBuilder();

        if (!StringUtils.isBlank(packageName)) {
            fullyQualifiedClassNameBuilder.append(packageName);
        }

        if (fullyQualifiedClassNameBuilder.length() > 0 &&
                fullyQualifiedClassNameBuilder.charAt(fullyQualifiedClassNameBuilder.length() - 1) != '.') {
            fullyQualifiedClassNameBuilder.append(".");
        }

        fullyQualifiedClassNameBuilder.append(entityName);

        fullyQualifiedClassNameBuilder.append(suffix);

        return fullyQualifiedClassNameBuilder.toString();
    }

    public FullyQualifiedJavaType generateServiceInterfaceType() {
        if (serviceInterfaceType == null) {
            serviceInterfaceType = new FullyQualifiedJavaType(generateTypeByPackageNameAndClassName(
                    serviceInterfacePackageName, getEntityName(), SERVICE_INTERFACE_SUFFIX));
        }

        return serviceInterfaceType;
    }

    public FullyQualifiedJavaType generateServiceImplClassType() {
        if (serviceImplClassType == null) {
            serviceImplClassType = new FullyQualifiedJavaType(generateTypeByPackageNameAndClassName(
                    serviceImplPackageName, getEntityName(), SERVICE_IMPL_CLASS_SUFFIX
            ));
        }

        return serviceImplClassType;
    }

    public FullyQualifiedJavaType generateControllerClassType() {
        if (controllerClassType == null) {
            controllerClassType = new FullyQualifiedJavaType(generateTypeByPackageNameAndClassName(
                    controllerPackageName, getEntityName(), CONTROLLER_CLASS_SUFFIX
            ));
        }

        return controllerClassType;
    }

    public FullyQualifiedJavaType generateKeyDefinitionClassType() {
        if (keyDefinitionClassType == null) {
            keyDefinitionClassType = new FullyQualifiedJavaType(generateTypeByPackageNameAndClassName(
                    keyPackageName, getEntityName(), KEY_DEFINITION_CLASS_SUFFIX
            ));
        }

        return keyDefinitionClassType;
    }

    public String generateMapperClassName() {
        return generateTypeByPackageNameAndClassName(mapperPackageName, getEntityName(), MAPPER_INTERFACE_SUFFIX);
    }

    public String generateServiceInterfaceName() {
        return generateTypeByPackageNameAndClassName(
                serviceInterfacePackageName, getEntityName(), SERVICE_INTERFACE_SUFFIX);
    }

    public String generateServiceImplClassName() {
        return generateTypeByPackageNameAndClassName(
                serviceImplPackageName, getEntityName(), SERVICE_IMPL_CLASS_SUFFIX
        );
    }

    public String generateControllerClassName() {
        return generateTypeByPackageNameAndClassName(
                controllerPackageName, getEntityName(), CONTROLLER_CLASS_SUFFIX
        );
    }

    public String generateKeyDefinitionClassName() {
        return generateTypeByPackageNameAndClassName(
                keyPackageName, getEntityName(), KEY_DEFINITION_CLASS_SUFFIX
        );
    }

    public String generateMapperInstanceName() {
        return CodeGenerateUtil.getSimpleInstanceObjName(
                generateTypeByPackageNameAndClassName(null, getEntityName(), MAPPER_INTERFACE_SUFFIX));
    }

    public String generateServiceInstanceName() {
        return CodeGenerateUtil.getSimpleInstanceObjName(generateTypeByPackageNameAndClassName(
                null, getEntityName(), SERVICE_INTERFACE_SUFFIX));
    }

    @Override
    public String toString() {
        return "IntrospectedTable{" +
                "primitiveTableName='" + primitiveTableName + '\'' +
                ", runtimeTableName='" + runtimeTableName + '\'' +
                ", entityName='" + entityName + '\'' +
                ", tableType='" + tableType + '\'' +
                ", remarks='" + remarks + '\'' +
                ", identityColumns=" + identityColumns +
                ", baseColumns=" + baseColumns +
                ", blobColumns=" + blobColumns +
                ", primaryKeyColumns=" + primaryKeyColumns +
                ", foreignKeyColumns=" + foreignKeyColumns +
                ", referAsForeignKeyColumns=" + referAsForeignKeyColumns +
                ", uniqueIndexColumns=" + uniqueIndexColumns +
                '}';
    }

    public FullyQualifiedJavaType generateFullyQualifiedJavaType() {
        if (fullyQualifiedJavaType == null) {
            fullyQualifiedJavaType = new FullyQualifiedJavaType(generateTypeByPackageNameAndClassName(entityPackageName,entityName,""));
        }

        return fullyQualifiedJavaType;
    }

    public IntrospectedColumn getOnlyPrimaryColumn(){
        return CodeGenerateUtil.getOnlyPrimaryColumn(this);
    }

    /**
     * 判断当前表有没有blob列
     *
     * @return 如果blobColumns不为null且不为空返回true，否则返回false
     */
    public boolean hasBlobColumns() {
        return this.blobColumns != null && !this.blobColumns.isEmpty();
    }
}
