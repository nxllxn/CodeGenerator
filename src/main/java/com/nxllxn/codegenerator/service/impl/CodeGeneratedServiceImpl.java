package com.nxllxn.codegenerator.service.impl;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.codegen.generator.maven.PomCodeGenerator;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.JDBCConnectionConfiguration;
import com.nxllxn.codegenerator.config.MavenCoordinateConfiguration;
import com.nxllxn.codegenerator.config.ProjectBaseInfoConfiguration;
import com.nxllxn.codegenerator.config.xml.ConfigurationParser;
import com.nxllxn.codegenerator.controller.CodeGeneratorController;
import com.nxllxn.codegenerator.io.FileWriter;
import com.nxllxn.codegenerator.io.ZipFileWriter;
import com.nxllxn.codegenerator.jdbc.DatabaseIntrospector;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import com.nxllxn.codegenerator.jdbc.java.JDBCConnectionFactory;
import com.nxllxn.codegenerator.jdbc.java.JavaTypeResolver;
import com.nxllxn.codegenerator.service.CodeGeneratedService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodeGeneratedServiceImpl implements CodeGeneratedService {
    @Override
    public void generateCode(ProjectBaseInfoConfiguration projectBaseInfoConfiguration,
                             MavenCoordinateConfiguration mavenCoordinateConfiguration,
                             JDBCConnectionConfiguration jdbcConnectionConfiguration,
                             OutputStream outputStream) throws IOException, SQLException {
        InputStream inputStream = CodeGeneratorController.class.getClassLoader().getResourceAsStream("Configuration.xml");

        Context context = (Context) new ConfigurationParser().parseConfiguration(
                inputStream
        );

        context.setProjectBaseInfoConfiguration(projectBaseInfoConfiguration);
        context.setMavenCoordinateConfiguration(mavenCoordinateConfiguration);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

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

        FileWriter zipFileWriter = new ZipFileWriter(generatedFiles,
                projectBaseInfoConfiguration.getProjectName(), outputStream);
        zipFileWriter.write();
    }

    private DatabaseMetaData getDatabaseMetadata(Context configuration) throws SQLException {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = configuration.getJdbcConnectionConfiguration();

        Connection connection = new JDBCConnectionFactory(jdbcConnectionConfiguration).openConnection();

        return connection.getMetaData();
    }
}
