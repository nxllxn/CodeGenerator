package com.nxllxn.codegenerator.codegen;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.JDBCConnectionConfiguration;
import com.nxllxn.codegenerator.config.xml.ConfigurationParser;
import com.nxllxn.codegenerator.controller.CodeGeneratorController;
import com.nxllxn.codegenerator.io.FileWriter;
import com.nxllxn.codegenerator.io.ProjectFileWriter;
import com.nxllxn.codegenerator.io.ZipFileWriter;
import com.nxllxn.codegenerator.jdbc.DatabaseIntrospector;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import com.nxllxn.codegenerator.jdbc.java.JDBCConnectionFactory;
import com.nxllxn.codegenerator.jdbc.java.JavaTypeResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        writeToZipFile();

        //writeToCurrentProject();
    }

    /**
     * 生成项目代码并将代码按照指定层级结构写入到当前项目中
     */
    private static void writeToCurrentProject() throws Exception {
        InputStream inputStream = CodeGeneratorController.class.getClassLoader().getResourceAsStream("Configuration.xml");

        Context context = (Context) new ConfigurationParser().parseConfiguration(
                inputStream
        );

        DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(
                getDatabaseMetadata(context), new JavaTypeResolver(), context
        );

        List<IntrospectedTable> introspectedTables = databaseIntrospector.introspectedTables();

        List<AbstractCodeGenerator> abstractCodeGenerators = context.getCodeGenerators();

        List<GeneratedFile> generatedFiles = new ArrayList<>();
        for (AbstractCodeGenerator codeGenerator : abstractCodeGenerators) {
            codeGenerator.setIntrospectedTables(introspectedTables);
            codeGenerator.setContext(context);

            generatedFiles.addAll(codeGenerator.generate());
        }

        FileWriter fileWriter = new ProjectFileWriter(generatedFiles);
        fileWriter.write();
    }

    /**
     * 生成项目代码并将代码写入到Zip文件中
     * @throws Exception SQLException
     */
    private static void writeToZipFile() throws Exception {
        InputStream inputStream = CodeGeneratorController.class.getClassLoader().getResourceAsStream("Configuration.xml");

        Context context = (Context) new ConfigurationParser().parseConfiguration(
                inputStream
        );

        DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(
                getDatabaseMetadata(context), new JavaTypeResolver(), context
        );

        List<IntrospectedTable> introspectedTables = databaseIntrospector.introspectedTables();

        List<AbstractCodeGenerator> abstractCodeGenerators = context.getCodeGenerators();

        List<GeneratedFile> generatedFiles = new ArrayList<>();
        for (AbstractCodeGenerator codeGenerator : abstractCodeGenerators) {
            codeGenerator.setIntrospectedTables(introspectedTables);
            codeGenerator.setContext(context);

            generatedFiles.addAll(codeGenerator.generate());
        }

        File zipFile = new File("out/zip.zip");
        zipFile.getParentFile().mkdirs();
        zipFile.createNewFile();

        FileWriter zipFileWriter = new ZipFileWriter(
                generatedFiles,
                context.getProjectBaseInfoConfiguration().getProjectName(),
                new FileOutputStream(zipFile)
        );
        zipFileWriter.write();
    }

    private static DatabaseMetaData getDatabaseMetadata(Context configuration) throws SQLException {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = configuration.getJdbcConnectionConfiguration();

        Connection connection = new JDBCConnectionFactory(jdbcConnectionConfiguration).openConnection();

        return connection.getMetaData();
    }
}
