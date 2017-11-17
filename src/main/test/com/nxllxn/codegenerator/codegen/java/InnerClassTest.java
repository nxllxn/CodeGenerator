package com.nxllxn.codegenerator.codegen.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * InnerClass Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 21, 2017</pre>
 */
public class InnerClassTest {
    private InnerClass innerClassWithType;

    @Before
    public void before() throws Exception {
        innerClassWithType = new InnerClass();

        innerClassWithType.setType(new FullyQualifiedJavaType("com.foo.Bar"));
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: isClass()
     */
    @Test
    public void testIsClass() throws Exception {
        assertTrue(new InnerClass().isClass());
    }

    /**
     * Method: isInterface()
     */
    @Test
    public void testIsInterface() throws Exception {
        assertFalse(new InnerClass().isInterface());
    }

    /**
     * Method: isEnumeration()
     */
    @Test
    public void testIsEnumeration() throws Exception {
        assertFalse(new InnerClass().isEnumeration());
    }

    /**
     * Method: getFormattedContent(int indentLevel)
     */
    @Test
    public void testGetFormattedContent() throws Exception {

    }

    @Test
    public void testWithoutType(){
        InnerClass innerClass = new InnerClass();

        assertEquals("",innerClass.getFormattedContent());
    }

    @Test
    public void testWithType(){
        InnerClass innerClass = new InnerClass();

        innerClass.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("class Bar {} ",innerClass.getFormattedContent());
    }

    @Test
    public void testComment(){
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.addTypeComment("xxx");
        assertEquals("/**\n * xxx\n */\nclass Bar {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.addTypeComment("yyy");
        assertEquals("/**\n * xxx\n * yyy\n */\nclass Bar {} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testVisibility(){
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.setVisibility(Visibility.PRIVATE);
        assertEquals("private class Bar {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.setVisibility(Visibility.DEFAULT);
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.setVisibility(Visibility.PROTECTED);
        assertEquals("protected class Bar {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.setVisibility(Visibility.PUBLIC);
        assertEquals("public class Bar {} ",innerClassWithType.getFormattedContent());

    }

    @Test
    public void testStaticModifier(){
        innerClassWithType.setStatic(false);
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.setStatic(true);
        assertEquals("static class Bar {} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testFinalModifier(){
        innerClassWithType.setFinal(false);
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.setFinal(true);
        assertEquals("final class Bar {} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testSuperClass(){
        innerClassWithType.setSuperClass(null);
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.setSuperClass(new FullyQualifiedJavaType("com.foo.SuperBar"));
        innerClassWithType.setFinal(false);
        assertEquals("class Bar extends SuperBar {} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testSuperInterfaces(){
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.addSuperInterface(new FullyQualifiedJavaType("com.foo.BarInterface"));
        assertEquals("class Bar implements BarInterface {} ",innerClassWithType.getFormattedContent());

        //用于保存superInterface类型的是一个集合，集合是无无序的，输出的时候顺序有点奇怪
        innerClassWithType.addSuperInterface(new FullyQualifiedJavaType("com.foo.AnotherBarInterface"));
        assertEquals("class Bar implements AnotherBarInterface,BarInterface {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.addSuperInterface(new FullyQualifiedJavaType("com.foo.AnotherInterface"));
        assertEquals("class Bar implements AnotherBarInterface,BarInterface,AnotherInterface {} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testTypeParameters(){
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        FullyQualifiedJavaType list = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType compare = new FullyQualifiedJavaType("java.util.Comparator");

        TypeParameter typeParameter = new TypeParameter("T", Arrays.asList(list, compare));
        innerClassWithType.addTypeParameter(typeParameter);

        assertEquals("class Bar<T extends List & Comparator> {} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testAbstractModifier(){
        innerClassWithType.setAbstract(false);
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        innerClassWithType.setAbstract(true);
        assertEquals("abstract class Bar {} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testInitializationBlock(){
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        InitializationBlock initializationBlock = new InitializationBlock();
        initializationBlock.setStatic(true);

        initializationBlock.addBodyLine("String name = \"张三\";");
        innerClassWithType.addInitializationBlock(initializationBlock);
        assertEquals("class Bar { \n    static { \n        String name = \"张三\";\n    } \n} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testFields(){
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        Field fieldWithName = new Field();
        fieldWithName.setName("fieldName");
        fieldWithName.setType(new FullyQualifiedJavaType("java.lang.Integer"));
        fieldWithName.setVisibility(Visibility.PRIVATE);
        innerClassWithType.addField(fieldWithName);

        assertEquals("class Bar { \n    private Integer fieldName;\n} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testMethods(){
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        Method method = new Method();
        method.setName("methodName");
        innerClassWithType.addMethod(method);

        assertEquals("class Bar { \n    void methodName() {} \n} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testInnerClass(){
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        InnerClass innerClass = new InnerClass();
        innerClass.setVisibility(Visibility.PUBLIC);
        innerClass.setType(new FullyQualifiedJavaType("com.foo.SubBar"));
        innerClassWithType.addInnerClass(innerClass);

        Field field = new Field();
        field.setVisibility(Visibility.PUBLIC);
        field.setName("fieldName");
        field.setType(new FullyQualifiedJavaType("java.lang.Integer"));
        innerClass.addField(field);

        assertEquals("class Bar { \n    public class SubBar { \n        public Integer fieldName;\n    } \n} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testInnerInterface(){
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        InnerInterface innerInterface = new InnerInterface();
        innerInterface.setVisibility(Visibility.PUBLIC);
        innerInterface.setType(new FullyQualifiedJavaType("com.foo.SubBar"));
        innerClassWithType.addInnerInterface(innerInterface);

        Field field = new Field();
        field.setVisibility(Visibility.PUBLIC);
        field.setName("fieldName");
        field.setType(new FullyQualifiedJavaType("java.lang.Integer"));
        innerInterface.addField(field);

        assertEquals("class Bar { \n    public interface SubBar { \n        public Integer fieldName;\n    } \n} ",innerClassWithType.getFormattedContent());
    }

    @Test
    public void testInnerEnum(){
        assertEquals("class Bar {} ",innerClassWithType.getFormattedContent());

        InnerEnum innerEnum = new InnerEnum();
        innerEnum.setVisibility(Visibility.PUBLIC);
        innerEnum.setType(new FullyQualifiedJavaType("com.foo.SubBar"));
        innerClassWithType.addInnerEnum(innerEnum);

        Field field = new Field();
        field.setVisibility(Visibility.DEFAULT);
        field.setName("fieldName");
        field.setType(new FullyQualifiedJavaType("java.lang.Integer"));
        innerEnum.addField(field);

        assertEquals("class Bar { \n    public enum SubBar { \n        ;\n\n        Integer fieldName;\n    } \n} ",innerClassWithType.getFormattedContent());
    }

    /**
     * Method: assembleModifiers(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel)
     */
    @Test
    public void testAssembleModifiers() throws Exception {

    }

    /**
     * Method: assembleMainBlock(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService, int indentLevel)
     */
    @Test
    public void testAssembleMainBlock() throws Exception {

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
     * Method: addTypeParameter(TypeParameter typeParameter)
     */
    @Test
    public void testAddTypeParameter() throws Exception {

    }

    /**
     * Method: addTypeParameters(List<TypeParameter> typeParameters)
     */
    @Test
    public void testAddTypeParameters() throws Exception {

    }

    /**
     * Method: addInitializationBlock(InitializationBlock initializationBlock)
     */
    @Test
    public void testAddInitializationBlock() throws Exception {

    }

    /**
     * Method: addInitializationBlocks(List<InitializationBlock> initializationBlocks)
     */
    @Test
    public void testAddInitializationBlocks() throws Exception {

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }
}
