package com.nxllxn.codegenerator.codegen.resource;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Yaml 格式的配置文件
 */
public class YamlResource extends AbstractResourceUnit {
    private Map<String, Object> configurationMap;

    private static final String CONFIG_PATH_SEPARATOR = "\\.";

    public YamlResource() {
        configurationMap = new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public void appendConfiguration(String key, Object value) {
        if (StringUtils.isBlank(key) || value == null) {
            return;
        }

        String[] keyParts = key.split(CONFIG_PATH_SEPARATOR);
        Map<String, Object> configMap = configurationMap;
        Map<String, Object> subMap;
        int index = 0;
        for (; index < keyParts.length - 1; index++) {
            Object obj = configMap.get(keyParts[index]);

            if (!(obj instanceof Map)) {
                subMap = new LinkedHashMap<>();

                configMap.put(keyParts[index], subMap);

                configMap = subMap;
            } else {
                configMap = (LinkedHashMap<String, Object>) obj;
            }
        }

        configMap.put(keyParts[index], value);
    }

    @Override
    public String getFormattedContent() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(4);
        options.setPrettyFlow(true);
        options.setSplitLines(true);
        Yaml yaml = new Yaml(options);

        return yaml.dump(configurationMap);
    }

    @Override
    public String getFileExtension() {
        return ".yml";
    }
}
