package com.nxllxn.codegenerator.codegen.generator.controller;

import com.nxllxn.codegenerator.codegen.generator.TypeRegistry;
import com.nxllxn.codegenerator.codegen.generator.util.CodeGenerateUtil;
import com.nxllxn.codegenerator.codegen.java.*;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import com.nxllxn.codegenerator.jdbc.entities.ForeignKey;
import com.nxllxn.codegenerator.jdbc.entities.UniqueIndex;
import com.nxllxn.codegenerator.utils.AssembleUtil;
import com.nxllxn.codegenerator.utils.Inflector;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * controller层代码生成服务
 */
public class ControllerCodeGenerateServiceImpl implements ControllerCodeGenerateService {
    /**
     * Restful controller注解
     */
    private static final String ANNOTATION_REST_CONTROLLER = "@RestController";

    /**
     * Request Mapping,请求url Mapping注解
     */
    private static final String ANNOTATION_REQUEST_MAPPING = "@RequestMapping";

    /**
     * Get Mapping，Get类型的请求url Mapping注解
     */
    private static final String ANNOTATION_GET_MAPPING = "@GetMapping";

    /**
     * Post Mapping，Post类型的请求url Mapping注解
     */
    private static final String ANNOTATION_POST_MAPPING = "@PostMapping";

    /**
     * 请求体注解，用于Post请求取出整个请求体的数据
     */
    private static final String ANNOTATION_REQUEST_BODY = "@RequestBody";

    /**
     * 请求参数注解，用于Get请求取出url中的请求参数
     */
    private static final String ANNOTATION_REQUEST_PARAM = "@RequestParam";

    /**
     * spring 自动注入注解
     */
    private static final String ANNOTATION_AUTOWIRED = "@Autowired";

    /**
     * post请求接受post请求体的参数名称
     */
    private static final String POST_METHOD_PARAM_STR_NAME = "requestParamJsonStr";

    /**
     * post请求，请求体数据转换为的Json对象实例名称
     */
    private static final String POST_METHOD_PARAM_OBJ_NAME = "requestParamJsonObj";

    /**
     * post请求，请求体转换为的Json数组实例名称
     */
    private static final String POST_METHOD_PARAM_ARRAY_NAME = "requestParamJsonObjs";


    @Override
    public TopLevelClass generateControllerClass(IntrospectedTable introspectedTable, String targetPackage) {
        TopLevelClass controllerClass = new TopLevelClass();

        controllerClass.setPackageName(targetPackage);
        controllerClass.setVisibility(Visibility.PUBLIC);
        controllerClass.setType(introspectedTable.generateControllerClassType());

        //添加RestController注解以及RequestMapping注解
        controllerClass.addAnnotation(ANNOTATION_REST_CONTROLLER);
        controllerClass.addAnnotation(ANNOTATION_REQUEST_MAPPING + "(\"/" + introspectedTable.getInstanceName() + "\")");

        controllerClass.setSuperClass(new FullyQualifiedJavaType(targetPackage + AssembleUtil.PACKAGE_SEPERATOR + "AbstractController"));

        //添加额外导入
        controllerClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        controllerClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.PostMapping"));
        controllerClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.GetMapping"));
        controllerClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestBody"));
        controllerClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping"));
        controllerClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RestController"));
        controllerClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestParam"));
        controllerClass.addExtraNonStaticImport(new FullyQualifiedJavaType("com.alibaba.fastjson.JSONArray"));
        controllerClass.addExtraNonStaticImport(new FullyQualifiedJavaType("com.alibaba.fastjson.JSONObject"));
        controllerClass.addExtraNonStaticImport(FullyQualifiedJavaType.getNewListInstance());
        controllerClass.addExtraNonStaticImport(FullyQualifiedJavaType.getNewArrayListInstance());
        controllerClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.apache.commons.lang3.StringUtils"));
        controllerClass.addExtraNonStaticImport(introspectedTable.generateFullyQualifiedJavaType());
        controllerClass.addExtraNonStaticImport(introspectedTable.generateKeyDefinitionClassType());



        return controllerClass;
    }

    @Override
    public List<Field> generateDependentServiceField(IntrospectedTable introspectedTable) {
        List<Field> dependentServiceFields = new ArrayList<>();

        //默认一个Controller仅仅依赖一个底层的Service，比如RecordController仅仅依赖于RecordService，由RecordService提供CRUD的业务逻辑。
        Field dependentServiceField = new Field();
        dependentServiceField.setVisibility(Visibility.PRIVATE);
        dependentServiceField.setType(introspectedTable.generateServiceInterfaceType());
        dependentServiceField.setName(introspectedTable.generateServiceInstanceName());

        dependentServiceField.addExtraNonStaticImport(introspectedTable.generateServiceInterfaceType());

        dependentServiceFields.add(dependentServiceField);

        return dependentServiceFields;
    }

    @Override
    public Method generateAutowiredConstructor(IntrospectedTable introspectedTable) {
        Method constructorMethod = new Method();

        constructorMethod.setVisibility(Visibility.PUBLIC);
        constructorMethod.setName(introspectedTable.getControllerClassName());
        constructorMethod.setConstructor(true);

        String serviceInstanceName = introspectedTable.generateServiceInstanceName();

        Parameter parameter = new Parameter();
        parameter.setType(introspectedTable.generateServiceInterfaceType());
        parameter.setName(serviceInstanceName);
        constructorMethod.addParameter(parameter);

        StringBuilder codeLineBuilder = new StringBuilder();

        //不知道为什么，如果代码中没有对StringBuilder进复用的话，还是会提示直接使用字符串，这没必要吧！
        codeLineBuilder.setLength(0);
        codeLineBuilder.append("this.");
        codeLineBuilder.append(serviceInstanceName);
        codeLineBuilder.append(" = ");
        codeLineBuilder.append(serviceInstanceName);
        codeLineBuilder.append(";");
        constructorMethod.addBodyLine(codeLineBuilder.toString());

        //添加自动注入相关注解
        constructorMethod.addAnnotation(ANNOTATION_AUTOWIRED);

        return constructorMethod;
    }

    @Override
    public Method generateAddNewRecordImpl(IntrospectedTable introspectedTable) {
        String methodName = CodeGenerateUtil.generateAddRecordMethodName(introspectedTable.getEntityName());

        //组装一个新的AddNewRecord方法，请求类型为POST
        Method addNewRecordMethod = generateTemplatePostMethod(methodName);

        //添加Post请求字符串参数转Json代码块
        addParamConvertCodeBlock(addNewRecordMethod);

        //添加反序列化代码块
        addAntiSerializeJsonObjCodeBlock(
                addNewRecordMethod,
                introspectedTable.getEntityName(),
                introspectedTable.getInstanceName(),
                POST_METHOD_PARAM_OBJ_NAME
        );

        //添加Unique校验
        addUniqueCheckCodeBlock(introspectedTable, addNewRecordMethod);

        //添加 增加记录服务层接口调用
        addAddNewRecordServiceCall(introspectedTable, addNewRecordMethod);

        //添加操作成功返回数据
        addSimpleSuccessfulReturn(addNewRecordMethod);

        return addNewRecordMethod;
    }

    /**
     * 添加唯一性校验代码块
     *
     * @param introspectedTable 当前表结构信息
     * @param method            当前添加一条记录到数据的方法
     */
    private void addUniqueCheckCodeBlock(IntrospectedTable introspectedTable, Method method) {
        if (introspectedTable.getUniqueIndexColumns().isEmpty()) {
            return;
        }

        StringBuilder codeLineBuilder = new StringBuilder();

        for (UniqueIndex uniqueIndex : introspectedTable.getUniqueIndexColumns()) {
            codeLineBuilder.setLength(0);
            codeLineBuilder.append("if(");
            codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.generateIsSpecRecordExistsMethodName(introspectedTable.getEntityName(), uniqueIndex));
            codeLineBuilder.append("(");

            boolean isFirst = true;
            for (IntrospectedColumn onColumn : uniqueIndex.getOnColumns()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    codeLineBuilder.append(",");
                }

                codeLineBuilder.append(introspectedTable.getInstanceName());
                codeLineBuilder.append(".");
                codeLineBuilder.append(CodeGenerateUtil.assembleGetterMethodName(onColumn.getPropertyName(), onColumn.getJavaType()));
                codeLineBuilder.append("()");
            }

            codeLineBuilder.append(")){");
            method.addBodyLine(codeLineBuilder.toString());


            codeLineBuilder.setLength(0);
            codeLineBuilder.append("    ");
            codeLineBuilder.append("throw new ParamErrorException(\"");
            codeLineBuilder.append("抱歉！拥有指定");
            isFirst = true;
            for (IntrospectedColumn onColumn : uniqueIndex.getOnColumns()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    codeLineBuilder.append("-");
                }

                codeLineBuilder.append(StringUtils.isBlank(onColumn.getRemarks())
                        ? onColumn.getPropertyName() : onColumn.getRemarks());
            }
            codeLineBuilder.append("属性值的记录已存在！\");");
            method.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("}");
            method.addBodyLine(codeLineBuilder.toString());

            method.addExtraEmptyLine();
        }
    }

    /**
     * 添加一个添加一条记录的服务层接口调用
     *
     * @param introspectedTable  当前表结构信息
     * @param addNewRecordMethod 添加一条记录的Controller方法
     */
    private void addAddNewRecordServiceCall(IntrospectedTable introspectedTable, Method addNewRecordMethod) {
        //添加一条记录
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(CodeGenerateUtil.generateAddRecordMethodName(introspectedTable.getEntityName()));
        codeLineBuilder.append("(");
        codeLineBuilder.append(introspectedTable.getInstanceName());
        codeLineBuilder.append(");");
        addNewRecordMethod.addBodyLine(codeLineBuilder.toString());

        addNewRecordMethod.addExtraEmptyLine();
    }

    @Override
    public Method generateAddRecordBatchImpl(IntrospectedTable introspectedTable) {
        String methodName = CodeGenerateUtil.generateAddRecordBatchMethodName(introspectedTable.getEntityName());

        Method addRecordBatchMethod = generateTemplatePostMethod(methodName);

        //添加入参Json字符串到JsonArray的转换
        addJsonArrayParamConvertCodeBlock(addRecordBatchMethod);

        //添加反序列化代码块
        addAntiSerializeJsonArrayCodeBlock(
                addRecordBatchMethod, introspectedTable.getEntityName(), introspectedTable.getInstanceName());

        //添加uniqueIndex检查
        addBatchUniqueCheckCodeBlock(introspectedTable, addRecordBatchMethod);

        //添加 批量增加记录服务层接口调用
        addAddNewRecordBatchServiceCall(introspectedTable, addRecordBatchMethod);

        //添加操作成功返回数据
        addSimpleSuccessfulReturn(addRecordBatchMethod);

        return addRecordBatchMethod;
    }

    /**
     * 为批量添加记录的接口循环添加数据唯一性校验的代码块
     *
     * @param introspectedTable 当前表结构信息
     * @param method            批量添加记录到数据库的controller方法
     */
    private void addBatchUniqueCheckCodeBlock(IntrospectedTable introspectedTable, Method method) {
        if (introspectedTable.getUniqueIndexColumns().isEmpty()) {
            return;
        }

        String instanceName = introspectedTable.getInstanceName();
        String instancesName = Inflector.getInstance().pluralize(instanceName);

        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.append("for(");
        codeLineBuilder.append(introspectedTable.getEntityName());
        codeLineBuilder.append(" ");
        codeLineBuilder.append(instanceName);
        codeLineBuilder.append(":");
        codeLineBuilder.append(instancesName);
        codeLineBuilder.append("){");
        method.addBodyLine(codeLineBuilder.toString());

        boolean isFirstCodeBlock = true;
        for (UniqueIndex uniqueIndex : introspectedTable.getUniqueIndexColumns()) {
            if (isFirstCodeBlock) {
                isFirstCodeBlock = false;
            } else {
                method.addExtraEmptyLine();
            }

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("    ");
            codeLineBuilder.append("if(");
            codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(CodeGenerateUtil.generateIsSpecRecordExistsMethodName(introspectedTable.getEntityName(), uniqueIndex));
            codeLineBuilder.append("(");

            boolean isFirst = true;
            for (IntrospectedColumn onColumn : uniqueIndex.getOnColumns()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    codeLineBuilder.append(",");
                }

                codeLineBuilder.append(introspectedTable.getInstanceName());
                codeLineBuilder.append(".");
                codeLineBuilder.append(CodeGenerateUtil.assembleGetterMethodName(onColumn.getPropertyName(), onColumn.getJavaType()));
                codeLineBuilder.append("()");
            }

            codeLineBuilder.append(")){");
            method.addBodyLine(codeLineBuilder.toString());


            codeLineBuilder.setLength(0);
            codeLineBuilder.append("        ");
            codeLineBuilder.append("throw new ParamErrorException(\"");
            codeLineBuilder.append("抱歉！拥有指定");
            isFirst = true;
            for (IntrospectedColumn onColumn : uniqueIndex.getOnColumns()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    codeLineBuilder.append("-");
                }

                codeLineBuilder.append(StringUtils.isBlank(onColumn.getRemarks())
                        ? onColumn.getPropertyName() : onColumn.getRemarks());
            }
            codeLineBuilder.append("属性值的记录已存在！\");");
            method.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("    }");
            method.addBodyLine(codeLineBuilder.toString());
        }

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("}");
        method.addBodyLine(codeLineBuilder.toString());

        method.addExtraEmptyLine();
    }

    /**
     * 添加一个批量添加记录的服务层方法调用
     *
     * @param introspectedTable    当前表结构信息
     * @param addRecordBatchMethod 批量添加记录的controller接口
     */
    private void addAddNewRecordBatchServiceCall(IntrospectedTable introspectedTable, Method addRecordBatchMethod) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(CodeGenerateUtil.generateAddRecordBatchMethodName(introspectedTable.getEntityName()));
        codeLineBuilder.append("(");
        codeLineBuilder.append(Inflector.getInstance().pluralize(introspectedTable.getInstanceName()));
        codeLineBuilder.append(");");
        addRecordBatchMethod.addBodyLine(codeLineBuilder.toString());

        addRecordBatchMethod.addExtraEmptyLine();
    }

    @Override
    public Method generateRemoveRecordImpl(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryColumn == null) {
            return null;
        }

        String methodName = CodeGenerateUtil.generateRemoveRecordMethod(introspectedTable.getEntityName(), primaryColumn.getPropertyName());

        Method removeRecordMethod = generateTemplatePostMethod(methodName);

        //添加Post请求字符串参数转Json代码块
        addParamConvertCodeBlock(removeRecordMethod);

        //取出唯一主键Id
        addIdentifierConvert(removeRecordMethod,introspectedTable.getKeyClassName(),
                POST_METHOD_PARAM_OBJ_NAME,primaryColumn);

        //添加删除记录服务层调用
        addRemoveRecordServiceCall(introspectedTable, primaryColumn, removeRecordMethod);

        //添加操作成功返回数据
        addSimpleSuccessfulReturn(removeRecordMethod);

        return removeRecordMethod;
    }

    private void addIdentifierConvert(Method removeRecordMethod, String keyClassName, String jsonInstanceName, IntrospectedColumn propertyColumn) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.append(propertyColumn.getJavaType().getShortName());
        codeLineBuilder.append(" ");
        codeLineBuilder.append(propertyColumn.getPropertyName());
        codeLineBuilder.append(" = ");
        codeLineBuilder.append(jsonInstanceName);
        codeLineBuilder.append(".get");
        codeLineBuilder.append(propertyColumn.getJavaType().getShortName());
        codeLineBuilder.append("(");
        codeLineBuilder.append(keyClassName);
        codeLineBuilder.append(".");
        codeLineBuilder.append(propertyColumn.getKeyConstantName());
        codeLineBuilder.append(");");
        removeRecordMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("if(");
        if (propertyColumn.getJavaType().equals(FullyQualifiedJavaType.getStringInstance())) {
            codeLineBuilder.append("StringUtils.isBlank(");
            codeLineBuilder.append(propertyColumn.getPropertyName());
            codeLineBuilder.append(")");
        } else {
            codeLineBuilder.append(propertyColumn.getPropertyName());
            codeLineBuilder.append(" == null");
        }
        codeLineBuilder.append("){");
        removeRecordMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    ");
        codeLineBuilder.append("throw new ParamMissingException(\"");
        codeLineBuilder.append("抱歉！删除操作依赖的");
        codeLineBuilder.append(StringUtils.isBlank(propertyColumn.getRemarks())
                ? propertyColumn.getPropertyName() : propertyColumn.getRemarks());
        codeLineBuilder.append("不能为空！");
        codeLineBuilder.append("\");");
        removeRecordMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("}");
        removeRecordMethod.addBodyLine(codeLineBuilder.toString());
        removeRecordMethod.addExtraEmptyLine();
    }

    /**
     * 添加一个删除一条记录的服务层方法调用
     *
     * @param introspectedTable  当前表结构信息
     * @param primaryColumn      主键列，用于组装删除一条记录的方法名
     * @param removeRecordMethod 删除一条记录的controller接口
     */
    private void addRemoveRecordServiceCall(IntrospectedTable introspectedTable, IntrospectedColumn primaryColumn, Method removeRecordMethod) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(CodeGenerateUtil.generateRemoveRecordMethod(introspectedTable.getEntityName(), primaryColumn.getPropertyName()));
        codeLineBuilder.append("(");
        codeLineBuilder.append(primaryColumn.getPropertyName());
        codeLineBuilder.append(");");
        removeRecordMethod.addBodyLine(codeLineBuilder.toString());
        removeRecordMethod.addExtraEmptyLine();
    }

    @Override
    public List<Method> generateRemoveRecordByUniqueIndexImpl(IntrospectedTable introspectedTable) {
        List<Method> removeRecordMethods = new ArrayList<>();

        String removeRecordMethodName;
        Method removeRecordMethod;
        for (UniqueIndex uniqueIndex : introspectedTable.getUniqueIndexColumns()) {
            removeRecordMethodName = CodeGenerateUtil.generateRemoveRecordMethodNameByUniqueIndex(
                    introspectedTable.getEntityName(), uniqueIndex
            );

            removeRecordMethod = generateTemplatePostMethod(removeRecordMethodName);

            //添加入参Json字符串到JsonArray的转换
            addParamConvertCodeBlock(removeRecordMethod);

            //取出各个UniqueIndex依赖的列对应的属性
            //addParamConvertBlock(removeRecordMethod, uniqueIndex.getOnColumns());

            for (IntrospectedColumn identifierColumn:uniqueIndex.getOnColumns()){
                addIdentifierConvert(removeRecordMethod,introspectedTable.getKeyClassName(),POST_METHOD_PARAM_OBJ_NAME,identifierColumn);

                removeRecordMethod.addExtraEmptyLine();
            }

            addRemoveRecordByUniqueIndexServiceCall(removeRecordMethod, introspectedTable.generateServiceInstanceName(), removeRecordMethodName, uniqueIndex);

            addSimpleSuccessfulReturn(removeRecordMethod);

            removeRecordMethods.add(removeRecordMethod);
        }

        return removeRecordMethods;
    }

    /**
     * 添加一个按照指定UniqueIndex删除指定记录的服务层接口调用
     *
     * @param removeRecordMethod     当前方法
     * @param serviceInstanceName    service实例名称
     * @param removeRecordMethodName 删除一条记录的service层方法名称
     * @param uniqueIndex            唯一索引信息
     */
    private void addRemoveRecordByUniqueIndexServiceCall(
            Method removeRecordMethod, String serviceInstanceName,
            String removeRecordMethodName, UniqueIndex uniqueIndex) {
        StringBuilder codeLineBuilder = new StringBuilder();

        codeLineBuilder.setLength(0);
        codeLineBuilder.append(serviceInstanceName);
        codeLineBuilder.append(".");
        codeLineBuilder.append(removeRecordMethodName);
        codeLineBuilder.append("(");
        codeLineBuilder.append(assembleArgumentList(uniqueIndex.getOnColumns()));
        codeLineBuilder.append(");");
        removeRecordMethod.addBodyLine(codeLineBuilder.toString());

        removeRecordMethod.addExtraEmptyLine();
    }

    @Override
    public Method generateModifyRecordImpl(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryColumn == null) {
            return null;
        }

        String methodName = CodeGenerateUtil.generateModifyRecordMethodName(
                introspectedTable.getEntityName(), primaryColumn.getPropertyName());

        Method modifyRecordMethod = generateTemplatePostMethod(methodName);

        addParamConvertCodeBlock(modifyRecordMethod);

        //先取出待修改的标识符值
        addOldIdentifierCreateCodeBlock(modifyRecordMethod, introspectedTable.getKeyClassName(), POST_METHOD_PARAM_OBJ_NAME, primaryColumn);

        //再取出新的实例的实体
        addNewInstanceCreatedCodeBlock(modifyRecordMethod, introspectedTable.getEntityName(), introspectedTable.getInstanceName(), introspectedTable.getRemarks());

        //添加修改指定记录的service层调用
        addModifyRecordServiceCall(
                modifyRecordMethod, introspectedTable.generateServiceInstanceName(),
                methodName, introspectedTable.getInstanceName(), primaryColumn.getPropertyName());

        //没有返回值，简单的返回一个状态码
        addSimpleSuccessfulReturn(modifyRecordMethod);

        return modifyRecordMethod;
    }

    /**
     * 添加待修改的旧的标识符转换代码，比如根据recordId修改Record，那么旧的标识符使用的key为old_record_id,实例名称为oldRecordId
     *
     * @param keyClassName       当前实体类对应的字符串常量key定义类名称
     * @param jsonInstanceName   入参json对象实例名称
     * @param primaryColumn      当前表结构唯一主键列
     * @param modifyRecordMethod 当前修改指定记录的方法
     */
    private void addOldIdentifierCreateCodeBlock(Method modifyRecordMethod, String keyClassName, String jsonInstanceName, IntrospectedColumn primaryColumn) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.append(primaryColumn.getJavaType().getShortName());
        codeLineBuilder.append(" ");
        codeLineBuilder.append("old");
        codeLineBuilder.append(CodeGenerateUtil.uppercaseFistCharacter(primaryColumn.getPropertyName()));
        codeLineBuilder.append(" = ");
        codeLineBuilder.append(jsonInstanceName);
        codeLineBuilder.append(".get");
        codeLineBuilder.append(primaryColumn.getJavaType().getShortName());
        codeLineBuilder.append("(");
        codeLineBuilder.append("\"old_\" + ");
        codeLineBuilder.append(keyClassName);
        codeLineBuilder.append(".");
        codeLineBuilder.append(primaryColumn.getKeyConstantName());
        codeLineBuilder.append(");");
        modifyRecordMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("if(");
        if (primaryColumn.getJavaType().equals(FullyQualifiedJavaType.getStringInstance())) {
            codeLineBuilder.append("StringUtils.isBlank(");
            codeLineBuilder.append("old");
            codeLineBuilder.append(CodeGenerateUtil.uppercaseFistCharacter(primaryColumn.getPropertyName()));
            codeLineBuilder.append(")");
        } else {
            codeLineBuilder.append("old");
            codeLineBuilder.append(CodeGenerateUtil.uppercaseFistCharacter(primaryColumn.getPropertyName()));
            codeLineBuilder.append(" == null");
        }
        codeLineBuilder.append("){");
        modifyRecordMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    ");
        codeLineBuilder.append("throw new ParamMissingException(\"");
        codeLineBuilder.append("抱歉！修改操作依赖的旧的");
        codeLineBuilder.append(StringUtils.isBlank(primaryColumn.getRemarks())
                ? primaryColumn.getPropertyName() : primaryColumn.getRemarks());
        codeLineBuilder.append("不能为空！");
        codeLineBuilder.append("\");");
        modifyRecordMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("}");
        modifyRecordMethod.addBodyLine(codeLineBuilder.toString());
        modifyRecordMethod.addExtraEmptyLine();
    }

    /**
     * 添加修改到的新的实例转换代码，比如根据recordId修改record，那么新的实例使用的key为new_record，实例名称为newRecord
     *
     * @param entityName         当前实体名称
     * @param instanceName       当前实例名称
     * @param tableRemark        当前表结构评论，用于构造异常信息字符串
     * @param modifyRecordMethod 修改指定记录的controller层接口名称
     */
    private void addNewInstanceCreatedCodeBlock(Method modifyRecordMethod, String entityName, String instanceName, String tableRemark) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append(entityName);
        codeLineBuilder.append(" ");
        codeLineBuilder.append("new");
        codeLineBuilder.append(CodeGenerateUtil.uppercaseFistCharacter(instanceName));
        codeLineBuilder.append(" = ");
        codeLineBuilder.append("new ");
        codeLineBuilder.append(entityName);
        codeLineBuilder.append("()");
        codeLineBuilder.append(".fromJson(");
        codeLineBuilder.append(POST_METHOD_PARAM_OBJ_NAME);
        codeLineBuilder.append(".getJSONObject(");
        codeLineBuilder.append("\"new_\" + \"");
        codeLineBuilder.append(CodeGenerateUtil.getUnderScoreString(entityName));
        codeLineBuilder.append("\"));");
        modifyRecordMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("if(");
        codeLineBuilder.append("new");
        codeLineBuilder.append(CodeGenerateUtil.uppercaseFistCharacter(instanceName));
        codeLineBuilder.append(" == null){");
        modifyRecordMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    ");
        codeLineBuilder.append("throw new ParamErrorException(\"");
        codeLineBuilder.append("新的");
        codeLineBuilder.append(StringUtils.isBlank(tableRemark) ? entityName : tableRemark);
        codeLineBuilder.append("基本属性不能为空！\");");
        modifyRecordMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("}");
        modifyRecordMethod.addBodyLine(codeLineBuilder.toString());

        modifyRecordMethod.addExtraEmptyLine();
    }

    /**
     * 添加一个修改指定记录的服务层方法调用
     *
     * @param serviceInstanceName       服务实例名称
     * @param instanceName              实例名称
     * @param primaryColumnPropertyName 主键列属性名称
     * @param methodName                调用方法名称
     * @param modifyRecordMethod        当前修改指定记录的方法
     */
    private void addModifyRecordServiceCall(
            Method modifyRecordMethod, String serviceInstanceName, String methodName, String instanceName, String primaryColumnPropertyName) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append(serviceInstanceName);
        codeLineBuilder.append(".");
        codeLineBuilder.append(methodName);
        codeLineBuilder.append("(");
        codeLineBuilder.append("old");
        codeLineBuilder.append(CodeGenerateUtil.uppercaseFistCharacter(primaryColumnPropertyName));
        codeLineBuilder.append(",");
        codeLineBuilder.append("new");
        codeLineBuilder.append(CodeGenerateUtil.uppercaseFistCharacter(instanceName));
        codeLineBuilder.append(");");
        modifyRecordMethod.addBodyLine(codeLineBuilder.toString());

        modifyRecordMethod.addExtraEmptyLine();
    }

    @Override
    public Method generateQueryRecordImpl(IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryColumn == null) {
            return null;
        }

        String methodName = CodeGenerateUtil.generateQueryRecordMethodName(
                introspectedTable.getEntityName(), primaryColumn.getPropertyName());

        List<IntrospectedColumn> paramColumns = new ArrayList<>();
        paramColumns.add(primaryColumn);

        //按照Get请求模板生成查询指定记录的方法模板
        Method queryRecordMethod = generateGetMethod(paramColumns, methodName);

        //参数转换代码块
        addParamConvertBlock(queryRecordMethod, paramColumns);

        //查询一条记录底层service调用
        addQueryRecordServiceCall(queryRecordMethod, introspectedTable.getEntityName(), introspectedTable.getInstanceName(),
                introspectedTable.generateServiceInstanceName(), methodName, primaryColumn.getPropertyName());

        //返回单个实体
        addEntityReturn(queryRecordMethod, introspectedTable.getInstanceName());

        return queryRecordMethod;
    }

    @Override
    public Method generateQueryRecordWithBlobImpl(IntrospectedTable introspectedTable) {
        if (!introspectedTable.hasBlobColumns()) {
            return null;
        }

        IntrospectedColumn primaryColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryColumn == null) {
            return null;
        }

        String methodName = CodeGenerateUtil.generateQueryRecordWithBlobMethodName(
                introspectedTable.getEntityName(), primaryColumn.getPropertyName());

        List<IntrospectedColumn> paramColumns = new ArrayList<>();
        paramColumns.add(primaryColumn);

        Method queryRecordMethod = generateGetMethod(paramColumns, methodName);

        //类型转换
        addParamConvertBlock(queryRecordMethod, paramColumns);

        //调用底层service
        addQueryRecordServiceCall(queryRecordMethod, introspectedTable.getEntityName(), introspectedTable.getInstanceName(),
                introspectedTable.generateServiceInstanceName(), methodName, primaryColumn.getPropertyName());

        //返回Json
        addEntityReturn(queryRecordMethod, introspectedTable.getInstanceName());

        return queryRecordMethod;
    }

    @Override
    public Method generateQueryRecordOnlyBlobImpl(IntrospectedTable introspectedTable) {
        if (!introspectedTable.hasBlobColumns()) {
            return null;
        }

        IntrospectedColumn primaryColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryColumn == null) {
            return null;
        }

        String methodName = CodeGenerateUtil.generateQueryRecordWithOnlyBlobMethodName(
                introspectedTable.getEntityName(), primaryColumn.getPropertyName());

        List<IntrospectedColumn> paramColumns = new ArrayList<>();
        paramColumns.add(primaryColumn);

        Method queryRecordMethod = generateGetMethod(paramColumns, methodName);

        //类型转换
        addParamConvertBlock(queryRecordMethod, paramColumns);

        //调用底层service
        addQueryRecordServiceCall(queryRecordMethod, introspectedTable.getEntityName(), introspectedTable.getInstanceName(),
                introspectedTable.generateServiceInstanceName(), methodName, primaryColumn.getPropertyName());

        //返回Json
        addEntityReturn(queryRecordMethod, introspectedTable.getInstanceName());

        return queryRecordMethod;
    }

    /**
     * 添加一个查询指定记录的service层接口调用
     *
     * @param queryRecordMethod   查询指定记录的方法
     * @param entityName          当前实体名称
     * @param instanceName        当前实例名称
     * @param serviceInstanceName service实例名称
     * @param methodName          调用方法名称
     * @param propertyName        主键属性名称
     */
    private void addQueryRecordServiceCall(
            Method queryRecordMethod, String entityName, String instanceName,
            String serviceInstanceName, String methodName, String propertyName) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append(entityName);
        codeLineBuilder.append(" ");
        codeLineBuilder.append(instanceName);
        codeLineBuilder.append(" = ");
        codeLineBuilder.append(serviceInstanceName);
        codeLineBuilder.append(".");
        codeLineBuilder.append(methodName);
        codeLineBuilder.append("(");
        codeLineBuilder.append(propertyName);
        codeLineBuilder.append(");");
        queryRecordMethod.addBodyLine(codeLineBuilder.toString());

        queryRecordMethod.addExtraEmptyLine();
    }

    @Override
    public Method generateQueryAllRecordImpl(IntrospectedTable introspectedTable) {
        String methodName = CodeGenerateUtil.generateQueryAllRecordMethodName(introspectedTable.getEntityName());

        Method queryAllMethod = generateGetMethod(null, methodName);

        addQueryAllRecordServiceCall(introspectedTable, methodName, queryAllMethod);

        addEntitiesReturn(queryAllMethod, introspectedTable.getInstanceName());

        return queryAllMethod;
    }

    private void addQueryAllRecordServiceCall(IntrospectedTable introspectedTable, String methodName, Method queryAllMethod) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append("List<");
        codeLineBuilder.append(introspectedTable.getEntityName());
        codeLineBuilder.append(">");
        codeLineBuilder.append(" ");
        codeLineBuilder.append(Inflector.getInstance().pluralize(introspectedTable.getInstanceName()));
        codeLineBuilder.append(" = ");
        codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(methodName);
        codeLineBuilder.append("();");
        queryAllMethod.addBodyLine(codeLineBuilder.toString());

        queryAllMethod.addExtraEmptyLine();
    }

    @Override
    public Method generateQueryAllRecordWithPageImpl(IntrospectedTable introspectedTable) {
        String methodName = CodeGenerateUtil.generateQueryAllRecordWithPageMethodName(introspectedTable.getEntityName());

        Method queryAllMethod = generateGetMethod(null, methodName);

        List<String> paramsList = new ArrayList<>();
        paramsList.add("pageNumber");

        addPageParams(queryAllMethod, paramsList);

        //参数类型转换
        queryAllMethod.addBodyLine("Integer pageNumber = Integer.valueOf(pageNumberStr);");
        queryAllMethod.addBodyLine("if (pageNumber == null){");
        queryAllMethod.addBodyLine("    pageNumber = 1;");
        queryAllMethod.addBodyLine("}");
        queryAllMethod.addExtraEmptyLine();

        //底层Service调用
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append("List<");
        codeLineBuilder.append(introspectedTable.getEntityName());
        codeLineBuilder.append(">");
        codeLineBuilder.append(" ");
        codeLineBuilder.append(Inflector.getInstance().pluralize(introspectedTable.getInstanceName()));
        codeLineBuilder.append(" = ");
        codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
        codeLineBuilder.append(".");
        codeLineBuilder.append(methodName);
        codeLineBuilder.append("(");
        codeLineBuilder.append("pageNumber");
        codeLineBuilder.append(",");
        codeLineBuilder.append("DEFAULT_PAGE_SIZE");
        codeLineBuilder.append(");");
        queryAllMethod.addBodyLine(codeLineBuilder.toString());

        queryAllMethod.addExtraEmptyLine();

        //返回Json
        addEntitiesReturn(queryAllMethod, introspectedTable.getInstanceName());

        return queryAllMethod;
    }

    /**
     * 添加分页相关的参数
     *
     * @param queryAllMethod 查询全部记录的方法
     * @param paramsList     参数列表
     */
    private void addPageParams(Method queryAllMethod, List<String> paramsList) {
        Parameter parameter;
        for (String param : paramsList) {
            parameter = new Parameter();

            parameter.setType(FullyQualifiedJavaType.getStringInstance());
            parameter.setName(param + "Str");

            parameter.addAnnotation(ANNOTATION_REQUEST_PARAM + "(\"" +
                    CodeGenerateUtil.getUnderScoreString(param) + "\")");

            queryAllMethod.addParameter(parameter);
        }
    }

    @Override
    public List<Method> generateIfExistsImpl(IntrospectedTable introspectedTable) {
        List<Method> ifExistsMethods = new ArrayList<>();

        String methodName;
        Method ifExistsMethod;
        for (UniqueIndex uniqueIndex : introspectedTable.getUniqueIndexColumns()) {
            methodName = CodeGenerateUtil.generateIsSpecRecordExistsMethodName(introspectedTable.getEntityName(), uniqueIndex);

            ifExistsMethod = generateGetMethod(uniqueIndex.getOnColumns(), methodName);

            //类型转换
            addParamConvertBlock(ifExistsMethod, uniqueIndex.getOnColumns());

            //底层服务调用
            StringBuilder codeLineBuilder = new StringBuilder();
            codeLineBuilder.setLength(0);
            codeLineBuilder.append("Boolean isExists = ");
            codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(methodName);
            codeLineBuilder.append("(");
            codeLineBuilder.append(assembleArgumentList(uniqueIndex.getOnColumns()));
            codeLineBuilder.append(");");
            ifExistsMethod.addBodyLine(codeLineBuilder.toString());
            ifExistsMethod.addExtraEmptyLine();

            //组装返回结果
            ifExistsMethod.addBodyLine("JSONObject resultJsonObj = new JSONObject();");
            ifExistsMethod.addBodyLine("resultJsonObj.put(\"is_exists\",isExists);");
            ifExistsMethod.addExtraEmptyLine();

            addJSONReturn(ifExistsMethod, "resultJsonObj");

            ifExistsMethods.add(ifExistsMethod);
        }

        return ifExistsMethods;
    }

    private static String assembleArgumentList(List<IntrospectedColumn> introspectedColumns) {
        StringBuilder argumentListBuilder = new StringBuilder();

        boolean isFirst = true;
        for (IntrospectedColumn onColumn : introspectedColumns) {
            if (isFirst) {
                isFirst = false;
            } else {
                argumentListBuilder.append(",");
            }

            argumentListBuilder.append(onColumn.getPropertyName());
        }

        return argumentListBuilder.toString();
    }

    @Override
    public List<Method> generateBinaryCascadeQueryImpl(IntrospectedTable introspectedTable) {
        List<Method> cascadeQueryMethods = new ArrayList<>();

        IntrospectedColumn primaryKeyColumn = introspectedTable.getOnlyPrimaryColumn();
        if (primaryKeyColumn == null) {
            return null;
        }


        List<IntrospectedColumn> paramColumns = new ArrayList<>();
        paramColumns.add(primaryKeyColumn);

        String methodName;
        Method cascadeQueryMethod;
        for (ForeignKey foreignKey : introspectedTable.getForeignKeyColumns()) {
            //associate

            methodName = CodeGenerateUtil.generateQueryRecordWithAssociationMethodName(introspectedTable, foreignKey.getReferToTable());

            cascadeQueryMethod = generateGetMethod(paramColumns, methodName);

            addParamConvertBlock(cascadeQueryMethod, paramColumns);

            //底层服务调用
            StringBuilder codeLineBuilder = new StringBuilder();
            codeLineBuilder.setLength(0);
            codeLineBuilder.append(introspectedTable.getEntityName());
            codeLineBuilder.append(" ");
            codeLineBuilder.append(introspectedTable.getInstanceName());
            codeLineBuilder.append(" = ");
            codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(methodName);
            codeLineBuilder.append("(");
            codeLineBuilder.append(primaryKeyColumn.getPropertyName());
            codeLineBuilder.append(");");
            cascadeQueryMethod.addBodyLine(codeLineBuilder.toString());

            cascadeQueryMethod.addExtraEmptyLine();

            addEntityReturn(cascadeQueryMethod, introspectedTable.getInstanceName());


            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        for (ForeignKey referAsForeignKey : introspectedTable.getReferAsForeignKeyColumns()) {
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null) {
                //single join

                methodName = CodeGenerateUtil.generateQueryRecordWithCollectionMethodName(
                        introspectedTable, referAsForeignKey.getReferFromTable());

                cascadeQueryMethod = generateGetMethod(paramColumns, methodName);

                addParamConvertBlock(cascadeQueryMethod, paramColumns);

                //底层服务调用
                StringBuilder codeLineBuilder = new StringBuilder();
                codeLineBuilder.setLength(0);
                codeLineBuilder.append(introspectedTable.getEntityName());
                codeLineBuilder.append(" ");
                codeLineBuilder.append(introspectedTable.getInstanceName());
                codeLineBuilder.append(" = ");
                codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
                codeLineBuilder.append(".");
                codeLineBuilder.append(methodName);
                codeLineBuilder.append("(");
                codeLineBuilder.append(primaryKeyColumn.getPropertyName());
                codeLineBuilder.append(");");
                cascadeQueryMethod.addBodyLine(codeLineBuilder.toString());

                addEntityReturn(cascadeQueryMethod, introspectedTable.getInstanceName());

                continue;
            }

            IntrospectedTable referFromTable = referAsForeignKey.getReferFromTable();

            methodName = CodeGenerateUtil.generateQueryRecordWithCollectionMethodName(introspectedTable, referFromTable);

            cascadeQueryMethod = generateGetMethod(paramColumns, methodName);

            addParamConvertBlock(cascadeQueryMethod, paramColumns);

            //底层服务调用
            StringBuilder codeLineBuilder = new StringBuilder();
            codeLineBuilder.setLength(0);
            codeLineBuilder.append(introspectedTable.getEntityName());
            codeLineBuilder.append(" ");
            codeLineBuilder.append(introspectedTable.getInstanceName());
            codeLineBuilder.append(" = ");
            codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(methodName);
            codeLineBuilder.append("(");
            codeLineBuilder.append(primaryKeyColumn.getPropertyName());
            codeLineBuilder.append(");");
            cascadeQueryMethod.addBodyLine(codeLineBuilder.toString());

            addEntityReturn(cascadeQueryMethod, introspectedTable.getInstanceName());

            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        return cascadeQueryMethods;
    }

    @Override
    public List<Method> generateSingleCascadeQueryImpl(IntrospectedTable introspectedTable) {
        List<Method> cascadeQueryMethods = new ArrayList<>();

        String methodName;
        Method cascadeQueryMethod;
        for (ForeignKey referAsForeignKey : introspectedTable.getReferAsForeignKeyColumns()) {
            if (referAsForeignKey.getInnerFromForeignKey() == null
                    || referAsForeignKey.getInnerToForeignKey() == null) {
                continue;
            }

            IntrospectedColumn referFromColumn = referAsForeignKey.getInnerFromForeignKey().getReferFromColumn();

            methodName = CodeGenerateUtil.generateQueryRecordByForeignColumn(introspectedTable, referFromColumn);

            List<IntrospectedColumn> paramColumns = new ArrayList<>();
            paramColumns.add(referFromColumn);

            cascadeQueryMethod = generateGetMethod(paramColumns, methodName);

            addParamConvertBlock(cascadeQueryMethod, paramColumns);

            //底层服务调用
            StringBuilder codeLineBuilder = new StringBuilder();
            codeLineBuilder.setLength(0);
            codeLineBuilder.append("List<");
            codeLineBuilder.append(introspectedTable.getEntityName());
            codeLineBuilder.append(">");
            codeLineBuilder.append(" ");
            codeLineBuilder.append(Inflector.getInstance().pluralize(introspectedTable.getInstanceName()));
            codeLineBuilder.append(" = ");
            codeLineBuilder.append(introspectedTable.generateServiceInstanceName());
            codeLineBuilder.append(".");
            codeLineBuilder.append(methodName);
            codeLineBuilder.append("(");
            codeLineBuilder.append(referFromColumn.getPropertyName());
            codeLineBuilder.append(");");
            cascadeQueryMethod.addBodyLine(codeLineBuilder.toString());

            cascadeQueryMethod.addExtraEmptyLine();

            addEntitiesReturn(cascadeQueryMethod, introspectedTable.getInstanceName());

            cascadeQueryMethods.add(cascadeQueryMethod);
        }

        return cascadeQueryMethods;
    }

    /**
     * 生成一个通用的Post请求方法模板;
     * 1）默认添加一个@PostMapping注解，注解指定的url路径由入参methodName构造而来，默认格式为 `/methodName`;
     * 2）可见性默认为public;
     * 3）因为是Restful的接口，方法默认返回类型为String，即返回序列化后的Json字符串
     * 4）方法名称由入参methodName指定;
     * 5）方法默认接受一个由@RequestBody注解进行注解的名称为requestParamJsonStr的字符串参数，此参数包含Post请求体中全部数据，默认也会按照Json对象进行处理;
     *
     * @param methodName Post请求方法名称
     * @return Post请求方法模板
     */
    private Method generateTemplatePostMethod(String methodName) {
        Method templatePostMethod = new Method();

        //添加一个PostMapping注解
        templatePostMethod.addAnnotation(ANNOTATION_POST_MAPPING + "(\"/" + methodName + "\")");

        //方法可见性默认为public
        templatePostMethod.setVisibility(Visibility.PUBLIC);

        //方法返回序列化后的Json字符串
        templatePostMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());

        //方法名称由入参指定
        templatePostMethod.setName(methodName);

        //添加一个由@RequestBody注解进行注解的名称为requestParamJsonStr的字符串参数
        Parameter requestParamJsonStrParameter = new Parameter();
        requestParamJsonStrParameter.setType(FullyQualifiedJavaType.getStringInstance());
        requestParamJsonStrParameter.setName(POST_METHOD_PARAM_STR_NAME);
        requestParamJsonStrParameter.addAnnotation(ANNOTATION_REQUEST_BODY);
        templatePostMethod.addParameter(requestParamJsonStrParameter);

        return templatePostMethod;
    }

    /**
     * 为Post请求方法添加Json字符串入参到Json对象的转换，并加入相应的非空校验
     *
     * @param postMethod 当前Post方法
     */
    private void addParamConvertCodeBlock(Method postMethod) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append("if(StringUtils.isBlank(");
        codeLineBuilder.append(POST_METHOD_PARAM_STR_NAME);
        codeLineBuilder.append(")){");
        postMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    throw new EmptyRequestBodyException();");
        postMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("}");
        postMethod.addBodyLine(codeLineBuilder.toString());

        postMethod.addExtraEmptyLine();

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("JSONObject ");
        codeLineBuilder.append(POST_METHOD_PARAM_OBJ_NAME);
        codeLineBuilder.append(" = JSONObject.parseObject(");
        codeLineBuilder.append(POST_METHOD_PARAM_STR_NAME);
        codeLineBuilder.append(");");
        postMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("if(");
        codeLineBuilder.append(POST_METHOD_PARAM_OBJ_NAME);
        codeLineBuilder.append(" == null || ");
        codeLineBuilder.append(POST_METHOD_PARAM_OBJ_NAME);
        codeLineBuilder.append(".isEmpty()){");

        postMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    throw new EmptyRequestBodyException();");
        postMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("}");
        postMethod.addBodyLine(codeLineBuilder.toString());

        postMethod.addExtraEmptyLine();
    }

    /**
     * 为Post请求方法添加Json字符串到Json数组的转换，并加入相应的非空校验
     *
     * @param postMethod 当前Post方法
     */
    private void addJsonArrayParamConvertCodeBlock(Method postMethod) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append("if(StringUtils.isBlank(");
        codeLineBuilder.append(POST_METHOD_PARAM_STR_NAME);
        codeLineBuilder.append(")){");
        postMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    throw new EmptyRequestBodyException();");
        postMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("}");
        postMethod.addBodyLine(codeLineBuilder.toString());

        postMethod.addExtraEmptyLine();

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("JSONArray ");
        codeLineBuilder.append(POST_METHOD_PARAM_ARRAY_NAME);
        codeLineBuilder.append(" = JSONObject.parseArray(");
        codeLineBuilder.append(POST_METHOD_PARAM_STR_NAME);
        codeLineBuilder.append(");");
        postMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("if(");
        codeLineBuilder.append(POST_METHOD_PARAM_ARRAY_NAME);
        codeLineBuilder.append(" == null || ");
        codeLineBuilder.append(POST_METHOD_PARAM_ARRAY_NAME);
        codeLineBuilder.append(".isEmpty()){");

        postMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    throw new EmptyRequestBodyException();");
        postMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("}");
        postMethod.addBodyLine(codeLineBuilder.toString());

        postMethod.addExtraEmptyLine();
    }

    private void addSimpleSuccessfulReturn(Method method) {
        method.addBodyLine("return responseSuccess();");
    }

    private void addJSONReturn(Method method, String jsonObjName) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append("return responseJson(");
        codeLineBuilder.append(jsonObjName);
        codeLineBuilder.append(");");

        method.addBodyLine(codeLineBuilder.toString());
    }

    private void addEntityReturn(Method method, String instanceName) {
        method.addBodyLine("return responseJson(" + instanceName + ");");
    }

    private void addEntitiesReturn(Method queryAllMethod, String instanceName) {
        String instancesName = Inflector.getInstance().pluralize(instanceName);

        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append("return responseJson(");
        codeLineBuilder.append(instancesName);
        codeLineBuilder.append(");");

        queryAllMethod.addBodyLine(codeLineBuilder.toString());
    }

    private Method generateGetMethod(List<IntrospectedColumn> parameterColumns, String methodName) {
        Method getMethod = new Method();

        getMethod.setVisibility(Visibility.PUBLIC);
        getMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        getMethod.setName(methodName);

        if (parameterColumns != null) {
            Parameter propertyParameter;
            for (IntrospectedColumn parameterColumn : parameterColumns) {
                propertyParameter = new Parameter();

                propertyParameter.setType(FullyQualifiedJavaType.getStringInstance());
                propertyParameter.setName(parameterColumn.getPropertyName() + "Str");

                propertyParameter.addAnnotation(ANNOTATION_REQUEST_PARAM + "(\"" +
                        CodeGenerateUtil.getUnderScoreString(parameterColumn.getPropertyName()) + "\")");

                getMethod.addParameter(propertyParameter);
            }
        }

        getMethod.addAnnotation(ANNOTATION_GET_MAPPING + "(\"/" + methodName + "\")");

        return getMethod;
    }

    private void addParamConvertBlock(Method method, List<IntrospectedColumn> introspectedColumns) {
        StringBuilder codeLineBuilder = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            codeLineBuilder.setLength(0);
            codeLineBuilder.append(introspectedColumn.getJavaType().getShortName());
            codeLineBuilder.append(" ");
            codeLineBuilder.append(introspectedColumn.getPropertyName());
            codeLineBuilder.append(" = ");
            codeLineBuilder.append(introspectedColumn.getJavaType().getShortName());
            codeLineBuilder.append(".valueOf(");
            codeLineBuilder.append(introspectedColumn.getPropertyName());
            codeLineBuilder.append("Str");
            codeLineBuilder.append(");");
            method.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("if(");
            if (introspectedColumn.getJavaType().equals(FullyQualifiedJavaType.getStringInstance())) {
                codeLineBuilder.append("StringUtils.isBlank(");
                codeLineBuilder.append(introspectedColumn.getPropertyName());
                codeLineBuilder.append(")");
            } else {
                codeLineBuilder.append(introspectedColumn.getPropertyName());
                codeLineBuilder.append(" == null");
            }
            codeLineBuilder.append("){");
            method.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("    ");
            codeLineBuilder.append("throw new ParamMissingException(\"");
            codeLineBuilder.append("抱歉！查询依赖的参数");
            codeLineBuilder.append(CodeGenerateUtil.getUnderScoreString(introspectedColumn.getPropertyName()));
            codeLineBuilder.append("不能为空!\");");
            method.addBodyLine(codeLineBuilder.toString());

            codeLineBuilder.setLength(0);
            codeLineBuilder.append("}");
            method.addBodyLine(codeLineBuilder.toString());
        }

        method.addExtraEmptyLine();
    }

    private void addAntiSerializeJsonArrayCodeBlock(
            Method method, String entityName, String instanceName) {
        String instancesName = Inflector.getInstance().pluralize(instanceName);

        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.append("List<");
        codeLineBuilder.append(entityName);
        codeLineBuilder.append(">");
        codeLineBuilder.append(" ");
        codeLineBuilder.append(instancesName);
        codeLineBuilder.append(" = new ArrayList<>();");
        method.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("JSONObject ");
        codeLineBuilder.append(POST_METHOD_PARAM_OBJ_NAME);
        codeLineBuilder.append(";");
        method.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("for(int index = 0,length = ");
        codeLineBuilder.append(POST_METHOD_PARAM_ARRAY_NAME);
        codeLineBuilder.append(".size();index < length;index ++){");
        method.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    ");
        codeLineBuilder.append(POST_METHOD_PARAM_OBJ_NAME);
        codeLineBuilder.append(" = ");
        codeLineBuilder.append(POST_METHOD_PARAM_ARRAY_NAME);
        codeLineBuilder.append(".getJSONObject(index);");
        method.addBodyLine(codeLineBuilder.toString());

        method.addExtraEmptyLine();

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    ");
        codeLineBuilder.append(entityName);
        codeLineBuilder.append(" ");
        codeLineBuilder.append(instanceName);
        codeLineBuilder.append(" = ");
        codeLineBuilder.append("new ");
        codeLineBuilder.append(entityName);
        codeLineBuilder.append("()");
        codeLineBuilder.append(".fromJson(");
        codeLineBuilder.append(POST_METHOD_PARAM_OBJ_NAME);
        codeLineBuilder.append(");");
        method.addBodyLine(codeLineBuilder.toString());

        method.addExtraEmptyLine();

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    ");
        codeLineBuilder.append(instancesName);
        codeLineBuilder.append(".add(");
        codeLineBuilder.append(instanceName);
        codeLineBuilder.append(");");
        method.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("}");
        method.addBodyLine(codeLineBuilder.toString());

        method.addExtraEmptyLine();
    }

    /**
     * 添加反序列化声明，即根据入参Json对象中的Json数据反序列化得到对应的Java实例，并提供简单的非空校验
     *
     * @param method       当前方法
     * @param entityName   当前实体名称，用于作为反序列化实例对象声明的类型
     * @param instanceName 当前待生成实例名称，用于作为反序列化实例对象名称
     * @param jsonObjName  使用的Json数据实例名称
     */
    private void addAntiSerializeJsonObjCodeBlock(
            Method method, String entityName, String instanceName, String jsonObjName) {
        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.setLength(0);
        codeLineBuilder.append(entityName);
        codeLineBuilder.append(" ");
        codeLineBuilder.append(instanceName);
        codeLineBuilder.append(" = ");
        codeLineBuilder.append("new ");
        codeLineBuilder.append(entityName);
        codeLineBuilder.append("()");
        codeLineBuilder.append(".fromJson(");
        codeLineBuilder.append(jsonObjName);
        codeLineBuilder.append(");");
        method.addBodyLine(codeLineBuilder.toString());

        method.addExtraEmptyLine();
    }

    @Override
    public TopLevelClass generateAbstractControllerClass(String controllerPackageName,String entityPackageName) {
        TopLevelClass abstractControllerClass = new TopLevelClass();

        abstractControllerClass.setPackageName(controllerPackageName);
        abstractControllerClass.setVisibility(Visibility.PUBLIC);
        abstractControllerClass.setType(new FullyQualifiedJavaType(controllerPackageName + AssembleUtil.PACKAGE_SEPERATOR + "AbstractController"));

        abstractControllerClass.addFields(generateConstantFields());

        abstractControllerClass.addMethod(generateResponseSuccessMethod());

        abstractControllerClass.addMethod(generateResponseJSONMethod());

        abstractControllerClass.addMethod(generateResponseEntityMethod(entityPackageName));

        abstractControllerClass.addMethod(generateResponseEntitiesMethod(entityPackageName));

        abstractControllerClass.addExtraNonStaticImport(TypeRegistry.FAST_JSON_JSON_OBJECT_TYPE);
        abstractControllerClass.addExtraNonStaticImport(TypeRegistry.FAST_JSON_SERIALIZER_FEATURE_TYPE);

        return abstractControllerClass;
    }

    private Method generateResponseJSONMethod() {
        Method responseJSONMethod = new Method();

        responseJSONMethod.setVisibility(Visibility.PROTECTED);
        responseJSONMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        responseJSONMethod.setName("responseJson");

        Parameter parameter = new Parameter();
        parameter.setType(TypeRegistry.FAST_JSON_JSON_TYPE);
        parameter.setName("jsonObj");
        responseJSONMethod.addParameter(parameter);

        responseJSONMethod.addBodyLine("JSONObject responseJsonObj = new JSONObject();");
        responseJSONMethod.addBodyLine("responseJsonObj.put(RESPONSE_CODE,\"00\");");
        responseJSONMethod.addBodyLine("responseJsonObj.put(CONTENT,jsonObj);");
        responseJSONMethod.addBodyLine("return JSONObject.toJSONString(responseJsonObj,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);");

        return responseJSONMethod;
    }

    private Method generateResponseEntityMethod(String entityPackageName) {
        Method responseEntityMethod = new Method();


        responseEntityMethod.setVisibility(Visibility.PROTECTED);
        responseEntityMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        responseEntityMethod.setName("responseJson");

        Parameter parameter = new Parameter();
        parameter.setType(TypeRegistry.generateSerializerParameterType(entityPackageName));
        parameter.setName("entity");
        responseEntityMethod.addParameter(parameter);

        responseEntityMethod.addBodyLine("JSONObject responseJsonObj = new JSONObject();");
        responseEntityMethod.addBodyLine("responseJsonObj.put(RESPONSE_CODE,\"00\");");
        responseEntityMethod.addBodyLine("responseJsonObj.put(CONTENT,entity == null ? null : entity.toJson());");
        responseEntityMethod.addBodyLine("return JSONObject.toJSONString(responseJsonObj,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);");

        return responseEntityMethod;
    }

    private Method generateResponseEntitiesMethod(String entityPackageName) {
        Method responseEntitiesMethod = new Method();

        responseEntitiesMethod.setVisibility(Visibility.PROTECTED);
        responseEntitiesMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        responseEntitiesMethod.setName("responseJson");

        Parameter parameter = new Parameter();
        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
        listType.addTypeArgument(new FullyQualifiedJavaType("? extends Serializer"));
        parameter.setType(listType);
        parameter.setName("entities");
        responseEntitiesMethod.addParameter(parameter);

        responseEntitiesMethod.addBodyLine("JSONObject responseJsonObj = new JSONObject();");
        responseEntitiesMethod.addBodyLine("responseJsonObj.put(RESPONSE_CODE,\"00\");");
        responseEntitiesMethod.addBodyLine("responseJsonObj.put(CONTENT,AbstractSerializer.serializeBatch(entities));");
        responseEntitiesMethod.addBodyLine("return JSONObject.toJSONString(responseJsonObj,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);");

        responseEntitiesMethod.addExtraNonStaticImport(TypeRegistry.generateAbstractSerializerClass(entityPackageName));
        responseEntitiesMethod.addExtraNonStaticImport(TypeRegistry.generateSerializerInterfaceType(entityPackageName));

        return responseEntitiesMethod;
    }

    private List<Field> generateConstantFields() {
        List<Field> constantFields = new ArrayList<>();

        Field field = new Field();
        field.setVisibility(Visibility.PRIVATE);
        field.setStatic(true);
        field.setFinal(true);
        field.setType(FullyQualifiedJavaType.getStringInstance());
        field.setName("RESPONSE_CODE");
        field.setInitStrValue("response_code");
        constantFields.add(field);

        field = new Field();
        field.setVisibility(Visibility.PRIVATE);
        field.setStatic(true);
        field.setFinal(true);
        field.setType(FullyQualifiedJavaType.getStringInstance());
        field.setName("CONTENT");
        field.setInitStrValue("content");
        constantFields.add(field);

        field = new Field();
        field.setVisibility(Visibility.PROTECTED);
        field.setStatic(true);
        field.setFinal(true);
        field.setType(FullyQualifiedJavaType.getIntInstance());
        field.setName("DEFAULT_PAGE_SIZE");
        field.setInitStrValue("200");
        constantFields.add(field);

        return constantFields;
    }

    private Method generateResponseSuccessMethod() {
        Method method = new Method();
        method.setVisibility(Visibility.PROTECTED);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("responseSuccess");

        method.addBodyLine("JSONObject responseJsonObj = new JSONObject();");
        method.addBodyLine("responseJsonObj.put(RESPONSE_CODE,\"00\");");
        method.addBodyLine("return JSONObject.toJSONString(responseJsonObj,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);");

        return method;
    }
}
