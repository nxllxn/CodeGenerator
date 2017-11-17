package com.nxllxn.codegenerator.codegen.xml;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * XmlDocument Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 22, 2017</pre>
 */
public class XmlDocumentTest {

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
    }

    @Test
    public void testPublicIdAddSystemId(){
        XmlDocument xmlDocument = new XmlDocument();

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>",xmlDocument.getFormattedContent());

        TagNode tagNode = new TagNode();
        tagNode.setName("root");
        xmlDocument.setRootNode(tagNode);

        xmlDocument.setPublicId(null);
        xmlDocument.setSystemId(null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE root PUBLIC \"\" \"\">\n\n<root/>",xmlDocument.getFormattedContent());

        xmlDocument.setPublicId("publicId");
        xmlDocument.setSystemId(null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE root PUBLIC \"publicId\" \"\">\n\n<root/>",xmlDocument.getFormattedContent());

        xmlDocument.setPublicId(null);
        xmlDocument.setSystemId("systemId");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE root PUBLIC \"\" \"systemId\">\n\n<root/>",xmlDocument.getFormattedContent());

        xmlDocument.setPublicId("publicId");
        xmlDocument.setSystemId("systemId");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE root PUBLIC \"publicId\" \"systemId\">\n\n<root/>",xmlDocument.getFormattedContent());
    }

    /**
     * Method: getPublicId()
     */
    @Test
    public void testGetPublicId() throws Exception {

    }

    /**
     * Method: setPublicId(String publicId)
     */
    @Test
    public void testSetPublicId() throws Exception {

    }

    /**
     * Method: getSystemId()
     */
    @Test
    public void testGetSystemId() throws Exception {

    }

    /**
     * Method: setSystemId(String systemId)
     */
    @Test
    public void testSetSystemId() throws Exception {

    }

    /**
     * Method: getRootNode()
     */
    @Test
    public void testGetRootNode() throws Exception {

    }

    /**
     * Method: setRootNode(TagNode rootNode)
     */
    @Test
    public void testSetRootNode() throws Exception {

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }
}
