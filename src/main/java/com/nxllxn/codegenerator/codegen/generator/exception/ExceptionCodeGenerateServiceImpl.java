package com.nxllxn.codegenerator.codegen.generator.exception;

import com.nxllxn.codegenerator.codegen.generator.TypeRegistry;
import com.nxllxn.codegenerator.codegen.java.*;
import com.nxllxn.codegenerator.utils.AssembleUtil;

/**
 * 异常相关代码代码生成服务实现类
 *
 * @author wenchao
 */
public class ExceptionCodeGenerateServiceImpl implements ExceptionCodeGenerateService {
    private static final String[][] ENUM_CONSTANTS = new String[][]{
            {"CODE_00","00","请求正常"},
            {"CODE_100001","100001","服务器内部异常"},
            {"CODE_100002","100002","缺少参数"},
            {"CODE_100003","100003","参数类型错误"}
    };

    @Override
    public TopLevelEnum generateCodeDefinitionEnum(String targetPackage) {
        TopLevelEnum codeDefinitionEnum = new TopLevelEnum();

        codeDefinitionEnum.setPackageName(targetPackage);
        codeDefinitionEnum.setVisibility(Visibility.PUBLIC);
        codeDefinitionEnum.setType(TypeRegistry.generateCodeEnumType());

        EnumConstant enumConstant;
        for (String[] enumConstantDefinition:ENUM_CONSTANTS){
            enumConstant = new EnumConstant();
            enumConstant.setEnumConstantName(enumConstantDefinition[0]);
            codeDefinitionEnum.addEnumConstant(enumConstant);
        }

        Method toStringMethod = new Method();
        toStringMethod.setVisibility(Visibility.PUBLIC);
        toStringMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        toStringMethod.setName("toString");
        toStringMethod.addBodyLine("return super.toString().replace(\"CODE_\",\"\");");
        codeDefinitionEnum.addMethod(toStringMethod);

        return codeDefinitionEnum;
    }

    @Override
    public TopLevelInterface generateWellFormedExceptionInterface(String targetPackage) {
        TopLevelInterface wellFormedInterface = new TopLevelInterface();
        wellFormedInterface.setPackageName(targetPackage);
        wellFormedInterface.setVisibility(Visibility.PUBLIC);
        wellFormedInterface.setType(TypeRegistry.generateWellFormedExceptionType());

        Method method = new Method();
        method.setInterfaceMethod(true);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("getResponseCode");
        wellFormedInterface.addMethod(method);

        method = new Method();
        method.setInterfaceMethod(true);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("getResponseMsg");
        wellFormedInterface.addMethod(method);

        method = new Method();
        method.setInterfaceMethod(true);
        method.setReturnType(TypeRegistry.generateWellFormedExceptionType());
        method.setName("fromPrimitiveException");

        Parameter parameter = new Parameter();
        parameter.setType(new FullyQualifiedJavaType("java.lang.Exception"));
        parameter.setName("primitiveException");
        method.addParameter(parameter);

        wellFormedInterface.addMethod(method);

        return wellFormedInterface;
    }

    @Override
    public TopLevelClass generateAbstractExceptionClass(String targetPackage) {
        TopLevelClass abstractExceptionClass = new TopLevelClass();

        abstractExceptionClass.setPackageName(targetPackage);

        abstractExceptionClass.setVisibility(Visibility.PUBLIC);
        abstractExceptionClass.setType(TypeRegistry.generateAbstractExceptionType());
        abstractExceptionClass.setSuperClass(TypeRegistry.RUNTIME_EXCEPTION_TYPE);
        abstractExceptionClass.addSuperInterface(TypeRegistry.generateWellFormedExceptionType());

        Field field = new Field();
        field.setVisibility(Visibility.PRIVATE);
        field.setType(TypeRegistry.generateCodeEnumType());
        field.setName("responseCode");
        abstractExceptionClass.addField(field);

        field = new Field();
        field.setVisibility(Visibility.PRIVATE);
        field.setType(FullyQualifiedJavaType.getStringInstance());
        field.setName("responseMsg");
        abstractExceptionClass.addField(field);

        //无参构造函数
        Method constructorMethod = new Method();
        constructorMethod.setVisibility(Visibility.PUBLIC);
        constructorMethod.setConstructor(true);
        constructorMethod.setName("AbstractException");
        abstractExceptionClass.addMethod(constructorMethod);

        //带参构造函数
        constructorMethod = new Method();
        constructorMethod.setVisibility(Visibility.PUBLIC);
        constructorMethod.setConstructor(true);
        constructorMethod.setName("AbstractException");
        Parameter responseCodeParameter = new Parameter();
        responseCodeParameter.setType(TypeRegistry.generateCodeEnumType());
        responseCodeParameter.setName("responseCode");
        constructorMethod.addParameter(responseCodeParameter);
        Parameter responseMsgParameter = new Parameter();
        responseMsgParameter.setType(FullyQualifiedJavaType.getStringInstance());
        responseMsgParameter.setName("responseMsg");
        constructorMethod.addParameter(responseMsgParameter);
        constructorMethod.addBodyLine("this.responseCode = responseCode;");
        constructorMethod.addBodyLine("this.responseMsg = responseMsg;");
        abstractExceptionClass.addMethod(constructorMethod);

        Method method = new Method();
        method.setVisibility(Visibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("getResponseCode");
        method.addBodyLine("return this.responseCode.toString();");
        abstractExceptionClass.addMethod(method);

        method = new Method();
        method.setVisibility(Visibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("getResponseMsg");
        method.addBodyLine("return this.responseMsg;");
        abstractExceptionClass.addMethod(method);

        method = new Method();
        method.setVisibility(Visibility.PUBLIC);
        method.setReturnType(TypeRegistry.generateWellFormedExceptionType());
        method.setName("fromPrimitiveException");
        Parameter parameter = new Parameter();
        parameter.setType(TypeRegistry.BASE_EXCEPTION_TYPE);
        parameter.setName("primitiveException");
        method.addParameter(parameter);

        method.addBodyLine("if(primitiveException instanceof WellFormedException){");
        method.addBodyLine("    return (WellFormedException)primitiveException;");
        method.addBodyLine("} else if (primitiveException instanceof HttpMessageNotReadableException){");
        method.addBodyLine("    return new EmptyRequestBodyException();");
        method.addBodyLine("} else if (primitiveException instanceof MissingServletRequestParameterException){");
        method.addBodyLine("    return new ParamMissingException(primitiveException.getMessage());");
        method.addBodyLine("} else if (primitiveException instanceof HttpRequestMethodNotSupportedException){");
        method.addBodyLine("    return new ParamErrorException(primitiveException.getMessage());");
        method.addBodyLine("} else if (primitiveException instanceof MethodArgumentTypeMismatchException){");
        method.addBodyLine("    return new ParamErrorException(\"抱歉，参数类型不匹配！\");");
        method.addBodyLine("} else if (primitiveException instanceof NullPointerException){");
        method.addBodyLine("    return new ParamErrorException(\"抱歉，未查询到数据！\");");
        method.addBodyLine("} else if (primitiveException instanceof JSONException){");
        method.addBodyLine("    return new ParamErrorException(\"抱歉，请求参数不是一个有效的Json字符串！\");");
        method.addBodyLine("} else if (primitiveException instanceof NumberFormatException){");
        method.addBodyLine("    return new ParamErrorException(\"抱歉，请求参数不是一个有效的数值属性！\");");
        method.addBodyLine("} else {");
        method.addBodyLine("    return new InternalServerException();");
        method.addBodyLine("}");

        //添加额外导入
        method.addExtraNonStaticImport(new FullyQualifiedJavaType("com.alibaba.fastjson.JSONException"));
        method.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.http.converter.HttpMessageNotReadableException"));
        method.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.web.HttpRequestMethodNotSupportedException"));
        method.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.web.bind.MissingServletRequestParameterException"));
        method.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.web.method.annotation.MethodArgumentTypeMismatchException"));

        abstractExceptionClass.addMethod(method);

        return abstractExceptionClass;
    }

    @Override
    public TopLevelClass generateInternalServerException(String targetPackage) {
        String defaultCodeName = "Code.CODE_100001";
        String defaultMsg = "\"系统开小差了，请稍后再试!\"";

        return generateExceptionClass(targetPackage, TypeRegistry.INTERNAL_SERVER_EXCEPTION_CLASS_NAME, defaultCodeName, defaultMsg);
    }

    @Override
    public TopLevelClass generateParamMissingException(String targetPackage) {
        String defaultCodeName = "Code.CODE_100002";
        String defaultMsg = "\"抱歉，请求参数缺失!\"";

        return generateExceptionClass(targetPackage, TypeRegistry.PARAM_MISSING_CLASS_NAME, defaultCodeName, defaultMsg);
    }

    @Override
    public TopLevelClass generateParamErrorException(String targetPackage) {
        String defaultCodeName = "Code.CODE_100003";
        String defaultMsg = "\"抱歉，参数类型不匹配！\"";

        return generateExceptionClass(targetPackage, TypeRegistry.PARAM_ERROR_CLASS_NAME, defaultCodeName, defaultMsg);
    }

    @Override
    public TopLevelClass generateEmptyRequestBodyExceptionClass(String targetPackage) {
        String className = "EmptyRequestBodyException";
        String defaultCodeName = "Code.CODE_100002";
        String defaultMsg = "\"抱歉，当前请求请求Body为空！\"";

        return generateExceptionClass(targetPackage, className, defaultCodeName, defaultMsg);
    }

    private TopLevelClass generateExceptionClass(String targetPackage, String className, String defaultCodeName, String defaultMsg) {
        TopLevelClass internalServerExceptionClass = new TopLevelClass();

        internalServerExceptionClass.setPackageName(targetPackage);

        internalServerExceptionClass.setVisibility(Visibility.PUBLIC);
        internalServerExceptionClass.setType(new FullyQualifiedJavaType(targetPackage + AssembleUtil.PACKAGE_SEPERATOR + className));
        internalServerExceptionClass.setSuperClass(TypeRegistry.generateAbstractExceptionType());

        Parameter codeParameter = new Parameter();
        codeParameter.setType(TypeRegistry.generateCodeEnumType());
        codeParameter.setName("code");

        Parameter msgParameter = new Parameter();
        msgParameter.setType(FullyQualifiedJavaType.getStringInstance());
        msgParameter.setName("msg");

        Method constructorMethod = new Method();
        constructorMethod.setVisibility(Visibility.PUBLIC);
        constructorMethod.setName(className);
        constructorMethod.setConstructor(true);
        constructorMethod.addBodyLine("this(" + defaultMsg + ");");
        internalServerExceptionClass.addMethod(constructorMethod);

        constructorMethod = new Method();
        constructorMethod.setVisibility(Visibility.PUBLIC);
        constructorMethod.setName(className);
        constructorMethod.setConstructor(true);
        constructorMethod.addParameter(msgParameter);
        constructorMethod.addBodyLine("this(" + defaultCodeName + ",msg);");
        internalServerExceptionClass.addMethod(constructorMethod);

        constructorMethod = new Method();
        constructorMethod.setVisibility(Visibility.PUBLIC);
        constructorMethod.setName(className);
        constructorMethod.setConstructor(true);
        constructorMethod.addParameter(codeParameter);
        constructorMethod.addParameter(msgParameter);
        constructorMethod.addBodyLine("super(code,msg);");
        internalServerExceptionClass.addMethod(constructorMethod);
        return internalServerExceptionClass;
    }

    @Override
    public TopLevelClass generateGlobalExceptionHandlerClass(String targetPackage) {
        String className = "GlobalExceptionHandler";

        TopLevelClass globalExceptionHandlerClass = new TopLevelClass();

        globalExceptionHandlerClass.setPackageName(targetPackage);

        globalExceptionHandlerClass.setVisibility(Visibility.PUBLIC);
        globalExceptionHandlerClass.setType(new FullyQualifiedJavaType(targetPackage + AssembleUtil.PACKAGE_SEPERATOR + className));
        globalExceptionHandlerClass.addAnnotation("@RestControllerAdvice");

        Field loggerField = new Field();
        loggerField.setVisibility(Visibility.PRIVATE);
        loggerField.setType(new FullyQualifiedJavaType("org.slf4j.Logger"));
        //添加一个额外的依赖LoggerFactory
        loggerField.addExtraNonStaticImport(new FullyQualifiedJavaType("org.slf4j.LoggerFactory"));
        loggerField.setName("logger");
        loggerField.setInitStrValue("LoggerFactory.getLogger(GlobalExceptionHandler.class)");
        globalExceptionHandlerClass.addField(loggerField);

        Method exceptionHandleMethod = new Method();
        exceptionHandleMethod.setVisibility(Visibility.PUBLIC);
        exceptionHandleMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        exceptionHandleMethod.setName("exceptionHandler");
        exceptionHandleMethod.addAnnotation("@ExceptionHandler");

        Parameter parameter = new Parameter();
        parameter.setType(new FullyQualifiedJavaType("java.lang.Exception"));
        parameter.setName("exception");
        exceptionHandleMethod.addParameter(parameter);

        StringBuilder codeLineBuilder = new StringBuilder();
        codeLineBuilder.append("WellFormedException wellFormedException;");
        exceptionHandleMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("if(exception instanceof WellFormedException){");
        exceptionHandleMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    ");
        codeLineBuilder.append("wellFormedException = (WellFormedException)exception;");
        exceptionHandleMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("} else {");
        exceptionHandleMethod.addBodyLine(codeLineBuilder.toString());

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("    ");
        codeLineBuilder.append("wellFormedException = new AbstractException().fromPrimitiveException(exception);");
        exceptionHandleMethod.addBodyLine(codeLineBuilder.toString());

        exceptionHandleMethod.addExtraEmptyLine();
        exceptionHandleMethod.addBodyLine("    logger.error(\"{}\",wellFormedException.getResponseMsg());");

        codeLineBuilder.setLength(0);
        codeLineBuilder.append("}");
        exceptionHandleMethod.addBodyLine(codeLineBuilder.toString());

        exceptionHandleMethod.addExtraEmptyLine();

        exceptionHandleMethod.addBodyLine("JSONObject resultJsonObj = new JSONObject();");
        exceptionHandleMethod.addBodyLine("resultJsonObj.put(\"response_code\",wellFormedException.getResponseCode());");
        exceptionHandleMethod.addBodyLine("resultJsonObj.put(\"response_msg\",wellFormedException.getResponseMsg());");
        exceptionHandleMethod.addBodyLine("return JSONObject.toJSONString(resultJsonObj,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);");

        exceptionHandleMethod.addExtraNonStaticImport(TypeRegistry.FAST_JSON_JSON_OBJECT_TYPE);
        exceptionHandleMethod.addExtraNonStaticImport(TypeRegistry.FAST_JSON_SERIALIZER_FEATURE_TYPE);
        exceptionHandleMethod.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.ExceptionHandler"));
        exceptionHandleMethod.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RestControllerAdvice"));

        globalExceptionHandlerClass.addMethod(exceptionHandleMethod);

        return globalExceptionHandlerClass;
    }
}
