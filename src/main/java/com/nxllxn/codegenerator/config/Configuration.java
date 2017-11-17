package com.nxllxn.codegenerator.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 配置信息接口
 */
public interface Configuration {
    /**
     * 从某个XMl节点中加载配置信息，更新当前实例
     * @param element 指定XML节点s
     */
    void fromXmlNode(Node element);

    /**
     * 将当前实例对应的配置信息转换为对应的XMl节点
     * @return Xml节点信息
     */
    Element toXmlNode();

    /**
     * 对解析得到的Xml配置信息进行校验
     */
    void validate();
}
