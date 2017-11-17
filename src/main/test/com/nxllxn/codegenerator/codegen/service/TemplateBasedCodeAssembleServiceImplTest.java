package com.nxllxn.codegenerator.codegen.service;

import com.nxllxn.codegenerator.utils.AssembleUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TemplateBasedCodeAssembleServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 19, 2017</pre>
 */
public class TemplateBasedCodeAssembleServiceImplTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: assembleJavaDoc(List<String> javaDocs)
     */
    @Test
    public void testAssembleJavaDoc() throws Exception {
        TemplateBasedCodeAssembleServiceImpl service = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        assertTrue(StringUtils.isBlank(service.assembleJavaDoc(null,0)));
        assertTrue(StringUtils.isBlank(service.assembleJavaDoc(new ArrayList<String>(),0)));

        List<String> javaDocs = new ArrayList<>();
        javaDocs.add("xxx");
        String expectedResult = "/**" + AssembleUtil.LINE_SEPARATOR +
                " * xxx" + AssembleUtil.LINE_SEPARATOR +
                " */" + AssembleUtil.LINE_SEPARATOR;
        assertEquals(expectedResult,service.assembleJavaDoc(javaDocs,0));

        javaDocs.add("yyy");
        expectedResult = "/**" + AssembleUtil.LINE_SEPARATOR +
                " * xxx" + AssembleUtil.LINE_SEPARATOR +
                " * yyy" + AssembleUtil.LINE_SEPARATOR +
                " */" + AssembleUtil.LINE_SEPARATOR;
        assertEquals(expectedResult,service.assembleJavaDoc(javaDocs,0));

        javaDocs = new ArrayList<>();
        javaDocs.add("xxx");
        expectedResult = AssembleUtil.DEFAULT_INDEX_STR + "/**" + AssembleUtil.LINE_SEPARATOR +
                AssembleUtil.DEFAULT_INDEX_STR +  " * xxx" + AssembleUtil.LINE_SEPARATOR +
                AssembleUtil.DEFAULT_INDEX_STR + " */" + AssembleUtil.LINE_SEPARATOR;
        assertEquals(expectedResult,service.assembleJavaDoc(javaDocs,1));

        javaDocs.add("yyy");
        expectedResult = AssembleUtil.DEFAULT_INDEX_STR + "/**" + AssembleUtil.LINE_SEPARATOR +
                AssembleUtil.DEFAULT_INDEX_STR + " * xxx" + AssembleUtil.LINE_SEPARATOR +
                AssembleUtil.DEFAULT_INDEX_STR + " * yyy" + AssembleUtil.LINE_SEPARATOR +
                AssembleUtil.DEFAULT_INDEX_STR + " */" + AssembleUtil.LINE_SEPARATOR;
        assertEquals(expectedResult,service.assembleJavaDoc(javaDocs,1));
    }

    /**
     * Method: assembleClassAnnotation(Set<String> annotations)
     */
    @Test
    public void testAssembleClassAnnotation() throws Exception {
        TemplateBasedCodeAssembleServiceImpl assembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        assertTrue(StringUtils.isBlank(assembleService.assembleClassAnnotation(null,0)));
        assertTrue(StringUtils.isBlank(assembleService.assembleClassAnnotation(new HashSet<String>(),0)));

        Set<String> annotations = new HashSet<>();
        annotations.add("@XXX");

        String expected = "@XXX" + AssembleUtil.LINE_SEPARATOR;

        assertEquals(expected,assembleService.assembleClassAnnotation(annotations,-1));

        annotations.add("@YYY");
        expected = "@XXX" + AssembleUtil.LINE_SEPARATOR
                + "@YYY" + AssembleUtil.LINE_SEPARATOR;
        assertEquals(expected,assembleService.assembleClassAnnotation(annotations,-1));


        annotations = new HashSet<>();
        annotations.add("@XXX");

        expected = "@XXX" + AssembleUtil.LINE_SEPARATOR;

        assertEquals(expected,assembleService.assembleClassAnnotation(annotations,0));

        annotations.add("@YYY");
        expected = "@XXX" + AssembleUtil.LINE_SEPARATOR
                + "@YYY" + AssembleUtil.LINE_SEPARATOR;
        assertEquals(expected,assembleService.assembleClassAnnotation(annotations,0));


        annotations = new HashSet<>();
        annotations.add("@XXX");

        expected = AssembleUtil.DEFAULT_INDEX_STR + "@XXX" + AssembleUtil.LINE_SEPARATOR;

        assertEquals(expected,assembleService.assembleClassAnnotation(annotations,1));

        annotations.add("@YYY");
        expected = AssembleUtil.DEFAULT_INDEX_STR + "@XXX" + AssembleUtil.LINE_SEPARATOR
                + AssembleUtil.DEFAULT_INDEX_STR +  "@YYY" + AssembleUtil.LINE_SEPARATOR;
        assertEquals(expected,assembleService.assembleClassAnnotation(annotations,1));
    }

    /**
     * Method: assembleMethodAnnotation(Set<String> annotations)
     */
    @Test
    public void testAssembleMethodAnnotation() throws Exception {

    }

    /**
     * Method: assembleFieldAnnotation(Set<String> annotations)
     */
    @Test
    public void testAssembleFieldAnnotation() throws Exception {

    }

    /**
     * Method: assembleParameterAnnotation(Set<String> annotations)
     */
    @Test
    public void testAssembleParameterAnnotation() throws Exception {
        TemplateBasedCodeAssembleServiceImpl assembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        assertTrue(StringUtils.isBlank(assembleService.assembleParameterAnnotation(null,0)));
        assertTrue(StringUtils.isBlank(assembleService.assembleParameterAnnotation(new HashSet<String>(),0)));

        Set<String> annotations = new HashSet<>();
        annotations.add("@XXX");

        String expected = "@XXX ";

        assertEquals(expected,assembleService.assembleParameterAnnotation(annotations,-1));

        annotations.add("@YYY");
        expected = "@XXX "
                + "@YYY ";
        assertEquals(expected,assembleService.assembleParameterAnnotation(annotations,-1));


        annotations = new HashSet<>();
        annotations.add("@XXX");

        expected = "@XXX ";

        assertEquals(expected,assembleService.assembleParameterAnnotation(annotations,0));

        annotations.add("@YYY");
        expected = "@XXX "
                + "@YYY ";
        assertEquals(expected,assembleService.assembleParameterAnnotation(annotations,0));


        annotations = new HashSet<>();
        annotations.add("@XXX");

        expected = AssembleUtil.DEFAULT_INDEX_STR + "@XXX ";

        assertEquals(expected,assembleService.assembleParameterAnnotation(annotations,1));

        annotations.add("@YYY");
        expected = AssembleUtil.DEFAULT_INDEX_STR + "@XXX "
                + AssembleUtil.DEFAULT_INDEX_STR +  "@YYY ";
        assertEquals(expected,assembleService.assembleParameterAnnotation(annotations,1));
    }
}
