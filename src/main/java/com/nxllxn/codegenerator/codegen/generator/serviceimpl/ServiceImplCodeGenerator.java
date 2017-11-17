package com.nxllxn.codegenerator.codegen.generator.serviceimpl;


import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generated.GeneratedJavaFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.java.TopLevelClass;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.ServiceImplGeneratorConfiguration;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.ArrayList;
import java.util.List;

/**
 * servie实现层带代码生成器
 */
public class ServiceImplCodeGenerator extends AbstractCodeGenerator {
    /**
     * service实现层代码生成服务
     */
    private ServiceImplCodeGenerateService codeGenerateService;

    /**
     * service实现层代码生成相关配置
     */
    private ServiceImplGeneratorConfiguration configuration;

    /**
     * 文件编码
     */
    private String fileEncoding;

    public ServiceImplCodeGenerator() {
        codeGenerateService = new ServiceImplCodeGenerateServiceImpl();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        configuration = context.getServiceImplGeneratorConfiguration();

        fileEncoding = context.getFileEncoding();
    }

    @Override
    public List<GeneratedFile> generate() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();

        TopLevelClass seviceImplClass;
        GeneratedJavaFile generatedJavaFile;
        for (IntrospectedTable introspectedTable:introspectedTables){
            introspectedTable.setServiceImplPackageName(configuration.getTargetPackage());

            //1.生成service接口实现类
            seviceImplClass = codeGenerateService.generateServiceImplClass(introspectedTable,configuration.getTargetPackage());

            //1.1添加依赖的Mapper
            seviceImplClass.addFields(codeGenerateService.generateDependentMapperField(introspectedTable));

            //1.2添加构造器自动注入
            seviceImplClass.addMethod(codeGenerateService.generateAutowiredConstructor(introspectedTable));

            //2.为ServiceImpl添加一个添加一条记录的接口实现
            seviceImplClass.addMethod(codeGenerateService.generateAddNewRecordImpl(introspectedTable));

            //3.为ServiceImpl添加一个批量添加记录的接口实现
            seviceImplClass.addMethod(codeGenerateService.generateAddRecordBatchImpl(introspectedTable));

            //4.为ServiceImpl添加删除指定标识符对应记录的接口实现
            seviceImplClass.addMethod(codeGenerateService.generateRemoveRecordImpl(introspectedTable));

            //added:13.为ServiceImpl添加按照当前表包含的UniqueIndex删除指定记录接口实现，注意UniqueIndex可能多个
            seviceImplClass.addMethods(codeGenerateService.generateRemoveRecordByUniqueIndexImpl(introspectedTable));

            //5.为ServiceImpl添加按照指定标识符修改记录的接口实现
            seviceImplClass.addMethod(codeGenerateService.generateModifyRecordImpl(introspectedTable));

            //6.为ServiceImpl添加按照指定标识符查询指定记录的接口实现（只包含主键列和基础列）
            seviceImplClass.addMethod(codeGenerateService.generateQueryRecordImpl(introspectedTable));

            //7.为ServiceImpl添加按照指定标识符查询指定记录的接口实现（包括主键列，基础列，blob列）
            seviceImplClass.addMethod(codeGenerateService.generateQueryRecordWithBlobImpl(introspectedTable));

            //8.为ServiceImpl添加按照指定标识符查询指定记录的接口实现（仅包括blob列）
            seviceImplClass.addMethod(codeGenerateService.generateQueryRecordOnlyBlobImpl(introspectedTable));

            //9.为Service添加查询全部记录的接口实现（只包含主键列和基础列）
            seviceImplClass.addMethod(codeGenerateService.generateQueryAllRecordImpl(introspectedTable));

            //10.为ServiceImpl添加查询全部记录的接口实现，此接口提供分页
            seviceImplClass.addMethod(codeGenerateService.generateQueryAllRecordWithPageImpl(introspectedTable));

            //11.为ServiceImpl添加查询指定UniqueIndex对应的记录是否存在的接口实现，注意UniqueIndex可能多个
            seviceImplClass.addMethods(codeGenerateService.generateIfExistsImpl(introspectedTable));

            //12.按照当前列表依赖的外键依赖 利用join进行级联查询,注意外键依赖可能多个，目前实现只考虑一层的级联，可以多个表
            seviceImplClass.addMethods(codeGenerateService.generateBinaryCascadeQueryImpl(introspectedTable));

            //14.当前表包含的外键依赖，逆向查询当前实体基本属性接口实现
            seviceImplClass.addMethods(codeGenerateService.generateSingleCascadeQueryImpl(introspectedTable));

            //15.为serviceImpl添加integrityCheck的接口实现
            seviceImplClass.addMethod(codeGenerateService.generateIntegrityCheckImpl(introspectedTable));

            seviceImplClass.calculateNonStaticImports();

            generatedJavaFile = new GeneratedJavaFile();
            generatedJavaFile.setTargetDir(configuration.getTargetDirectory());
            generatedJavaFile.setTargetPackage(configuration.getTargetPackage());
            generatedJavaFile.setFileEncoding(fileEncoding);
            generatedJavaFile.setCompilationUnit(seviceImplClass);

            generatedFiles.add(generatedJavaFile);
        }

        return generatedFiles;
    }
}
