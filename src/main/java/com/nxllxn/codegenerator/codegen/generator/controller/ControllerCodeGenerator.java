package com.nxllxn.codegenerator.codegen.generator.controller;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generated.GeneratedJavaFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.java.FullyQualifiedJavaType;
import com.nxllxn.codegenerator.codegen.java.TopLevelClass;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.ControllerGeneratorConfiguration;
import com.nxllxn.codegenerator.config.ExceptionGeneratorConfiguration;
import com.nxllxn.codegenerator.config.JavaEntityGeneratorConfiguration;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import com.nxllxn.codegenerator.utils.AssembleUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * controller层代码生成服务
 *
 * @author wenchao
 */
public class ControllerCodeGenerator extends AbstractCodeGenerator {
    private ControllerCodeGenerateService codeGenerateService;

    private ControllerGeneratorConfiguration configuration;

    private JavaEntityGeneratorConfiguration javaEntityGeneratorConfiguration;

    private ExceptionGeneratorConfiguration exceptionGeneratorConfiguration;

    private String fileEncoding;

    public ControllerCodeGenerator() {
        codeGenerateService = new ControllerCodeGenerateServiceImpl();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        configuration = context.getControllerGeneratorConfiguration();

        javaEntityGeneratorConfiguration = context.getJavaModelGeneratorConfiguration();

        exceptionGeneratorConfiguration = context.getExceptionGeneratorConfiguration();

        fileEncoding = context.getFileEncoding();
    }

    @Override
    public List<GeneratedFile> generate() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();

        GeneratedJavaFile generatedJavaFile;
        TopLevelClass controllerClass;
        for (IntrospectedTable introspectedTable:introspectedTables){
            introspectedTable.setControllerPackageName(configuration.getTargetPackage());
            //0.生成所有Controller的抽象父类AbstractController

            //1.生成controller Class
            controllerClass = codeGenerateService.generateControllerClass(introspectedTable,configuration.getTargetPackage());

            //1.0添加exception包的额外导入
            controllerClass.addExtraNonStaticImport(
                    new FullyQualifiedJavaType(exceptionGeneratorConfiguration.getTargetPackage() + AssembleUtil.PACKAGE_SEPERATOR + "*"));

            //1.1添加依赖的Service
            controllerClass.addFields(codeGenerateService.generateDependentServiceField(introspectedTable));

            //1.2添加构造器自动注入
            controllerClass.addMethod(codeGenerateService.generateAutowiredConstructor(introspectedTable));

            //2.为Controller添加一个添加一条记录的Web接口
            controllerClass.addMethod(codeGenerateService.generateAddNewRecordImpl(introspectedTable));

            //3.为Controller添加一个批量添加记录的Web接口
            controllerClass.addMethod(codeGenerateService.generateAddRecordBatchImpl(introspectedTable));

            //4.为Controller添加删除指定标识符对应记录的Web接口
            controllerClass.addMethod(codeGenerateService.generateRemoveRecordImpl(introspectedTable));

            //added:13.为Controller添加按照当前表包含的UniqueIndex删除指定记录Web接口，注意UniqueIndex可能多个
            controllerClass.addMethods(codeGenerateService.generateRemoveRecordByUniqueIndexImpl(introspectedTable));

            //5.为Controller添加按照指定标识符修改记录的Web接口
            controllerClass.addMethod(codeGenerateService.generateModifyRecordImpl(introspectedTable));

            //6.为Controller添加按照指定标识符查询指定记录的Web接口（只包含主键列和基础列）
            controllerClass.addMethod(codeGenerateService.generateQueryRecordImpl(introspectedTable));

            //7.为Controller添加按照指定标识符查询指定记录的Web接口（包括主键列，基础列，blob列）
            controllerClass.addMethod(codeGenerateService.generateQueryRecordWithBlobImpl(introspectedTable));

            //8.为Controller添加按照指定标识符查询指定记录的Web接口（仅包括blob列）
            controllerClass.addMethod(codeGenerateService.generateQueryRecordOnlyBlobImpl(introspectedTable));

            //9.为Service添加查询全部记录的Web接口（只包含主键列和基础列）
            controllerClass.addMethod(codeGenerateService.generateQueryAllRecordImpl(introspectedTable));

            //10.为Controller添加查询全部记录的Web接口，此接口提供分页
            controllerClass.addMethod(codeGenerateService.generateQueryAllRecordWithPageImpl(introspectedTable));

            //11.为Controller添加查询指定UniqueIndex对应的记录是否存在的Web接口，注意UniqueIndex可能多个
            controllerClass.addMethods(codeGenerateService.generateIfExistsImpl(introspectedTable));

            //12.按照当前列表依赖的外键依赖 利用join进行级联查询,注意外键依赖可能多个，目前实现只考虑一层的级联，可以多个表
            controllerClass.addMethods(codeGenerateService.generateBinaryCascadeQueryImpl(introspectedTable));

            //14.当前表包含的外键依赖，逆向查询当前实体基本属性的Web接口
            controllerClass.addMethods(codeGenerateService.generateSingleCascadeQueryImpl(introspectedTable));

            controllerClass.calculateNonStaticImports();

            generatedJavaFile = new GeneratedJavaFile();
            generatedJavaFile.setTargetPackage(configuration.getTargetPackage());
            generatedJavaFile.setTargetDir(configuration.getTargetDirectory());
            generatedJavaFile.setCompilationUnit(controllerClass);
            generatedJavaFile.setFileEncoding(fileEncoding);
            generatedFiles.add(generatedJavaFile);
        }

        TopLevelClass abstractControllerClass = codeGenerateService.generateAbstractControllerClass(
                configuration.getTargetPackage(),javaEntityGeneratorConfiguration.getTargetPackage()
        );
        abstractControllerClass.calculateNonStaticImports();
        generatedJavaFile = new GeneratedJavaFile();
        generatedJavaFile.setTargetPackage(configuration.getTargetPackage());
        generatedJavaFile.setTargetDir(configuration.getTargetDirectory());
        generatedJavaFile.setCompilationUnit(abstractControllerClass);
        generatedJavaFile.setFileEncoding(fileEncoding);
        generatedFiles.add(generatedJavaFile);


        return generatedFiles;
    }
}
