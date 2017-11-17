package com.nxllxn.codegenerator.codegen.generated;


import com.nxllxn.codegenerator.codegen.resource.ResourceUnit;

/**
 * 生成的配置文件描述类
 *
 * @author wenchao
 */
public class GeneratedConfigFile extends AbstractGeneratedFile {
    private ResourceUnit resourceUnit;

    public void setResourceUnit(ResourceUnit resourceUnit) {
        this.resourceUnit = resourceUnit;
    }

    @Override
    public String getFormattedContent() {
        return resourceUnit.getFormattedContent();
    }

    @Override
    public String getFileName() {
        return resourceUnit.getResourceFileName();
    }

    @Override
    public String getFileExtension() {
        return resourceUnit.getFileExtension();
    }
}
