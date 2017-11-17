package com.nxllxn.codegenerator.codegen.java;

import com.nxllxn.codegenerator.utils.AssembleUtil;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Method Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 20, 2017</pre>
 */
public class MethodTest {
    private Method methodWithName;

    @Before
    public void before() throws Exception {
        methodWithName = new Method();
        methodWithName.setName("methodName");
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getFormattedContent(int indentLevel)
     */
    @Test
    public void testGetFormattedContent() throws Exception {
        Method method = new Method();

        assertEquals("", method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        method.setName("methodName");
        assertEquals("void methodName() {} ", method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        method.setVisibility(Visibility.PUBLIC);
        assertEquals("public void methodName() {} ", method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        method.setStatic(true);
        assertEquals("public static void methodName() {} ", method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        method.setFinal(true);
        assertEquals("public static final void methodName() {} ", method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        method.setNative(true);
        assertEquals("public static final native void methodName() {} ", method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        method.setDefault(true);
        assertEquals("public static final default native void methodName() {} ", method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        method.setReturnType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("public static final default native Bar methodName() {} ", method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        Parameter parameter = new Parameter();
        parameter.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        parameter.setName("parameterName");
        parameter.setVarAs(true);
        Set<String> annotations = new HashSet<>();
        annotations.add("@XXX");
        parameter.addAnnotations(annotations);

        method.addParameter(parameter);

        assertEquals("public static final default native Bar methodName(@XXX Bar... parameterName) {} ", method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        FullyQualifiedJavaType exception = new FullyQualifiedJavaType("java.lang.Exception");
        FullyQualifiedJavaType another = new FullyQualifiedJavaType("java.lang.AnotherException");
        method.addThrowsException(exception);
        method.addThrowsException(another);

        assertEquals(
                "public static final default native Bar methodName(@XXX Bar... parameterName) throws Exception,AnotherException {} ",
                method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL)
        );

        method.addBodyLine("String name = \"张三\";");
        assertEquals(
                "public static final default native Bar methodName(@XXX Bar... parameterName) throws Exception,AnotherException { \n    String name = \"张三\";\n} ",
                method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL)
        );
    }

    @Test
    public void testWithoutMethodName() {
        Method method = new Method();

        assertEquals("", method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testWithMethodName() {
        Method method = new Method();
        method.setName("methodName");

        assertEquals("void methodName() {} ", method.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void test(){
        Method method = new Method();

        method.setVisibility(Visibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        method.setName("toJson");

        method.setReturnType(new FullyQualifiedJavaType("com.alibaba.fastjson.JSONObject"));

        assertEquals("    public JSONObject toJson() {} ",method.getFormattedContent(1));
    }

    @Test
    public void testVisibility() {
        methodWithName.setVisibility(Visibility.PRIVATE);
        assertEquals("private void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.setVisibility(Visibility.DEFAULT);
        assertEquals("void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.setVisibility(Visibility.PROTECTED);
        assertEquals("protected void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.setVisibility(Visibility.PUBLIC);
        assertEquals("public void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testStaticModifier() {
        methodWithName.setStatic(false);
        assertEquals("void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.setStatic(true);
        assertEquals("static void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testFinalModifier() {
        methodWithName.setFinal(false);
        assertEquals("void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.setFinal(true);
        assertEquals("final void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testNativeModifier() {
        methodWithName.setNative(false);
        assertEquals("void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.setNative(true);
        assertEquals("native void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testDefaultModifier() {
        methodWithName.setDefault(false);
        assertEquals("void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.setDefault(true);
        assertEquals("default void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testReturnType() {
        methodWithName.setReturnType(null);
        assertEquals("void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.setReturnType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("Bar methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testParameter() {
        Parameter parameter = new Parameter();
        parameter.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        parameter.setName("parameterName");
        parameter.setVarAs(true);
        Set<String> annotations = new HashSet<>();
        annotations.add("@XXX");
        parameter.addAnnotations(annotations);

        methodWithName.addParameter(parameter);

        assertEquals("void methodName(@XXX Bar... parameterName) {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testThrowsExceptions() {
        FullyQualifiedJavaType exception = new FullyQualifiedJavaType("java.lang.Exception");
        methodWithName.addThrowsException(exception);
        assertEquals("void methodName() throws Exception {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        FullyQualifiedJavaType another = new FullyQualifiedJavaType("java.lang.AnotherException");
        methodWithName.addThrowsException(another);
        assertEquals("void methodName() throws Exception,AnotherException {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testBodyLines() {
        methodWithName.addBodyLine("String name = \"张三\";");
        assertEquals("void methodName() { \n    String name = \"张三\";\n} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.addBodyLine("String gender = \"male\";");
        assertEquals("void methodName() { \n    String name = \"张三\";\n    String gender = \"male\";\n} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testTypeParameters() {
        methodWithName.addTypeParameter(new TypeParameter("T", Collections.singletonList(FullyQualifiedJavaType.getNewListInstance())));
        assertEquals("<T extends List> methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.addTypeParameter(new TypeParameter("R", Arrays.asList(FullyQualifiedJavaType.getNewListInstance(), new FullyQualifiedJavaType("java.lang.Comparator"))));
        assertEquals("<T extends List,R extends List & Comparator> methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testConstructor() {
        methodWithName.setConstructor(false);
        assertEquals("void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.setConstructor(true);
        assertEquals("MethodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testComments(){
        assertEquals("void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.addElementComment("xxx");
        assertEquals("/**\n * xxx\n */\nvoid methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.addElementComment("yyy");
        assertEquals("/**\n * xxx\n * yyy\n */\nvoid methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testAnnotations(){
        assertEquals("void methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.addAnnotation("@XXX");
        assertEquals("@XXX\nvoid methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        methodWithName.addAnnotation("@YYY");
        assertEquals("@XXX\n@YYY\nvoid methodName() {} ", methodWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
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
     * Method: isSynchronized()
     */
    @Test
    public void testIsSynchronized() throws Exception {

    }

    /**
     * Method: setSynchronized(boolean aSynchronized)
     */
    @Test
    public void testSetSynchronized() throws Exception {

    }

    /**
     * Method: isDefault()
     */
    @Test
    public void testIsDefault() throws Exception {

    }

    /**
     * Method: setDefault(boolean aDefault)
     */
    @Test
    public void testSetDefault() throws Exception {

    }

    /**
     * Method: isNative()
     */
    @Test
    public void testIsNative() throws Exception {

    }

    /**
     * Method: setNative(boolean aNative)
     */
    @Test
    public void testSetNative() throws Exception {

    }

    /**
     * Method: isConstructor()
     */
    @Test
    public void testIsConstructor() throws Exception {

    }

    /**
     * Method: setConstructor(boolean constructor)
     */
    @Test
    public void testSetConstructor() throws Exception {

    }

    /**
     * Method: getReturnType()
     */
    @Test
    public void testGetReturnType() throws Exception {

    }

    /**
     * Method: setReturnType(FullyQualifiedJavaType returnType)
     */
    @Test
    public void testSetReturnType() throws Exception {

    }

    /**
     * Method: getName()
     */
    @Test
    public void testGetName() throws Exception {

    }

    /**
     * Method: setName(String name)
     */
    @Test
    public void testSetName() throws Exception {

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
     * Method: getParameters()
     */
    @Test
    public void testGetParameters() throws Exception {

    }

    /**
     * Method: setParameters(List<Parameter> parameters)
     */
    @Test
    public void testSetParameters() throws Exception {

    }

    /**
     * Method: getThrowsExceptions()
     */
    @Test
    public void testGetThrowsExceptions() throws Exception {

    }

    /**
     * Method: setThrowsExceptions(List<FullyQualifiedJavaType> throwsExceptions)
     */
    @Test
    public void testSetThrowsExceptions() throws Exception {

    }

    /**
     * Method: getBodyLines()
     */
    @Test
    public void testGetBodyLines() throws Exception {

    }

    /**
     * Method: setBodyLines(List<String> bodyLines)
     */
    @Test
    public void testSetBodyLines() throws Exception {

    }

    /**
     * Method: addParameter(Parameter parameter)
     */
    @Test
    public void testAddParameter() throws Exception {

    }

    /**
     * Method: addParameters(List<Parameter> parameters)
     */
    @Test
    public void testAddParameters() throws Exception {

    }

    /**
     * Method: addBodyLine(String bodyLine)
     */
    @Test
    public void testAddBodyLine() throws Exception {

    }

    /**
     * Method: addBodyLines(List<String> bodyLines)
     */
    @Test
    public void testAddBodyLines() throws Exception {

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
     * Method: addThrowsException(FullyQualifiedJavaType throwsException)
     */
    @Test
    public void testAddThrowsException() throws Exception {

    }

    /**
     * Method: addThrowsExceptions(List<FullyQualifiedJavaType> throwsExceptions)
     */
    @Test
    public void testAddThrowsExceptions() throws Exception {

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }


} 
