package com.nxllxn.codegenerator.codegen.generator.entity;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generated.GeneratedJavaFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.java.TopLevelClass;
import com.nxllxn.codegenerator.codegen.java.TopLevelInterface;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.JavaEntityGeneratorConfiguration;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体代码生成器
 *
 * @author wenchao
 */
public class EntityCodeGenerator extends AbstractCodeGenerator {
    /**
     * 带阿妈生成服务
     */
    private EntityCodeGenerateService codeGenerateService;

    /**
     * Java实体类生成相关配置
     */
    private JavaEntityGeneratorConfiguration entityGenerateConfiguration;

    /**
     * 文件编码
     */
    private String fileEncoding;

    public EntityCodeGenerator() {
        //TODO 目前直接new一个实例，之后需要利用工厂方法以及基于配置信息进行调整
        codeGenerateService = new EntityCodeGenerateServiceImpl();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        entityGenerateConfiguration = context.getJavaModelGeneratorConfiguration();
        fileEncoding = context.getFileEncoding();
    }

    @Override
    public List<GeneratedFile> generate() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();

        GeneratedJavaFile generatedJavaFile;
        for (IntrospectedTable introspectedTable:introspectedTables){
            introspectedTable.setEntityPackageName(entityGenerateConfiguration.getTargetPackage());
            //1.根据表结构信息生成实体类
            TopLevelClass entityClass = codeGenerateService.generateEntityClass(introspectedTable,entityGenerateConfiguration.getTargetPackage());

            //2.根据表结构包含的列集合生成实体字段列表    //TODO 外健
            entityClass.addFields(codeGenerateService.generateFields(introspectedTable));

            //3.根据表结构信息生成实体类默认构造函数，只会生成默认构造函数，不会有带参构造函数    //TODO 外健
            entityClass.addMethod(codeGenerateService.generateConstructor(introspectedTable));

            //4.根据表结构的列信息生成字段Getter    //TODO 外健
            entityClass.addMethods(codeGenerateService.generateFieldsGetter(introspectedTable));

            //5.根据表结构的列信息生成字段Setter    //TODO 外健
            entityClass.addMethods(codeGenerateService.generateFieldsSetter(introspectedTable));

            //6.根据表结构信息生成hashCode方法    //TODO 外健
            entityClass.addMethod(codeGenerateService.generateHashCodeMethod(introspectedTable));

            //7.根据表结构信息生成equals方法    //TODO 外健
            entityClass.addMethod(codeGenerateService.generateEqualsMethod(introspectedTable));

            //8.根据表结构信息生成toString方法    //TODO 外健
            entityClass.addMethod(codeGenerateService.generateToStringMethod(introspectedTable));

            //9.根据表结构信息生成fromJson方法    //TODO 外健
            entityClass.addMethod(codeGenerateService.generateFromJsonMethod(introspectedTable));

            //10.根据表结构信息生成toJson方法    //TODO 外健
            entityClass.addMethod(codeGenerateService.generateToJsonMethod(introspectedTable));

            //11.根据表结构信息生成Builder    //TODO 外健
            entityClass.addInnerClass(codeGenerateService.generateBuilder(introspectedTable));

            //12.完整性校验（实现有点问题） TODO fix later

            //13.根据外健依赖关系，构建依赖属性

            entityClass.calculateNonStaticImports();

            generatedJavaFile = new GeneratedJavaFile();
            generatedJavaFile.setTargetDir(entityGenerateConfiguration.getTargetDirectory());
            generatedJavaFile.setTargetPackage(entityGenerateConfiguration.getTargetPackage());
            generatedJavaFile.setCompilationUnit(entityClass);
            generatedJavaFile.setFileEncoding(fileEncoding);

            generatedFiles.add(generatedJavaFile);
        }

        //序列化接口Serializer
        TopLevelInterface serializerInterface = codeGenerateService.generateSerializerInterface(entityGenerateConfiguration.getTargetPackage());
        serializerInterface.calculateNonStaticImports();
        generatedJavaFile = new GeneratedJavaFile();
        generatedJavaFile.setTargetDir(entityGenerateConfiguration.getTargetDirectory());
        generatedJavaFile.setTargetPackage(entityGenerateConfiguration.getTargetPackage());
        generatedJavaFile.setCompilationUnit(serializerInterface);
        generatedJavaFile.setFileEncoding(fileEncoding);
        generatedFiles.add(generatedJavaFile);

        //序列化接口Serializer抽象实现
        TopLevelClass abstractSerializerClass = codeGenerateService.generateSerializerAbstractClass(entityGenerateConfiguration.getTargetPackage());
        abstractSerializerClass.calculateNonStaticImports();
        generatedJavaFile = new GeneratedJavaFile();
        generatedJavaFile.setTargetDir(entityGenerateConfiguration.getTargetDirectory());
        generatedJavaFile.setTargetPackage(entityGenerateConfiguration.getTargetPackage());
        generatedJavaFile.setCompilationUnit(abstractSerializerClass);
        generatedJavaFile.setFileEncoding(fileEncoding);
        generatedFiles.add(generatedJavaFile);

        return generatedFiles;
    }
}
