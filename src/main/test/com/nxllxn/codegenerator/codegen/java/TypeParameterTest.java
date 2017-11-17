package com.nxllxn.codegenerator.codegen.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TypeParameter Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 21, 2017</pre>
 */
public class TypeParameterTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getExtendsType()
     */
    @Test
    public void testGetExtendsType() throws Exception {

    }

    /**
     * Method: setExtendsType(List<FullyQualifiedJavaType> extendsType)
     */
    @Test
    public void testSetExtendsType() throws Exception {

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
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }

    @Test
    public void testConstructor() {
        TypeParameter typeParameter = new TypeParameter("T");
        assertNotNull(typeParameter);
        assertEquals("T", typeParameter.getName());
        assertNotNull(typeParameter.getExtendsTypes());
        assertEquals(0, typeParameter.getExtendsTypes().size());
    }

    @Test
    public void testConstructorWIthExtends() {
        FullyQualifiedJavaType list = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType compare = new FullyQualifiedJavaType("java.util.Comparator");

        TypeParameter typeParameter = new TypeParameter("T", Arrays.asList(list, compare));
        assertNotNull(typeParameter);
        assertEquals("T", typeParameter.getName());
        assertNotNull(typeParameter.getExtendsTypes());
        assertEquals(2, typeParameter.getExtendsTypes().size());
    }

    @Test
    public void testGetFormattedContent() {
        FullyQualifiedJavaType list = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType compare = new FullyQualifiedJavaType("java.util.Comparator");

        TypeParameter typeParameter = new TypeParameter("T", Arrays.asList(list, compare));
        assertNotNull(typeParameter);
        assertEquals("T extends List & Comparator", typeParameter.getFormattedContent());
    }
} 
