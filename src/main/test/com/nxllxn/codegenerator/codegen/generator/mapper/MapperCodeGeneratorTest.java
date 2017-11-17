package com.nxllxn.codegenerator.codegen.generator.mapper;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.java.FullyQualifiedJavaType;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.JavaClientGeneratorConfiguration;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import com.nxllxn.codegenerator.utils.EncodingUtil;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * MapperCodeGenerator Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 23, 2017</pre>
 */
public class MapperCodeGeneratorTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: generate()
     */
    @Test
    public void testGenerate() throws Exception {
        IntrospectedTable introspectedTable = new IntrospectedTable();
        introspectedTable.setEntityName("Bar");

        IntrospectedColumn introspectedColumn = new IntrospectedColumn();
        introspectedColumn.setJavaType(FullyQualifiedJavaType.getStringInstance());
        introspectedColumn.setPropertyName("id");
        introspectedColumn.setRemarks("主键");
        introspectedColumn.setAutoIncrement(true);
        introspectedColumn.setGeneratedColumn(true);
        introspectedTable.appendIdentityColumn(introspectedColumn);

        introspectedColumn = new IntrospectedColumn();
        introspectedColumn.setJavaType(FullyQualifiedJavaType.getStringInstance());
        introspectedColumn.setPropertyName("propertyOne");
        introspectedColumn.setRemarks("字段评论信息1");

        introspectedTable.appendBaseColumn(introspectedColumn);

        introspectedColumn = new IntrospectedColumn();
        introspectedColumn.setJavaType(FullyQualifiedJavaType.getStringInstance());
        introspectedColumn.setPropertyName("propertyTwo");
        introspectedColumn.setRemarks("字段评论信息2");

        introspectedTable.appendBaseColumn(introspectedColumn);

        Context context = new Context(new Properties(),new Properties(),new Properties());
        JavaClientGeneratorConfiguration configuration = new JavaClientGeneratorConfiguration(new Properties(),new Properties(),new Properties());
        configuration.setTargetDirectory("src/main/java");
        configuration.setTargetPackage("com.foo.example.key");
        context.setFileEncoding(EncodingUtil.DEFAULT_FILE_ENCODING);
        context.setJavaClientGeneratorConfiguration(configuration);

        MapperCodeGenerator mapperCodeGenerator = new MapperCodeGenerator();
        mapperCodeGenerator.setIntrospectedTables(Arrays.asList(introspectedTable));
        mapperCodeGenerator.setContext(context);

        List<GeneratedFile> generatedFiles = mapperCodeGenerator.generate();

        for (GeneratedFile generatedFile:generatedFiles){
            System.out.println(generatedFile.getFormattedContent());
        }
    }
}
