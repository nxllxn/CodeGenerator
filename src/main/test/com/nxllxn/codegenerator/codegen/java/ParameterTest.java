package com.nxllxn.codegenerator.codegen.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Parameter Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 20, 2017</pre>
 */
public class ParameterTest {
    private Parameter parameterWithName;

    @Before
    public void before() throws Exception {
        parameterWithName = new Parameter();

        parameterWithName.setName("parameterName");
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getAnnotations()
     */
    @Test
    public void testGetAnnotations() throws Exception {

    }

    /**
     * Method: setAnnotations(Set<String> annotations)
     */
    @Test
    public void testSetAnnotations() throws Exception {

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
     * Method: isVarAs()
     */
    @Test
    public void testIsVarAs() throws Exception {

    }

    /**
     * Method: setVarAs(boolean varAs)
     */
    @Test
    public void testSetVarAs() throws Exception {

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {

    }

    /**
     * Method: getFormattedContent()
     */
    @Test
    public void testGetFormattedContent() throws Exception {
        Parameter parameter = new Parameter();

        assertEquals("",parameter.getFormattedContent());

        parameter.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("Bar ",parameter.getFormattedContent());

        parameter.setName("parameterName");
        assertEquals("Bar parameterName",parameter.getFormattedContent());

        parameter.setVarAs(true);
        assertEquals("Bar... parameterName",parameter.getFormattedContent());

        parameter.addAnnotation("@XXX");
        assertEquals("@XXX Bar... parameterName",parameter.getFormattedContent());
    }

    @Test
    public void testWithoutName(){
        Parameter parameter = new Parameter();

        assertEquals("",parameter.getFormattedContent());
    }

    @Test
    public void testWithName(){
        Parameter parameter = new Parameter();

        parameter.setName("parameterName");

        assertEquals("parameterName",parameter.getFormattedContent());
    }

    @Test
    public void testType(){
        parameterWithName.setType(null);
        assertEquals("parameterName",parameterWithName.getFormattedContent());

        parameterWithName.setType(new FullyQualifiedJavaType("int"));
        assertEquals("int parameterName",parameterWithName.getFormattedContent());

        parameterWithName.setType(new FullyQualifiedJavaType("java.lang.Integer"));
        assertEquals("Integer parameterName",parameterWithName.getFormattedContent());

        parameterWithName.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("Bar parameterName",parameterWithName.getFormattedContent());
    }

    @Test
    public void testVarArgs(){
        parameterWithName.setType(new FullyQualifiedJavaType("com.foo.Bar"));

        parameterWithName.setVarAs(false);
        assertEquals("Bar parameterName",parameterWithName.getFormattedContent());

        parameterWithName.setVarAs(true);
        assertEquals("Bar... parameterName",parameterWithName.getFormattedContent());
    }

    @Test
    public void testAnnotations(){
        parameterWithName.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("Bar parameterName",parameterWithName.getFormattedContent());

        parameterWithName.addAnnotation("@XXX");
        assertEquals("@XXX Bar parameterName",parameterWithName.getFormattedContent());
    }
}
