package com.nxllxn.codegenerator.codegen.xml;

/**
 * XML 元，用于定义Xml元素通用接口
 *
 * @author wenchao
 */
public interface XmlUnit {
    /**
     * 获取当前节点格式化的内容
     * @return 格式化的xml代码
     */
    String getFormattedContent();

    /**
     * 按照指定缩进等级组装Xml代码
     * @param indentLevel 指定缩进等级
     * @return 格式化的xml代码
     */
    String getFormattedContent(int indentLevel);

    /**
     * 判断当前元素是否有子节点
     * @return 存在子节点返回true，否则返回false
     */
    boolean hasChildNode();
}
