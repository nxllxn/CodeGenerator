package com.nxllxn.codegenerator.codegen.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TopLevelInterface Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 22, 2017</pre>
 */
public class TopLevelInterfaceTest {

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
        assertFalse(new TopLevelInterface().isClass());
    }

    /**
     * Method: isInterface()
     */
    @Test
    public void testIsInterface() throws Exception {
        assertTrue(new TopLevelInterface().isInterface());
    }

    /**
     * Method: isEnumeration()
     */
    @Test
    public void testIsEnumeration() throws Exception {
        assertFalse(new TopLevelInterface().isEnumeration());
    }

    /**
     * Method: getFormattedContent(int indentLevel)
     */
    @Test
    public void testGetFormattedContent() throws Exception {

    }

    @Test
    public void testPackageName() {
        TopLevelInterface topLevelInterface = new TopLevelInterface();

        assertEquals("", topLevelInterface.getFormattedContent());

        topLevelInterface.setPackageName("com.foo");
        assertEquals("package com.foo;\n\n", topLevelInterface.getFormattedContent());
    }

    @Test
    public void testNonStaticImports() {
        TopLevelInterface topLevelInterface = new TopLevelInterface();

        assertEquals("", topLevelInterface.getFormattedContent());

        topLevelInterface.addImport(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("import com.foo.Bar;\n\n", topLevelInterface.getFormattedContent());

        topLevelInterface.addImport(new FullyQualifiedJavaType("java.lang.Integer"));
        assertEquals("import com.foo.Bar;\n\n", topLevelInterface.getFormattedContent());

        topLevelInterface.addImport(new FullyQualifiedJavaType("com.foo.AnotherBar"));
        assertEquals("import com.foo.AnotherBar;\nimport com.foo.Bar;\n\n", topLevelInterface.getFormattedContent());
    }

    @Test
    public void testStaticImport() {
        TopLevelInterface topLevelInterface = new TopLevelInterface();

        assertEquals("", topLevelInterface.getFormattedContent());

        topLevelInterface.addStaticImport("com.foo.Bar.STATIC_VALUE");
        assertEquals("import static com.foo.Bar.STATIC_VALUE;\n\n", topLevelInterface.getFormattedContent());

        topLevelInterface.addStaticImport("com.foo.Bar.ANOTHER_STATIC_VALUE");
        assertEquals("import static com.foo.Bar.ANOTHER_STATIC_VALUE;\nimport static com.foo.Bar.STATIC_VALUE;\n\n", topLevelInterface.getFormattedContent());
    }

    @Test
    public void testInnerInterface(){
        TopLevelInterface topLevelInterface = new TopLevelInterface();

        topLevelInterface.setVisibility(Visibility.PUBLIC);
        topLevelInterface.setType(new FullyQualifiedJavaType("com.foo.Bar"));

        assertEquals("public interface Bar {} ",topLevelInterface.getFormattedContent());
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
     * Method: getInnerInterface()
     */
    @Test
    public void testGetInnerInterface() throws Exception {

    }

    /**
     * Method: setInnerInterface(InnerInterface innerInterface)
     */
    @Test
    public void testSetInnerInterface() throws Exception {

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
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }
}
