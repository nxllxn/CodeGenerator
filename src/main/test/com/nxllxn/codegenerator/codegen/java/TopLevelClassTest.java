package com.nxllxn.codegenerator.codegen.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * TopLevelClass Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 19, 2017</pre>
 */
public class TopLevelClassTest {

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

    }

    /**
     * Method: isInterface()
     */
    @Test
    public void testIsInterface() throws Exception {

    }

    /**
     * Method: isEnumeration()
     */
    @Test
    public void testIsEnumeration() throws Exception {

    }
    /**
     * Method: getFormattedContent(int indentLevel)
     */
    @Test
    public void testGetFormattedContent() throws Exception {
    }

    @Test
    public void testPackageName() {
        TopLevelClass topLevelClass = new TopLevelClass();

        assertEquals("", topLevelClass.getFormattedContent());

        topLevelClass.setPackageName("com.foo");
        assertEquals("package com.foo;\n\n", topLevelClass.getFormattedContent());
    }

    @Test
    public void testNonStaticImports() {
        TopLevelClass topLevelClass = new TopLevelClass();

        assertEquals("", topLevelClass.getFormattedContent());

        topLevelClass.addImport(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("import com.foo.Bar;\n\n", topLevelClass.getFormattedContent());

        topLevelClass.addImport(new FullyQualifiedJavaType("java.lang.Integer"));
        assertEquals("import com.foo.Bar;\n\n", topLevelClass.getFormattedContent());

        topLevelClass.addImport(new FullyQualifiedJavaType("com.foo.AnotherBar"));
        assertEquals("import com.foo.AnotherBar;\nimport com.foo.Bar;\n\n", topLevelClass.getFormattedContent());
    }

    @Test
    public void testStaticImport() {
        TopLevelClass topLevelClass = new TopLevelClass();

        assertEquals("", topLevelClass.getFormattedContent());

        topLevelClass.addStaticImport("com.foo.Bar.STATIC_VALUE");
        assertEquals("import static com.foo.Bar.STATIC_VALUE;\n\n", topLevelClass.getFormattedContent());

        topLevelClass.addStaticImport("com.foo.Bar.ANOTHER_STATIC_VALUE");
        assertEquals("import static com.foo.Bar.ANOTHER_STATIC_VALUE;\nimport static com.foo.Bar.STATIC_VALUE;\n\n", topLevelClass.getFormattedContent());
    }

    @Test
    public void testInnerClass(){
        TopLevelClass topLevelClass = new TopLevelClass();

        topLevelClass.setVisibility(Visibility.PUBLIC);
        topLevelClass.setType(new FullyQualifiedJavaType("com.foo.Bar"));

        assertEquals("public class Bar {} ",topLevelClass.getFormattedContent());
    }

    /**
     * Method: assembleMainBlock(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel)
     */
    @Test
    public void testAssembleMainBlock() throws Exception {

    }

    /**
     * Method: assembleModifiers(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel)
     */
    @Test
    public void testAssembleModifiers() throws Exception {

    }

    /**
     * Method: isAbstract()
     */
    @Test
    public void testIsAbstract() throws Exception {

    }

    /**
     * Method: setAbstract(boolean anAbstract)
     */
    @Test
    public void testSetAbstract() throws Exception {

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
     * Method: getInnerInterfaces()
     */
    @Test
    public void testGetInnerInterfaces() throws Exception {

    }

    /**
     * Method: setInnerInterfaces(List<InnerInterface> innerInterfaces)
     */
    @Test
    public void testSetInnerInterfaces() throws Exception {

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
     * Method: getTypeParameters()
     */
    @Test
    public void testGetTypeParameters() throws Exception {

    }

    /**
     * Method: setTypeParameters(List<TypeParameter> typeParameters)
     */
    @Test
    public void testSetTypeParameters() throws Exception {

    }

    /**
     * Method: getInitializationBlocks()
     */
    @Test
    public void testGetInitializationBlocks() throws Exception {

    }

    /**
     * Method: setInitializationBlocks(List<InitializationBlock> initializationBlocks)
     */
    @Test
    public void testSetInitializationBlocks() throws Exception {

    }

    /**
     * Method: getInnerClass()
     */
    @Test
    public void testGetInnerClass() throws Exception {

    }

    /**
     * Method: setInnerClass(InnerClass innerClass)
     */
    @Test
    public void testSetInnerClass() throws Exception {

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }


}
