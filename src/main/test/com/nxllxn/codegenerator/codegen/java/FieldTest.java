package com.nxllxn.codegenerator.codegen.java;

import com.nxllxn.codegenerator.utils.AssembleUtil;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Field Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 20, 2017</pre>
 */
public class FieldTest {
    private Field fieldWithName;

    @Before
    public void before() throws Exception {
        fieldWithName = new Field();
        fieldWithName.setName("fieldName");
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getFormattedContent(int indentLevel)
     */
    @Test
    public void testGetFormattedContent() throws Exception {
        assertEquals("", new Field().getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        Field field = new Field();
        field.setName("fieldId");
        assertEquals("fieldId;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setType(new FullyQualifiedJavaType("int"));
        assertEquals("int fieldId;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setInitStrValue("1");
        assertEquals("int fieldId = 1;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setVolatile(true);
        assertEquals("volatile int fieldId = 1;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setTransient(true);
        assertEquals("transient volatile int fieldId = 1;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testGetFormattedContent1() throws Exception {
        Field field = new Field();
        assertEquals("", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setName("fieldId");
        assertEquals("fieldId;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setType(new FullyQualifiedJavaType("java.lang.Integer"));
        assertEquals("Integer fieldId;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setInitStrValue("1");
        assertEquals("Integer fieldId = 1;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setVolatile(true);
        assertEquals("volatile Integer fieldId = 1;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setTransient(true);
        assertEquals("transient volatile Integer fieldId = 1;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setFinal(true);
        assertEquals("final transient volatile Integer fieldId = 1;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setVisibility(Visibility.PUBLIC);
        assertEquals("public final transient volatile Integer fieldId = 1;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        field.setStatic(true);
        assertEquals("public static final transient volatile Integer fieldId = 1;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        Set<String> annotations = new HashSet<>();
        annotations.add("@XXX");
        field.setAnnotations(annotations);
        assertEquals("@XXX\npublic static final transient volatile Integer fieldId = 1;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        List<String> comments = new ArrayList<>();
        comments.add("xxx");
        field.addElementComments(comments);
        assertEquals("/**\n * xxx\n */\n@XXX\npublic static final transient volatile Integer fieldId = 1;", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testWithoutName() {
        Field field = new Field();
        assertEquals("", field.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testWithName() {
        assertEquals("fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testType() {
        fieldWithName.setType(null);
        assertEquals("fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setType(new FullyQualifiedJavaType("int"));
        assertEquals("int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setType(new FullyQualifiedJavaType("java.lang.Integer"));
        assertEquals("Integer fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("Bar fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testInitValue() {
        fieldWithName.setInitStrValue("");
        fieldWithName.setType(new FullyQualifiedJavaType("int"));
        assertEquals("int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setInitStrValue("1");
        fieldWithName.setType(new FullyQualifiedJavaType("int"));
        assertEquals("int fieldName = 1;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setType(new FullyQualifiedJavaType("byte"));
        assertEquals("byte fieldName = 1;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setType(new FullyQualifiedJavaType("long"));
        assertEquals("long fieldName = 1;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setType(new FullyQualifiedJavaType("java.lang.Integer"));
        assertEquals("Integer fieldName = 1;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setInitStrValue("dsadasda");
        fieldWithName.setType(new FullyQualifiedJavaType("boolean"));
        assertEquals("boolean fieldName = false;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setInitStrValue("true");
        fieldWithName.setType(new FullyQualifiedJavaType("boolean"));
        assertEquals("boolean fieldName = true;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setInitStrValue("false");
        fieldWithName.setType(new FullyQualifiedJavaType("boolean"));
        assertEquals("boolean fieldName = false;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setInitStrValue("str");
        fieldWithName.setType(new FullyQualifiedJavaType("java.lang.String"));
        assertEquals("String fieldName = \"str\";", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setInitStrValue("str");
        fieldWithName.setType(new FullyQualifiedJavaType("java.lang.Object"));
        assertEquals("Object fieldName = str;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testVolatileModifier() {
        fieldWithName.setType(new FullyQualifiedJavaType("int"));

        fieldWithName.setVolatile(false);
        assertEquals("int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setVolatile(true);
        assertEquals("volatile int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testTransient() {
        fieldWithName.setType(new FullyQualifiedJavaType("int"));

        fieldWithName.setTransient(false);
        assertEquals("int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setTransient(true);
        assertEquals("transient int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testStatic() {
        fieldWithName.setType(new FullyQualifiedJavaType("int"));

        fieldWithName.setStatic(false);
        assertEquals("int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setStatic(true);
        assertEquals("static int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testFinal() {
        fieldWithName.setType(new FullyQualifiedJavaType("int"));

        fieldWithName.setFinal(false);
        assertEquals("int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setFinal(true);
        assertEquals("final int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testVisibility() {
        fieldWithName.setType(new FullyQualifiedJavaType("int"));

        fieldWithName.setVisibility(Visibility.PRIVATE);
        assertEquals("private int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setVisibility(Visibility.DEFAULT);
        assertEquals("int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setVisibility(Visibility.PROTECTED);
        assertEquals("protected int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.setVisibility(Visibility.PUBLIC);
        assertEquals("public int fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testComment() {
        fieldWithName.setType(new FullyQualifiedJavaType("int"));

        fieldWithName.addElementComment("xxx");
        assertEquals("/**\n * xxx\n */\nint fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.addElementComment("yyy");
        assertEquals("/**\n * xxx\n * yyy\n */\nint fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
    }

    @Test
    public void testAnnotation() {
        fieldWithName.setType(new FullyQualifiedJavaType("int"));

        fieldWithName.addAnnotation("@XXX");
        assertEquals("@XXX\nint fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));

        fieldWithName.addAnnotation("@YYY");
        assertEquals("@XXX\n@YYY\nint fieldName;", fieldWithName.getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL));
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
     * Method: isTransient()
     */
    @Test
    public void testIsTransient() throws Exception {

    }

    /**
     * Method: setTransient(boolean aTransient)
     */
    @Test
    public void testSetTransient() throws Exception {

    }

    /**
     * Method: isVolatile()
     */
    @Test
    public void testIsVolatile() throws Exception {

    }

    /**
     * Method: setVolatile(boolean aVolatile)
     */
    @Test
    public void testSetVolatile() throws Exception {

    }

    /**
     * Method: getType()
     */
    @Test
    public void testGetType() throws Exception {

    }

    /**
     * Method: setType(FullyQualifiedJavaType type)
     */
    @Test
    public void testSetType() throws Exception {

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
     * Method: getInitStrValue()
     */
    @Test
    public void testGetInitStrValue() throws Exception {

    }

    /**
     * Method: setInitStrValue(String initStrValue)
     */
    @Test
    public void testSetInitStrValue() throws Exception {

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }
}
