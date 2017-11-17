package com.nxllxn.codegenerator.tpl;

/**
 * 模板名称
 */
public enum TemplateName {
    /**
     * 普通字段评论模板
     */
    TEMPLATE_NORMAL_FIELD_COMMENT,

    /**
     * 实体字段评论模板
     */
    TEMPLATE_ENTITY_FIELD_COMMENT,

    /**
     * 实体类评论模板
     */
    TEMPLATE_CLASS_COMMENT,

    /**
     * getter评论模板
     */
    TEMPLATE_GETTER_COMMENT,

    /**
     * setter评论模板
     */
    TEMPLATE_SETTER_COMMENT;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
