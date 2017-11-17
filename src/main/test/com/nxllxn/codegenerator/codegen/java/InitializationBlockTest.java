package com.nxllxn.codegenerator.codegen.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * InitializationBlock Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 21, 2017</pre>
 */
public class InitializationBlockTest {
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getBlockComments()
     */
    @Test
    public void testGetBlockComments() throws Exception {

    }

    /**
     * Method: setBlockComments(List<String> blockComments)
     */
    @Test
    public void testSetBlockComments() throws Exception {

    }

    /**
     * Method: isStatic()
     */
    @Test
    public void testIsStatic() throws Exception {

    }

    /**
     * Method: setStatic(boolean aStatic)
     */
    @Test
    public void testSetStatic() throws Exception {

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
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

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
     * Method: getFormattedContent()
     */
    @Test
    public void testGetFormattedContent() throws Exception {

    }

    /*blockComments;
    private boolean isStatic;
    private List<String> bodyLines;*/

    @Test
    public void testBlockComments() {
        InitializationBlock initializationBlock = new InitializationBlock();
        assertEquals("{ \n} \n", initializationBlock.getFormattedContent());

        initializationBlock.addBlockComment("xxx");
        assertEquals("/**\n * xxx\n */\n{ \n} \n", initializationBlock.getFormattedContent());
    }

    @Test
    public void testStaticModifier() {
        InitializationBlock initializationBlock = new InitializationBlock();

        initializationBlock.setStatic(false);
        assertEquals("{ \n} \n", initializationBlock.getFormattedContent());

        initializationBlock.setStatic(true);
        assertEquals("static { \n} \n", initializationBlock.getFormattedContent());
    }

    @Test
    public void testBodyLines() {
        InitializationBlock initializationBlock = new InitializationBlock();

        initializationBlock.addBodyLine("String name = \"张三\";");
        assertEquals("{ \n    String name = \"张三\";\n} \n", initializationBlock.getFormattedContent());

        initializationBlock.addBodyLine("String gender = \"male\";");
        assertEquals("{ \n    String name = \"张三\";\n    String gender = \"male\";\n} \n", initializationBlock.getFormattedContent());
    }

    /**
     * Method: getFormattedContent(int indentLevel)
     */
    @Test
    public void testGetFormattedContentIndentLevel() throws Exception {

    }


} 
