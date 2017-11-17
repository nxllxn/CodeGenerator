package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Properties;

/**
 * 待生成项目maven坐标相关配置
 *
 * @author wenchao
 */
public class MavenCoordinateConfiguration extends AbstractConfiguration {
    /**
     * groupId
     */
    private String groupId;
    private static final String DEFAULT_GROUP_ID = "com.foo";

    /**
     * artifactId
     */
    private String artifactId;
    private static final String DEFAULT_ARTIFACT_ID = "bar";

    /**
     * version
     */
    private String version;
    private static final String DEFAULT_VARSION = "1.0.0-SNAPSHOT";

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public MavenCoordinateConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }

    @Override
    public void fromXmlNode(Node element) {
        Properties properties = parseProperties(element);

        groupId = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_GROUP_ID);
        artifactId = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_ARTIFACT_ID);
        version = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_VERSION);
    }

    @Override
    public Element toXmlNode() {
        return null;
    }

    @Override
    public void validate() {

    }

    public String getGroupId() {
        if (StringUtils.isBlank(groupId)) {
            return DEFAULT_GROUP_ID;
        }

        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        if (StringUtils.isBlank(artifactId)) {
            return DEFAULT_ARTIFACT_ID;
        }

        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        if (StringUtils.isBlank(version)) {
            return DEFAULT_VARSION;
        }

        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "MavenCoordinateConfiguration{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
