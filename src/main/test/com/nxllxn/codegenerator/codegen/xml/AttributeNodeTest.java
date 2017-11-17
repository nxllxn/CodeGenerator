package com.nxllxn.codegenerator.codegen.xml;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * AttributeNode Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 22, 2017</pre>
 */
public class AttributeNodeTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getFormattedContent(int indentLevel)
     */
    @Test
    public void testGetFormattedContent() throws Exception {
        AttributeNode attributeNode = new AttributeNode();

        assertEquals("",attributeNode.getFormattedContent());
    }

    @Test
    public void testNameAndValue(){
        AttributeNode attributeNode;

        attributeNode = new AttributeNode();
        attributeNode.setName("name");
        assertEquals("",attributeNode.getFormattedContent());

        attributeNode = new AttributeNode();
        attributeNode.setName("value");
        assertEquals("",attributeNode.getFormattedContent());

        attributeNode = new AttributeNode();
        attributeNode.setName("name");
        attributeNode.setValue("value");
        assertEquals("name=\"value\"",attributeNode.getFormattedContent());
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
     * Method: getValue()
     */
    @Test
    public void testGetValue() throws Exception {

    }

    /**
     * Method: setValue(String value)
     */
    @Test
    public void testSetValue() throws Exception {

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }
}
