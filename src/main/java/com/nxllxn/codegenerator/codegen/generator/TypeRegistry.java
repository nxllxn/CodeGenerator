package com.nxllxn.codegenerator.codegen.generator;

import com.nxllxn.codegenerator.codegen.java.FullyQualifiedJavaType;
import com.nxllxn.codegenerator.utils.AssembleUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 一些类型的全局定义，方便全局重复引用，此外可以动态更新全部的引用
 *
 * @author wenchao
 */
public class TypeRegistry {
    /**
     * 序列化接口全限定类型
     */
    private static FullyQualifiedJavaType SERIALIZER_INTERFACE_TYPE = null;

    /**
     * 序列化接口作为参数时的类型
     */
    private static FullyQualifiedJavaType SERIALIZER_PARAMETER_TYPE = null;

    public static final String SERIALIZER_CLASS_NAME = "Serializer";

    /**
     * 序列化接口抽象实现类全限定类型
     */
    private static FullyQualifiedJavaType ABSTRACT_SERIALIZER_CLASS_TYPE = null;

    public static final String ABSTRACT_SERIALIZER_CLASS_NAME = "AbstractSerializer";

    public static FullyQualifiedJavaType FAST_JSON_JSON_TYPE = new FullyQualifiedJavaType("com.alibaba.fastjson.JSON");

    public static FullyQualifiedJavaType FAST_JSON_JSON_OBJECT_TYPE = new FullyQualifiedJavaType("com.alibaba.fastjson.JSONObject");

    public static FullyQualifiedJavaType FAST_JSON_JSON_ARRAY_TYPE = new FullyQualifiedJavaType("com.alibaba.fastjson.JSONArray");

    public static FullyQualifiedJavaType FAST_JSON_SERIALIZER_FEATURE_TYPE = new FullyQualifiedJavaType("com.alibaba.fastjson.serializer.SerializerFeature");

    public static FullyQualifiedJavaType JAVA_UTIL_OBJECTS_TYPE = new FullyQualifiedJavaType("java.util.Objects");

    public static FullyQualifiedJavaType BASE_EXCEPTION_TYPE = new FullyQualifiedJavaType("java.lang.Exception");

    public static FullyQualifiedJavaType RUNTIME_EXCEPTION_TYPE = new FullyQualifiedJavaType("java.lang.RuntimeException");

    private static String EXCEPTION_PACKAGE_NAME;
    private static FullyQualifiedJavaType CODE_ENUM_TYPE;
    private static final String CODE_ENUM_NAME = "Code";
    private static FullyQualifiedJavaType WELL_FORMED_EXCEPTION_TYPE;
    private static final String CODE_EXCEPTION_CLASS_NAME = "WellFormedException";
    private static FullyQualifiedJavaType ABSTRACT_EXCEPTION_TYPE;
    private static final String ABSTRACT_EXCEPTION_CLASS_NAME = "AbstractException";
    private static FullyQualifiedJavaType INTERNAL_SERVER_EXCEPTION_TYPE;
    public static final String INTERNAL_SERVER_EXCEPTION_CLASS_NAME = "InternalServerException";
    private static FullyQualifiedJavaType PARAM_MISSING_TYPE;
    public static final String PARAM_MISSING_CLASS_NAME = "ParamMissingException";
    private static FullyQualifiedJavaType PARAM_ERROR_TYPE;
    public static final String PARAM_ERROR_CLASS_NAME = "ParamErrorException";

    public static void setExceptionPackageName(String exceptionPackageName) {
        if(StringUtils.isBlank(exceptionPackageName)){
            return;
        }

        EXCEPTION_PACKAGE_NAME = exceptionPackageName;

        if (CODE_ENUM_TYPE != null){
            CODE_ENUM_TYPE.setPackageName(exceptionPackageName);
        }

        if (WELL_FORMED_EXCEPTION_TYPE != null){
            WELL_FORMED_EXCEPTION_TYPE.setPackageName(exceptionPackageName);
        }

        if (ABSTRACT_EXCEPTION_TYPE != null){
            ABSTRACT_EXCEPTION_TYPE.setPackageName(exceptionPackageName);
        }

        if (INTERNAL_SERVER_EXCEPTION_TYPE != null){
            INTERNAL_SERVER_EXCEPTION_TYPE.setPackageName(exceptionPackageName);
        }

        if (PARAM_MISSING_TYPE != null){
            PARAM_MISSING_TYPE.setPackageName(exceptionPackageName);
        }

        if (PARAM_ERROR_TYPE != null){
            PARAM_ERROR_TYPE.setPackageName(exceptionPackageName);
        }
    }

    public static FullyQualifiedJavaType generateCodeEnumType(){
        CODE_ENUM_TYPE =  generateTypeByPackageNameAndClassName(EXCEPTION_PACKAGE_NAME, CODE_ENUM_NAME, CODE_ENUM_TYPE);

        return CODE_ENUM_TYPE;
    }

    public static FullyQualifiedJavaType generateWellFormedExceptionType() {
        WELL_FORMED_EXCEPTION_TYPE = generateTypeByPackageNameAndClassName(EXCEPTION_PACKAGE_NAME, CODE_EXCEPTION_CLASS_NAME, WELL_FORMED_EXCEPTION_TYPE);

        return WELL_FORMED_EXCEPTION_TYPE;
    }

    public static FullyQualifiedJavaType generateAbstractExceptionType() {
        ABSTRACT_EXCEPTION_TYPE = generateTypeByPackageNameAndClassName(EXCEPTION_PACKAGE_NAME, ABSTRACT_EXCEPTION_CLASS_NAME, ABSTRACT_EXCEPTION_TYPE);

        return ABSTRACT_EXCEPTION_TYPE;
    }

    public static FullyQualifiedJavaType generateInternalServerExceptionType() {
        INTERNAL_SERVER_EXCEPTION_TYPE = generateTypeByPackageNameAndClassName(EXCEPTION_PACKAGE_NAME, INTERNAL_SERVER_EXCEPTION_CLASS_NAME, INTERNAL_SERVER_EXCEPTION_TYPE);

        return INTERNAL_SERVER_EXCEPTION_TYPE;
    }

    public static FullyQualifiedJavaType generateParamMissingExceptionType() {
        PARAM_MISSING_TYPE = generateTypeByPackageNameAndClassName(EXCEPTION_PACKAGE_NAME, PARAM_MISSING_CLASS_NAME, PARAM_MISSING_TYPE);

        return PARAM_MISSING_TYPE;
    }

    public static FullyQualifiedJavaType generateParamErrorExceptionType() {
        PARAM_ERROR_TYPE = generateTypeByPackageNameAndClassName(EXCEPTION_PACKAGE_NAME, PARAM_ERROR_CLASS_NAME, PARAM_ERROR_TYPE);

        return PARAM_ERROR_TYPE;
    }

    private static FullyQualifiedJavaType generateTypeByPackageNameAndClassName(String packageName, String className, FullyQualifiedJavaType generatedType) {
        if (generatedType == null) {
            if (packageName == null) {
                generatedType = new FullyQualifiedJavaType(className);
            } else {
                generatedType = new FullyQualifiedJavaType(packageName + AssembleUtil.PACKAGE_SEPERATOR + className);
            }
        }

        return generatedType;
    }

    public static FullyQualifiedJavaType generateSerializerInterfaceType(String packageName) {
        if (SERIALIZER_INTERFACE_TYPE == null) {
            SERIALIZER_INTERFACE_TYPE = new FullyQualifiedJavaType(packageName + AssembleUtil.PACKAGE_SEPERATOR + SERIALIZER_CLASS_NAME);

            SERIALIZER_INTERFACE_TYPE.addTypeArgument(FullyQualifiedJavaType.getGenericTypeInstance());
        }

        return SERIALIZER_INTERFACE_TYPE;
    }

    public static FullyQualifiedJavaType generateSerializerParameterType(String packageName) {
        if (SERIALIZER_PARAMETER_TYPE == null) {
            SERIALIZER_PARAMETER_TYPE = new FullyQualifiedJavaType(packageName + AssembleUtil.PACKAGE_SEPERATOR + SERIALIZER_CLASS_NAME);
        }

        return SERIALIZER_PARAMETER_TYPE;
    }

    public static FullyQualifiedJavaType generateSuperSerializerType(String entityPackageName, FullyQualifiedJavaType entityType) {
        FullyQualifiedJavaType superSerializerType = new FullyQualifiedJavaType(
                entityPackageName + AssembleUtil.PACKAGE_SEPERATOR + "Serializer");
        superSerializerType.addTypeArgument(entityType);

        return superSerializerType;
    }

    public static FullyQualifiedJavaType generateSuperAbstractSerializerType(String entityPackageName, FullyQualifiedJavaType entityType) {
        FullyQualifiedJavaType superSerializerType = new FullyQualifiedJavaType(
                entityPackageName + AssembleUtil.PACKAGE_SEPERATOR + "AbstractSerializer");
        superSerializerType.addTypeArgument(entityType);

        return superSerializerType;
    }

    public static FullyQualifiedJavaType generateAbstractSerializerClass(String packageName) {
        if (ABSTRACT_SERIALIZER_CLASS_TYPE == null) {
            ABSTRACT_SERIALIZER_CLASS_TYPE = new FullyQualifiedJavaType(packageName + AssembleUtil.PACKAGE_SEPERATOR + ABSTRACT_SERIALIZER_CLASS_NAME);

            ABSTRACT_SERIALIZER_CLASS_TYPE.addTypeArgument(FullyQualifiedJavaType.getGenericTypeInstance());
        }

        return ABSTRACT_SERIALIZER_CLASS_TYPE;
    }
}
