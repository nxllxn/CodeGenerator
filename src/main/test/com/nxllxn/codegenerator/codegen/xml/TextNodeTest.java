package com.nxllxn.codegenerator.codegen.xml;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * TextNode Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 22, 2017</pre>
 */
public class TextNodeTest {

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
        TextNode textNode = new TextNode();

        assertEquals("",textNode.getFormattedContent());
    }

    @Test
    public void testText(){
        TextNode textNode = new TextNode();

        textNode.setText("");
        assertEquals("\n",textNode.getFormattedContent());

        textNode.setText("\n");
        assertEquals("\n\n",textNode.getFormattedContent());

        assertEquals("\n",TextNode.EMPTY_LINE_TEXT_NODE.getFormattedContent());
    }

    /**
     * Method: getText()
     */
    @Test
    public void testGetText() throws Exception {

    }

    /**
     * Method: setText(String text)
     */
    @Test
    public void testSetText() throws Exception {

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }
}
