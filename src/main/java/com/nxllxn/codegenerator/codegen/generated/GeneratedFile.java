package com.nxllxn.codegenerator.codegen.generated;

import java.io.File;
import java.io.IOException;

/**
 * 生成文件接口
 *
 * @author wenchao
 */
public interface GeneratedFile {
    /**
     * 获取当前文件内容
     *
     * @return 当前文件代码内容
     */
    String getFormattedContent();

    /**
     * 获取当前文件所在文件夹
     *
     * @return 当前文件所在文件夹
     */
    String getTargetDirectory();

    /**
     * 获取待写入文件文件名称
     * @return 文件名称
     */
    String getFileName();

    /**
     * 获取待写入文件扩展名
     *
     * @return 待写入文件扩展名
     */
    String getFileExtension();

    /**
     * 获取待写入的文件对象
     *
     * @return 待写入的文件对象
     */
    File getWriteToFile() throws IOException;

    String getRelativeFilePath();

    String getFileEncoding();
}
