package com.nxllxn.codegenerator.codegen.java;

import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import com.nxllxn.codegenerator.utils.RegExpUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import static com.nxllxn.codegenerator.utils.AssembleUtil.*;


/**
 * 用于描述Java方法的实体类
 *
 * @author wenchao
 */
public class Method extends AbstractElement {
    /**
     * 该方法是否为同步方法
     */
    private boolean isSynchronized;

    /**
     * 是否为默认方法
     */
    private boolean isDefault;

    /**
     * 是否为本地方法
     */
    private boolean isNative;

    /**
     * 是否为抽象方法
     */
    private boolean isAbstract;

    /**
     * 是否为构造函数
     */
    private boolean isConstructor;

    /**
     * 是否为接口方法
     */
    private boolean isInterfaceMethod;

    /**
     * 方法返回值
     */
    private FullyQualifiedJavaType returnType;

    /**
     * 方法名称
     */
    private String name;

    /**
     * 方法 type parameter
     */
    private List<TypeParameter> typeParameters;

    /**
     * 方法参数列表
     */
    private List<Parameter> parameters;

    /**
     * 方法抛出的异常
     */
    private List<FullyQualifiedJavaType> throwsExceptions;

    /**
     * 方法体
     */
    private List<String> bodyLines;

    public Method() {
        this.typeParameters = new ArrayList<>();
        this.parameters = new ArrayList<>();
        this.throwsExceptions = new ArrayList<>();
        this.bodyLines = new ArrayList<>();
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        if (StringUtils.isBlank(name)){
            return "";
        }

        return super.getFormattedContent(indentLevel);
    }

    @Override
    protected void assembleModifiers(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel) {
        super.assembleModifiers(formattedContentBuilder, codeAssembleService, indentLevel);


        formattedContentBuilder.append(codeAssembleService.assembleAbstractModifier(isAbstract));
        formattedContentBuilder.append(codeAssembleService.assembleSynchronizeModifier(isSynchronized));
        formattedContentBuilder.append(codeAssembleService.assembleDefaultModifier(isDefault));
        formattedContentBuilder.append(codeAssembleService.assembleNativeModifier(isNative));
    }

    @Override
    protected void assembleMainBlock(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel) {

        //如果不是构造函数，那么需要添加返回值类型
        if (!isConstructor){
            formattedContentBuilder.append(codeAssembleService.assembleTypeParameters(typeParameters));

            formattedContentBuilder.append(codeAssembleService.assembleReturnType(returnType));
        }

        //方法名称
        formattedContentBuilder.append(codeAssembleService.assembleFunctionName(name,isConstructor));

        //方法参数
        formattedContentBuilder.append(codeAssembleService.assembleParameters(parameters));

        //异常抛出
        formattedContentBuilder.append(codeAssembleService.assembleThrowsExceptions(throwsExceptions));

        //如果是一个接口方法，那么直接不要方法体
        if (isInterfaceMethod){
            //去掉最后面那个空格
            formattedContentBuilder.setLength(formattedContentBuilder.length() - 1);

            formattedContentBuilder.append(";");

            return;
        }

        //代码块开始标识符
        formattedContentBuilder.append(BLOCK_OPEN_IDENTIFIER);

        startNewLine(formattedContentBuilder);

        //代码块主体代码
        formattedContentBuilder.append(codeAssembleService.assembleCodeBlockBody(bodyLines,indentLevel + INDENT_LEVEL_INCREASED));

        indentWith(formattedContentBuilder,indentLevel);

        //如果代码块没有代码，那么取消开始标识符{与结束标识符}之间的空白字符
        Matcher invalidSpaceMatcher = RegExpUtil.PATTERN_FOR_CODE_BLOCK_OPEN_IDENTIFIER.matcher(formattedContentBuilder.toString());
        if (invalidSpaceMatcher.find()){
            formattedContentBuilder.setLength(formattedContentBuilder.length() - invalidSpaceMatcher.group(1).length());
        }

        //代码块结束标识符
        formattedContentBuilder.append(BLOCK_CLOSE_IDENTIFIER);
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isNative() {
        return isNative;
    }

    public void setNative(boolean aNative) {
        isNative = aNative;
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    public void setConstructor(boolean constructor) {
        isConstructor = constructor;
    }

    public FullyQualifiedJavaType getReturnType() {
        return returnType;
    }

    public void setReturnType(FullyQualifiedJavaType returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public List<FullyQualifiedJavaType> getThrowsExceptions() {
        return throwsExceptions;
    }

    public List<String> getBodyLines() {
        return bodyLines;
    }

    public void addParameter(Parameter parameter){
        this.parameters.add(parameter);
    }

    public void addParameters(List<Parameter> parameters){
        this.parameters.addAll(parameters);
    }

    public void addBodyLine(String bodyLine){
        this.bodyLines.add(bodyLine);
    }

    public void addExtraEmptyLine(){
        this.bodyLines.add("");
    }

    public void addBodyLines(List<String> bodyLines){
        this.bodyLines.addAll(bodyLines);
    }

    public void addTypeParameter(TypeParameter typeParameter){
        this.typeParameters.add(typeParameter);
    }

    public void addTypeParameters(List<TypeParameter> typeParameters){
        this.typeParameters.addAll(typeParameters);
    }

    public void addThrowsException(FullyQualifiedJavaType throwsException){
        this.throwsExceptions.add(throwsException);
    }

    public void addThrowsExceptions(List<FullyQualifiedJavaType> throwsExceptions){
        this.throwsExceptions.addAll(throwsExceptions);
    }

    public boolean isInterfaceMethod() {
        return isInterfaceMethod;
    }

    public void setInterfaceMethod(boolean interfaceMethod) {
        isInterfaceMethod = interfaceMethod;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    @Override
    public String toString() {
        return "Method{" +
                "elementComments=" + elementComments +
                ", isSynchronized=" + isSynchronized +
                ", annotations=" + annotations +
                ", isDefault=" + isDefault +
                ", visibility=" + visibility +
                ", isNative=" + isNative +
                ", isStatic=" + isStatic +
                ", isAbstract=" + isAbstract +
                ", isFinal=" + isFinal +
                ", isConstructor=" + isConstructor +
                ", extraNonStaticImports=" + extraNonStaticImports +
                ", isInterfaceMethod=" + isInterfaceMethod +
                ", returnType=" + returnType +
                ", name='" + name + '\'' +
                ", typeParameters=" + typeParameters +
                ", parameters=" + parameters +
                ", throwsExceptions=" + throwsExceptions +
                ", bodyLines=" + bodyLines +
                '}';
    }

    @Override
    public Set<FullyQualifiedJavaType> calculateNonStaticImports() {
        Set<FullyQualifiedJavaType> nonStaticImports = super.calculateNonStaticImports();

        if (returnType != null){
            nonStaticImports.add(returnType);
        }

        for (TypeParameter typeParameter:typeParameters){
            nonStaticImports.addAll(typeParameter.calculateNonStaticImports());
        }

        for (Parameter parameter:parameters){
            nonStaticImports.addAll(parameter.calculateNonStaticImports());
        }

        nonStaticImports.addAll(throwsExceptions);

        return nonStaticImports;
    }
}
