package com.nxllxn.codegenerator.codegen.generator.entity;

import com.nxllxn.codegenerator.codegen.generated.GeneratedFile;
import com.nxllxn.codegenerator.codegen.java.FullyQualifiedJavaType;
import com.nxllxn.codegenerator.config.Context;
import com.nxllxn.codegenerator.config.JavaEntityGeneratorConfiguration;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import com.nxllxn.codegenerator.utils.EncodingUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * EntityCodeGenerator Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 22, 2017</pre>
 */
public class EntityCodeGeneratorTest {

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
        introspectedColumn.setPropertyName("propertyOne");
        introspectedColumn.setRemarks("字段评论信息1");

        introspectedTable.appendBaseColumn(introspectedColumn);

        introspectedColumn = new IntrospectedColumn();
        introspectedColumn.setJavaType(FullyQualifiedJavaType.getStringInstance());
        introspectedColumn.setPropertyName("propertyTwo");
        introspectedColumn.setRemarks("字段评论信息2");

        introspectedTable.appendBaseColumn(introspectedColumn);

        Context context = new Context(new Properties(),new Properties(),new Properties());
        JavaEntityGeneratorConfiguration configuration = new JavaEntityGeneratorConfiguration(new Properties(),new Properties(),new Properties());
        configuration.setTargetDirectory("src/main/java");
        configuration.setTargetPackage("com.foo.example.entities");
        context.setFileEncoding(EncodingUtil.DEFAULT_FILE_ENCODING);
        context.setJavaModelGeneratorConfiguration(configuration);

        EntityCodeGenerator entityCodeGenerator = new EntityCodeGenerator();
        entityCodeGenerator.setIntrospectedTables(Collections.singletonList(introspectedTable));
        entityCodeGenerator.setContext(context);

        List<GeneratedFile> generatedFiles = entityCodeGenerator.generate();

        for (GeneratedFile generatedFile:generatedFiles){
            System.out.println(generatedFile.getFormattedContent());
        }
    }
}
