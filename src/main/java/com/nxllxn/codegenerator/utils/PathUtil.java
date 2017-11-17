package com.nxllxn.codegenerator.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class PathUtil {
    /**
     * 根据包名称生成文件相对于classpath的路径
     * @param targetPackage 文件所在报名
     */
    public static String packageNameToPath(String targetPackage) {
        if (StringUtils.isBlank(targetPackage)){
            return "";
        }

        return targetPackage.replace(AssembleUtil.PACKAGE_SEPERATOR, File.separator);
    }
}
