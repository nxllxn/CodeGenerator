package com.nxllxn.codegenerator.codegen.generator.serviceimpl;

import com.nxllxn.codegenerator.codegen.generator.TypeRegistry;
import com.nxllxn.codegenerator.codegen.generator.util.CodeGenerateUtil;
import com.nxllxn.codegenerator.codegen.java.*;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import com.nxllxn.codegenerator.jdbc.entities.ForeignKey;
import com.nxllxn.codegenerator.jdbc.entities.UniqueIndex;
import com.nxllxn.codegenerator.utils.Inflector;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * service实现层代码生成服务实现类
 *
 * @author wenchao
 */
public class ServiceImplCodeGenerateServiceImpl implements ServiceImplCodeGenerateService {
    private static final String ANNOTATION_TRANSACTIONAL_REQUIRED = "@Transactional(propagation = Propagation.REQUIRED)";
    private static final String ANNOTATION_OVERRIDE= "@Override";


    @Override
    public TopLevelClass generateServiceImplClass(IntrospectedTable introspectedTable, String targetPackage) {
        TopLevelClass serviceImplClass = new TopLevelClass();

        serviceImplClass.setPackageName(targetPackage);
        serviceImplClass.setVisibility(Visibility.PUBLIC);
        serviceImplClass.setType(introspectedTable.generateServiceImplClassType());
        serviceImplClass.addSuperInterface(introspectedTable.generateServiceInterfaceType());

        serviceImplClass.addAnnotation("@Service");

        serviceImplClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        serviceImplClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
        serviceImplClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.transaction.annotation.Propagation"));
        serviceImplClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.transaction.annotation.Transactional"));
        serviceImplClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.apache.commons.lang3.StringUtils"));
        serviceImplClass.addExtraNonStaticImport(TypeRegistry.generateParamMissingExceptionType());


        return serviceImplClass;
    }

    @Override
    public List<Field> generateDependentMapperField(IntrospectedTable introspectedTable) {
        List<Field> dependentMapperFields = new ArrayList<>();

        Field dependentMapperField;

        dependentMapperField = new Field();
        dependentMapperField.setVisibility(Visibility.PRIVATE);
        dependentMapperField.setType(introspectedTable.generateMapperInterfaceType());
        dependentMapperField.setName(CodeGenerateUtil.getSimpleInstanceObjName(introspectedTable.getMapperInterfaceName()));
        dependentMapperFields.add(dependentMapperField);

        //直接的外键依赖不需要级联操作

        IntrospectedTable referFromTable;
        for (ForeignKey foreignKey : introspectedTable.getReferAsForeignKeyColumns()) {
            if (foreignKey.getInnerFromForeignKey() == null
                    || foreignKey.getInnerToForeignKey() == null) {
                //直接级联
                referFromTable = foreignKey.getReferFromTable();

                dependentMapperField = new Field();
                dependentMapperField.setVisibility(Visibility.PRIVATE);
                dependentMapperField.setType(referFromTable.generateMapperInterfaceType());
                dependentMapperField.setName(CodeGenerateUtil.getSimpleInstanceObjName(referFromTable.getMapperInterfaceName()));
                dependentMapperFields.add(dependentMapperField);
            }
        }

        return dependentMapperFields;
    }

    @Override
    public Method generateAutowiredConstructor(IntrospectedTable introspectedTable) {
        Method autowiredConstructor = new Method();

        autowiredConstructor.setVisibility(Visibility.PUBLIC);
        autowiredConstructor.setName(introspectedTable.getServiceImplementClassName());
        autowiredConstructor.setConstructor(true);

        String mapperInstanceName = introspectedTable.generateMapperInstanceName();
        Parameter parameter = new Parameter();
        parameter.setType(introspectedTable.generateMapperInterfaceType());
        parameter.setName(mapperInstanceName);
        autowiredConstructor.addParameter(parameter);

        StringBuilder bodyLineBuilder = new StringBuilder();
        bodyLineBuilder.append("this.");
        bodyLineBuilder.append(mapperInstanceName);
        bodyLineBuilder.append(" = ");
        bodyLineBuilder.append(mapperInstanceName);
        bodyLineBuilder.append(";");
        autowiredConstructor.addBodyLine(bodyLineBuilder.toString());


        IntrospectedTable referFromTable;
        for (ForeignKey foreignKey : introspectedTable.getReferAsForeignKeyColumns()) {
            if (foreignKey.getInnerFromForeignKey() == null
                    || foreignKey.getInnerToForeignKey() == null) {
                //直接级联
                referFromTable = foreignKey.getReferFromTable();

                mapperInstanceName = referFromTable.generateMapperInstanceName();
                parameter = new Parameter();
                parameter.setType(referFromTable.generateMapperInterfaceType());
                parameter.setName(mapperInstanceName);
                autowiredConstructor.addParameter(parameter);

                bodyLineBuilder = new StringBuilder();
                bodyLineBuilder.append("this.");
                bodyLineBuilder.append(mapperInstanceName);
                bodyLineBuilder.append(" = ");
                bodyLineBuilder.append(mapperInstanceName);
                bodyLineBuilder.append(";");
                autowiredConstructor.addBodyLine(bodyLineBuilder.toString());
            }
        }

        autowiredConstructor.addAnnotation("@Autowired");

        return autowiredConstructor;
    }

    @Override
    public Method generateAddNewRecordImpl(IntrospectedTable introspectedTable) {
        Method addNewRecordMethod = new Method();
        addNewRecordMethod.addAnnotation(ANNOTATION_OVERRIDE);

        String addRecordMethodName = CodeGenerateUtil.generateAddRecordMethodName(introspectedTable.getEntityName());
        String instanceName = introspectedTable.getInstanceName();
        String mapperInstanceName = introspectedTable.generateMapperInstanceName();

        addNewRecordMethod.setVisibility(Visibility.PUBLIC);
        addNewRecordMethod.setName(addRecordMethodName);

        Parameter addNewRecordParameter = new Parameter();
        addNewRecordParameter.setType(introspectedTable.generateFullyQualifiedJavaType());
        addNewRecordParameter.setName(instanceName);
        addNewRecordMethod.addParameter(addNewRecordParameter);

        //完整性校验
        addNewRecordMethod.addBodyLine("this.checkIntegrity(" + instanceName + ");");
        addNewRecordMethod.addExtraEmptyLine();

        StringBuilder codeLineBuilder = new StringBuilder();

        //添加当前实例
        codeLineBuilder.append(mapperInstanceName);
        codeLineBuilder.append(".");
        codeLineBuilder.append(addRecordMethodName);
        codeLineBuilder.append("(");
        codeLineBuilder.append(instanceName);
        codeLineBuilder.append(");");
        addNewRecordMethod.addBodyLine(codeLineBuilder.toString());

        String foreignKeyInstanceName;
        String foreignKeyMapperInstanceName;
        //如果有直接的referAsForeignKey外键依赖，那么需要级联增加
        for (ForeignKey foreignKey : introspectedTable.getReferAsForeignKeyColumns()) {
            if (foreignKey.getInnerToForeignKey() != null
                    || foreignKey.getInnerFromForeignKey() != null) {
                //此处为通过relation表的间接依赖
                continue;
            }

            //额外空行
            addNewRecordMethod.addExtraEmptyLine();

            IntrospectedTable referFromTable = foreignKey.getReferFromTable();
            IntrospectedColumn referFromColumn = foreignKey.getReferFromColumn();

            addNewRecordMethod.addExtraNonStaticImport(referFromTable.generateFullyQualifiedJavaType());

            foreignKeyInstanceName = referFromTable.getInstanceName();
            foreignKeyMapperInstanceName = referFromTable.generateMapperInstanceName();

            //直接的外键依赖

            String getterMethodName = CodeGenerateUtil.assembleGetterMethodName(
                    Inflector.getInstance().pluralize(foreignKeyInstanceName), referFromTable.generateFullyQualifiedJavaType()
            );

            String addRecordBatchMethodName = CodeGenerateUtil.generateAddRecordBatchMethodName(referFromTable.getEntityName());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("if (");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(getterMethodName);
            codeLineBuilder.append("() != null && !");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(getterMethodName);
            codeLineBuilder.append("().isEmpty()){");
            addNewRecordMethod.addBodyLine(codeLineBuilder.toString());

            //for循环建立外键依赖
            codeLineBuilder.setLength(0);
            codeLineBuilder.append("    for(");
            codeLineBuilder.append(referFromTable.getEntityName());
            codeLineBuilder.append(" ");
            codeLineBuilder.append(foreignKeyInstanceName);
            codeLineBuilder.append(":");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(getterMethodName);
            codeLineBuilder.append("()){");
            addNewRecordMethod.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("        ");
            codeLineBuilder.append(foreignKeyInstanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.assembleSetterMethodName(referFromColumn.getPropertyName()));
            codeLineBuilder.append("(");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.assembleGetterMethodName(referFromColumn.getPropertyName(), referFromColumn.getJavaType()));
            codeLineBuilder.append("());");
            addNewRecordMethod.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("    }");
            addNewRecordMethod.addBodyLine(codeLineBuilder.toString());


            codeLineBuilder.setLength(0);
            codeLineBuilder.append("    ");
            codeLineBuilder.append(foreignKeyMapperInstanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(addRecordBatchMethodName);
            codeLineBuilder.append("(");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(getterMethodName);
            codeLineBuilder.append("());");
            addNewRecordMethod.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("}");
            addNewRecordMethod.addBodyLine(codeLineBuilder.toString());
        }

        addNewRecordMethod.addAnnotation(ANNOTATION_TRANSACTIONAL_REQUIRED);

        return addNewRecordMethod;
    }

    @Override
    public Method generateAddRecordBatchImpl(IntrospectedTable introspectedTable) {
        Method addNewRecordBatchMethod = new Method();
        addNewRecordBatchMethod.addAnnotation(ANNOTATION_OVERRIDE);

        String addRecordBatchMethodName = CodeGenerateUtil.generateAddRecordBatchMethodName(introspectedTable.getEntityName());
        String instanceName = introspectedTable.getInstanceName();
        String instancesName = Inflector.getInstance().pluralize(instanceName);
        String mapperInstanceName = introspectedTable.generateMapperInstanceName();

        addNewRecordBatchMethod.setVisibility(Visibility.PUBLIC);
        addNewRecordBatchMethod.setName(addRecordBatchMethodName);

        Parameter addNewRecordParameter = new Parameter();
        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
        listType.addTypeArgument(introspectedTable.generateFullyQualifiedJavaType());
        addNewRecordParameter.setType(listType);
        addNewRecordParameter.setName(instancesName);
        addNewRecordBatchMethod.addParameter(addNewRecordParameter);

        //循环做完整性校验
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.append("for(");
        codeLineBuilder.append(introspectedTable.generateFullyQualifiedJavaType().getShortName());
        codeLineBuilder.append(" ");
        codeLineBuilder.append(instanceName);
        codeLineBuilder.append(":");
        codeLineBuilder.append(instancesName);
        codeLineBuilder.append("){");
        addNewRecordBatchMethod.addBodyLine(codeLineBuilder.toString());
        addNewRecordBatchMethod.addBodyLine("    this.checkIntegrity(" + instanceName + ");");
        addNewRecordBatchMethod.addBodyLine("}");
        addNewRecordBatchMethod.addExtraEmptyLine();

        //添加当前实例集合
        codeLineBuilder.setLength(0);
        codeLineBuilder.append(mapperInstanceName);
        codeLineBuilder.append(".");
        codeLineBuilder.append(addRecordBatchMethodName);
        codeLineBuilder.append("(");
        codeLineBuilder.append(instancesName);
        codeLineBuilder.append(");");
        addNewRecordBatchMethod.addBodyLine(codeLineBuilder.toString());

        String foreignKeyInstanceName;
        String foreignKeyMapperInstanceName;
        //如果有直接的referAsForeignKey外键依赖，那么需要级联增加
        for (ForeignKey foreignKey : introspectedTable.getReferAsForeignKeyColumns()) {
            if (foreignKey.getInnerToForeignKey() != null
                    || foreignKey.getInnerFromForeignKey() != null) {
                //此处为通过relation表的间接依赖
                continue;
            }

            //额外空行
            addNewRecordBatchMethod.addExtraEmptyLine();

            IntrospectedTable referFromTable = foreignKey.getReferFromTable();
            IntrospectedColumn referFromColumn = foreignKey.getReferFromColumn();


            foreignKeyInstanceName = referFromTable.getInstanceName();
            foreignKeyMapperInstanceName = referFromTable.generateMapperInstanceName();

            //直接的外键依赖

            String getterMethodName = CodeGenerateUtil.assembleGetterMethodName(
                    Inflector.getInstance().pluralize(foreignKeyInstanceName), referFromTable.generateFullyQualifiedJavaType()
            );

            String addCascadeRecordBatchMethodName = CodeGenerateUtil.generateAddRecordBatchMethodName(referFromTable.getEntityName());

            //外围for循环遍历user，添加必要的子属性
            codeLineBuilder.setLength(0);
            codeLineBuilder.append("for(");
            codeLineBuilder.append(introspectedTable.generateFullyQualifiedJavaType().getFullyQualifiedName());
            codeLineBuilder.append(" ");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(":");
            codeLineBuilder.append(instancesName);
            codeLineBuilder.append("){");
            addNewRecordBatchMethod.addBodyLine(codeLineBuilder.toString());


            codeLineBuilder.setLength(0);
            codeLineBuilder.append("    ");
            codeLineBuilder.append("if (");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(getterMethodName);
            codeLineBuilder.append("() != null && !");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(getterMethodName);
            codeLineBuilder.append("().isEmpty()){");
            addNewRecordBatchMethod.addBodyLine(codeLineBuilder.toString());

            //for循环建立外键依赖
            codeLineBuilder.setLength(0);
            codeLineBuilder.append("        for(");
            codeLineBuilder.append(referFromTable.getEntityName());
            codeLineBuilder.append(" ");
            codeLineBuilder.append(foreignKeyInstanceName);
            codeLineBuilder.append(":");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(getterMethodName);
            codeLineBuilder.append("()){");
            addNewRecordBatchMethod.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("            ");
            codeLineBuilder.append(foreignKeyInstanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.assembleSetterMethodName(referFromColumn.getPropertyName()));
            codeLineBuilder.append("(");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.assembleGetterMethodName(referFromColumn.getPropertyName(), referFromColumn.getJavaType()));
            codeLineBuilder.append("());");
            addNewRecordBatchMethod.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("        }");
            addNewRecordBatchMethod.addBodyLine(codeLineBuilder.toString());

            //递归批量添加子属性集合
            codeLineBuilder.setLength(0);
            codeLineBuilder.append("        ");
            codeLineBuilder.append(foreignKeyMapperInstanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(addCascadeRecordBatchMethodName);
            codeLineBuilder.append("(");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(getterMethodName);
            codeLineBuilder.append("());");
            addNewRecordBatchMethod.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("    ");
            codeLineBuilder.append("}");
            addNewRecordBatchMethod.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("");
            codeLineBuilder.append("}");
            addNewRecordBatchMethod.addBodyLine(codeLineBuilder.toString());
        }

        addNewRecordBatchMethod.addAnnotation(ANNOTATION_TRANSACTIONAL_REQUIRED);

        return addNewRecordBatchMethod;
    }

    @Override
    public Method generateRemoveRecordImpl(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }

        Method removeRecordMethod = new Method();
        removeRecordMethod.addAnnotation(ANNOTATION_OVERRIDE);

        removeRecordMethod.setVisibility(Visibility.PUBLIC);
        removeRecordMethod.setName(CodeGenerateUtil.generateRemoveRecordMethod(
                introspectedTable.getEntityName(),
                primaryKeyColumn.getPropertyName()));

        Parameter removeRecordMethodParameter = new Parameter();
        removeRecordMethodParameter.setType(primaryKeyColumn.getJavaType());
        removeRecordMethodParameter.setName(primaryKeyColumn.getPropertyName());
        removeRecordMethod.addParameter(removeRecordMethodParameter);

        IntrospectedTable referFromTable;
        IntrospectedColumn referToColumn;
        for (ForeignKey referAsForeignKey : introspectedTable.getReferAsForeignKeyColumns()) {
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null) {
                continue;
            }

            referFromTable = referAsForeignKey.getInnerFromForeignKey().getReferFromTable();    //B
            referToColumn = referAsForeignKey.getInnerToForeignKey().getReferToColumn();    //a column

            //添加方法体
            //第一步查询当前uniqueIndex对应的记录
            StringBuilder codeLineBuilder = new StringBuilder();
            codeLineBuilder.append(introspectedTable.getEntityName());
            codeLineBuilder.append(" ");
            codeLineBuilder.append(introspectedTable.getInstanceName());
            codeLineBuilder.append(" = this.");
            codeLineBuilder.append(CodeGenerateUtil.generateQueryRecordMethodName(
                    introspectedTable.getEntityName(), primaryKeyColumn.getPropertyName()));
            codeLineBuilder.append("(");
            codeLineBuilder.append(primaryKeyColumn.getPropertyName());
            codeLineBuilder.append(")");
            codeLineBuilder.append(";");
            removeRecordMethod.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("if(");
            codeLineBuilder.append(introspectedTable.getInstanceName());
            codeLineBuilder.append(" != null){");
            removeRecordMethod.addBodyLine(codeLineBuilder.toString());


            //第二步，级联删除，关系表中的记录
            codeLineBuilder.setLength(0);
            codeLineBuilder.append("    ");
            codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.generateRemoveRelationsMethodName(referFromTable, referToColumn));
            codeLineBuilder.append("(");
            codeLineBuilder.append(introspectedTable.getInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.assembleGetterMethodName(referToColumn.getPropertyName(), referToColumn.getJavaType()));
            codeLineBuilder.append("());");
            removeRecordMethod.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("}");
            removeRecordMethod.addBodyLine(codeLineBuilder.toString());

            //添加一个空行
            removeRecordMethod.addExtraEmptyLine();
        }

        //第二步，删除当前实体
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(CodeGenerateUtil.generateRemoveRecordMethod(introspectedTable.getEntityName(), primaryKeyColumn.getPropertyName()));
        codeLineBuilder.append("(");
        codeLineBuilder.append(primaryKeyColumn.getPropertyName());
        codeLineBuilder.append(");");
        removeRecordMethod.addBodyLine(codeLineBuilder.toString());

        removeRecordMethod.addAnnotation(ANNOTATION_TRANSACTIONAL_REQUIRED);

        return removeRecordMethod;
    }

    @Override
    public List<Method> generateRemoveRecordByUniqueIndexImpl(IntrospectedTable introspectedTable) {
        List<Method> removeRecordByUniqueIndexMethods = new ArrayList<>();

        Method removeRecordMethod;
        for (UniqueIndex uniqueIndex : introspectedTable.getUniqueIndexColumns()) {
            removeRecordMethod = new Method();
            removeRecordMethod.addAnnotation(ANNOTATION_OVERRIDE);

            removeRecordMethod.setVisibility(Visibility.PUBLIC);
            removeRecordMethod.setName(CodeGenerateUtil.generateRemoveRecordMethodNameByUniqueIndex(
                    introspectedTable.getEntityName(),
                    uniqueIndex));

            addParameterGeneratedFromUniqueIndex(removeRecordMethod, uniqueIndex);

            IntrospectedTable referFromTable;
            IntrospectedColumn referToColumn;
            for (ForeignKey referAsForeignKey : introspectedTable.getReferAsForeignKeyColumns()) {
                if (referAsForeignKey.getInnerFromForeignKey() == null
                        || referAsForeignKey.getInnerToForeignKey() == null) {
                    continue;
                }

                referFromTable = referAsForeignKey.getInnerFromForeignKey().getReferFromTable();    //B
                referToColumn = referAsForeignKey.getInnerToForeignKey().getReferToColumn();    //a column

                StringBuilder codeLineBuilder = new StringBuilder();

                //第一步查询当前uniqueIndex对应的记录
                codeLineBuilder.append(introspectedTable.getEntityName());
                codeLineBuilder.append(" ");
                codeLineBuilder.append(introspectedTable.getInstanceName());
                codeLineBuilder.append(" = ");
                codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
                codeLineBuilder.append(".");
                codeLineBuilder.append(CodeGenerateUtil.generateQueryRecordByUniqueIndexMethodName(
                        introspectedTable.getEntityName(), uniqueIndex));
                codeLineBuilder.append("(");

                boolean isFirst = true;
                for (IntrospectedColumn onColumn : uniqueIndex.getOnColumns()) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        codeLineBuilder.append(",");
                    }

                    codeLineBuilder.append(onColumn.getPropertyName());
                }

                codeLineBuilder.append(")");
                codeLineBuilder.append(";");
                removeRecordMethod.addBodyLine(codeLineBuilder.toString());

                codeLineBuilder.setLength(0);
                codeLineBuilder.append("if(");
                codeLineBuilder.append(introspectedTable.getInstanceName());
                codeLineBuilder.append(" != null){");
                removeRecordMethod.addBodyLine(codeLineBuilder.toString());

                //第二步，级联删除，关系表中的记录

                codeLineBuilder.setLength(0);
                codeLineBuilder.append("    ");
                codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
                codeLineBuilder.append(".");
                codeLineBuilder.append(CodeGenerateUtil.generateRemoveRelationsMethodName(referFromTable, referToColumn));
                codeLineBuilder.append("(");
                codeLineBuilder.append(introspectedTable.getInstanceName());
                codeLineBuilder.append(".");
                codeLineBuilder.append(CodeGenerateUtil.assembleGetterMethodName(referToColumn.getPropertyName(), referToColumn.getJavaType()));
                codeLineBuilder.append("());");
                removeRecordMethod.addBodyLine(codeLineBuilder.toString());

                codeLineBuilder.setLength(0);
                codeLineBuilder.append("}");
                removeRecordMethod.addBodyLine(codeLineBuilder.toString());

                //添加一个空行
                removeRecordMethod.addExtraEmptyLine();
            }

            //第二步，删除当前实体
            StringBuilder codeLineBuilder = new StringBuilder();
            codeLineBuilder.setLength(0);
            codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.generateRemoveRecordMethodNameByUniqueIndex(
                    introspectedTable.getEntityName(), uniqueIndex));
            codeLineBuilder.append("(");

            //参数列表
            boolean isFirst = true;
            for (IntrospectedColumn onColumn : uniqueIndex.getOnColumns()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    codeLineBuilder.append(",");
                }

                codeLineBuilder.append(onColumn.getPropertyName());
            }

            codeLineBuilder.append(");");
            removeRecordMethod.addBodyLine(codeLineBuilder.toString());

            removeRecordMethod.addAnnotation(ANNOTATION_TRANSACTIONAL_REQUIRED);

            removeRecordByUniqueIndexMethods.add(removeRecordMethod);
        }


        return removeRecordByUniqueIndexMethods;
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

            removeRecordByUniqueIndexMethod.addParameter(parameter);
        }
    }

    @Override
    public Method generateModifyRecordImpl(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null){
            return null;
        }

        Method modifyRecordMethod = new Method();
        modifyRecordMethod.addAnnotation(ANNOTATION_OVERRIDE);

        modifyRecordMethod.setVisibility(Visibility.PUBLIC);
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

        //添加函数体
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(CodeGenerateUtil.generateModifyRecordMethodName(introspectedTable.getEntityName(),primaryKeyColumn.getPropertyName()));
        codeLineBuilder.append("(");
        codeLineBuilder.append(oldIdentifierParamName);
        codeLineBuilder.append(",");
        codeLineBuilder.append(newEntityParamName);
        codeLineBuilder.append(");");
        modifyRecordMethod.addBodyLine(codeLineBuilder.toString());


        return modifyRecordMethod;
    }

    @Override
    public Method generateQueryRecordImpl(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null){
            return null;
        }

        Method queryOneRecordMethod = new Method();
        queryOneRecordMethod.addAnnotation(ANNOTATION_OVERRIDE);

        queryOneRecordMethod.setVisibility(Visibility.PUBLIC);
        queryOneRecordMethod.setName(CodeGenerateUtil.generateQueryRecordMethodName(
                introspectedTable.getEntityName(), primaryKeyColumn.getPropertyName()
        ));

        Parameter queryOneRecordMethodParameter = new Parameter();
        queryOneRecordMethodParameter.setType(primaryKeyColumn.getJavaType());
        queryOneRecordMethodParameter.setName(primaryKeyColumn.getPropertyName());
        queryOneRecordMethod.addParameter(queryOneRecordMethodParameter);

        queryOneRecordMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());

        //添加方法体
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.append("return ");
        codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(CodeGenerateUtil.generateQueryRecordMethodName(
                introspectedTable.getEntityName(),primaryKeyColumn.getPropertyName()));
        codeLineBuilder.append("(");
        codeLineBuilder.append(primaryKeyColumn.getPropertyName());
        codeLineBuilder.append(");");
        queryOneRecordMethod.addBodyLine(codeLineBuilder.toString());

        return queryOneRecordMethod;
    }

    @Override
    public Method generateQueryRecordWithBlobImpl(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null){
            return null;
        }

        //如果当前表没有blob属性列，那么不添加此方法
        if (introspectedTable.getBlobColumns().isEmpty()){
            return null;
        }

        Method queryRecordWithBlobMethod = new Method();
        queryRecordWithBlobMethod.addAnnotation(ANNOTATION_OVERRIDE);

        queryRecordWithBlobMethod.setVisibility(Visibility.PUBLIC);
        queryRecordWithBlobMethod.setName(CodeGenerateUtil.generateQueryRecordWithBlobMethodName(
                introspectedTable.getEntityName(), primaryKeyColumn.getPropertyName()
        ));

        Parameter queryOneRecordWithBlobMethodParameter = new Parameter();
        queryOneRecordWithBlobMethodParameter.setType(primaryKeyColumn.getJavaType());
        queryOneRecordWithBlobMethodParameter.setName(primaryKeyColumn.getPropertyName());
        queryRecordWithBlobMethod.addParameter(queryOneRecordWithBlobMethodParameter);

        queryRecordWithBlobMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());

        //添加方法体
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.append("return ");
        codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(CodeGenerateUtil.generateQueryRecordWithBlobMethodName(
                introspectedTable.getEntityName(),primaryKeyColumn.getPropertyName()));
        codeLineBuilder.append("(");
        codeLineBuilder.append(primaryKeyColumn.getPropertyName());
        codeLineBuilder.append(");");
        queryRecordWithBlobMethod.addBodyLine(codeLineBuilder.toString());

        return queryRecordWithBlobMethod;
    }

    @Override
    public Method generateQueryRecordOnlyBlobImpl(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null){
            return null;
        }

        //如果当前表没有blob属性列，那么不添加此方法
        if (!introspectedTable.hasBlobColumns()){
            return null;
        }

        Method queryRecordWithOnlyBlobMethod = new Method();
        queryRecordWithOnlyBlobMethod.addAnnotation(ANNOTATION_OVERRIDE);

        queryRecordWithOnlyBlobMethod.setVisibility(Visibility.PUBLIC);
        queryRecordWithOnlyBlobMethod.setName(CodeGenerateUtil.generateQueryRecordWithOnlyBlobMethodName(
                introspectedTable.getEntityName(),
                primaryKeyColumn.getPropertyName()));

        Parameter queryOneRecordWithOnlyBlobMethodParameter = new Parameter();
        queryOneRecordWithOnlyBlobMethodParameter.setType(primaryKeyColumn.getJavaType());
        queryOneRecordWithOnlyBlobMethodParameter.setName(primaryKeyColumn.getPropertyName());
        queryRecordWithOnlyBlobMethod.addParameter(queryOneRecordWithOnlyBlobMethodParameter);

        queryRecordWithOnlyBlobMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());

        //添加方法体
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.append("return ");
        codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(CodeGenerateUtil.generateQueryRecordWithOnlyBlobMethodName(
                introspectedTable.getEntityName(),primaryKeyColumn.getPropertyName()));
        codeLineBuilder.append("(");
        codeLineBuilder.append(primaryKeyColumn.getPropertyName());
        codeLineBuilder.append(");");
        queryRecordWithOnlyBlobMethod.addBodyLine(codeLineBuilder.toString());

        return queryRecordWithOnlyBlobMethod;
    }

    @Override
    public Method generateQueryAllRecordImpl(IntrospectedTable introspectedTable) {
        Method queryAllRecordMethod = new Method();
        queryAllRecordMethod.addAnnotation(ANNOTATION_OVERRIDE);

        queryAllRecordMethod.setVisibility(Visibility.PUBLIC);
        queryAllRecordMethod.setName(CodeGenerateUtil.generateQueryAllRecordMethodName(introspectedTable.getEntityName()));

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        returnType.addTypeArgument(introspectedTable.generateFullyQualifiedJavaType());
        queryAllRecordMethod.setReturnType(returnType);

        //添加方法体
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.append("return ");
        codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(CodeGenerateUtil.generateQueryAllRecordMethodName(introspectedTable.getEntityName()));
        codeLineBuilder.append("();");
        queryAllRecordMethod.addBodyLine(codeLineBuilder.toString());

        return queryAllRecordMethod;
    }

    @Override
    public Method generateQueryAllRecordWithPageImpl(IntrospectedTable introspectedTable) {
        Method queryAllRecordWithPageMethod = new Method();
        queryAllRecordWithPageMethod.addAnnotation(ANNOTATION_OVERRIDE);

        queryAllRecordWithPageMethod.setVisibility(Visibility.PUBLIC);

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

        //添加方法体
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.append("return ");
        codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(CodeGenerateUtil.generateQueryAllRecordWithPageMethodName(
                introspectedTable.getEntityName()
        ));
        codeLineBuilder.append("(pageNumber * recordCountPerPage,recordCountPerPage);");
        queryAllRecordWithPageMethod.addBodyLine(codeLineBuilder.toString());

        return queryAllRecordWithPageMethod;
    }

    @Override
    public List<Method> generateIfExistsImpl(IntrospectedTable introspectedTable) {
        List<Method> isSpecRecordExistsMethods = new ArrayList<>();

        Method isSpecRecordExistsMethod;
        for (UniqueIndex uniqueIndex:introspectedTable.getUniqueIndexColumns()){
            isSpecRecordExistsMethod = new Method();
            isSpecRecordExistsMethod.addAnnotation(ANNOTATION_OVERRIDE);
            isSpecRecordExistsMethod.setVisibility(Visibility.PUBLIC);
            isSpecRecordExistsMethod.setName(CodeGenerateUtil.generateIsSpecRecordExistsMethodName(
                    introspectedTable.getEntityName(),uniqueIndex));
            isSpecRecordExistsMethod.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());

            addParameterGeneratedFromUniqueIndex(isSpecRecordExistsMethod, uniqueIndex);


            //添加方法体
            //添加方法体
            StringBuilder codeLineBuilder = new StringBuilder();
            codeLineBuilder.append("return ");
            codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.generateQueryCountMethodNameByUniqueIndex(
                    introspectedTable.getEntityName(),uniqueIndex
            ));
            codeLineBuilder.append("(");
            boolean isFirst = true;
            for (IntrospectedColumn onColumn:uniqueIndex.getOnColumns()){
                if (isFirst){
                    isFirst = false;
                } else {
                    codeLineBuilder.append(",");
                }

                codeLineBuilder.append(onColumn.getPropertyName());
            }

            codeLineBuilder.append(") >= 1;");
            isSpecRecordExistsMethod.addBodyLine(codeLineBuilder.toString());

            isSpecRecordExistsMethods.add(isSpecRecordExistsMethod);
        }

        return isSpecRecordExistsMethods;
    }

    @Override
    public List<Method> generateBinaryCascadeQueryImpl(IntrospectedTable introspectedTable) {
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

            cascadeQueryMethod = new Method();
            cascadeQueryMethod.addAnnotation(ANNOTATION_OVERRIDE);
            cascadeQueryMethod.setVisibility(Visibility.PUBLIC);
            cascadeQueryMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());
            cascadeQueryMethod.setName(methodName);
            Parameter cascadeQueryMethodParameter = new Parameter();
            cascadeQueryMethodParameter.setType(primaryKeyColumn.getJavaType());
            cascadeQueryMethodParameter.setName(primaryKeyColumn.getPropertyName());
            cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

            //添加方法体
            StringBuilder codeLineBuilder = new StringBuilder();
            codeLineBuilder.append("return ");
            codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(methodName);
            codeLineBuilder.append("(");
            codeLineBuilder.append(primaryKeyColumn.getPropertyName());
            codeLineBuilder.append(");");
            cascadeQueryMethod.addBodyLine(codeLineBuilder.toString());

            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        for (ForeignKey referAsForeignKey:introspectedTable.getReferAsForeignKeyColumns()){
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null){
                //single join

                methodName = CodeGenerateUtil.generateQueryRecordWithCollectionMethodName(
                        introspectedTable, referAsForeignKey.getReferFromTable());

                cascadeQueryMethod = new Method();
                cascadeQueryMethod.addAnnotation(ANNOTATION_OVERRIDE);
                cascadeQueryMethod.setVisibility(Visibility.PUBLIC);
                cascadeQueryMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());
                cascadeQueryMethod.setName(methodName);
                Parameter cascadeQueryMethodParameter = new Parameter();
                cascadeQueryMethodParameter.setType(primaryKeyColumn.getJavaType());
                cascadeQueryMethodParameter.setName(primaryKeyColumn.getPropertyName());
                cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

                //添加方法体
                StringBuilder codeLineBuilder = new StringBuilder();
                codeLineBuilder.append("return ");
                codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
                codeLineBuilder.append(".");
                codeLineBuilder.append(methodName);
                codeLineBuilder.append("(");
                codeLineBuilder.append(primaryKeyColumn.getPropertyName());
                codeLineBuilder.append(");");
                cascadeQueryMethod.addBodyLine(codeLineBuilder.toString());

                cascadeQueryMethods.add(cascadeQueryMethod);

                continue;
            }

            IntrospectedTable referFromTable = referAsForeignKey.getReferFromTable();

            methodName = CodeGenerateUtil.generateQueryRecordWithCollectionMethodName(introspectedTable, referFromTable);

            cascadeQueryMethod = new Method();
            cascadeQueryMethod.addAnnotation(ANNOTATION_OVERRIDE);
            cascadeQueryMethod.setVisibility(Visibility.PUBLIC);
            cascadeQueryMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());
            cascadeQueryMethod.setName(methodName);
            Parameter cascadeQueryMethodParameter = new Parameter();
            cascadeQueryMethodParameter.setType(primaryKeyColumn.getJavaType());
            cascadeQueryMethodParameter.setName(primaryKeyColumn.getPropertyName());
            cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

            //添加方法体
            StringBuilder codeLineBuilder = new StringBuilder();
            codeLineBuilder.append("return ");
            codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(methodName);
            codeLineBuilder.append("(");
            codeLineBuilder.append(primaryKeyColumn.getPropertyName());
            codeLineBuilder.append(");");
            cascadeQueryMethod.addBodyLine(codeLineBuilder.toString());

            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        return cascadeQueryMethods;
    }

    @Override
    public List<Method> generateSingleCascadeQueryImpl(IntrospectedTable introspectedTable) {
        List<Method> cascadeQueryMethods = new ArrayList<>();

        Method cascadeQueryMethod;
        for (ForeignKey referAsForeignKey:introspectedTable.getReferAsForeignKeyColumns()){
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null){
                continue;
            }

            IntrospectedColumn referFromColumn = referAsForeignKey.getInnerFromForeignKey().getReferFromColumn();

            cascadeQueryMethod = new Method();
            cascadeQueryMethod.addAnnotation(ANNOTATION_OVERRIDE);
            cascadeQueryMethod.setVisibility(Visibility.PUBLIC);
            FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
            returnType.addTypeArgument(introspectedTable.generateFullyQualifiedJavaType());
            cascadeQueryMethod.setReturnType(returnType);
            cascadeQueryMethod.setName(
                    CodeGenerateUtil.generateQueryRecordByForeignColumn(introspectedTable, referFromColumn));
            Parameter cascadeQueryMethodParameter = new Parameter();
            cascadeQueryMethodParameter.setType(referFromColumn.getJavaType());
            cascadeQueryMethodParameter.setName(referFromColumn.getPropertyName());
            cascadeQueryMethod.addParameter(cascadeQueryMethodParameter);

            //添加方法体
            StringBuilder codeLineBuilder = new StringBuilder();
            codeLineBuilder.append("return ");
            codeLineBuilder.append(introspectedTable.generateMapperInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.generateQueryRecordByForeignColumn(introspectedTable,referFromColumn));
            codeLineBuilder.append("(");
            codeLineBuilder.append(referFromColumn.getPropertyName());
            codeLineBuilder.append(");");
            cascadeQueryMethod.addBodyLine(codeLineBuilder.toString());

            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        return cascadeQueryMethods;
    }

    @Override
    public Method generateIntegrityCheckImpl(IntrospectedTable introspectedTable) {
        Method integrityCheckMethod = new Method();

        integrityCheckMethod.setVisibility(Visibility.PUBLIC);
        integrityCheckMethod.setName("checkIntegrity");
        integrityCheckMethod.addAnnotation(ANNOTATION_OVERRIDE);

        Parameter parameter = new Parameter();
        parameter.setName(CodeGenerateUtil.getSimpleInstanceObjName(introspectedTable.getEntityName()));
        parameter.setType(introspectedTable.generateFullyQualifiedJavaType());
        integrityCheckMethod.addParameter(parameter);

        String instanceName = introspectedTable.getInstanceName();
        //添加方法体
        StringBuilder codeLineBuilder = new StringBuilder();
        boolean isFirst = true;
        for (IntrospectedColumn column:introspectedTable.getAllColumns()){
            if (!column.isNullable() && !column.isAutoIncrement() && !column.isGeneratedColumn()){
                if (isFirst){
                    isFirst = false;
                } else {
                    integrityCheckMethod.addExtraEmptyLine();
                }

                addNonNullColumnCheckCodeBlock(integrityCheckMethod, instanceName, codeLineBuilder, column);
            }
        }

        //如果包含一个直接的主动的外键依赖，比如一个字段 has a 字段类型
        for (ForeignKey foreignKey:introspectedTable.getForeignKeyColumns()){
            if (isFirst){
                isFirst = false;
            } else {
                integrityCheckMethod.addExtraEmptyLine();
            }

            IntrospectedColumn referFromColumn = foreignKey.getReferFromColumn();

            addNonNullColumnCheckCodeBlock(integrityCheckMethod, instanceName, codeLineBuilder, referFromColumn);
        }

        return integrityCheckMethod;
    }

    private void addNonNullColumnCheckCodeBlock(Method integrityCheckMethod, String instanceName, StringBuilder codeLineBuilder, IntrospectedColumn referFromColumn) {
        codeLineBuilder.setLength(0);
        codeLineBuilder.append("if(");
        if (referFromColumn.getJavaType().equals(FullyQualifiedJavaType.getStringInstance())){
            codeLineBuilder.append("StringUtils.isBlank(");
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.assembleGetterMethodName(referFromColumn.getPropertyName(),referFromColumn.getJavaType()));
            codeLineBuilder.append("()");
            codeLineBuilder.append(")){");
        } else {
            codeLineBuilder.append(instanceName);
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.assembleGetterMethodName(referFromColumn.getPropertyName(),referFromColumn.getJavaType()));
            codeLineBuilder.append("()");
            codeLineBuilder.append(" == null){");
        }
        integrityCheckMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    ");
        codeLineBuilder.append("throw new ParamMissingException(\"");
        codeLineBuilder.append(StringUtils.isBlank(referFromColumn.getRemarks()) ? referFromColumn.getPropertyName() : referFromColumn.getRemarks());
        codeLineBuilder.append("不能为空！");
        codeLineBuilder.append("\");");
        integrityCheckMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("}");
        integrityCheckMethod.addBodyLine(codeLineBuilder.toString());
    }
}
