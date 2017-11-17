package com.nxllxn.codegenerator.codegen.generator.mapper;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generated.GeneratedJavaFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.java.TopLevelInterface;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.JavaClientGeneratorConfiguration;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体Dao Mapper接口代码生成器
 *
 * @author wenchao
 */
public class MapperCodeGenerator extends AbstractCodeGenerator {
    /**
     * Mapper 接口的代码生成服务
     */
    private MapperCodeGenerateService codeGenerateService;

    /**
     * Java Mapper代码生成相关配置
     */
    private JavaClientGeneratorConfiguration configuration;

    /**
     * 文件编码
     */
    private String fileEncoding;

    public MapperCodeGenerator() {
        this.codeGenerateService = new MapperCodeGenerateServiceImpl();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        this.configuration = context.getJavaClientGeneratorConfiguration();
        this.fileEncoding = context.getFileEncoding();
    }

    @Override
    public List<GeneratedFile> generate() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();

        GeneratedJavaFile generatedJavaFile;
        for (IntrospectedTable introspectedTable:introspectedTables){
            introspectedTable.setMapperPackageName(configuration.getTargetPackage());

            //1.生成Mapper接口
            TopLevelInterface mapperInterface = codeGenerateService.generateMapperInterface(introspectedTable,configuration.getTargetPackage());

            //2.为Mapper添加增加一条记录接口
            mapperInterface.addMethod(codeGenerateService.generateAddRecordInterface(introspectedTable));

            //3.为Mapper添加批量添加记录接口
            mapperInterface.addMethod(codeGenerateService.generateAddRecordBatchInterface(introspectedTable));

            //4.为Mapper添加删除指定标识符对应记录的接口
            mapperInterface.addMethod(codeGenerateService.generateRemoveRecordInterface(introspectedTable));

            //added:13.为Mapper添加按照当前表包含的UniqueIndex删除指定记录接口，注意UniqueIndex可能多个
            mapperInterface.addMethods(codeGenerateService.generateRemoveRecordByUniqueIndexInterface(introspectedTable));

            //added:15.为Mapper添加删除指定关系表中和当前外键关联的关系条目的接口
            mapperInterface.addMethods(codeGenerateService.generateRemoveRelationsByForeignKey(introspectedTable));

            //5.为Mapper添加按照指定标识符修改记录的接口
            mapperInterface.addMethod(codeGenerateService.generateModifyRecordInterface(introspectedTable));

            //6.为Mapper添加按照指定标识符查询指定记录的接口（只包含主键列和基础列）
            mapperInterface.addMethod(codeGenerateService.generateQueryRecordInterface(introspectedTable));

            //7.为Mapper添加按照指定标识符查询指定记录的接口（包括主键列，基础列，blob列）
            mapperInterface.addMethod(codeGenerateService.generateQueryRecordWithBlobInterface(introspectedTable));

            //8.为Mapper添加按照指定标识符查询指定记录的接口（仅包括blob列）
            mapperInterface.addMethod(codeGenerateService.generateQueryRecordOnlyBlobInterface(introspectedTable));

            //9.为Mapper添加查询全部记录的接口（只包含主键列和基础列）
            mapperInterface.addMethod(codeGenerateService.generateQueryAllRecordInterface(introspectedTable));

            //10.为Mapper添加查询全部记录的接口，此接口提供分页
            mapperInterface.addMethod(codeGenerateService.generateQueryAllRecordWithPageInterface(introspectedTable));

            //11.为Mapper添加查询指定UniqueIndex对应的记录数目的接口，注意UniqueIndex可能多个
            mapperInterface.addMethods(codeGenerateService.generateQueryCountInterface(introspectedTable));

            //12.按照当前列表依赖的外键依赖 利用join进行级联查询,注意外键依赖可能多个，目前实现只考虑一层的级联，可以多个表
            mapperInterface.addMethods(codeGenerateService.generateBinaryCascadeQueryInterface(introspectedTable));

            //14.当前表包含的外键依赖，逆向查询当前实体基本属性
            mapperInterface.addMethods(codeGenerateService.generateSingleCascadeQueryInterface(introspectedTable));

            //15.根据当前表包含的唯一索引信息查询指定记录
            mapperInterface.addMethods(codeGenerateService.generateQueryRecordByUniqueIndex(introspectedTable));

            //to extension...

            mapperInterface.calculateNonStaticImports();

            generatedJavaFile = new GeneratedJavaFile();
            generatedJavaFile.setTargetDir(configuration.getTargetDirectory());
            generatedJavaFile.setTargetPackage(configuration.getTargetPackage());
            generatedJavaFile.setFileEncoding(fileEncoding);
            generatedJavaFile.setCompilationUnit(mapperInterface);

            generatedFiles.add(generatedJavaFile);
        }

        return generatedFiles;
    }
}
