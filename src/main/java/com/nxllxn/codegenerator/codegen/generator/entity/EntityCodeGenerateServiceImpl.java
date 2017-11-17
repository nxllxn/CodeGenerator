package com.nxllxn.codegenerator.codegen.generator.entity;

import com.nxllxn.codegenerator.codegen.generator.TypeRegistry;
import com.nxllxn.codegenerator.codegen.generator.util.CodeGenerateUtil;
import com.nxllxn.codegenerator.codegen.java.*;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成服务实现类
 *
 * @author wenchao
 */
public class EntityCodeGenerateServiceImpl implements EntityCodeGenerateService {
    /**
     * Override注解名称
     */
    private static final String ANNOTATION_NAME_OVERRIDE = "@Override";

    @Override
    public TopLevelClass generateEntityClass(IntrospectedTable introspectedTable, String entityPackageName) {
        TopLevelClass topLevelClass = new TopLevelClass();

        topLevelClass.setPackageName(entityPackageName);

        topLevelClass.setVisibility(Visibility.PUBLIC);
        topLevelClass.setType(introspectedTable.generateFullyQualifiedJavaType());

        topLevelClass.addExtraNonStaticImport(TypeRegistry.FAST_JSON_JSON_OBJECT_TYPE);
        topLevelClass.addExtraNonStaticImport(TypeRegistry.JAVA_UTIL_OBJECTS_TYPE);

        topLevelClass.setSuperClass(TypeRegistry.generateSuperAbstractSerializerType(
                entityPackageName, introspectedTable.generateFullyQualifiedJavaType()));

        return topLevelClass;
    }

    @Override
    public List<Field> generateFields(IntrospectedTable introspectedTable) {
        List<Field> fields = new ArrayList<>();

        Field field;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumnsIncludeGenerated()) {
            field = new Field();

            field.setVisibility(Visibility.PRIVATE);
            field.setType(introspectedColumn.getJavaType());
            field.setName(introspectedColumn.getPropertyName());

            fields.add(field);
        }

        return fields;
    }

    @Override
    public List<Method> generateFieldsGetter(IntrospectedTable introspectedTable) {
        List<Method> allGetterMethods = new ArrayList<>();

        Method method;
        StringBuilder codeBlockBuilder = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumnsIncludeGenerated()) {
            codeBlockBuilder.setLength(0);

            method = new Method();

            method.setVisibility(Visibility.PUBLIC);
            method.setReturnType(introspectedColumn.getJavaType());
            method.setName(CodeGenerateUtil.assembleGetterMethodName(
                    introspectedColumn.getPropertyName(), introspectedColumn.getJavaType()
            ));

            codeBlockBuilder.append("return ");
            codeBlockBuilder.append(introspectedColumn.getPropertyName());
            codeBlockBuilder.append(';');
            method.addBodyLine(codeBlockBuilder.toString());

            allGetterMethods.add(method);
        }

        return allGetterMethods;
    }

    @Override
    public List<Method> generateFieldsSetter(IntrospectedTable introspectedTable) {
        List<Method> allSetterMethods = new ArrayList<>();

        Method method;
        StringBuilder codeBlockBuilder = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumnsIncludeGenerated()) {
            codeBlockBuilder.setLength(0);

            method = new Method();

            method.setVisibility(Visibility.PUBLIC);
            method.setName(CodeGenerateUtil.assembleSetterMethodName(
                    introspectedColumn.getPropertyName()
            ));

            Parameter parameter = new Parameter();
            parameter.setType(introspectedColumn.getJavaType());

            parameter.setName(introspectedColumn.getPropertyName());
            method.addParameter(parameter);


            codeBlockBuilder.append("this.");
            codeBlockBuilder.append(introspectedColumn.getPropertyName());
            codeBlockBuilder.append(" = ");
            codeBlockBuilder.append(introspectedColumn.getPropertyName());
            codeBlockBuilder.append(";");
            method.addBodyLine(codeBlockBuilder.toString());

            allSetterMethods.add(method);
        }

        return allSetterMethods;
    }

    @Override
    public Method generateConstructor(IntrospectedTable introspectedTable) {
        Method constructorMethod = new Method();

        constructorMethod.setVisibility(Visibility.PUBLIC);
        constructorMethod.setName(introspectedTable.getEntityName());
        constructorMethod.setConstructor(true);

        //调用父类的构造方法
        constructorMethod.addBodyLine("super();");

        return constructorMethod;
    }

    @Override
    public Method generateToStringMethod(IntrospectedTable introspectedTable) {
        Method method = new Method();

        method.setVisibility(Visibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("toString");

        method.addAnnotation(ANNOTATION_NAME_OVERRIDE);

        StringBuilder codeBlockBuilder = new StringBuilder();
        codeBlockBuilder.append("return ");
        codeBlockBuilder.append("\"");
        codeBlockBuilder.append("Bar");
        codeBlockBuilder.append("{\"");
        codeBlockBuilder.append(" + ");

        boolean isFirst = true;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumnsIncludeGenerated()) {
            if (isFirst) {
                isFirst = false;
            } else {
                codeBlockBuilder.append(" + ");
                codeBlockBuilder.append("\",\"");
                codeBlockBuilder.append(" + ");
            }

            codeBlockBuilder.append("\"");
            codeBlockBuilder.append(introspectedColumn.getPropertyName());
            codeBlockBuilder.append("=");
            codeBlockBuilder.append(introspectedColumn.getJavaType().equals(FullyQualifiedJavaType.getStringInstance()) ? "'" : "");
            codeBlockBuilder.append("\"");
            codeBlockBuilder.append(" + ");
            codeBlockBuilder.append(introspectedColumn.getPropertyName());

            if (introspectedColumn.getJavaType().equals(FullyQualifiedJavaType.getStringInstance())) {
                codeBlockBuilder.append(" + ");
                codeBlockBuilder.append("\"'\"");
            }
        }
        codeBlockBuilder.append(" + ");
        codeBlockBuilder.append("\"}\"");
        codeBlockBuilder.append(";");

        method.addBodyLine(codeBlockBuilder.toString());

        return method;
    }

    @Override
    public Method generateEqualsMethod(IntrospectedTable introspectedTable) {
        Method method = new Method();

        method.setVisibility(Visibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        method.setName("equals");

        method.addAnnotation(ANNOTATION_NAME_OVERRIDE);

        Parameter parameter = new Parameter();
        parameter.setType(FullyQualifiedJavaType.getObjectInstance());
        parameter.setName("obj");
        method.addParameter(parameter);

        method.addBodyLine("if (obj == this){");
        method.addBodyLine("    return true;");
        method.addBodyLine("}");
        method.addBodyLine("");
        method.addBodyLine("if (!(obj instanceof " + introspectedTable.getEntityName() + ")){");
        method.addBodyLine("    return false;");
        method.addBodyLine("}");
        method.addBodyLine("");
        method.addBodyLine("return obj.hashCode() == this.hashCode();");

        return method;
    }

    @Override
    public Method generateHashCodeMethod(IntrospectedTable introspectedTable) {
        Method method = new Method();

        method.setVisibility(Visibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("hashCode");

        method.addAnnotation(ANNOTATION_NAME_OVERRIDE);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("return Objects.hash(");
        boolean isFirst = true;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumnsIncludeGenerated()) {
            if (isFirst) {
                isFirst = false;
            } else {
                stringBuilder.append(",");
            }

            stringBuilder.append(introspectedColumn.getPropertyName());
        }

        stringBuilder.append(");");

        method.addBodyLine(stringBuilder.toString());

        return method;
    }

    @Override
    public Method generateFromJsonMethod(IntrospectedTable introspectedTable) {
        Method method = new Method();

        method.addAnnotation("@Override");

        method.setVisibility(Visibility.PUBLIC);
        method.setReturnType(introspectedTable.generateFullyQualifiedJavaType());
        method.setName("fromJson");

        Parameter parameter = new Parameter();
        parameter.setType(TypeRegistry.FAST_JSON_JSON_OBJECT_TYPE);
        parameter.setName("fromJsonObj");
        method.addParameter(parameter);

        method.addBodyLine("if (fromJsonObj == null || fromJsonObj.isEmpty()){");
        method.addBodyLine("    return null;");
        method.addBodyLine("}");
        method.addExtraEmptyLine();

        method.addBodyLine("return new " + introspectedTable.getEntityName() + ".Builder()");
        StringBuilder codeBlockBuilder = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            codeBlockBuilder.setLength(0);
            codeBlockBuilder.append("        .");
            codeBlockBuilder.append(CodeGenerateUtil.assembleWithMethod(introspectedColumn.getPropertyName()));
            codeBlockBuilder.append("(");

            codeBlockBuilder.append("fromJsonObj.get");
            codeBlockBuilder.append(introspectedColumn.getJavaType().getShortName());
            codeBlockBuilder.append("(");
            codeBlockBuilder.append(introspectedTable.getKeyClassName());
            codeBlockBuilder.append(".");
            codeBlockBuilder.append(introspectedColumn.getKeyConstantName());
            codeBlockBuilder.append(")");
            codeBlockBuilder.append(")");
            method.addBodyLine(codeBlockBuilder.toString());
        }

        for (IntrospectedColumn generatedColumn:introspectedTable.calculateGeneratedColumns()){
            codeBlockBuilder.setLength(0);
            if (generatedColumn.getJavaType().getShortName().startsWith("List")){
                codeBlockBuilder.append("        .");
                codeBlockBuilder.append(CodeGenerateUtil.assembleWithMethod(generatedColumn.getPropertyName()));
                codeBlockBuilder.append("(");
                codeBlockBuilder.append("antiSerializeBatch(");
                codeBlockBuilder.append("fromJsonObj.getJSONArray(");
                codeBlockBuilder.append(introspectedTable.getKeyClassName());
                codeBlockBuilder.append(".");
                codeBlockBuilder.append(generatedColumn.getKeyConstantName());
                codeBlockBuilder.append(")");
                codeBlockBuilder.append(",");
                codeBlockBuilder.append(generatedColumn.getJavaType().getTypeArguments().get(0).getShortName());
                codeBlockBuilder.append(".class");
                codeBlockBuilder.append(")");
                codeBlockBuilder.append(")");
            } else if (!generatedColumn.getJavaType().isPrimitive()
                    && !(generatedColumn.getJavaType() instanceof PrimitiveTypeWrapper)){
                codeBlockBuilder.append("        .");
                codeBlockBuilder.append(CodeGenerateUtil.assembleWithMethod(generatedColumn.getPropertyName()));
                codeBlockBuilder.append("(");
                codeBlockBuilder.append("antiSerialize(");
                codeBlockBuilder.append("fromJsonObj.getJSONObject(");
                codeBlockBuilder.append(introspectedTable.getKeyClassName());
                codeBlockBuilder.append(".");
                codeBlockBuilder.append(generatedColumn.getKeyConstantName());
                codeBlockBuilder.append(")");
                codeBlockBuilder.append(",");
                codeBlockBuilder.append(generatedColumn.getJavaType().getShortName());
                codeBlockBuilder.append(".class");
                codeBlockBuilder.append(")");
                codeBlockBuilder.append(")");
            } else {
                codeBlockBuilder.setLength(0);
                codeBlockBuilder.append("        .");
                codeBlockBuilder.append(CodeGenerateUtil.assembleWithMethod(generatedColumn.getPropertyName()));
                codeBlockBuilder.append("(");

                codeBlockBuilder.append("fromJsonObj.get");
                codeBlockBuilder.append(generatedColumn.getJavaType().getShortName());
                codeBlockBuilder.append("(");
                codeBlockBuilder.append(introspectedTable.getKeyClassName());
                codeBlockBuilder.append(".");
                codeBlockBuilder.append(generatedColumn.getKeyConstantName());
                codeBlockBuilder.append(")");
                codeBlockBuilder.append(")");
            }

            method.addBodyLine(codeBlockBuilder.toString());
        }
        method.addBodyLine("        .build();");

        method.addExtraNonStaticImport(introspectedTable.generateKeyDefinitionClassType());

        return method;
    }

    @Override
    public Method generateToJsonMethod(IntrospectedTable introspectedTable) {
        Method method = new Method();

        method.addAnnotation("@Override");
        method.setVisibility(Visibility.PUBLIC);
        method.setReturnType(TypeRegistry.FAST_JSON_JSON_OBJECT_TYPE);
        method.setName("toJson");

        method.addBodyLine("JSONObject toJsonObj = new JSONObject();");
        method.addExtraEmptyLine();

        StringBuilder codeBlockBuilder = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            codeBlockBuilder.setLength(0);
            codeBlockBuilder.append("toJsonObj.put(");
            codeBlockBuilder.append(introspectedTable.getKeyClassName());
            codeBlockBuilder.append(".");
            codeBlockBuilder.append(introspectedColumn.getKeyConstantName());
            codeBlockBuilder.append(",");
            codeBlockBuilder.append(introspectedColumn.getPropertyName());
            codeBlockBuilder.append(");");
            method.addBodyLine(codeBlockBuilder.toString());
        }

        for (IntrospectedColumn generatedColumn:introspectedTable.calculateGeneratedColumns()){
            codeBlockBuilder.setLength(0);
            if (generatedColumn.getJavaType().getShortName().startsWith("List")){
                codeBlockBuilder.append("toJsonObj.put(");
                codeBlockBuilder.append(introspectedTable.getKeyClassName());
                codeBlockBuilder.append(".");
                codeBlockBuilder.append(generatedColumn.getKeyConstantName());
                codeBlockBuilder.append(",");
                codeBlockBuilder.append("serializeBatch(");
                codeBlockBuilder.append(generatedColumn.getPropertyName());
                codeBlockBuilder.append(")");
                codeBlockBuilder.append(");");
            } else if (!generatedColumn.getJavaType().isPrimitive()
                    && !(generatedColumn.getJavaType() instanceof PrimitiveTypeWrapper)){
                codeBlockBuilder.append("toJsonObj.put(");
                codeBlockBuilder.append(introspectedTable.getKeyClassName());
                codeBlockBuilder.append(".");
                codeBlockBuilder.append(generatedColumn.getKeyConstantName());
                codeBlockBuilder.append(",");
                codeBlockBuilder.append("serialize(");
                codeBlockBuilder.append(generatedColumn.getPropertyName());
                codeBlockBuilder.append(")");
                codeBlockBuilder.append(");");
            } else {
                codeBlockBuilder.setLength(0);
                codeBlockBuilder.append("toJsonObj.put(");
                codeBlockBuilder.append(introspectedTable.getKeyClassName());
                codeBlockBuilder.append(".");
                codeBlockBuilder.append(generatedColumn.getKeyConstantName());
                codeBlockBuilder.append(",");
                codeBlockBuilder.append(generatedColumn.getPropertyName());
                codeBlockBuilder.append(");");
            }

            method.addBodyLine(codeBlockBuilder.toString());
        }

        method.addExtraEmptyLine();
        method.addBodyLine("return toJsonObj;");

        method.addExtraNonStaticImport(introspectedTable.generateKeyDefinitionClassType());

        return method;
    }

    @Override
    public InnerClass generateBuilder(IntrospectedTable introspectedTable) {
        InnerClass builderClass = new InnerClass();

        FullyQualifiedJavaType builderType = new FullyQualifiedJavaType("Builder");

        builderClass.setVisibility(Visibility.PUBLIC);
        builderClass.setType(builderType);

        builderClass.addTypeComment(introspectedTable.getEntityName() + " Builder");

        Field privateField;
        Method withMethod;
        Parameter withMethodParameter;
        StringBuilder codeBlockBuilder = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumnsIncludeGenerated()) {
            //builder的字段
            privateField = new Field();
            privateField.setVisibility(Visibility.PRIVATE);
            privateField.setType(introspectedColumn.getJavaType());
            privateField.setName(introspectedColumn.getPropertyName());
            privateField.addElementComment(introspectedColumn.getRemarks());
            builderClass.addField(privateField);

            //builder的withXXX方法
            withMethod = new Method();
            withMethod.setVisibility(Visibility.PUBLIC);
            withMethod.setReturnType(builderType);
            withMethod.setName(CodeGenerateUtil.assembleWithMethod(introspectedColumn.getPropertyName()));

            withMethod.addElementComment("设置" + introspectedColumn.getPropertyName());
            withMethod.addElementComment("");
            withMethod.addElementComment("@param " + introspectedColumn.getPropertyName() + " " + "待设置的新的" + introspectedColumn.getPropertyName());
            withMethod.addElementComment("");
            withMethod.addElementComment("@return 当前Builder对象");

            withMethodParameter = new Parameter();
            withMethodParameter.setType(introspectedColumn.getJavaType());
            withMethodParameter.setName(introspectedColumn.getPropertyName());
            withMethod.addParameter(withMethodParameter);

            codeBlockBuilder.setLength(0);
            codeBlockBuilder.append("this.");
            codeBlockBuilder.append(introspectedColumn.getPropertyName());
            codeBlockBuilder.append(" = ");
            codeBlockBuilder.append(introspectedColumn.getPropertyName());
            codeBlockBuilder.append(";");
            withMethod.addBodyLine(codeBlockBuilder.toString());
            withMethod.addBodyLine("return this;");

            builderClass.addMethod(withMethod);
        }

        Method buildMethod = new Method();
        buildMethod.setVisibility(Visibility.PUBLIC);
        buildMethod.setReturnType(introspectedTable.generateFullyQualifiedJavaType());
        buildMethod.setName("build");

        buildMethod.addElementComment("组建" + introspectedTable.getEntityName() + "实例");
        buildMethod.addElementComment("");
        buildMethod.addElementComment("@return " + introspectedTable.getEntityName() + "实例");


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("return ");
        stringBuilder.append("new Builder()");
        buildMethod.addBodyLine(stringBuilder.toString());

        String propertyName;
        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumnsIncludeGenerated();
        for (IntrospectedColumn introspectedColumn : allColumns) {
            propertyName = introspectedColumn.getPropertyName();

            stringBuilder.setLength(0);

            stringBuilder.append("        .");
            stringBuilder.append(CodeGenerateUtil.assembleWithMethod(propertyName));
            stringBuilder.append("(this.");
            stringBuilder.append(propertyName);
            stringBuilder.append(")");

            buildMethod.addBodyLine(stringBuilder.toString());
        }
        buildMethod.addBodyLine("        .build();");
        builderClass.addMethod(buildMethod);

        return builderClass;
    }

    @Override
    public TopLevelInterface generateSerializerInterface(String targetPackage) {
        TopLevelInterface serializerInterface = new TopLevelInterface();

        serializerInterface.setPackageName(targetPackage);

        FullyQualifiedJavaType serializerClassType = TypeRegistry.generateSerializerInterfaceType(targetPackage);
        if (serializerClassType == null) {
            serializerClassType = TypeRegistry.generateSerializerInterfaceType(targetPackage);
        }

        serializerInterface.setVisibility(Visibility.PUBLIC);
        serializerInterface.setType(serializerClassType);

        serializerInterface.addMethod(generateFromJsonInterface());
        serializerInterface.addMethod(generateToJsonInterface());

        return serializerInterface;
    }

    private Method generateToJsonInterface() {
        Method toJsonMethod = new Method();

        toJsonMethod.setInterfaceMethod(true);
        toJsonMethod.setReturnType(TypeRegistry.FAST_JSON_JSON_OBJECT_TYPE);
        toJsonMethod.setName("toJson");

        return toJsonMethod;
    }

    private Method generateFromJsonInterface() {
        Method fromJsonMethod = new Method();

        fromJsonMethod.setInterfaceMethod(true);
        fromJsonMethod.setReturnType(new FullyQualifiedJavaType("T"));
        fromJsonMethod.setName("fromJson");

        Parameter fromJsonObjParameter = new Parameter();
        fromJsonObjParameter.setType(TypeRegistry.FAST_JSON_JSON_OBJECT_TYPE);
        fromJsonObjParameter.setName("fromJsonObj");
        fromJsonMethod.addParameter(fromJsonObjParameter);

        return fromJsonMethod;
    }

    @Override
    public TopLevelClass generateSerializerAbstractClass(String targetPackage) {
        TopLevelClass abstractSerializer = new TopLevelClass();

        abstractSerializer.setPackageName(targetPackage);

        abstractSerializer.setVisibility(Visibility.PUBLIC);
        abstractSerializer.setAbstract(true);
        abstractSerializer.setType(TypeRegistry.generateAbstractSerializerClass(targetPackage));

        abstractSerializer.addSuperInterface(TypeRegistry.generateSuperSerializerType(targetPackage,FullyQualifiedJavaType.getGenericTypeInstance()));

        abstractSerializer.addMethod(generateSerializeEntityMethod(targetPackage));
        abstractSerializer.addMethod(generateAntiSerializeEntityMethod(targetPackage));
        abstractSerializer.addMethod(generateSerializeEntitiesMethod());
        abstractSerializer.addMethod(generateAntiSerializeEntitiesMethod(targetPackage));

        return abstractSerializer;
    }

    private Method generateSerializeEntityMethod(String targetPackage) {
        Method serializeEntitiesMethod = new Method();

        serializeEntitiesMethod.setVisibility(Visibility.PUBLIC);
        serializeEntitiesMethod.setStatic(true);
        serializeEntitiesMethod.setReturnType(TypeRegistry.FAST_JSON_JSON_OBJECT_TYPE);
        serializeEntitiesMethod.setName("serialize");

        Parameter parameter = new Parameter();
        parameter.setType(TypeRegistry.generateSerializerParameterType(targetPackage));
        parameter.setName("instance");
        serializeEntitiesMethod.addParameter(parameter);

        serializeEntitiesMethod.addBodyLine("if (instance == null){");
        serializeEntitiesMethod.addBodyLine("    return null;");
        serializeEntitiesMethod.addBodyLine("}");
        serializeEntitiesMethod.addExtraEmptyLine();
        serializeEntitiesMethod.addBodyLine("return instance.toJson();");

        return serializeEntitiesMethod;
    }

    private Method generateAntiSerializeEntityMethod(String targetPackage) {
        Method antiSerializeEntitiesMethod = new Method();

        antiSerializeEntitiesMethod.setVisibility(Visibility.PUBLIC);
        antiSerializeEntitiesMethod.setStatic(true);

        TypeParameter typeParameter = new TypeParameter();
        typeParameter.addExtendType(TypeRegistry.generateSerializerInterfaceType(targetPackage));
        typeParameter.setName("T");
        antiSerializeEntitiesMethod.addTypeParameter(typeParameter);

        antiSerializeEntitiesMethod.setReturnType(FullyQualifiedJavaType.getGenericTypeInstance());

        antiSerializeEntitiesMethod.setName("antiSerialize");

        Parameter fromJsonObjsParameter = new Parameter();
        fromJsonObjsParameter.setType(TypeRegistry.FAST_JSON_JSON_OBJECT_TYPE);
        fromJsonObjsParameter.setName("fromJsonObject");
        antiSerializeEntitiesMethod.addParameter(fromJsonObjsParameter);

        Parameter classTypeParameter = new Parameter();
        FullyQualifiedJavaType classType = FullyQualifiedJavaType.getClassInstance();
        classType.addTypeArgument(FullyQualifiedJavaType.getGenericTypeInstance());
        classTypeParameter.setType(classType);
        classTypeParameter.setName("cls");
        antiSerializeEntitiesMethod.addParameter(classTypeParameter);

        antiSerializeEntitiesMethod.addBodyLine("try {");
        antiSerializeEntitiesMethod.addBodyLine("    T instance = cls.newInstance();");
        antiSerializeEntitiesMethod.addBodyLine("    instance.fromJson(fromJsonObject);");
        antiSerializeEntitiesMethod.addBodyLine("    return instance;");
        antiSerializeEntitiesMethod.addBodyLine("} catch (IllegalAccessException ok) {");
        antiSerializeEntitiesMethod.addBodyLine("    //it`s safe to go");
        antiSerializeEntitiesMethod.addBodyLine("} catch (InstantiationException  ok){");
        antiSerializeEntitiesMethod.addBodyLine("    //it`s safe to go,no need to process");
        antiSerializeEntitiesMethod.addBodyLine("}");
        antiSerializeEntitiesMethod.addExtraEmptyLine();
        antiSerializeEntitiesMethod.addBodyLine("return null;");

        antiSerializeEntitiesMethod.addExtraNonStaticImport(FullyQualifiedJavaType.getNewIteratorInstance());
        antiSerializeEntitiesMethod.addExtraNonStaticImport(FullyQualifiedJavaType.getNewArrayListInstance());
        antiSerializeEntitiesMethod.addExtraNonStaticImport(TypeRegistry.FAST_JSON_JSON_OBJECT_TYPE);


        return antiSerializeEntitiesMethod;
    }

    /**
     * 生成批量序列化继承于Serializer接口实体集合的方法
     *
     * @return 序列化实体集合的方法
     */
    private Method generateSerializeEntitiesMethod() {
        Method serializeEntitiesMethod = new Method();

        serializeEntitiesMethod.setVisibility(Visibility.PUBLIC);
        serializeEntitiesMethod.setStatic(true);
        serializeEntitiesMethod.setReturnType(TypeRegistry.FAST_JSON_JSON_ARRAY_TYPE);
        serializeEntitiesMethod.setName("serializeBatch");

        Parameter parameter = new Parameter();
        FullyQualifiedJavaType parameterType = FullyQualifiedJavaType.getCollectionInstance();
        parameterType.addTypeArgument(new FullyQualifiedJavaType("? extends Serializer"));
        parameter.setType(parameterType);
        parameter.setName("instances");
        serializeEntitiesMethod.addParameter(parameter);

        serializeEntitiesMethod.addBodyLine("JSONArray toJsonObjs = new JSONArray();");
        serializeEntitiesMethod.addExtraEmptyLine();
        serializeEntitiesMethod.addBodyLine("if (instances == null){");
        serializeEntitiesMethod.addBodyLine("    return toJsonObjs;");
        serializeEntitiesMethod.addBodyLine("}");
        serializeEntitiesMethod.addExtraEmptyLine();
        serializeEntitiesMethod.addBodyLine("Iterator<? extends Serializer> iterator = instances.iterator();");
        serializeEntitiesMethod.addBodyLine("while (iterator.hasNext()){");
        serializeEntitiesMethod.addBodyLine("    toJsonObjs.add(iterator.next().toJson());");
        serializeEntitiesMethod.addBodyLine("}");
        serializeEntitiesMethod.addExtraEmptyLine();
        serializeEntitiesMethod.addBodyLine("return toJsonObjs;");

        return serializeEntitiesMethod;
    }

    /**
     * 生成批量反序列化继承于Serializer接口实体集合的方法
     *
     * @param targetPackage 实体类所在包名
     * @return 反序列化实体集合的方法
     */
    private Method generateAntiSerializeEntitiesMethod(String targetPackage) {
        Method antiSerializeEntitiesMethod = new Method();

        antiSerializeEntitiesMethod.setVisibility(Visibility.PUBLIC);
        antiSerializeEntitiesMethod.setStatic(true);

        TypeParameter typeParameter = new TypeParameter();
        typeParameter.addExtendType(TypeRegistry.generateSerializerInterfaceType(targetPackage));
        typeParameter.setName("T");
        antiSerializeEntitiesMethod.addTypeParameter(typeParameter);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        returnType.addTypeArgument(FullyQualifiedJavaType.getGenericTypeInstance());
        antiSerializeEntitiesMethod.setReturnType(returnType);

        antiSerializeEntitiesMethod.setName("antiSerializeBatch");

        Parameter fromJsonObjsParameter = new Parameter();
        fromJsonObjsParameter.setType(TypeRegistry.FAST_JSON_JSON_ARRAY_TYPE);
        fromJsonObjsParameter.setName("fromJsonObjects");
        antiSerializeEntitiesMethod.addParameter(fromJsonObjsParameter);

        Parameter classTypeParameter = new Parameter();
        FullyQualifiedJavaType classType = FullyQualifiedJavaType.getClassInstance();
        classType.addTypeArgument(FullyQualifiedJavaType.getGenericTypeInstance());
        classTypeParameter.setType(classType);
        classTypeParameter.setName("cls");
        antiSerializeEntitiesMethod.addParameter(classTypeParameter);

        antiSerializeEntitiesMethod.addBodyLine("List<T> instances = new ArrayList<>();");
        antiSerializeEntitiesMethod.addExtraEmptyLine();
        antiSerializeEntitiesMethod.addBodyLine("if (fromJsonObjects == null){");
        antiSerializeEntitiesMethod.addBodyLine("    return instances;");
        antiSerializeEntitiesMethod.addBodyLine("}");
        antiSerializeEntitiesMethod.addExtraEmptyLine();
        antiSerializeEntitiesMethod.addBodyLine("Iterator iterator = fromJsonObjects.iterator();");
        antiSerializeEntitiesMethod.addBodyLine("T instance;");
        antiSerializeEntitiesMethod.addBodyLine("while (iterator.hasNext()){");
        antiSerializeEntitiesMethod.addBodyLine("    try {");
        antiSerializeEntitiesMethod.addBodyLine("        instance = cls.newInstance();");
        antiSerializeEntitiesMethod.addBodyLine("        instance.fromJson((JSONObject) iterator.next());");
        antiSerializeEntitiesMethod.addBodyLine("        instances.add(instance);");
        antiSerializeEntitiesMethod.addBodyLine("    } catch (IllegalAccessException ok) {");
        antiSerializeEntitiesMethod.addBodyLine("        //it`s safe to go");
        antiSerializeEntitiesMethod.addBodyLine("    } catch (InstantiationException  ok){");
        antiSerializeEntitiesMethod.addBodyLine("        //it`s safe to go,no need to process");
        antiSerializeEntitiesMethod.addBodyLine("    }");
        antiSerializeEntitiesMethod.addBodyLine("}");
        antiSerializeEntitiesMethod.addExtraEmptyLine();
        antiSerializeEntitiesMethod.addBodyLine("return instances;");

        antiSerializeEntitiesMethod.addExtraNonStaticImport(FullyQualifiedJavaType.getNewIteratorInstance());
        antiSerializeEntitiesMethod.addExtraNonStaticImport(FullyQualifiedJavaType.getNewArrayListInstance());
        antiSerializeEntitiesMethod.addExtraNonStaticImport(TypeRegistry.FAST_JSON_JSON_OBJECT_TYPE);


        return antiSerializeEntitiesMethod;
    }
}
