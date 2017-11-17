package com.nxllxn.codegenerator.codegen.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TopLevelEnum Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 22, 2017</pre>
 */
public class TopLevelEnumTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: isClass()
     */
    @Test
    public void testIsClass() throws Exception {
        assertFalse(new InnerEnum().isClass());
    }

    /**
     * Method: isInterface()
     */
    @Test
    public void testIsInterface() throws Exception {
        assertFalse(new InnerEnum().isInterface());
    }

    /**
     * Method: isEnumeration()
     */
    @Test
    public void testIsEnumeration() throws Exception {
        assertTrue(new InnerEnum().isEnumeration());
    }

    /**
     * Method: getFormattedContent(int indentLevel)
     */
    @Test
    public void testGetFormattedContent() throws Exception {

    }

    @Test
    public void testPackageName() {
        TopLevelEnum topLevelEnum = new TopLevelEnum();

        assertEquals("", topLevelEnum.getFormattedContent());

        topLevelEnum.setPackageName("com.foo");
        assertEquals("package com.foo;\n\n", topLevelEnum.getFormattedContent());
    }

    @Test
    public void testNonStaticImports() {
        TopLevelEnum topLevelEnum = new TopLevelEnum();

        assertEquals("", topLevelEnum.getFormattedContent());

        topLevelEnum.addImport(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("import com.foo.Bar;\n\n", topLevelEnum.getFormattedContent());

        topLevelEnum.addImport(new FullyQualifiedJavaType("java.lang.Integer"));
        assertEquals("import com.foo.Bar;\n\n", topLevelEnum.getFormattedContent());

        topLevelEnum.addImport(new FullyQualifiedJavaType("com.foo.AnotherBar"));
        assertEquals("import com.foo.AnotherBar;\nimport com.foo.Bar;\n\n", topLevelEnum.getFormattedContent());
    }

    @Test
    public void testStaticImport() {
        TopLevelEnum topLevelEnum = new TopLevelEnum();

        assertEquals("", topLevelEnum.getFormattedContent());

        topLevelEnum.addStaticImport("com.foo.Bar.STATIC_VALUE");
        assertEquals("import static com.foo.Bar.STATIC_VALUE;\n\n", topLevelEnum.getFormattedContent());

        topLevelEnum.addStaticImport("com.foo.Bar.ANOTHER_STATIC_VALUE");
        assertEquals("import static com.foo.Bar.ANOTHER_STATIC_VALUE;\nimport static com.foo.Bar.STATIC_VALUE;\n\n", topLevelEnum.getFormattedContent());
    }

    @Test
    public void testInnerEnum(){
        TopLevelEnum topLevelEnum = new TopLevelEnum();

        topLevelEnum.setVisibility(Visibility.PUBLIC);
        topLevelEnum.setType(new FullyQualifiedJavaType("com.foo.Bar"));

        assertEquals("public enum Bar { \n    ;\n} ",topLevelEnum.getFormattedContent());
    }

    /**
     * Method: getFields()
     */
    @Test
    public void testGetFields() throws Exception {

    }

    /**
     * Method: setFields(List<Field> fields)
     */
    @Test
    public void testSetFields() throws Exception {

    }

    /**
     * Method: getInnerClasses()
     */
    @Test
    public void testGetInnerClasses() throws Exception {

    }

    /**
     * Method: setInnerClasses(List<InnerClass> innerClasses)
     */
    @Test
    public void testSetInnerClasses() throws Exception {

    }

    /**
     * Method: getInnerEnums()
     */
    @Test
    public void testGetInnerEnums() throws Exception {

    }

    /**
     * Method: setInnerEnums(List<InnerEnum> innerEnums)
     */
    @Test
    public void testSetInnerEnums() throws Exception {

    }

    /**
     * Method: getMethods()
     */
    @Test
    public void testGetMethods() throws Exception {

    }

    /**
     * Method: setMethods(List<Method> methods)
     */
    @Test
    public void testSetMethods() throws Exception {

    }

    /**
     * Method: getEnumConstants()
     */
    @Test
    public void testGetEnumConstants() throws Exception {

    }

    /**
     * Method: setEnumConstants(List<EnumConstant> enumConstants)
     */
    @Test
    public void testSetEnumConstants() throws Exception {

    }

    /**
     * Method: getInnerEnum()
     */
    @Test
    public void testGetInnerEnum() throws Exception {

    }

    /**
     * Method: setInnerEnum(InnerEnum innerEnum)
     */
    @Test
    public void testSetInnerEnum() throws Exception {

    }

    /**
     * Method: addField(Field field)
     */
    @Test
    public void testAddField() throws Exception {

    }

    /**
     * Method: addFields(List<Field> fields)
     */
    @Test
    public void testAddFields() throws Exception {

    }

    /**
     * Method: addMethod(Method method)
     */
    @Test
    public void testAddMethod() throws Exception {

    }

    /**
     * Method: addMethods(List<Method> methods)
     */
    @Test
    public void testAddMethods() throws Exception {

    }

    /**
     * Method: addInnerClass(InnerClass innerClass)
     */
    @Test
    public void testAddInnerClass() throws Exception {

    }

    /**
     * Method: addInnerClasses(List<InnerClass> innerClasses)
     */
    @Test
    public void testAddInnerClasses() throws Exception {

    }

    /**
     * Method: addInnerEnum(InnerEnum innerEnum)
     */
    @Test
    public void testAddInnerEnum() throws Exception {

    }

    /**
     * Method: addInnerEnums(List<InnerEnum> innerEnums)
     */
    @Test
    public void testAddInnerEnums() throws Exception {

    }

    /**
     * Method: addEnumConstant(EnumConstant enumConstant)
     */
    @Test
    public void testAddEnumConstant() throws Exception {

    }

    /**
     * Method: addEnumConstants(List<EnumConstant> enumConstants)
     */
    @Test
    public void testAddEnumConstants() throws Exception {

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }


} 
