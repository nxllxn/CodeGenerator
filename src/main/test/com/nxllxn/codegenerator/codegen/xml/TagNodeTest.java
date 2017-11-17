package com.nxllxn.codegenerator.codegen.xml;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * TagNode Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 22, 2017</pre>
 */
public class TagNodeTest {
    private TagNode tagNodeWithName;

    @Before
    public void before() throws Exception {
        tagNodeWithName = new TagNode();
        tagNodeWithName.setName("name");
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getFormattedContent(int indentLevel)
     */
    @Test
    public void testGetFormattedContent() throws Exception {
        TagNode tagNode = new TagNode();

        assertEquals("",tagNode.getFormattedContent());
    }

    @Test
    public void testName(){
        TagNode tagNode = new TagNode();

        tagNode.setName("name");
        assertEquals("<name/>",tagNode.getFormattedContent());
    }

    @Test
    public void testAttribute(){
        assertEquals("<name/>",tagNodeWithName.getFormattedContent());

        tagNodeWithName.addAttributeNode(new AttributeNode("name","value"));
        assertEquals("<name name=\"value\"/>",tagNodeWithName.getFormattedContent());

        tagNodeWithName.addAttributeNode(new AttributeNode("name1","value1"));
        assertEquals("<name name=\"value\" name1=\"value1\"/>",tagNodeWithName.getFormattedContent());
    }

    @Test
    public void testChildNodes(){
        assertEquals("<name/>",tagNodeWithName.getFormattedContent());

        TagNode tagNode = new TagNode();
        tagNode.setName("anotherTag");
        tagNodeWithName.addChildNode(tagNode);
        assertEquals("<name>\n    <anotherTag/>\n</name>",tagNodeWithName.getFormattedContent());
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
     * Method: getAttributeNodes()
     */
    @Test
    public void testGetAttributeNodes() throws Exception {

    }

    /**
     * Method: getChildNodes()
     */
    @Test
    public void testGetChildNodes() throws Exception {

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }

    /**
     * Method: addAttributeNode(AttributeNode attributeNode)
     */
    @Test
    public void testAddAttributeNode() throws Exception {

    }

    /**
     * Method: addAttributeNodes(List<AttributeNode> attributeNodes)
     */
    @Test
    public void testAddAttributeNodes() throws Exception {

    }

    /**
     * Method: addChildNode(TextNode xmlUnit)
     */
    @Test
    public void testAddChildNodeXmlUnit() throws Exception {

    }

    /**
     * Method: addTextNodes(List<TextNode> xmlUnits)
     */
    @Test
    public void testAddTextNodes() throws Exception {

    }

    /**
     * Method: addTagNodes(List<TagNode> xmlUnits)
     */
    @Test
    public void testAddTagNodes() throws Exception {

    }
}
