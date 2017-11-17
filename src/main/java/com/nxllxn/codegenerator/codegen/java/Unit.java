package com.nxllxn.codegenerator.codegen.java;

/**
 * 元，用于定义元素共有的一些接口
 *
 * @author wenchao
 */
public interface Unit {
    /**
     * 标识当前类型是否一个类
     * @return 如果当前类型一个类，返回true
     */
    boolean isClass();

    /**
     * 标识当前类型是否为一个接口
     * @return 如果当前类型是一个接口，返回true
     */
    boolean isInterface();

    /**
     * 标识当前类型是否为枚举类型
     * @return 如果当前类型是枚举类型,返回true
     */
    boolean isEnumeration();


    /**
     * 获取当前类型格式化的代码字符串
     * @return 当前类型对应的代码
     */
    String getFormattedContent();

    /**
     * 获取当前类型格式化的代码字符串
     *
     * @param indentLevel 缩进等级
     *
     * @return 当前类型对应的代码
     */
    String getFormattedContent(int indentLevel);
}
