package com.nxllxn.codegenerator.codegen.generator.mapper;

import com.nxllxn.codegenerator.codegen.generator.util.CodeGenerateUtil;
import com.nxllxn.codegenerator.codegen.java.*;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import com.nxllxn.codegenerator.jdbc.entities.ForeignKey;
import com.nxllxn.codegenerator.jdbc.entities.UniqueIndex;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper代码生成服务实现类
 *
 * @author wenchao
 */
public class MapperCodeGenerateServiceImpl implements MapperCodeGenerateService {
    @Override
    public TopLevelInterface generateMapperInterface(IntrospectedTable introspectedTable, String targetPackage) {
        TopLevelInterface mapperInterface = new TopLevelInterface();

        mapperInterface.setPackageName(targetPackage);
        mapperInterface.setVisibility(Visibility.PUBLIC);
        mapperInterface.setType(introspectedTable.generateMapperInterfaceType());

        mapperInterface.addAnnotation("@Mapper");
        mapperInterface.addAnnotation("@Repository");

        mapperInterface.addExtraNonStaticImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        mapperInterface.addExtraNonStaticImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        mapperInterface.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));

        return mapperInterface;
    }

    @Override
    public Method generateAddRecordInterface(IntrospectedTable introspectedTable) {
        Method addNewRecordMethod = newInterfaceMethod();

        addNewRecordMethod.setName(CodeGenerateUtil.generateAddRecordMethodName(introspectedTable.getEntityName()));

        Parameter addNewRecordParameter = new Parameter();
        addNewRecordParameter.setType(introspectedTable.generateFullyQualifiedJavaType());
        String instanceName = CodeGenerateUtil.getSimpleInstanceObjName(introspectedTable.getEntityName());
        addNewRecordParameter.setName(instanceName);
        addNewRecordParameter.addAnnotation(assembleParamAnnotation(instanceName));
        addNewRecordMethod.addParameter(addNewRecordParameter);

        return addNewRecordMethod;
    }

    private String assembleParamAnnotation(String instanceName) {
        return "@Param(\"" + instanceName + "\")";
    }

    @Override
    public Method generateAddRecordBatchInterface(IntrospectedTable introspectedTable) {
        Method addNewRecordBatchMethod = newInterfaceMethod();

        addNewRecordBatchMethod.setName(CodeGenerateUtil.generateAddRecordBatchMethodName(introspectedTable.getEntityName()));

        Parameter addNewRecordParameter = new Parameter();

        FullyQualifiedJavaType addNewRecordBatchParameterType = FullyQualifiedJavaType.getNewListInstance();
        addNewRecordBatchParameterType.addTypeArgument(introspectedTable.generateFullyQualifiedJavaType());
        addNewRecordParameter.setType(addNewRecordBatchParameterType);

        //此处需要注意一点，因为我们希望在插入数据的同时利用generatedKey来得到自增的主键，
        //但是MyBatis对于批量查询获取主键有点问题，必须将参数名称设置为list，
        //且不能使用注解@Param，否则将无法获取到自增的主键Id
        String instanceName = "list";
        addNewRecordParameter.setName(instanceName);
        addNewRecordBatchMethod.addParameter(addNewRecordParameter);

        return addNewRecordBatchMethod;
    }

    @Override
    public Method generateRemoveRecordInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        Method removeRecordMethod = newInterfaceMethod();

        removeRecordMethod.setName(CodeGenerateUtil.generateRemoveRecordMethod(
                introspectedTable.getEntityName(),
                primaryKeyColumn.getPropertyName()));

        Parameter removeRecordMethodParameter = new Parameter();
        removeRecordMethodParameter.setType(primaryKeyColumn.getJavaType());
        removeRecordMethodParameter.setName(primaryKeyColumn.getPropertyName());
        removeRecordMethodParameter.addAnnotation(assembleParamAnnotation(primaryKeyColumn.getPropertyName()));
        removeRecordMethod.addParameter(removeRecordMethodParameter);

        return removeRecordMethod;
    }

    @Override
    public Method generateModifyRecordInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        Method modifyRecordMethod = newInterfaceMethod();

        modifyRecordMethod.setName(CodeGenerateUtil.generateModifyRecordMethodName(
                introspectedTable.getEntityName(), primaryKeyColumn.getPropertyName()
        ));

        String oldIdentifierParamName = "old" + CodeGenerateUtil.uppercaseFistCharacter(primaryKeyColumn.getPropertyName());
        Parameter modifyRecordMethodParameter = new Parameter();
        modifyRecordMethodParameter.setType(primaryKeyColumn.getJavaType());
        modifyRecordMethodParameter.setName(oldIdentifierParamName);
        modifyRecordMethodParameter.addAnnotation(assembleParamAnnotation(oldIdentifierParamName));
        modifyRecordMethod.addParameter(modifyRecordMethodParameter);

        String newEntityParamName = "new" + introspectedTable.getEntityName();
        modifyRecordMethodParameter = new Parameter();
        modifyRecordMethodParameter.setType(introspectedTable.generateFullyQualifiedJavaType());
        modifyRecordMethodParameter.setName(newEntityParamName);
        modifyRecordMethodParameter.addAnnotation(assembleParamAnnotation(newEntityParamName));
        modifyRecordMethod.addParameter(modifyRecordMethodParameter);

        return modifyRecordMethod;
    }

    @Override
    public Method generateQueryRecordInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        Method queryOneRecordMethod = newInterfaceMethod();

        queryOneRecordMethod.setName(CodeGenerateUtil.generateQueryRecordMethodName(
                introspectedTable.getEntityName(), primaryKeyColumn.getPropertyName()
        ));

        Parameter queryOneRecordMethodParameter = new Parameter();
        queryOneRecordMethodParameter.setType(primaryKeyColumn.getJavaType());
        queryOneRecordMethodParameter.setName(primaryKeyColumn.getPropertyName());
        queryOneRecordMethodParameter.addAnnotation(assembleParamAnnotation(primaryKeyColumn.getPropertyName()));
        queryOneRecordMethod.addParameter(queryOneRecordMethodParameter);

        queryOneRecordMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());

        return queryOneRecordMethod;
    }

    @Override
    public Method generateQueryRecordWithBlobInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        //如果当前表没有blob属性列，那么不添加此方法
        if (introspectedTable.getBlobColumns().isEmpty()) {
            return null;
        }

        Method queryRecordWithBlobMethod = newInterfaceMethod();

        queryRecordWithBlobMethod.setName(CodeGenerateUtil.generateQueryRecordWithBlobMethodName(
                introspectedTable.getEntityName(), primaryKeyColumn.getPropertyName()
        ));

        Parameter queryOneRecordWithBlobMethodParameter = new Parameter();
        queryOneRecordWithBlobMethodParameter.setType(primaryKeyColumn.getJavaType());
        queryOneRecordWithBlobMethodParameter.setName(primaryKeyColumn.getPropertyName());
        queryOneRecordWithBlobMethodParameter.addAnnotation(assembleParamAnnotation(primaryKeyColumn.getPropertyName()));
        queryRecordWithBlobMethod.addParameter(queryOneRecordWithBlobMethodParameter);

        queryRecordWithBlobMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());

        return queryRecordWithBlobMethod;
    }

    @Override
    public Method generateQueryRecordOnlyBlobInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        //如果当前表没有blob属性列，那么不添加此方法
        if (!introspectedTable.hasBlobColumns()) {
            return null;
        }

        Method queryRecordWithOnlyBlobMethod = newInterfaceMethod();

        queryRecordWithOnlyBlobMethod.setName(CodeGenerateUtil.generateQueryRecordWithOnlyBlobMethodName(
                introspectedTable.getEntityName(),
                primaryKeyColumn.getPropertyName()));

        Parameter queryOneRecordWithOnlyBlobMethodParameter = new Parameter();
        queryOneRecordWithOnlyBlobMethodParameter.setType(primaryKeyColumn.getJavaType());
        queryOneRecordWithOnlyBlobMethodParameter.setName(primaryKeyColumn.getPropertyName());
        queryOneRecordWithOnlyBlobMethodParameter.addAnnotation(assembleParamAnnotation(primaryKeyColumn.getPropertyName()));
        queryRecordWithOnlyBlobMethod.addParameter(queryOneRecordWithOnlyBlobMethodParameter);

        queryRecordWithOnlyBlobMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());

        return queryRecordWithOnlyBlobMethod;
    }

    @Override
    public Method generateQueryAllRecordInterface(IntrospectedTable introspectedTable) {
        Method queryAllRecordMethod = newInterfaceMethod();

        queryAllRecordMethod.setName(CodeGenerateUtil.generateQueryAllRecordMethodName(introspectedTable.getEntityName()));

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        returnType.addTypeArgument(introspectedTable.generateFullyQualifiedJavaType());
        queryAllRecordMethod.setReturnType(returnType);

        return queryAllRecordMethod;
    }

    @Override
    public List<Method> generateBinaryCascadeQueryInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        List<Method> cascadeQueryMethods = new ArrayList<>();

        FullyQualifiedJavaType genericListType = FullyQualifiedJavaType.getNewListInstance();
        genericListType.addTypeArgument(introspectedTable.generateFullyQualifiedJavaType());

        Method cascadeQueryMethod;
        for (ForeignKey foreignKey : introspectedTable.getForeignKeyColumns()) {
            //associate

            cascadeQueryMethod = newInterfaceMethod();
            cascadeQueryMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());
            cascadeQueryMethod.setName(
                    CodeGenerateUtil.generateQueryRecordWithAssociationMethodName(introspectedTable, foreignKey.getReferToTable()));
            Parameter cascadeQueryMethodParameter = new Parameter();
            cascadeQueryMethodParameter.setType(primaryKeyColumn.getJavaType());
            cascadeQueryMethodParameter.setName(primaryKeyColumn.getPropertyName());
            cascadeQueryMethodParameter.addAnnotation(assembleParamAnnotation(primaryKeyColumn.getPropertyName()));
            cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        for (ForeignKey referAsForeignKey : introspectedTable.getReferAsForeignKeyColumns()) {
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null) {
                //single join
                cascadeQueryMethod = newInterfaceMethod();
                cascadeQueryMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());
                cascadeQueryMethod.setName(
                        CodeGenerateUtil.generateQueryRecordWithCollectionMethodName(introspectedTable, referAsForeignKey.getReferFromTable()));
                Parameter cascadeQueryMethodParameter = new Parameter();
                cascadeQueryMethodParameter.setType(primaryKeyColumn.getJavaType());
                cascadeQueryMethodParameter.setName(primaryKeyColumn.getPropertyName());
                cascadeQueryMethodParameter.addAnnotation(assembleParamAnnotation(primaryKeyColumn.getPropertyName()));
                cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

                cascadeQueryMethods.add(cascadeQueryMethod);

                continue;
            }

            //级联join
            IntrospectedTable referFromTable = referAsForeignKey.getReferFromTable();

            cascadeQueryMethod = newInterfaceMethod();
            cascadeQueryMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());
            cascadeQueryMethod.setName(
                    CodeGenerateUtil.generateQueryRecordWithCollectionMethodName(introspectedTable, referFromTable));
            Parameter cascadeQueryMethodParameter = new Parameter();
            cascadeQueryMethodParameter.setType(primaryKeyColumn.getJavaType());
            cascadeQueryMethodParameter.setName(primaryKeyColumn.getPropertyName());
            cascadeQueryMethodParameter.addAnnotation(assembleParamAnnotation(primaryKeyColumn.getPropertyName()));
            cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        return cascadeQueryMethods;
    }

    @Override
    public List<Method> generateSingleCascadeQueryInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        List<Method> cascadeQueryMethods = new ArrayList<>();

        Method cascadeQueryMethod;
        for (ForeignKey referAsForeignKey : introspectedTable.getReferAsForeignKeyColumns()) {
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null) {
                continue;
            }

            IntrospectedColumn referFromColumn = referAsForeignKey.getInnerFromForeignKey().getReferFromColumn();

            cascadeQueryMethod = newInterfaceMethod();
            FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
            returnType.addTypeArgument(introspectedTable.generateFullyQualifiedJavaType());
            cascadeQueryMethod.setReturnType(returnType);
            cascadeQueryMethod.setName(
                    CodeGenerateUtil.generateQueryRecordByForeignColumn(introspectedTable, referFromColumn));
            Parameter cascadeQueryMethodParameter = new Parameter();
            cascadeQueryMethodParameter.setType(referFromColumn.getJavaType());
            cascadeQueryMethodParameter.setName(referFromColumn.getPropertyName());
            cascadeQueryMethodParameter.addAnnotation(assembleParamAnnotation(referFromColumn.getPropertyName()));
            cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        return cascadeQueryMethods;
    }

    @Override
    public Method generateQueryAllRecordWithPageInterface(IntrospectedTable introspectedTable) {
        Method queryAllRecordWithPageMethod = newInterfaceMethod();

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        returnType.addTypeArgument(introspectedTable.generateFullyQualifiedJavaType());
        queryAllRecordWithPageMethod.setReturnType(returnType);

        queryAllRecordWithPageMethod.setName(CodeGenerateUtil.generateQueryAllRecordWithPageMethodName(
                introspectedTable.getEntityName()
        ));


        Parameter pageNumberParameter = new Parameter();
        pageNumberParameter.setType(FullyQualifiedJavaType.getIntInstance());
        pageNumberParameter.setName("offset");
        pageNumberParameter.addAnnotation(assembleParamAnnotation("offset"));
        queryAllRecordWithPageMethod.addParameter(pageNumberParameter);

        Parameter recordCountPerPageParameter = new Parameter();
        recordCountPerPageParameter.setType(FullyQualifiedJavaType.getIntInstance());
        recordCountPerPageParameter.setName("limit");
        recordCountPerPageParameter.addAnnotation(assembleParamAnnotation("limit"));
        queryAllRecordWithPageMethod.addParameter(recordCountPerPageParameter);

        return queryAllRecordWithPageMethod;
    }

    @Override
    public List<Method> generateQueryCountInterface(IntrospectedTable introspectedTable) {
        List<Method> queryCountMethods = new ArrayList<>();

        Method queryCountMethod;
        for (UniqueIndex uniqueIndex : introspectedTable.getUniqueIndexColumns()) {
            queryCountMethod = newInterfaceMethod();

            queryCountMethod.setReturnType(FullyQualifiedJavaType.getIntInstance());

            queryCountMethod.setName(CodeGenerateUtil.generateQueryCountMethodNameByUniqueIndex(introspectedTable.getEntityName(), uniqueIndex));

            addParameterGeneratedFromUniqueIndex(queryCountMethod, uniqueIndex);

            queryCountMethods.add(queryCountMethod);
        }

        return queryCountMethods;
    }

    @Override
    public List<Method> generateRemoveRecordByUniqueIndexInterface(IntrospectedTable introspectedTable) {
        List<Method> removeRecordByUniqueIndexMethods = new ArrayList<>();

        Method removeRecordByUniqueIndexMethod;
        for (UniqueIndex uniqueIndex : introspectedTable.getUniqueIndexColumns()) {
            removeRecordByUniqueIndexMethod = newInterfaceMethod();

            removeRecordByUniqueIndexMethod.setName(CodeGenerateUtil.generateRemoveRecordMethodNameByUniqueIndex(introspectedTable.getEntityName(), uniqueIndex));

            addParameterGeneratedFromUniqueIndex(removeRecordByUniqueIndexMethod, uniqueIndex);

            removeRecordByUniqueIndexMethods.add(removeRecordByUniqueIndexMethod);
        }

        return removeRecordByUniqueIndexMethods;
    }

    /**
     * 新建一个InterfaceMethod
     *
     * @return method对象，其isInterfaceMethod属性被设置为true
     */
    private Method newInterfaceMethod() {
        Method interfaceMethod = new Method();

        interfaceMethod.setInterfaceMethod(true);

        return interfaceMethod;
    }

    /**
     * 添加由UniqueIndex组装的参数列表
     *
     * @param removeRecordByUniqueIndexMethod 待添加参数的方法
     * @param uniqueIndex                     当前UniqueIndex对象
     */
    private void addParameterGeneratedFromUniqueIndex(Method removeRecordByUniqueIndexMethod, UniqueIndex uniqueIndex) {
        Parameter parameter;
        for (IntrospectedColumn onColumn : uniqueIndex.getOnColumns()) {
            parameter = new Parameter();

            parameter.setType(onColumn.getJavaType());
            parameter.setName(onColumn.getPropertyName());
            parameter.addAnnotation(assembleParamAnnotation(onColumn.getPropertyName()));

            removeRecordByUniqueIndexMethod.addParameter(parameter);
        }
    }

    @Override
    public List<Method> generateRemoveRelationsByForeignKey(IntrospectedTable introspectedTable) {
        List<Method> removeRelationMethods = new ArrayList<>();

        IntrospectedTable referFromTable;
        //IntrospectedColumn referFromColumn;
        //IntrospectedTable throughTable;
        //IntrospectedColumn throughColumn;
        //IntrospectedTable referToTable;
        IntrospectedColumn referToColumn;
        Method removeRelationMethod;
        for (ForeignKey referAsForeignKey : introspectedTable.getReferAsForeignKeyColumns()) {
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null) {
                continue;
            }

            referFromTable = referAsForeignKey.getInnerFromForeignKey().getReferFromTable();    //B
            //referFromColumn= referAsForeignKey.getInnerFromForeignKey().getReferFromColumn();    //B column
            //throughTable = referAsForeignKey.getInnerFromForeignKey().getReferToTable();    //C
            //throughColumn = referAsForeignKey.getInnerFromForeignKey().getReferToColumn();    //c b column
            //referToTable = referAsForeignKey.getInnerToForeignKey().getReferToTable();    //A
            referToColumn = referAsForeignKey.getInnerToForeignKey().getReferToColumn();    //a column

            //组装方法名称 removeBRelationsByAColumn
            removeRelationMethod = newInterfaceMethod();
            removeRelationMethod.setName(CodeGenerateUtil.generateRemoveRelationsMethodName(referFromTable, referToColumn));

            Parameter parameter = new Parameter();
            parameter.setType(referToColumn.getJavaType());
            parameter.setName(referToColumn.getPropertyName());
            parameter.addAnnotation(assembleParamAnnotation(referToColumn.getPropertyName()));

            removeRelationMethod.addParameter(parameter);

            removeRelationMethods.add(removeRelationMethod);
        }

        return removeRelationMethods;
    }

    @Override
    public List<Method> generateQueryRecordByUniqueIndex(IntrospectedTable introspectedTable) {
        List<Method> queryRecordByUniqueIndexMethods = new ArrayList<>();

        String queryRecordByUniqueIndexMethodName;
        Method queryRecordByUniqueIndexMethod;
        for (UniqueIndex uniqueIndex:introspectedTable.getUniqueIndexColumns()){
            queryRecordByUniqueIndexMethodName = CodeGenerateUtil
                    .generateQueryRecordByUniqueIndexMethodName(introspectedTable.getEntityName(),uniqueIndex);

            queryRecordByUniqueIndexMethod = newInterfaceMethod();

            queryRecordByUniqueIndexMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());

            queryRecordByUniqueIndexMethod.setName(queryRecordByUniqueIndexMethodName);

            addParameterGeneratedFromUniqueIndex(queryRecordByUniqueIndexMethod, uniqueIndex);

            queryRecordByUniqueIndexMethods.add(queryRecordByUniqueIndexMethod);
        }

        return queryRecordByUniqueIndexMethods;
    }
}
