package com.nxllxn.codegenerator.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 对象工厂，用于生成对象实例或者加载外部资源
 */
public class ObjectFactory {
    private static List<ClassLoader> externalClassLoaders;

    static {
        externalClassLoaders = new ArrayList<>();
    }

    /**
     * 静态工具类，不需要构造函数
     */
    private ObjectFactory(){

    }

    /**
     * 缓存ClassLoader
     * @param classLoader 待缓存ClassLoader
     */
    public static synchronized void addExternalClassLoaders(ClassLoader classLoader){
        externalClassLoaders.add(classLoader);
    }

    /**
     * 清除缓存
     */
    public static void clear(){
        externalClassLoaders.clear();
    }

    /**
     * 利用外部类加载器，上下文类加载器，当前类类加载器加载指定资源resourceName
     * @param resourceName 指定资源文件名称
     * @return 指定资源文件Url
     */
    public static URL getResource(String resourceName){
        if (StringUtils.isBlank(resourceName)){
            return null;
        }

        URL resourceUrl;

        for (ClassLoader classLoader:externalClassLoaders){
            resourceUrl = classLoader.getResource(resourceName);
            if (resourceUrl != null){
                return resourceUrl;
            }
        }

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        resourceUrl = contextClassLoader.getResource(resourceName);
        if (resourceUrl != null){
            return resourceUrl;
        }

        resourceUrl = ObjectFactory.class.getResource(resourceName);

        return resourceUrl;
    }
}
