package com.nxllxn.codegenerator.codegen.resource;

/**
 * 資源文件格式定义接口
 */
public interface ResourceUnit {
    /**
     * 获取当前资源文件内容
     * @return 当前资源文件文本内容字符串
     */
    String getFormattedContent();

    /**
     * 获取资源文件名称
     * @return 资源文件名称
     */
    String getResourceFileName();

    /**
     * 获取资源文件扩展名，如.yml,.yaml
     * @return 资源文件扩展名
     */
    String getFileExtension();
}
