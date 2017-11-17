package com.nxllxn.codegenerator.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 指定类型的资源文件查找、过滤函数
 */
public class ResourceFinder{
    /**
     * 查找指定类型的资源文件
     * @param rootFile 待查找的根目录
     * @param includes 需要过滤的资源文件类型（后缀名不包括'.'）
     * @return 指定类型的资源文件集合
     * @throws Exception IOException
     */
    public List<File> findResources(File rootFile, List<String> includes) throws Exception{
        List<File> resources = new ArrayList<>();

        findResourcesHelper(rootFile,includes,resources);

        return resources;
    }

    /**
     * 私有函数，用于递归的查找指定类型的资源文件
     * @param rootFile 待查找的根目录
     * @param includes 需要过滤的资源文件类型（后缀名不包括'.'）
     * @param resources 用于存储查找结果的集合
     * @throws Exception IOException
     */
    private void findResourcesHelper(File rootFile,List<String> includes,List<File> resources) throws Exception{
        if (rootFile == null || ! rootFile.exists() || includes == null || includes.isEmpty()){
            return;
        }

        if (rootFile.isFile()){
            String fileExtension = getFileExtension(rootFile.getName());

            if (!includes.contains(fileExtension)){
                return;
            }

            if (StringUtils.isBlank(fileExtension)){
                return;
            }

            if (includes.contains(fileExtension)){
                resources.add(rootFile);
            }
        }

        if (rootFile.isDirectory()){
            File [] subFiles = rootFile.listFiles();

            if (subFiles == null){
                return;
            }

            for (File subFile:subFiles){
                if (subFile == null || !subFile.exists()){
                    continue;
                }

                findResourcesHelper(subFile,includes,resources);
            }
        }
    }

    /**
     * 获取文件后缀名
     * @param fileName 文件名称
     * @return 文件后缀名不包括'.'
     */
    private String getFileExtension(String fileName){
        if (StringUtils.isBlank(fileName)){
            return null;
        }

        int lastDotPosition = fileName.lastIndexOf(".");
        if (lastDotPosition == -1){
            return null;
        }

        return fileName.substring(
                Math.min(lastDotPosition + 1,fileName.length() - 1)
        );
    }
}
