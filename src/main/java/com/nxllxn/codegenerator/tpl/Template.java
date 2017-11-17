package com.nxllxn.codegenerator.tpl;

/**
 * 代码模块实体类
 */
public abstract class Template {
    /**
     * 模板文件后缀名
     */
    public static final String TEMPLATE_FILE_EXTENSION = "tpl";

    /**
     * 模板字符串
     */
    protected String template;

    /**
     *  模板渲染函数
     * @param attrs 模板渲染使用的参数
     * @return 渲染后的模板字符串
     */
    public abstract String render(Object ... attrs);

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * builder 构造模板字符串
     * @param template 模板字符串
     * @return 模板对象
     */
    public Template withTemplate(String template) {
        this.template = template;

        return this;
    }

    @Override
    public String toString() {
        return "Template{" +
                "template='" + template + '\'' +
                '}';
    }
}
