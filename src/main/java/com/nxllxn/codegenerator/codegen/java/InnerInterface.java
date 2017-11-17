package com.nxllxn.codegenerator.codegen.java;

import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import com.nxllxn.codegenerator.exception.NeedForImplementException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.nxllxn.codegenerator.utils.AssembleUtil.extraEmptyLine;
import static com.nxllxn.codegenerator.utils.AssembleUtil.removeExtraEmptyLineInCodeBlockEnd;

/**
 * 内部接口描述实体类
 *
 * @author wenchao
 */
public class InnerInterface extends AbstractInnerCompilationUnit {
    /**
     * 当前接口字段列表
     */
    protected List<Field> fields;

    /**
     * 当前接口方法列表
     */
    protected List<Method> methods;

    public InnerInterface() {
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    @Override
    public boolean isClass() {
        return false;
    }

    @Override
    public boolean isInterface() {
        return true;
    }

    @Override
    public boolean isEnumeration() {
        return false;
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        return super.getFormattedContent(indentLevel);
    }

    @Override
    protected void assembleMainBlock(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel) {
        //全部字段
        for (Field field : fields) {
            formattedContentBuilder.append(field.getFormattedContent(indentLevel));

            //额外空行
            extraEmptyLine(formattedContentBuilder);
        }

        //全部方法
        for (Method method : methods) {
            formattedContentBuilder.append(method.getFormattedContent(indentLevel));

            //额外空行
            extraEmptyLine(formattedContentBuilder);
        }

        //移除代码块最后面的空行
        removeExtraEmptyLineInCodeBlockEnd(formattedContentBuilder);
    }

    @Override
    protected void assembleTypeParameters(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService) {
        //do nothing,接口类型没有泛型参数
    }

    @Override
    protected void assembleModifiers(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel) {
        super.assembleModifiers(formattedContentBuilder, codeAssembleService, indentLevel);

        formattedContentBuilder.append(codeAssembleService.assembleElementTypeInterface());
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public void addField(Field field) {
        if (field == null){
            return;
        }

        this.fields.add(field);
    }

    public void addFields(List<Field> fields) {
        if (fields == null || fields.isEmpty()) {
            return;
        }

        this.fields.addAll(fields);
    }

    public void addMethod(Method method) {
        if (method == null){
            return;
        }

        this.methods.add(method);
    }

    public void addMethods(List<Method> methods) {
        if (methods == null || methods.isEmpty()) {
            return;
        }

        this.methods.addAll(methods);
    }

    @Override
    public String toString() {
        return "InnerInterface{" +
                "fields=" + fields +
                ", methods=" + methods +
                '}';
    }

    @Override
    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        //接口其实是可以继承接口的，此处暂不实现
        throw new NeedForImplementException();
    }

    @Override
    public void addSuperInterfaces(List<FullyQualifiedJavaType> superInterfaces) {
        //接口其实是可以继承接口的，此处暂不实现
        throw new NeedForImplementException();
    }

    @Override
    public Set<FullyQualifiedJavaType> calculateNonStaticImports() {
        Set<FullyQualifiedJavaType> nonStaticImports = super.calculateNonStaticImports();

        for (Field field:fields){
            nonStaticImports.addAll(field.calculateNonStaticImports());
        }

        for (Method method:methods){
            nonStaticImports.addAll(method.calculateNonStaticImports());
        }

        return nonStaticImports;
    }
}
