package com.nxllxn.codegenerator.tpl;

/**
 * 抽象模板类
 */
public class AbstractTemplate extends Template {
    /**
     *  默认的占位符
     */
    public static final String PLACE_HOLDER_PATTERN = "\\{}";

    @Override
    public String render(Object ... attrs) {
        String strRendered = template;

        for (Object attr:attrs){
            strRendered = strRendered.replaceFirst(PLACE_HOLDER_PATTERN,String.valueOf(attr));
        }

        return strRendered;
    }
}
