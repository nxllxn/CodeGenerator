package com.nxllxn.codegenerator.codegen.generator.exception;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generated.GeneratedJavaFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.generator.TypeRegistry;
import com.nxllxn.codegenerator.codegen.java.*;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.ExceptionGeneratorConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常处理机制定义+全局异常处理代码生成器
 *
 * @author wenchao
 */
public class ExceptionHandlerCodeGenerator extends AbstractCodeGenerator {
    private ExceptionCodeGenerateService codeGenerateService;

    private String fileEncoding;

    private ExceptionGeneratorConfiguration configuration;

    public ExceptionHandlerCodeGenerator() {
        this.codeGenerateService = new ExceptionCodeGenerateServiceImpl();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        this.configuration = context.getExceptionGeneratorConfiguration();

        this.fileEncoding = context.getFileEncoding();
    }

    @Override
    public List<GeneratedFile> generate() {
        List<CompilationUnit> compilationUnits = new ArrayList<>();

        TypeRegistry.setExceptionPackageName(configuration.getTargetPackage());

        //Code Definitions
        TopLevelEnum codeDefinitionsEnum = codeGenerateService.generateCodeDefinitionEnum(configuration.getTargetPackage());
        compilationUnits.add(codeDefinitionsEnum);

        //Well Formed Exception
        TopLevelInterface wellFormedExceptionInterface = codeGenerateService.generateWellFormedExceptionInterface(configuration.getTargetPackage());
        compilationUnits.add(wellFormedExceptionInterface);

        //Abstract Exception
        TopLevelClass abstractExceptionClass = codeGenerateService.generateAbstractExceptionClass(configuration.getTargetPackage());
        compilationUnits.add(abstractExceptionClass);

        //sub implement
        TopLevelClass internalServerException = codeGenerateService.generateInternalServerException(configuration.getTargetPackage());
        compilationUnits.add(internalServerException);

        TopLevelClass paramLessExceptionClass = codeGenerateService.generateParamMissingException(configuration.getTargetPackage());
        compilationUnits.add(paramLessExceptionClass);

        TopLevelClass paramErrorException = codeGenerateService.generateParamErrorException(configuration.getTargetPackage());
        compilationUnits.add(paramErrorException);

        TopLevelClass emptyRequestBodyExceptionClass = codeGenerateService.generateEmptyRequestBodyExceptionClass(configuration.getTargetPackage());
        compilationUnits.add(emptyRequestBodyExceptionClass);

        //Global Exception Handler
        TopLevelClass globalExceptionHandlerClass = codeGenerateService.generateGlobalExceptionHandlerClass(configuration.getTargetPackage());
        compilationUnits.add(globalExceptionHandlerClass);

        List<GeneratedFile> generatedFiles = new ArrayList<>();

        GeneratedJavaFile generatedJavaFile;
        for (CompilationUnit compilationUnit:compilationUnits){
            ((AbstractCompilationUnit)compilationUnit).calculateNonStaticImports();

            generatedJavaFile = new GeneratedJavaFile();
            generatedJavaFile.setCompilationUnit(compilationUnit);
            generatedJavaFile.setTargetDir(configuration.getTargetDirectory());
            generatedJavaFile.setTargetPackage(configuration.getTargetPackage());
            generatedJavaFile.setFileEncoding(fileEncoding);
            generatedFiles.add(generatedJavaFile);
        }

        return generatedFiles;
    }
}
