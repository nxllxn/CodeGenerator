package com.nxllxn.codegenerator.codegen.service;

import com.nxllxn.codegenerator.utils.AssembleUtil;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * AssembleUtil Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 19, 2017</pre>
 */
public class AssembleUtilTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: startNewLine(StringBuilder stringBuilder)
     */
    @Test
    public void testStartNewLine() throws Exception {
        //nothing will happen
        AssembleUtil.startNewLine(null);

        StringBuilder stringBuilder = new StringBuilder();
        AssembleUtil.startNewLine(stringBuilder);

        assertEquals("\n",stringBuilder.toString());
    }

    /**
     * Method: indentWith(StringBuilder stringBuilder, int indentLevel)
     */
    @Test
    public void testIndentWith() throws Exception {
        //nothing will happen
        AssembleUtil.indentWith(null,0);

        StringBuilder stringBuilder = new StringBuilder();

        //nothing will happen
        AssembleUtil.indentWith(stringBuilder,-1);

        AssembleUtil.indentWith(stringBuilder,1);
        assertEquals(AssembleUtil.DEFAULT_INDEX_STR,stringBuilder.toString());

        AssembleUtil.indentWith(stringBuilder,1);
        assertEquals(AssembleUtil.DEFAULT_INDEX_STR + AssembleUtil.DEFAULT_INDEX_STR,stringBuilder.toString());
    }

    @Test
    public void testExtraEmptyLine(){
        //nothing will happen
        AssembleUtil.extraEmptyLine(null);

        StringBuilder stringBuilder = new StringBuilder();
        AssembleUtil.extraEmptyLine(stringBuilder);

        assertEquals("\n\n",stringBuilder.toString());
    }
}
