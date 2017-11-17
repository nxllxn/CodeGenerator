package com.nxllxn.codegenerator.codegen.xml;

import com.nxllxn.codegenerator.utils.AssembleUtil;

/**
 * XMLUnit的中抽象实现类，用于提供一些基本实现
 *
 * @author wenchao
 */
public abstract class AbstractXmlUnit implements XmlUnit {
    @Override
    public String getFormattedContent() {
        return getFormattedContent(AssembleUtil.DEFAULT_INDENT_LEVEL);
    }
}
