package com.nxllxn.codegenerator.codegen.generator.service;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generated.GeneratedJavaFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.java.TopLevelInterface;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.ServiceGeneratorConfiguration;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Service层代码生成器
 *
 * @author wenchao
 */
public class ServiceCodeGenerator extends AbstractCodeGenerator {
    private ServiceCodeGenerateService codeGenerateService;

    private ServiceGeneratorConfiguration configuration;

    private String fileEncoding;

    public ServiceCodeGenerator() {
        this.codeGenerateService = new ServiceCodeGenerateServiceImpl();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        this.configuration = context.getServiceGeneratorConfiguration();

        this.fileEncoding = context.getFileEncoding();
    }

    @Override
    public List<GeneratedFile> generate() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();

        TopLevelInterface serviceInterface;
        GeneratedJavaFile generatedJavaFile;
        for (IntrospectedTable introspectedTable:introspectedTables){
            introspectedTable.setServiceInterfacePackageName(configuration.getTargetPackage());

            //1生成一个Service接口
            serviceInterface = codeGenerateService.generateServiceInterface(introspectedTable,configuration.getTargetPackage());

            //2.为Service添加一个添加一条记录的接口
            serviceInterface.addMethod(codeGenerateService.generateAddNewRecordInterface(introspectedTable));

            //3.为Service添加一个批量添加记录的接口
            serviceInterface.addMethod(codeGenerateService.generateAddRecordBatchInterface(introspectedTable));

            //4.为Service添加删除指定标识符对应记录的接口
            serviceInterface.addMethod(codeGenerateService.generateRemoveRecordInterface(introspectedTable));

            //added:13.为Service添加按照当前表包含的UniqueIndex删除指定记录接口，注意UniqueIndex可能多个
            serviceInterface.addMethods(codeGenerateService.generateRemoveRecordByUniqueIndexInterface(introspectedTable));

            //5.为Service添加按照指定标识符修改记录的接口
            serviceInterface.addMethod(codeGenerateService.generateModifyRecordInterface(introspectedTable));

            //6.为Service添加按照指定标识符查询指定记录的接口（只包含主键列和基础列）
            serviceInterface.addMethod(codeGenerateService.generateQueryRecordInterface(introspectedTable));

            //7.为Service添加按照指定标识符查询指定记录的接口（包括主键列，基础列，blob列）
            serviceInterface.addMethod(codeGenerateService.generateQueryRecordWithBlobInterface(introspectedTable));

            //8.为Service添加按照指定标识符查询指定记录的接口（仅包括blob列）
            serviceInterface.addMethod(codeGenerateService.generateQueryRecordOnlyBlobInterface(introspectedTable));

            //9.为Service添加查询全部记录的接口（只包含主键列和基础列）
            serviceInterface.addMethod(codeGenerateService.generateQueryAllRecordInterface(introspectedTable));

            //10.为Service添加查询全部记录的接口，此接口提供分页
            serviceInterface.addMethod(codeGenerateService.generateQueryAllRecordWithPageInterface(introspectedTable));

            //11.为Service添加查询指定UniqueIndex对应的记录是否存在的接口，注意UniqueIndex可能多个
            serviceInterface.addMethods(codeGenerateService.generateIfExistsInterface(introspectedTable));

            //12.按照当前列表依赖的外键依赖 利用join进行级联查询,注意外键依赖可能多个，目前实现只考虑一层的级联，可以多个表
            serviceInterface.addMethods(codeGenerateService.generateBinaryCascadeQueryInterface(introspectedTable));

            //14.当前表包含的外键依赖，逆向查询当前实体基本属性
            serviceInterface.addMethods(codeGenerateService.generateSingleCascadeQueryInterface(introspectedTable));

            //15.为service添加integrityCheck的接口
            serviceInterface.addMethod(codeGenerateService.generateIntegrityCheckInterface(introspectedTable));

            serviceInterface.calculateNonStaticImports();

            generatedJavaFile = new GeneratedJavaFile();
            generatedJavaFile.setTargetDir(configuration.getTargetDirectory());
            generatedJavaFile.setTargetPackage(configuration.getTargetPackage());
            generatedJavaFile.setFileEncoding(fileEncoding);
            generatedJavaFile.setCompilationUnit(serviceInterface);

            generatedFiles.add(generatedJavaFile);
        }

        return generatedFiles;
    }
}
