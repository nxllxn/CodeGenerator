package com.nxllxn.codegenerator.codegen.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * InnerEnum Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 21, 2017</pre>
 */
public class InnerEnumTest {
    private InnerEnum innerEnumWithType;

    @Before
    public void before() throws Exception {
        innerEnumWithType = new InnerEnum();

        innerEnumWithType.setType(new FullyQualifiedJavaType("com.foo.Bar"));
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
    public void testGetFormattedContentIndentLevel() throws Exception {

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
     * Method: assembleTypeParameters(StringBuilder formattedContentBuilder, CodeAssembleService codeAssembleService)
     */
    @Test
    public void testAssembleTypeParameters() throws Exception {

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

    /**
     * Method: getEnumConstantName()
     */
    @Test
    public void testGetEnumConstantName() throws Exception {

    }

    /**
     * Method: setEnumConstantName(String enumConstantName)
     */
    @Test
    public void testSetEnumConstantName() throws Exception {

    }

    /**
     * Method: getFieldStringValues()
     */
    @Test
    public void testGetFieldStringValues() throws Exception {

    }

    /**
     * Method: setFieldStringValues(List<String> fieldStringValues)
     */
    @Test
    public void testSetFieldStringValues() throws Exception {

    }

    /**
     * Method: getFormattedContent(int indentLevel, List<Field> fields)
     */
    @Test
    public void testGetFormattedContentForIndentLevelFields() throws Exception {

    }

    /**
     * 当前类型继承的超类
     */
    protected FullyQualifiedJavaType superClass;

    /**
     * 当前类型实现的接口
     */

    protected Set<FullyQualifiedJavaType> superInterfaces;

    @Test
    public void testWithoutType() {
        InnerEnum innerEnum = new InnerEnum();

        assertEquals("", innerEnum.getFormattedContent());
    }

    @Test
    public void testWithType() {
        InnerEnum innerEnum = new InnerEnum();

        innerEnum.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("enum Bar { \n    ;\n} ", innerEnum.getFormattedContent());
    }

    @Test
    public void testTypeComments() {
        innerEnumWithType.addTypeComment("xxx");
        assertEquals("/**\n * xxx\n */\nenum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());
    }

    @Test
    public void testTypeAnnotations() {
        innerEnumWithType.addAnnotation("@XXX");

        assertEquals("@XXX\nenum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());
    }

    @Test
    public void testVisibility() {
        innerEnumWithType.setVisibility(Visibility.PRIVATE);
        assertEquals("private enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());

        innerEnumWithType.setVisibility(Visibility.DEFAULT);
        assertEquals("enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());

        innerEnumWithType.setVisibility(Visibility.PROTECTED);
        assertEquals("protected enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());

        innerEnumWithType.setVisibility(Visibility.PUBLIC);
        assertEquals("public enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());
    }

    @Test
    public void testStaticModifier() {
        innerEnumWithType.setStatic(false);
        assertEquals("enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());

        innerEnumWithType.setStatic(true);
        assertEquals("static enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());
    }

    @Test
    public void testFinalModifier() {
        innerEnumWithType.setFinal(false);
        assertEquals("enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());

        innerEnumWithType.setFinal(true);
        assertEquals("final enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());
    }

    @Test
    public void testSuperClass() {
        innerEnumWithType.setSuperClass(null);
        assertEquals("enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());

        innerEnumWithType.setSuperClass(new FullyQualifiedJavaType("com.foo.SuperBar"));
        assertEquals("enum Bar extends SuperBar { \n    ;\n} ", innerEnumWithType.getFormattedContent());
    }

    /**
     * 枚举包含的内部类
     */
    protected List<InnerClass> innerClasses;

    /**
     * 枚举包含的内部枚举
     */
    protected List<InnerEnum> innerEnums;

    @Test
    public void testField(){
        Field field = new Field();
        field.setVisibility(Visibility.PRIVATE);
        field.setType(new FullyQualifiedJavaType("int"));
        field.setName("fieldName");

        assertEquals("enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());

        innerEnumWithType.addField(field);
        assertEquals("enum Bar { \n    ;\n\n    private int fieldName;\n} ", innerEnumWithType.getFormattedContent());
    }

    @Test
    public void testMethod(){
        assertEquals("enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());

        Method method = new Method();
        method.setVisibility(Visibility.PUBLIC);
        method.setName("toString");
        method.setReturnType(new FullyQualifiedJavaType("java.lang.String"));

        innerEnumWithType.addMethod(method);
        assertEquals("enum Bar { \n    ;\n\n    public String toString() {} \n} ", innerEnumWithType.getFormattedContent());
    }

    @Test
    public void testEnumConstant(){
        EnumConstant enumConstant = new EnumConstant();
        enumConstant.setEnumConstantName("enum_constant");

        assertEquals("enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());

        innerEnumWithType.addEnumConstant(enumConstant);
        assertEquals("enum Bar { \n    ENUM_CONSTANT;\n} ", innerEnumWithType.getFormattedContent());
    }

    @Test
    public void testInnerClass(){
        assertEquals("enum Bar { \n    ;\n} ", innerEnumWithType.getFormattedContent());

        InnerClass innerClass = new InnerClass();
        innerClass.setVisibility(Visibility.PUBLIC);
        innerClass.setType(new FullyQualifiedJavaType("com.foo.SubBar"));

        innerEnumWithType.addInnerClass(innerClass);
        assertEquals("enum Bar { \n    ;\n\n    public class SubBar {} \n} ", innerEnumWithType.getFormattedContent());
    }
}
