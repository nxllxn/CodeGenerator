package com.nxllxn.codegenerator.codegen.java;

import com.nxllxn.codegenerator.codegen.service.CodeAssembleService;
import com.nxllxn.codegenerator.codegen.service.TemplateBasedCodeAssembleServiceImpl;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nxllxn.codegenerator.utils.AssembleUtil.DEFAULT_INDENT_LEVEL;
import static com.nxllxn.codegenerator.utils.AssembleUtil.extraEmptyLine;

/**
 * 编译单元的抽象实现，提供一些必要的默认方法以及属性
 *
 * @author wenchao
 */
public abstract class AbstractCompilationUnit implements CompilationUnit {
    /**
     * 当前类型包名
     */
    protected String packageName;

    /**
     * 当前类型依赖的非静态导入
     */
    protected Set<FullyQualifiedJavaType> imports;

    /**
     * 当前类型依赖的静态导入
     */
    protected Set<String> staticImports;

    /**
     * 由于Java不支持多继承，所以此处利用内部属性的方式来进行组织，这也是《Effective Java》推荐的方式
     *
     * 此属性包含了内部InnerXXX的实现，比如TopLevelClass包含一个InnerClass，TopLevelInterface包含一个InnerInterface;
     */
    protected AbstractInnerCompilationUnit innerCompilationUnit;

    public AbstractCompilationUnit() {
        this.imports = new HashSet<>();
        this.staticImports = new HashSet<>();
    }

    @Override
    public void addImport(FullyQualifiedJavaType nonStaticImport) {
        this.imports.add(nonStaticImport);
    }

    @Override
    public void addImports(Set<FullyQualifiedJavaType> nonStaticImports) {
        if (nonStaticImports == null){
            return;
        }

        this.imports.addAll(nonStaticImports);
    }

    @Override
    public void addStaticImport(String staticImport) {
        this.staticImports.add(staticImport);
    }

    @Override
    public void addStaticImports(Set<String> staticImports) {
        if (staticImports == null){
            return;
        }

        this.staticImports.addAll(staticImports);
    }

    @Override
    public Set<FullyQualifiedJavaType> getImports() {
        return imports;
    }

    @Override
    public Set<String> getStaticImports() {
        return staticImports;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getFormattedContent() {
        return getFormattedContent(DEFAULT_INDENT_LEVEL);
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        StringBuilder formattedContentBuilder = new StringBuilder();

        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        if (!StringUtils.isBlank(packageName)){
            //包名称
            formattedContentBuilder.append(codeAssembleService.assemblePackage(packageName));

            //额外空行
            extraEmptyLine(formattedContentBuilder);
        }

        Set<FullyQualifiedJavaType> nonStaticImports = new HashSet<>();
        if (imports != null && !imports.isEmpty()){
            //非静态导入,如果需要显式导入且待导入类型package不是当前类型的父package，那么导入指定类型
            for (FullyQualifiedJavaType nonStaticImport:imports){
                if (nonStaticImport.isExplicitlyImported()){
                    if (StringUtils.isBlank(innerCompilationUnit.getType().getPackageName())
                            || StringUtils.isBlank(nonStaticImport.getPackageName())
                            || innerCompilationUnit.getType().getPackageName().equals(nonStaticImport.getPackageName())){
                        continue;
                    }

                    nonStaticImports.add(nonStaticImport);
                }

                for (FullyQualifiedJavaType typeArgument:nonStaticImport.getTypeArguments()){
                    importTypeArgumentHelper(typeArgument, nonStaticImports);
                }
            }

            for (FullyQualifiedJavaType nonStaticImport:nonStaticImports){
                formattedContentBuilder.append(codeAssembleService.assembleNonStaticImport(nonStaticImport.getFullyQualifiedNameWithoutTypeParameters()));
            }

            //额外空行
            extraEmptyLine(formattedContentBuilder);
        }

        if (staticImports != null && !staticImports.isEmpty()){
            for (String staticImport:staticImports){
                formattedContentBuilder.append(codeAssembleService.assembleStaticImport(staticImport));
            }

            //额外空行
            extraEmptyLine(formattedContentBuilder);
        }

        //从innerXXX（AbstractInnerCompilationUnit）中继承过来的内容
        formattedContentBuilder.append(innerCompilationUnit.getFormattedContent(indentLevel));

        return formattedContentBuilder.toString();
    }

    private void importTypeArgumentHelper(FullyQualifiedJavaType typeArgument,Set<FullyQualifiedJavaType> nonStaticImports){
        if (typeArgument == null){
            return;
        }

        if (typeArgument.isExplicitlyImported()){
            if (!StringUtils.isBlank(innerCompilationUnit.getType().getPackageName())
                    && !StringUtils.isBlank(typeArgument.getPackageName())
                    && !innerCompilationUnit.getType().getPackageName().equals(typeArgument.getPackageName())){

                nonStaticImports.add(typeArgument);
            }
        }

        for (FullyQualifiedJavaType subTypeArgument:typeArgument.getTypeArguments()){
            importTypeArgumentHelper(subTypeArgument, nonStaticImports);
        }
    }

    public void setTypeComments(List<String> typeComments) {
        this.innerCompilationUnit.typeComments = typeComments;
    }

    public void setTypeAnnotations(Set<String> typeAnnotations) {
        this.innerCompilationUnit.typeAnnotations = typeAnnotations;
    }

    public void setVisibility(Visibility visibility) {
        this.innerCompilationUnit.visibility = visibility;
    }

    public void setStatic(boolean aStatic) {
        this.innerCompilationUnit.isStatic = aStatic;
    }

    public void setFinal(boolean aFinal) {
        this.innerCompilationUnit.isFinal = aFinal;
    }

    public void setType(FullyQualifiedJavaType type) {
        this.innerCompilationUnit.type = type;
    }

    public void setSuperClass(FullyQualifiedJavaType superClass) {
        this.innerCompilationUnit.superClass = superClass;
    }

    public void setSuperInterfaces(Set<FullyQualifiedJavaType> superInterfaces) {
        this.innerCompilationUnit.superInterfaces = superInterfaces;
    }

    public void calculateNonStaticImports(){
        this.imports.addAll(innerCompilationUnit.calculateNonStaticImports());
    }
}
