package com.nxllxn.codegenerator.codegen.resource;

/**
 * 资源文件格式定义接口抽象实现类
 *
 * @author wenchao
 */
public abstract class AbstractResourceUnit implements ResourceUnit {
    private String fileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getResourceFileName() {
        return fileName;
    }
}
