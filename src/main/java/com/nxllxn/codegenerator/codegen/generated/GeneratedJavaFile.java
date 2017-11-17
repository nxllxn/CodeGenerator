package com.nxllxn.codegenerator.codegen.generated;

import com.nxllxn.codegenerator.codegen.java.CompilationUnit;

/**
 * 代码生成的java文件实体
 *
 * @author wenchao
 */
public class GeneratedJavaFile extends AbstractGeneratedFile {
    /**
     * TopLevelClass，TopLevelInterface，TopLevelEnum
     */
    private CompilationUnit compilationUnit;

    /**
     * Java代码文件扩展名
     */
    private static final String JAVA_CODE_FILE_EXTENSION = ".java";

    @Override
    public String getFormattedContent() {
        return compilationUnit.getFormattedContent();
    }

    @Override
    public String getFileName() {
        return compilationUnit.getType().getShortNameWithoutTypeArguments();
    }

    @Override
    public String getFileExtension() {
        return JAVA_CODE_FILE_EXTENSION;
    }

    public GeneratedJavaFile() {
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public void setCompilationUnit(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }
}
