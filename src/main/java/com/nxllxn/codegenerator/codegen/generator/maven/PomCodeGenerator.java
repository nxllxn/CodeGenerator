package com.nxllxn.codegenerator.codegen.generator.maven;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generated.GeneratedXmlFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.xml.TagNode;
import com.nxllxn.codegenerator.codegen.xml.XmlDocument;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.MavenCoordinateConfiguration;
import com.nxllxn.codegenerator.config.PomGeneratorConfiguration;
import com.nxllxn.codegenerator.config.ProjectBaseInfoConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * maven pom文件代码生成器
 */
public class PomCodeGenerator extends AbstractCodeGenerator {
    private PomCodeGenerateService codeGenerateService;

    private ProjectBaseInfoConfiguration projectBaseInfoConfiguration;

    private MavenCoordinateConfiguration mavenCoordinateConfiguration;

    private PomGeneratorConfiguration pomGeneratorConfiguration;

    private String fileEncoding;

    public PomCodeGenerator() {
        this.codeGenerateService = new PomCodeGenerateServiceImpl();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        this.projectBaseInfoConfiguration = context.getProjectBaseInfoConfiguration();
        this.mavenCoordinateConfiguration = context.getMavenCoordinateConfiguration();
        this.pomGeneratorConfiguration = context.getPomGeneratorConfiguration();

        this.fileEncoding = context.getFileEncoding();
    }

    @Override
    public List<GeneratedFile> generate() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();

        //1.生成pom文件空白文档
        XmlDocument pomXmlDocument = codeGenerateService.generatePomXmlDocument(
                pomGeneratorConfiguration.getPublicId(),
                pomGeneratorConfiguration.getSystemId()
        );

        //2.生成根结点
        TagNode rootNode = codeGenerateService.generatePomRootNode(pomGeneratorConfiguration);

        //3.addModelVersion
        rootNode.addChildNode(codeGenerateService.generateModelVersionNode());

        //4.添加maven坐标
        rootNode.addTagNodes(codeGenerateService.generateMavenCoordinate(
                mavenCoordinateConfiguration.getGroupId(),
                mavenCoordinateConfiguration.getArtifactId(),
                mavenCoordinateConfiguration.getVersion(),
                PomGeneratorConfiguration.PACKAGE_TYPE_JAR,
                projectBaseInfoConfiguration.getProjectName()
        ));

        //5.添加Properties
        TagNode propertiesNode = codeGenerateService.generatePropertiesNode();
        rootNode.addChildNode(propertiesNode);

        //6.添加spring-boot-starter-parent
        rootNode.addChildNode(codeGenerateService.generateParentNode());

        //7.添加maven依赖
        TagNode dependenciesNode = codeGenerateService.generateDependenciesNode();
        rootNode.addChildNode(dependenciesNode);

        dependenciesNode.addTagNodes(codeGenerateService.generateDependencyNodes(propertiesNode));

        //8.添加build节点
        TagNode buildNode = codeGenerateService.generateBuildNode();
        rootNode.addChildNode(buildNode);

        //9.添加构建项目名称
        buildNode.addChildNode(codeGenerateService.generateFinalNameNode(projectBaseInfoConfiguration.getProjectName()));

        //10.添加compile插件 默认主类mainClass为rootPackage.Application
        buildNode.addChildNode(codeGenerateService.generatePluginsNode(projectBaseInfoConfiguration.getRootPackage()));

        //11.添加资源过滤相关配置
        buildNode.addChildNode(codeGenerateService.generateResourceFilterNode(
                projectBaseInfoConfiguration.getResourceDirectory(),
                projectBaseInfoConfiguration.getSourceDirectory()
        ));

        //11.添加根节点project节点
        pomXmlDocument.setRootNode(rootNode);

        GeneratedXmlFile generatedXmlFile = new GeneratedXmlFile();
        generatedXmlFile.setTargetDir(pomGeneratorConfiguration.getTargetDirectory());
        generatedXmlFile.setTargetPackage(pomGeneratorConfiguration.getTargetPackage());
        generatedXmlFile.setFileEncoding(fileEncoding);
        generatedXmlFile.setXmlDocument(pomXmlDocument);
        generatedXmlFile.setFileName("pom");
        generatedFiles.add(generatedXmlFile);

        return generatedFiles;
    }
}
