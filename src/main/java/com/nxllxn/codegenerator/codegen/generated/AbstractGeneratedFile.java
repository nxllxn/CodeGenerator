package com.nxllxn.codegenerator.codegen.generated;

import com.nxllxn.codegenerator.utils.PathUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * 生成文件接口的抽象实现，用于提供一些默认实现
 *
 * @author wenchao
 */
public abstract class AbstractGeneratedFile implements GeneratedFile {
    /**
     * 获取当前工作目录路径
     */
    private static final String WOKE_PLACE_DIR_PROPERTY_NAME = "user.dir";

    /**
     * 文件存放的目标目录
     */
    protected String targetDir;

    /**
     * 文件包名称
     */
    protected String targetPackage;

    /**
     * 文件编码
     */
    protected String fileEncoding;

    @Override
    public File getWriteToFile() throws IOException {
        return new File(getFilePath());
    }

    /**
     * 获取当前文件路径
     * @return 当前文件路径
     */
    private String getFilePath() {
        StringBuilder writeToFilePathBuilder = new StringBuilder();

        appendSubPathToFilePath(writeToFilePathBuilder,System.getProperty(WOKE_PLACE_DIR_PROPERTY_NAME));

        appendSubPathToFilePath(writeToFilePathBuilder,targetDir == null ? "" : targetDir);

        appendSubPathToFilePath(writeToFilePathBuilder, PathUtil.packageNameToPath(targetPackage));

        if (writeToFilePathBuilder.length() != 0){
            writeToFilePathBuilder.append(File.separator);
        }

        writeToFilePathBuilder.append(getFileName());

        writeToFilePathBuilder.append(getFileExtension());

        return writeToFilePathBuilder.toString().replaceAll("\\s*","");
    }

    @Override
    public String getRelativeFilePath() {
        StringBuilder writeToFilePathBuilder = new StringBuilder();

        appendSubPathToFilePath(writeToFilePathBuilder,targetDir == null ? "" : targetDir);

        appendSubPathToFilePath(writeToFilePathBuilder, PathUtil.packageNameToPath(targetPackage));

        if (writeToFilePathBuilder.length() != 0){
            writeToFilePathBuilder.append(File.separator);
        }

        writeToFilePathBuilder.append(getFileName());

        writeToFilePathBuilder.append(getFileExtension());

        return writeToFilePathBuilder.toString().replaceAll("\\s*","");
    }

    /**
     * 将指定路径填充到文件路径中
     * @param writeToFilePathBuilder 文件路径builder
     * @param subPath 待添加路径
     */
    protected void appendSubPathToFilePath(StringBuilder writeToFilePathBuilder, String subPath){
        if (writeToFilePathBuilder == null || StringUtils.isBlank(subPath)){
            return;
        }

        if (writeToFilePathBuilder.length() == 0){
            writeToFilePathBuilder.append(subPath);

            return;
        }

        //如果待append路径第一个字符是文件分隔符,那么删除第一个字符
        while (isFileSeparatorInSpecIndex(subPath,0)){
            subPath = subPath.substring(1);
        }

        //如果当前路径最后面不是一个文件路径分隔符
        if (!isFileSeparatorInSpecIndex(writeToFilePathBuilder,writeToFilePathBuilder.length() - 1)){
            writeToFilePathBuilder.append(File.separator);
        }

        //如果待append路径最后一个字符是文件分隔符,那么删除最后一个字符
        while (isFileSeparatorInSpecIndex(subPath,subPath.length() - 1)){
            subPath = subPath.substring(0,subPath.length() - 1);
        }

        writeToFilePathBuilder.append(subPath);
    }

    /**
     * 判断指定字符序列指定位置是不是文件分隔符
     * @param charSequence 指定字符序列
     * @param index 指定索引位置
     * @return 如果指定位置字符是File.separator（注意此处需要考虑到平台兼容性）,那么返回true，否则返回false
     */
    private boolean isFileSeparatorInSpecIndex(CharSequence charSequence, int index) {
        if (charSequence.length() == 0){
            return false;
        }

        if(index < 0 || index > charSequence.length() - 1){
            throw new StringIndexOutOfBoundsException();
        }

        return charSequence.charAt(index) == File.separatorChar;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    @Override
    public String getTargetDirectory() {
        return targetDir;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    @Override
    public String toString() {
        return getRelativeFilePath();
    }
}
