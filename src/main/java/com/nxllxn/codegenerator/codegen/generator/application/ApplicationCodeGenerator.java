package com.nxllxn.codegenerator.codegen.generator.application;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generated.GeneratedJavaFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.java.TopLevelClass;
import com.nxllxn.codegenerator.config.ApplicationGeneratorConfiguration;
import com.nxllxn.codegenerator.config.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring boot application code generator
 *
 * @author wenchao
 */
public class ApplicationCodeGenerator extends AbstractCodeGenerator {
    private ApplicationGeneratorConfiguration configuration;

    private String fileEncoding;

    private ApplicationCodeGenerateService codeGenerateService;


    public ApplicationCodeGenerator() {
        this.codeGenerateService = new ApplicationCodeGenerateServiceImpl();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        this.fileEncoding = context.getFileEncoding();

        this.configuration = context.getApplicationGeneratorConfiguration();
    }

    @Override
    public List<GeneratedFile> generate() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();

        //生成SpringBoot Application主类
        TopLevelClass applicationClass = codeGenerateService.generateApplicationClass(configuration.getTargetPackage());

        //添加main方法
        applicationClass.addMethod(codeGenerateService.generateMainMethod());

        applicationClass.calculateNonStaticImports();

        GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile();
        generatedJavaFile.setTargetDir(configuration.getTargetDirectory());
        generatedJavaFile.setTargetPackage(configuration.getTargetPackage());
        generatedJavaFile.setFileEncoding(fileEncoding);
        generatedJavaFile.setCompilationUnit(applicationClass);

        generatedFiles.add(generatedJavaFile);

        return generatedFiles;
    }
}
