package com.nxllxn.codegenerator.codegen.generator.service;

import com.nxllxn.codegenerator.codegen.generator.util.CodeGenerateUtil;
import com.nxllxn.codegenerator.codegen.java.*;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import com.nxllxn.codegenerator.jdbc.entities.ForeignKey;
import com.nxllxn.codegenerator.jdbc.entities.UniqueIndex;
import com.nxllxn.codegenerator.utils.Inflector;

import java.util.ArrayList;
import java.util.List;

/**
 * service层代码生成服务实现类
 *
 * @author wenchao
 */
public class ServiceCodeGenerateServiceImpl implements ServiceCodeGenerateService {
    @Override
    public TopLevelInterface generateServiceInterface(IntrospectedTable introspectedTable, String targetPackage) {
        TopLevelInterface serviceInterface = new TopLevelInterface();

        serviceInterface.setVisibility(Visibility.PUBLIC);
        serviceInterface.setPackageName(targetPackage);

        serviceInterface.setType(introspectedTable.generateServiceInterfaceType());

        return serviceInterface;
    }

    @Override
    public Method generateAddNewRecordInterface(IntrospectedTable introspectedTable) {
        Method addNewRecordMethod = newInterfaceMethod();

        addNewRecordMethod.setName(CodeGenerateUtil.generateAddRecordMethodName(introspectedTable.getEntityName()));

        Parameter addNewRecordParameter = new Parameter();
        addNewRecordParameter.setType(introspectedTable.generateFullyQualifiedJavaType());
        String instanceName = CodeGenerateUtil.getSimpleInstanceObjName(introspectedTable.getEntityName());
        addNewRecordParameter.setName(instanceName);
        addNewRecordMethod.addParameter(addNewRecordParameter);

        return addNewRecordMethod;
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
        String instanceName = Inflector.getInstance().pluralize(
                CodeGenerateUtil.getSimpleInstanceObjName(introspectedTable.getEntityName())
        );
        addNewRecordParameter.setName(instanceName);
        addNewRecordBatchMethod.addParameter(addNewRecordParameter);

        return addNewRecordBatchMethod;
    }

    @Override
    public Method generateRemoveRecordInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null){
            return null;
        }

        Method removeRecordMethod = newInterfaceMethod();

        removeRecordMethod.setName(CodeGenerateUtil.generateRemoveRecordMethod(
                introspectedTable.getEntityName(),
                primaryKeyColumn.getPropertyName()));

        Parameter removeRecordMethodParameter = new Parameter();
        removeRecordMethodParameter.setType(primaryKeyColumn.getJavaType());
        removeRecordMethodParameter.setName(primaryKeyColumn.getPropertyName());
        removeRecordMethod.addParameter(removeRecordMethodParameter);

        return removeRecordMethod;
    }

    @Override
    public List<Method> generateRemoveRecordByUniqueIndexInterface(IntrospectedTable introspectedTable) {
        List<Method> removeRecordByUniqueIndexMethods = new ArrayList<>();

        Method removeRecordByUniqueIndexMethod;
        for (UniqueIndex uniqueIndex:introspectedTable.getUniqueIndexColumns()){
            removeRecordByUniqueIndexMethod = newInterfaceMethod();

            removeRecordByUniqueIndexMethod.setName(
                    CodeGenerateUtil.generateRemoveRecordMethodNameByUniqueIndex(introspectedTable.getEntityName(),uniqueIndex)
            );

            addParameterGeneratedFromUniqueIndex(removeRecordByUniqueIndexMethod, uniqueIndex);

            removeRecordByUniqueIndexMethods.add(removeRecordByUniqueIndexMethod);
        }

        return removeRecordByUniqueIndexMethods;
    }

    @Override
    public Method generateModifyRecordInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null){
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
        modifyRecordMethod.addParameter(modifyRecordMethodParameter);

        String newEntityParamName = "new" + introspectedTable.getEntityName();
        modifyRecordMethodParameter = new Parameter();
        modifyRecordMethodParameter.setType(introspectedTable.generateFullyQualifiedJavaType());
        modifyRecordMethodParameter.setName(newEntityParamName);
        modifyRecordMethod.addParameter(modifyRecordMethodParameter);

        return modifyRecordMethod;
    }

    @Override
    public Method generateQueryRecordInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null){
            return null;
        }

        Method queryOneRecordMethod = newInterfaceMethod();

        queryOneRecordMethod.setName(CodeGenerateUtil.generateQueryRecordMethodName(
                introspectedTable.getEntityName(), primaryKeyColumn.getPropertyName()
        ));

        Parameter queryOneRecordMethodParameter = new Parameter();
        queryOneRecordMethodParameter.setType(primaryKeyColumn.getJavaType());
        queryOneRecordMethodParameter.setName(primaryKeyColumn.getPropertyName());
        queryOneRecordMethod.addParameter(queryOneRecordMethodParameter);

        queryOneRecordMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());

        return queryOneRecordMethod;
    }

    @Override
    public Method generateQueryRecordWithBlobInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null){
            return null;
        }

        //如果当前表没有blob属性列，那么不添加此方法
        if (introspectedTable.getBlobColumns().isEmpty()){
            return null;
        }

        Method queryRecordWithBlobMethod = newInterfaceMethod();

        queryRecordWithBlobMethod.setName(CodeGenerateUtil.generateQueryRecordWithBlobMethodName(
                introspectedTable.getEntityName(), primaryKeyColumn.getPropertyName()
        ));

        Parameter queryOneRecordWithBlobMethodParameter = new Parameter();
        queryOneRecordWithBlobMethodParameter.setType(primaryKeyColumn.getJavaType());
        queryOneRecordWithBlobMethodParameter.setName(primaryKeyColumn.getPropertyName());
        queryRecordWithBlobMethod.addParameter(queryOneRecordWithBlobMethodParameter);

        queryRecordWithBlobMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());

        return queryRecordWithBlobMethod;
    }

    @Override
    public Method generateQueryRecordOnlyBlobInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null){
            return null;
        }

        //如果当前表没有blob属性列，那么不添加此方法
        if (!introspectedTable.hasBlobColumns()){
            return null;
        }

        Method queryRecordWithOnlyBlobMethod = newInterfaceMethod();

        queryRecordWithOnlyBlobMethod.setName(CodeGenerateUtil.generateQueryRecordWithOnlyBlobMethodName(
                introspectedTable.getEntityName(),
                primaryKeyColumn.getPropertyName()));

        Parameter queryOneRecordWithOnlyBlobMethodParameter = new Parameter();
        queryOneRecordWithOnlyBlobMethodParameter.setType(primaryKeyColumn.getJavaType());
        queryOneRecordWithOnlyBlobMethodParameter.setName(primaryKeyColumn.getPropertyName());
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
        pageNumberParameter.setName("pageNumber");
        queryAllRecordWithPageMethod.addParameter(pageNumberParameter);

        Parameter recordCountPerPageParameter = new Parameter();
        recordCountPerPageParameter.setType(FullyQualifiedJavaType.getIntInstance());
        recordCountPerPageParameter.setName("recordCountPerPage");
        queryAllRecordWithPageMethod.addParameter(recordCountPerPageParameter);

        return queryAllRecordWithPageMethod;
    }

    @Override
    public List<Method> generateIfExistsInterface(IntrospectedTable introspectedTable) {
        List<Method> isSpecRecordExistsMethods = new ArrayList<>();

        Method isSpecRecordExistsMethod;
        for (UniqueIndex uniqueIndex:introspectedTable.getUniqueIndexColumns()){
            isSpecRecordExistsMethod = newInterfaceMethod();

            isSpecRecordExistsMethod.setName(CodeGenerateUtil.generateIsSpecRecordExistsMethodName(
                    introspectedTable.getEntityName(),uniqueIndex));

            isSpecRecordExistsMethod.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());

            addParameterGeneratedFromUniqueIndex(isSpecRecordExistsMethod, uniqueIndex);

            isSpecRecordExistsMethods.add(isSpecRecordExistsMethod);
        }

        return isSpecRecordExistsMethods;
    }

    @Override
    public List<Method> generateBinaryCascadeQueryInterface(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null){
            return null;
        }

        List<Method> cascadeQueryMethods = new ArrayList<>();

        FullyQualifiedJavaType genericListType = FullyQualifiedJavaType.getNewListInstance();
        genericListType.addTypeArgument(introspectedTable.generateFullyQualifiedJavaType());

        String methodName;
        Method cascadeQueryMethod;
        for (ForeignKey foreignKey:introspectedTable.getForeignKeyColumns()){
            //associate

            methodName = CodeGenerateUtil.generateQueryRecordWithAssociationMethodName(introspectedTable, foreignKey.getReferToTable());

            cascadeQueryMethod = newInterfaceMethod();
            cascadeQueryMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());
            cascadeQueryMethod.setName(methodName);
            Parameter cascadeQueryMethodParameter = new Parameter();
            cascadeQueryMethodParameter.setType(primaryKeyColumn.getJavaType());
            cascadeQueryMethodParameter.setName(primaryKeyColumn.getPropertyName());
            cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        for (ForeignKey referAsForeignKey:introspectedTable.getReferAsForeignKeyColumns()){
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null){
                //single join

                methodName = CodeGenerateUtil.generateQueryRecordWithCollectionMethodName(
                        introspectedTable, referAsForeignKey.getReferFromTable());

                cascadeQueryMethod = newInterfaceMethod();
                cascadeQueryMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());
                cascadeQueryMethod.setName(methodName);
                Parameter cascadeQueryMethodParameter = new Parameter();
                cascadeQueryMethodParameter.setType(primaryKeyColumn.getJavaType());
                cascadeQueryMethodParameter.setName(primaryKeyColumn.getPropertyName());
                cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

                cascadeQueryMethods.add(cascadeQueryMethod);

                continue;
            }

            IntrospectedTable referFromTable = referAsForeignKey.getReferFromTable();

            methodName = CodeGenerateUtil.generateQueryRecordWithCollectionMethodName(introspectedTable, referFromTable);

            cascadeQueryMethod = newInterfaceMethod();
            cascadeQueryMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());
            cascadeQueryMethod.setName(methodName);
            Parameter cascadeQueryMethodParameter = new Parameter();
            cascadeQueryMethodParameter.setType(primaryKeyColumn.getJavaType());
            cascadeQueryMethodParameter.setName(primaryKeyColumn.getPropertyName());
            cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        return cascadeQueryMethods;
    }

    @Override
    public List<Method> generateSingleCascadeQueryInterface(IntrospectedTable introspectedTable) {
        List<Method> cascadeQueryMethods = new ArrayList<>();

        Method cascadeQueryMethod;
        for (ForeignKey referAsForeignKey:introspectedTable.getReferAsForeignKeyColumns()){
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null){
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
            cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        return cascadeQueryMethods;
    }

    @Override
    public Method generateIntegrityCheckInterface(IntrospectedTable introspectedTable) {
        Method integrityCheckMethod = newInterfaceMethod();

        integrityCheckMethod.setName("checkIntegrity");

        Parameter parameter = new Parameter();
        parameter.setName(CodeGenerateUtil.getSimpleInstanceObjName(introspectedTable.getEntityName()));
        parameter.setType(introspectedTable.generateFullyQualifiedJavaType());
        integrityCheckMethod.addParameter(parameter);

        return integrityCheckMethod;
    }

    /**
     * 新建一个InterfaceMethod
     * @return method对象，其isInterfaceMethod属性被设置为true
     */
    private Method newInterfaceMethod() {
        Method interfaceMethod = new Method();

        interfaceMethod.setInterfaceMethod(true);

        return interfaceMethod;
    }

    /**
     * 添加由UniqueIndex组装的参数列表
     * @param removeRecordByUniqueIndexMethod 待添加参数的方法
     * @param uniqueIndex 当前UniqueIndex对象
     */
    private void addParameterGeneratedFromUniqueIndex(Method removeRecordByUniqueIndexMethod, UniqueIndex uniqueIndex) {
        Parameter parameter;
        for (IntrospectedColumn onColumn:uniqueIndex.getOnColumns()){
            parameter = new Parameter();

            parameter.setType(onColumn.getJavaType());
            parameter.setName(onColumn.getPropertyName());

            removeRecordByUniqueIndexMethod.addParameter(parameter);
        }
    }
}
