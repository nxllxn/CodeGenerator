package com.nxllxn.codegenerator.codegen.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * InnerInterface Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 21, 2017</pre>
 */
public class InnerInterfaceTest {
    private InnerInterface innerInterfaceWithType;

    @Before
    public void before() throws Exception {
        innerInterfaceWithType = new InnerInterface();
        innerInterfaceWithType.setType(new FullyQualifiedJavaType("com.foo.Bar"));
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

    /**
     * Method: assembleMainBlock(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel)
     */
    @Test
    public void testAssembleMainBlock() throws Exception {

    }

    /**
     * Method: assembleTypeParameters(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService)
     */
    @Test
    public void testAssembleTypeParameters() throws Exception {

    }

    /**
     * Method: assembleModifiers(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel)
     */
    @Test
    public void testAssembleModifiers() throws Exception {

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

    /**
     * Method: addSuperInterface(FullyQualifiedJavaType superInterface)
     */
    @Test
    public void testAddSuperInterface() throws Exception {

    }

    /**
     * Method: addSuperInterfaces(List<FullyQualifiedJavaType> superInterfaces)
     */
    @Test
    public void testAddSuperInterfaces() throws Exception {

    }

    @Test
    public void testWithoutType(){
        InnerInterface innerInterface = new InnerInterface();

        assertEquals("",innerInterface.getFormattedContent());
    }

    @Test
    public void testWithType(){
        InnerInterface innerInterface = new InnerInterface();

        innerInterface.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("interface Bar {} ",innerInterface.getFormattedContent());
    }

    @Test
    public void testComment(){
        assertEquals("interface Bar {} ",innerInterfaceWithType.getFormattedContent());

        innerInterfaceWithType.addTypeComment("xxx");
        assertEquals("/**\n * xxx\n */\ninterface Bar {} ",innerInterfaceWithType.getFormattedContent());

        innerInterfaceWithType.addTypeComment("yyy");
        assertEquals("/**\n * xxx\n * yyy\n */\ninterface Bar {} ",innerInterfaceWithType.getFormattedContent());
    }

    @Test
    public void testVisibility(){
        assertEquals("interface Bar {} ",innerInterfaceWithType.getFormattedContent());

        innerInterfaceWithType.setVisibility(Visibility.PRIVATE);
        assertEquals("private interface Bar {} ",innerInterfaceWithType.getFormattedContent());

        innerInterfaceWithType.setVisibility(Visibility.DEFAULT);
        assertEquals("interface Bar {} ",innerInterfaceWithType.getFormattedContent());

        innerInterfaceWithType.setVisibility(Visibility.PROTECTED);
        assertEquals("protected interface Bar {} ",innerInterfaceWithType.getFormattedContent());

        innerInterfaceWithType.setVisibility(Visibility.PUBLIC);
        assertEquals("public interface Bar {} ",innerInterfaceWithType.getFormattedContent());

    }

    @Test
    public void testStaticModifier(){
        innerInterfaceWithType.setStatic(false);
        assertEquals("interface Bar {} ",innerInterfaceWithType.getFormattedContent());

        innerInterfaceWithType.setStatic(true);
        assertEquals("static interface Bar {} ",innerInterfaceWithType.getFormattedContent());
    }

    @Test
    public void testFinalModifier(){
        innerInterfaceWithType.setFinal(false);
        assertEquals("interface Bar {} ",innerInterfaceWithType.getFormattedContent());

        innerInterfaceWithType.setFinal(true);
        assertEquals("final interface Bar {} ",innerInterfaceWithType.getFormattedContent());
    }

    @Test
    public void testSuperClass(){
        innerInterfaceWithType.setSuperClass(null);
        assertEquals("interface Bar {} ",innerInterfaceWithType.getFormattedContent());

        innerInterfaceWithType.setSuperClass(new FullyQualifiedJavaType("com.foo.SuperBar"));
        innerInterfaceWithType.setFinal(false);
        assertEquals("interface Bar extends SuperBar {} ",innerInterfaceWithType.getFormattedContent());
    }

    @Test
    public void testFields(){
        assertEquals("interface Bar {} ",innerInterfaceWithType.getFormattedContent());

        Field fieldWithName = new Field();
        fieldWithName.setName("fieldName");
        fieldWithName.setType(new FullyQualifiedJavaType("java.lang.Integer"));
        fieldWithName.setVisibility(Visibility.PRIVATE);
        innerInterfaceWithType.addField(fieldWithName);

        assertEquals("interface Bar { \n    private Integer fieldName;\n} ",innerInterfaceWithType.getFormattedContent());
    }

    @Test
    public void testMethods(){
        assertEquals("interface Bar {} ",innerInterfaceWithType.getFormattedContent());

        Method method = new Method();
        method.setName("methodName");
        innerInterfaceWithType.addMethod(method);

        assertEquals("interface Bar { \n    void methodName() {} \n} ",innerInterfaceWithType.getFormattedContent());
    }
}
