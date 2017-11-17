package com.nxllxn.codegenerator.message;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 系统异常警告信息定义
 *
 * @author wenchao
 */
public class Messages {
    /**
     * 外化资源文件路径
     */
    private static final String BUNDLE_NAME = "com.nxllxn.codegenerator.message.messages";

    /**
     * 外部资源文件Bundle
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * 禁止生成实例
     */
    private Messages() {
    }

    /**
     * 加载指定messageKey对应的异常信息
     * @param messageKey 指定messageKey
     * @return messageKey对应的异常信息
     */
    public static String forMessage(MessageKey messageKey) {
        return forMessage(messageKey, (Object[]) null);
    }

    /**
     * 加载指定字符串key对应的异常信息
     * @param key 加载指定字符串key
     * @return 指定字符串key对应的异常信息
     */
    public static String forMessage(String key) {
        return forMessage(key, (Object[]) null);
    }

    /**
     * 加载指定异常信息messageKey对应的异常信息模板，并进行初始化
     *
     * @param messageKey 指定异常信息Key
     * @param params     格式化参数
     * @return 格式化后的异常信息
     */
    public static String forMessage(MessageKey messageKey, Object... params) {
        return forMessage(messageKey.toString(), params);
    }

    /**
     * 加载指定异常信息key对应的异常信息模板，并进行初始化
     *
     * @param key    指定字符串key
     * @param params 格式化参数
     * @return 格式化后的异常信息
     */
    private static String forMessage(String key, Object... params) {
        try {
            if (StringUtils.isBlank(key)){
                return null;
            }

            String messageFormat = RESOURCE_BUNDLE.getString(key);

            if (params == null || params.length == 0) {
                return messageFormat;
            }

            return MessageFormat.format(messageFormat, params);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * 一场信息Key枚举定义
     */
    public enum MessageKey {
        NEED_FOR_IMPLEMENT("need_for_implement");

        private String key;

        MessageKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }
}
