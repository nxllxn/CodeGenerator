package com.nxllxn.codegenerator.tpl;

import com.nxllxn.codegenerator.exception.NeedForImplementException;
import com.nxllxn.codegenerator.utils.ResourceFinder;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模板工厂，用于加载程序中各种代码模板
 */
public class TemplateFactory {
    /**
     * 用于存储模板的
     */
    private Map<TemplateName, Template> templateMap = new ConcurrentHashMap<>();

    private static class Holder {
        private static TemplateFactory templateFactory = new TemplateFactory();
    }

    public static TemplateFactory getSingleInstance() {
        return Holder.templateFactory;
    }


    private TemplateFactory() {
        synchronized (TemplateFactory.class) {
            init();
        }
    }

    /**
     * 模板工厂初始化方法，负责加载全部的模板资源
     */
    private void init() {
        try {
            File classPathRoot = new File(TemplateFactory.class.getResource("/").getFile());

            if (!classPathRoot.exists()) {
                return;
            }

            List<String> includes = Collections.singletonList(Template.TEMPLATE_FILE_EXTENSION);

            List<File> templateFiles = new ResourceFinder().findResources(classPathRoot, includes);

            InputStream inputStream;
            ByteArrayOutputStream byteArrayOutputStream;
            for (File templateFile : templateFiles) {
                try {
                    //获取当前文件名称对应的枚举定义以及当前文件的内容，然后建立map映射关系
                    inputStream = new FileInputStream(templateFile);

                    byteArrayOutputStream = new ByteArrayOutputStream();

                    int length;
                    byte[] buffer = new byte[1024];
                    while ((length = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, length);
                    }

                    TemplateName templateName = TemplateNameFactory.create(getRealFileName(templateFile.getName()));
                    Template template = new AbstractTemplate().withTemplate(new String(byteArrayOutputStream.toByteArray()));

                    templateMap.put(templateName, template);
                } catch (Exception e) {
                    throw new NeedForImplementException();
                }
            }
        } catch (Exception e) {
            throw new NeedForImplementException();
        }
    }

    /**
     * 根据文件路径得到文件名称（不带扩展名）
     *
     * @param fullFileName 文件路径
     * @return 文件名称（不带扩展名）
     */
    private String getRealFileName(String fullFileName) {
        if (StringUtils.isBlank(fullFileName)) {
            return null;
        }

        int lastFileSeparatorPosition = fullFileName.lastIndexOf(File.separator);
        int lastDotPosition = fullFileName.lastIndexOf('.');

        if (lastDotPosition == -1
                || lastFileSeparatorPosition >= lastDotPosition) {
            return null;
        }

        return fullFileName.substring(lastFileSeparatorPosition + 1, lastDotPosition);
    }


    /**
     * 从模板工厂中取出指定名称对应的模板
     *
     * @param templateName 指定模板名称
     * @return 指定名称对应的模板
     */
    public Template getTemplate(TemplateName templateName) {
        return templateMap.get(templateName);
    }

    public static void main(String[] args) {
        TemplateFactory templateFactory = TemplateFactory.getSingleInstance();

        Template template = templateFactory.getTemplate(TemplateName.TEMPLATE_CLASS_COMMENT);

        System.out.println(template.render("User实体类","version 1.0,2017-09-06","ningwenchao"));
    }
}
