package com.nxllxn.codegenerator.codegen.generator.entity;

import com.nxllxn.codegenerator.codegen.java.FullyQualifiedJavaType;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * CodeGenerateServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 22, 2017</pre>
 */
public class CodeGenerateServiceImplTest {
    private IntrospectedTable introspectedTable;

    private EntityCodeGenerateService codeGenerateService;

    @Before
    public void before() throws Exception {
        introspectedTable = new IntrospectedTable();
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

        codeGenerateService = new EntityCodeGenerateServiceImpl();
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: generateEntityClass(IntrospectedTable introspectedTable, String entityPackageName)
     */
    @Test
    public void testGenerateEntityClass() throws Exception {
        System.out.println(codeGenerateService.generateEntityClass(introspectedTable,"com.foo").getFormattedContent());
    }

    /**
     * Method: generateFields(IntrospectedTable introspectedTable)
     */
    @Test
    public void testGenerateFields() throws Exception {

    }

    /**
     * Method: generateFieldsGetter(IntrospectedTable introspectedTable)
     */
    @Test
    public void testGenerateFieldsGetter() throws Exception {

    }

    /**
     * Method: generateFieldsSetter(IntrospectedTable introspectedTable)
     */
    @Test
    public void testGenerateFieldsSetter() throws Exception {

    }

    /**
     * Method: generateConstructor(IntrospectedTable introspectedTable)
     */
    @Test
    public void testGenerateConstructor() throws Exception {

    }

    /**
     * Method: generateToStringMethod(IntrospectedTable introspectedTable)
     */
    @Test
    public void testGenerateToStringMethod() throws Exception {

    }

    /**
     * Method: generateEqualsMethod(IntrospectedTable introspectedTable)
     */
    @Test
    public void testGenerateEqualsMethod() throws Exception {

    }

    /**
     * Method: generateHashCodeMethod(IntrospectedTable introspectedTable)
     */
    @Test
    public void testGenerateHashCodeMethod() throws Exception {

    }

    /**
     * Method: generateFromJsonMethod(IntrospectedTable introspectedTable)
     */
    @Test
    public void testGenerateFromJsonMethod() throws Exception {

    }

    /**
     * Method: generateToJsonMethod(IntrospectedTable introspectedTable)
     */
    @Test
    public void testGenerateToJsonMethod() throws Exception {

    }

    /**
     * Method: generateBuilder(IntrospectedTable introspectedTable)
     */
    @Test
    public void testGenerateBuilder() throws Exception {

    }


} 
