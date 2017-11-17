package com.nxllxn.codegenerator.tpl;

import com.nxllxn.codegenerator.exception.UnknownCorrespondingTemplateNameException;
import org.apache.commons.lang3.StringUtils;

/**
 * 模板名称工厂
 */
public class TemplateNameFactory {
    /**
     * 根据文件名称找到对应的TemplateName定义
     * @param type 文件名称（模板名称字符串定义）
     * @return 对应的模板名称定义
     * @throws Exception UnknownCorrespondingTemplateNameException 如果没有对应的枚举定义，抛出此异常
     */
    public static TemplateName create(String type) throws Exception{
        if (StringUtils.isBlank(type)){
            return null;
        }

        try {
            return TemplateName.valueOf(type.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new UnknownCorrespondingTemplateNameException();
        }
    }
}
