package com.nxllxn.codegenerator.codegen.generator.config;

import com.nxllxn.codegenerator.codegen.generated.GeneratedConfigFile;
import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.resource.ResourceUnit;
import com.nxllxn.codegenerator.config.ConfigCodeGeneratorConfiguration;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.SqlMapGeneratorConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置文件代码生成器
 *
 * @author wenchao
 */
public class ConfigCodeGenerator extends AbstractCodeGenerator {
    private ConfigCodeGenerateService codeGenerateService;

    private ConfigCodeGeneratorConfiguration configuration;

    private SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration;

    private String fileEncoding;

    public ConfigCodeGenerator() {
        this.codeGenerateService = new ConfigCodeGenerateServiceImpl();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        this.configuration = context.getConfigCodeGeneratorConfiguration();
        this.sqlMapGeneratorConfiguration = context.getSqlMapGeneratorConfiguration();
        this.fileEncoding = context.getFileEncoding();
    }

    @Override
    public List<GeneratedFile> generate() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();

        List<ResourceUnit> resourceUnits = new ArrayList<>();

        //application.yml
        resourceUnits.add(codeGenerateService.generateMainConfigResourceUnit(
                configuration.getTargetDirectory(),sqlMapGeneratorConfiguration.getTargetPackage()));

        //application-dev.yml
        resourceUnits.add(codeGenerateService.generateDevConfigResourceUnit(configuration));

        //application--test.yml
        resourceUnits.add(codeGenerateService.generateTestConfigResourceUnit(configuration));

        //application-pro.yml
        resourceUnits.add(codeGenerateService.generateProConfigResourceUnit(configuration));

        for (ResourceUnit resourceUnit:resourceUnits){
            GeneratedConfigFile generatedConfigFile = new GeneratedConfigFile();
            generatedConfigFile.setResourceUnit(resourceUnit);
            generatedConfigFile.setFileEncoding(fileEncoding);
            generatedConfigFile.setTargetDir(configuration.getTargetDirectory());
            generatedFiles.add(generatedConfigFile);
        }

        return generatedFiles;
    }
}
