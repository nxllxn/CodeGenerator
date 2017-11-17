package com.nxllxn.codegenerator.codegen.generator.key;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generated.GeneratedJavaFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.java.TopLevelClass;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.KeyGeneratorConfiguration;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体类对应的属性字符串常量Key类生成器
 *
 * @author wenchao
 */
public class KeyCodeGenerator extends AbstractCodeGenerator {
    /**
     * 代码生成服务
     */
    private KeyCodeGenerateService codeGenerateService;

    private KeyGeneratorConfiguration keyGeneratorConfiguration;

    /**
     * 文件编码类型
     */
    private String fileEncoding;

    public KeyCodeGenerator() {
        codeGenerateService = new KeyCodeGenerateServiceImpl();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        this.fileEncoding = context.getFileEncoding();
        this.keyGeneratorConfiguration = context.getKeyGeneratorConfiguration();
    }

    @Override
    public List<GeneratedFile> generate() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();

        TopLevelClass keyDefinitionClass;
        GeneratedJavaFile generatedJavaFile;
        for (IntrospectedTable introspectedTable:introspectedTables){
            introspectedTable.setKeyPackageName(keyGeneratorConfiguration.getTargetPackage());

            keyDefinitionClass = codeGenerateService.generateKeyDefinitionClass(introspectedTable,keyGeneratorConfiguration.getTargetPackage());

            keyDefinitionClass.addFields(codeGenerateService.generateKeyDefinitions(introspectedTable));

            keyDefinitionClass.calculateNonStaticImports();

            generatedJavaFile = new GeneratedJavaFile();
            generatedJavaFile.setTargetDir(keyGeneratorConfiguration.getTargetDirectory());
            generatedJavaFile.setTargetPackage(keyGeneratorConfiguration.getTargetPackage());
            generatedJavaFile.setCompilationUnit(keyDefinitionClass);
            generatedJavaFile.setFileEncoding(fileEncoding);

            generatedFiles.add(generatedJavaFile);
        }

        return generatedFiles;
    }
}
