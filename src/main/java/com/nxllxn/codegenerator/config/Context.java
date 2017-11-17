package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.codegen.generator.AbstractCodeGenerator;
import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import com.nxllxn.codegenerator.exception.NeedForImplementException;
import com.nxllxn.codegenerator.utils.AssembleUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 配置信息上下文对象，包含了全局的配置信息
 *
 * @author wenchao
 */
public class Context extends AbstractConfiguration implements Configuration {
    /**
     * 日志 SLF4J + LOG4J
     */
    private Logger logger = LoggerFactory.getLogger(Context.class);

    /**
     * 是否自动用分隔符包裹关键字
     */
    private Boolean autoDelimitKeyWords;

    /**
     * java文件编码
     */
    private String fileEncoding;

    /**
     * 开始分隔符
     */
    private String beginningDelimiter;

    /**
     * 结束分隔符
     */
    private String endingDelimiter;

    /**
     * 待生成项目基本信息
     */
    private ProjectBaseInfoConfiguration projectBaseInfoConfiguration;

    /**
     * 待生成项目maven坐标相关配置
     */
    private MavenCoordinateConfiguration mavenCoordinateConfiguration;

    /**
     * 一个JDBC连接配置信息
     */
    private JDBCConnectionConfiguration jdbcConnectionConfiguration;


    /**
     * Java类型处理器相关配置，Java类型处理器用于JdbcType和JavaType之间的相互转换
     */
    private JavaTypeResolverConfiguration javaTypeResolverConfiguration;

    /**
     * Java实体类生成器相关配置
     */
    private JavaEntityGeneratorConfiguration javaModelGeneratorConfiguration;

    /**
     * Java 字符串常量Key生成器相关配置
     */
    private KeyGeneratorConfiguration keyGeneratorConfiguration;

    /**
     * sql xml 文件生成器相关配置
     */
    private SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration;

    /**
     * mybatis mapper生成器相关配置
     */
    private JavaClientGeneratorConfiguration javaClientGeneratorConfiguration;

    /**
     * service接口生成器相关配置
     */
    private ServiceGeneratorConfiguration serviceGeneratorConfiguration;

    /**
     * service接口实现类生成器相关配置
     */
    private ServiceImplGeneratorConfiguration serviceImplGeneratorConfiguration;

    /**
     * Web controller接口类生成器相关配置
     */
    private ControllerGeneratorConfiguration controllerGeneratorConfiguration;

    /**
     * 异常处理模块类生成器相关配置
     */
    private ExceptionGeneratorConfiguration exceptionGeneratorConfiguration;

    /**
     * Spring boot application 主类生成器相关配置
     */
    private ApplicationGeneratorConfiguration applicationGeneratorConfiguration;

    /**
     * 配置文件生成器相关配置
     */
    private ConfigCodeGeneratorConfiguration configCodeGeneratorConfiguration;

    /**
     * pom文件生成器相关配置
     */
    private PomGeneratorConfiguration pomGeneratorConfiguration;

    /**
     * 评论生成器相关配置
     */
    private CommentGeneratorConfiguration commentGeneratorConfiguration;

    /**
     * 插件相关配置
     */
    private List<PluginConfiguration> pluginConfigurations;

    /**
     * 表结构信息转换相关配置
     */
    private List<TableConfiguration> tableConfigurations;


    public Context(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);

        this.pluginConfigurations = new ArrayList<>();
        this.tableConfigurations = new ArrayList<>();
    }

    public List<AbstractCodeGenerator> getCodeGenerators() {
        List<TypeHolder> typeHolders = new ArrayList<>();
        typeHolders.add(this.getJavaModelGeneratorConfiguration());
        typeHolders.add(this.getKeyGeneratorConfiguration());
        typeHolders.add(this.getJavaClientGeneratorConfiguration());
        typeHolders.add(this.getSqlMapGeneratorConfiguration());
        typeHolders.add(this.getServiceGeneratorConfiguration());
        typeHolders.add(this.getServiceImplGeneratorConfiguration());
        typeHolders.add(this.getControllerGeneratorConfiguration());
        typeHolders.add(this.getExceptionGeneratorConfiguration());
        typeHolders.add(this.getConfigCodeGeneratorConfiguration());
        typeHolders.add(this.getApplicationGeneratorConfiguration());
        typeHolders.add(this.getPomGeneratorConfiguration());

        List<AbstractCodeGenerator> codeGenerators = new ArrayList<>();
        AbstractCodeGenerator codeGenerator;
        for (TypeHolder typeHolder : typeHolders) {
            codeGenerator = typeHolder.loadCodeGeneratorByType();

            if (codeGenerator == null) {
                continue;
            }

            codeGenerators.add(codeGenerator);
        }

        return codeGenerators;
    }

    @Override
    public void fromXmlNode(Node element) {
        NodeList nodeList = element.getChildNodes();

        int length = nodeList.getLength();
        Node childNode;
        String nodeName;
        for (int index = 0; index < length; index++) {
            childNode = nodeList.item(index);

            //如果不是元素节点，跳过
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            nodeName = childNode.getNodeName();
            if (StringUtils.isBlank(nodeName)) {
                continue;
            }

            switch (nodeName) {
                case XmlConstantsRegistry.NODE_NAME_PROPERTIES:
                    ExtraPropertiesConfiguration extraPropertiesConfiguration = new ExtraPropertiesConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    //递归,解析外部属性资源文件
                    extraPropertiesConfiguration.fromXmlNode(childNode);
                    break;
                //普通属性节点
                case XmlConstantsRegistry.NODE_NAME_PROPERTY:
                    //解析当前节点自定义属性
                    parseProperty(childNode);
                    break;
                case XmlConstantsRegistry.NODE_NAME_PROJECT_BASE_INFO:
                    ProjectBaseInfoConfiguration projectBaseInfoConfiguration = new ProjectBaseInfoConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    projectBaseInfoConfiguration.fromXmlNode(childNode);

                    this.projectBaseInfoConfiguration = projectBaseInfoConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_MAVEN_COORDINATE:
                    MavenCoordinateConfiguration mavenCoordinateConfiguration = new MavenCoordinateConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    mavenCoordinateConfiguration.fromXmlNode(childNode);

                    this.mavenCoordinateConfiguration = mavenCoordinateConfiguration;
                    break;
                //JDBC连接信息配置节点
                case XmlConstantsRegistry.NODE_NAME_JDBC_CONNECTION:
                    JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    //递归，JDBCConnectionConfiguration类负责JDBC相关的配置信息解析
                    jdbcConnectionConfiguration.fromXmlNode(childNode);

                    this.jdbcConnectionConfiguration = jdbcConnectionConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_JAVA_TYPE_RESOLVER:
                    JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    javaTypeResolverConfiguration.fromXmlNode(childNode);

                    this.javaTypeResolverConfiguration = javaTypeResolverConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_JAVA_MODEL_GENERATOR:
                    JavaEntityGeneratorConfiguration javaModelGeneratorConfiguration = new JavaEntityGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    javaModelGeneratorConfiguration.fromXmlNode(childNode);

                    this.javaModelGeneratorConfiguration = javaModelGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_KEY_DEFINITION_GENERATOR:
                    KeyGeneratorConfiguration keyGeneratorConfiguration = new KeyGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    keyGeneratorConfiguration.fromXmlNode(childNode);

                    this.keyGeneratorConfiguration = keyGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_SQL_MAP_GENERATOR:
                    SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    sqlMapGeneratorConfiguration.fromXmlNode(childNode);

                    this.sqlMapGeneratorConfiguration = sqlMapGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_JAVA_CLIENT_GENERATOR:
                    JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    javaClientGeneratorConfiguration.fromXmlNode(childNode);

                    this.javaClientGeneratorConfiguration = javaClientGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_SERVICE_GENERATOR:
                    ServiceGeneratorConfiguration serviceGeneratorConfiguration = new ServiceGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    serviceGeneratorConfiguration.fromXmlNode(childNode);

                    this.serviceGeneratorConfiguration = serviceGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_SERVICE_IMPL_GENERATOR:
                    ServiceImplGeneratorConfiguration serviceImplGeneratorConfiguration = new ServiceImplGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    serviceImplGeneratorConfiguration.fromXmlNode(childNode);

                    this.serviceImplGeneratorConfiguration = serviceImplGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_CONTROLLER_GENERATOR:
                    ControllerGeneratorConfiguration controllerGeneratorConfiguration = new ControllerGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    controllerGeneratorConfiguration.fromXmlNode(childNode);

                    this.controllerGeneratorConfiguration = controllerGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_APPLICATION_GENERATOR:
                    ApplicationGeneratorConfiguration applicationGeneratorConfiguration = new ApplicationGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    applicationGeneratorConfiguration.fromXmlNode(childNode);

                    this.applicationGeneratorConfiguration = applicationGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_EXCEPTION_HANDLER_GENERATOR:
                    ExceptionGeneratorConfiguration exceptionGeneratorConfiguration = new ExceptionGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    exceptionGeneratorConfiguration.fromXmlNode(childNode);

                    this.exceptionGeneratorConfiguration = exceptionGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_CONFIG_GENERATOR:
                    ConfigCodeGeneratorConfiguration configCodeGeneratorConfiguration = new ConfigCodeGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    configCodeGeneratorConfiguration.fromXmlNode(childNode);

                    this.configCodeGeneratorConfiguration = configCodeGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_POM_GENERATOR:
                    PomGeneratorConfiguration pomGeneratorConfiguration = new PomGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    pomGeneratorConfiguration.fromXmlNode(childNode);

                    this.pomGeneratorConfiguration = pomGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_COMMENT_GENERATOR:
                    CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    commentGeneratorConfiguration.fromXmlNode(childNode);

                    this.commentGeneratorConfiguration = commentGeneratorConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_PLUGIN:
                    PluginConfiguration pluginConfiguration = new PluginConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    pluginConfiguration.fromXmlNode(childNode);

                    pluginConfigurations.add(pluginConfiguration);
                    break;
                case XmlConstantsRegistry.NODE_NAME_TABLE:
                    TableConfiguration tableConfiguration = new TableConfiguration(
                            systemProperties, extraProperties, configProperties
                    );

                    tableConfiguration.fromXmlNode(childNode);

                    tableConfigurations.add(tableConfiguration);
                    break;
                default:
                    logger.info("待实现模块名称：" + nodeName);
                    throw new NeedForImplementException();
            }
        }
    }

    @Override
    protected void parseProperty(Node node) {
        Properties attributes = parseProperties(node);

        String name = attributes.getProperty("name");
        String value = attributes.getProperty("value");

        if (StringUtils.isBlank(name)) {
            return;
        }

        customProperties.setProperty(name, value);

        if (name.equals(XmlConstantsRegistry.NODE_NAME_AUTO_DELIMIT_KEYWORDS)) {
            autoDelimitKeyWords = Boolean.valueOf(value);
        } else if (name.equals(XmlConstantsRegistry.NODE_NAME_JAVA_FILE_ENCODING)) {
            fileEncoding = value;
        } else if (name.equals(XmlConstantsRegistry.NODE_NAME_BEGINNING_DELIMITER)) {
            beginningDelimiter = value;
        } else if (name.equals(XmlConstantsRegistry.NODE_NAME_ENDING_DELIMITER)) {
            endingDelimiter = value;
        }
    }

    @Override
    public Element toXmlNode() {
        throw new NeedForImplementException();
    }

    @Override
    public void validate() {
        throw new NeedForImplementException();
    }

    public JDBCConnectionConfiguration getJdbcConnectionConfiguration() {
        return jdbcConnectionConfiguration;
    }

    public void setJdbcConnectionConfiguration(JDBCConnectionConfiguration jdbcConnectionConfiguration) {
        this.jdbcConnectionConfiguration = jdbcConnectionConfiguration;
    }

    public Boolean getAutoDelimitKeyWords() {
        return autoDelimitKeyWords;
    }

    public void setAutoDelimitKeyWords(Boolean autoDelimitKeyWords) {
        this.autoDelimitKeyWords = autoDelimitKeyWords;
    }

    public String getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public String getBeginningDelimiter() {
        return beginningDelimiter;
    }

    public void setBeginningDelimiter(String beginningDelimiter) {
        this.beginningDelimiter = beginningDelimiter;
    }

    public String getEndingDelimiter() {
        return endingDelimiter;
    }

    public void setEndingDelimiter(String endingDelimiter) {
        this.endingDelimiter = endingDelimiter;
    }

    public JavaTypeResolverConfiguration getJavaTypeResolverConfiguration() {
        return javaTypeResolverConfiguration;
    }

    public void setJavaTypeResolverConfiguration(JavaTypeResolverConfiguration javaTypeResolverConfiguration) {
        this.javaTypeResolverConfiguration = javaTypeResolverConfiguration;
    }

    public JavaEntityGeneratorConfiguration getJavaModelGeneratorConfiguration() {
        if (javaModelGeneratorConfiguration == null) {
            javaModelGeneratorConfiguration = new JavaEntityGeneratorConfiguration(
                    systemProperties, extraProperties, configProperties
            );

            javaModelGeneratorConfiguration.setTargetPackage(
                    projectBaseInfoConfiguration.getRootPackage() + AssembleUtil.PACKAGE_SEPERATOR + "entities"
            );
            javaModelGeneratorConfiguration.setTargetDirectory(projectBaseInfoConfiguration.getSourceDirectory());
        }

        return javaModelGeneratorConfiguration;
    }

    public void setJavaModelGeneratorConfiguration(JavaEntityGeneratorConfiguration javaModelGeneratorConfiguration) {
        this.javaModelGeneratorConfiguration = javaModelGeneratorConfiguration;
    }

    public SqlMapGeneratorConfiguration getSqlMapGeneratorConfiguration() {
        if (sqlMapGeneratorConfiguration == null) {
            sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration(
                    systemProperties, extraProperties, configProperties
            );

            sqlMapGeneratorConfiguration.setTargetPackage(
                    projectBaseInfoConfiguration.getRootPackage()
                            + AssembleUtil.PACKAGE_SEPERATOR + "mapper"
                            + AssembleUtil.PACKAGE_SEPERATOR + "xml"
            );
            sqlMapGeneratorConfiguration.setTargetDirectory(projectBaseInfoConfiguration.getSourceDirectory());
            sqlMapGeneratorConfiguration.setXmlDocumentPublicId("-//mybatis.org//DTD Mapper 3.0//EN");
            sqlMapGeneratorConfiguration.setXmlDocumentSystemId("http://mybatis.org/dtd/mybatis-3-mapper.dtd");
        }

        return sqlMapGeneratorConfiguration;
    }

    public void setSqlMapGeneratorConfiguration(SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration) {
        this.sqlMapGeneratorConfiguration = sqlMapGeneratorConfiguration;
    }

    public JavaClientGeneratorConfiguration getJavaClientGeneratorConfiguration() {
        if (javaClientGeneratorConfiguration == null) {
            javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration(
                    systemProperties, extraProperties, configProperties
            );

            javaClientGeneratorConfiguration.setTargetPackage(
                    projectBaseInfoConfiguration.getRootPackage() + AssembleUtil.PACKAGE_SEPERATOR + "mapper"
            );
            javaClientGeneratorConfiguration.setTargetDirectory(projectBaseInfoConfiguration.getSourceDirectory());
        }

        return javaClientGeneratorConfiguration;
    }

    public void setJavaClientGeneratorConfiguration(JavaClientGeneratorConfiguration javaClientGeneratorConfiguration) {
        this.javaClientGeneratorConfiguration = javaClientGeneratorConfiguration;
    }

    public CommentGeneratorConfiguration getCommentGeneratorConfiguration() {
        return commentGeneratorConfiguration;
    }

    public void setCommentGeneratorConfiguration(CommentGeneratorConfiguration commentGeneratorConfiguration) {
        this.commentGeneratorConfiguration = commentGeneratorConfiguration;
    }

    public List<PluginConfiguration> getPluginConfigurations() {
        return pluginConfigurations;
    }

    public void setPluginConfigurations(List<PluginConfiguration> pluginConfigurations) {
        this.pluginConfigurations = pluginConfigurations;
    }

    public List<TableConfiguration> getTableConfigurations() {
        return tableConfigurations;
    }

    public void setTableConfigurations(List<TableConfiguration> tableConfigurations) {
        this.tableConfigurations = tableConfigurations;
    }

    public KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        return keyGeneratorConfiguration;
    }

    public void setKeyGeneratorConfiguration(KeyGeneratorConfiguration keyGeneratorConfiguration) {
        this.keyGeneratorConfiguration = keyGeneratorConfiguration;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public ServiceGeneratorConfiguration getServiceGeneratorConfiguration() {
        if (serviceGeneratorConfiguration == null) {
            serviceGeneratorConfiguration = new ServiceGeneratorConfiguration(
                    systemProperties, extraProperties, configProperties
            );

            serviceGeneratorConfiguration.setTargetPackage(
                    projectBaseInfoConfiguration.getRootPackage()
                            + AssembleUtil.PACKAGE_SEPERATOR + "service"
            );
            serviceGeneratorConfiguration.setTargetDirectory(projectBaseInfoConfiguration.getSourceDirectory());
        }

        return serviceGeneratorConfiguration;
    }

    public void setServiceGeneratorConfiguration(ServiceGeneratorConfiguration serviceGeneratorConfiguration) {
        this.serviceGeneratorConfiguration = serviceGeneratorConfiguration;
    }

    public ServiceImplGeneratorConfiguration getServiceImplGeneratorConfiguration() {
        if (serviceImplGeneratorConfiguration == null) {
            serviceImplGeneratorConfiguration = new ServiceImplGeneratorConfiguration(
                    systemProperties, extraProperties, configProperties
            );

            serviceImplGeneratorConfiguration.setTargetPackage(
                    projectBaseInfoConfiguration.getRootPackage()
                            + AssembleUtil.PACKAGE_SEPERATOR + "service"
                            + AssembleUtil.PACKAGE_SEPERATOR + "impl"
            );
            serviceImplGeneratorConfiguration.setTargetDirectory(projectBaseInfoConfiguration.getSourceDirectory());
        }

        return serviceImplGeneratorConfiguration;
    }

    public void setServiceImplGeneratorConfiguration(ServiceImplGeneratorConfiguration serviceImplGeneratorConfiguration) {
        this.serviceImplGeneratorConfiguration = serviceImplGeneratorConfiguration;
    }

    public ControllerGeneratorConfiguration getControllerGeneratorConfiguration() {
        if (controllerGeneratorConfiguration == null) {
            controllerGeneratorConfiguration = new ControllerGeneratorConfiguration(
                    systemProperties, extraProperties, configProperties
            );

            controllerGeneratorConfiguration.setTargetPackage(
                    projectBaseInfoConfiguration.getRootPackage()
                            + AssembleUtil.PACKAGE_SEPERATOR + "controller"
            );
            controllerGeneratorConfiguration.setTargetDirectory(projectBaseInfoConfiguration.getSourceDirectory());
        }

        return controllerGeneratorConfiguration;
    }

    public void setControllerGeneratorConfiguration(ControllerGeneratorConfiguration controllerGeneratorConfiguration) {
        this.controllerGeneratorConfiguration = controllerGeneratorConfiguration;
    }

    public ExceptionGeneratorConfiguration getExceptionGeneratorConfiguration() {
        if (exceptionGeneratorConfiguration == null) {
            exceptionGeneratorConfiguration = new ExceptionGeneratorConfiguration(
                    systemProperties, extraProperties, configProperties
            );

            exceptionGeneratorConfiguration.setTargetPackage(
                    projectBaseInfoConfiguration.getRootPackage()
                            + AssembleUtil.PACKAGE_SEPERATOR + "exception"
            );
            exceptionGeneratorConfiguration.setTargetDirectory(projectBaseInfoConfiguration.getSourceDirectory());
        }

        return exceptionGeneratorConfiguration;
    }

    public void setExceptionGeneratorConfiguration(ExceptionGeneratorConfiguration exceptionGeneratorConfiguration) {
        this.exceptionGeneratorConfiguration = exceptionGeneratorConfiguration;
    }

    public ApplicationGeneratorConfiguration getApplicationGeneratorConfiguration() {
        if (applicationGeneratorConfiguration == null) {
            applicationGeneratorConfiguration = new ApplicationGeneratorConfiguration(
                    systemProperties, extraProperties, configProperties
            );

            applicationGeneratorConfiguration.setTargetPackage(
                    projectBaseInfoConfiguration.getRootPackage()
            );
            applicationGeneratorConfiguration.setTargetDirectory(projectBaseInfoConfiguration.getSourceDirectory());
        }

        return applicationGeneratorConfiguration;
    }

    public void setApplicationGeneratorConfiguration(ApplicationGeneratorConfiguration applicationGeneratorConfiguration) {
        this.applicationGeneratorConfiguration = applicationGeneratorConfiguration;
    }

    public ConfigCodeGeneratorConfiguration getConfigCodeGeneratorConfiguration() {
        if (configCodeGeneratorConfiguration == null) {
            configCodeGeneratorConfiguration = new ConfigCodeGeneratorConfiguration(
                    systemProperties, extraProperties, configProperties
            );

            configCodeGeneratorConfiguration.setTargetDirectory(projectBaseInfoConfiguration.getResourceDirectory());
        }

        return configCodeGeneratorConfiguration;
    }

    public void setConfigCodeGeneratorConfiguration(ConfigCodeGeneratorConfiguration configCodeGeneratorConfiguration) {
        this.configCodeGeneratorConfiguration = configCodeGeneratorConfiguration;
    }

    public PomGeneratorConfiguration getPomGeneratorConfiguration() {
        if (pomGeneratorConfiguration == null) {
            pomGeneratorConfiguration = new PomGeneratorConfiguration(
                    systemProperties, extraProperties, configProperties
            );

            pomGeneratorConfiguration.setTargetDirectory("");
            pomGeneratorConfiguration.setPublicId("-//mybatis.org//DTD Mapper 3.0//EN");
            pomGeneratorConfiguration.setSystemId("http://mybatis.org/dtd/mybatis-3-mapper.dtd");
            pomGeneratorConfiguration.setNamespace("http://maven.apache.org/POM/4.0.0");
            pomGeneratorConfiguration.setXsiNamespace("http://www.w3.org/2001/XMLSchema-instance");
        }

        return pomGeneratorConfiguration;
    }

    public void setPomGeneratorConfiguration(PomGeneratorConfiguration pomGeneratorConfiguration) {
        this.pomGeneratorConfiguration = pomGeneratorConfiguration;
    }

    public ProjectBaseInfoConfiguration getProjectBaseInfoConfiguration() {
        return projectBaseInfoConfiguration;
    }

    public void setProjectBaseInfoConfiguration(ProjectBaseInfoConfiguration projectBaseInfoConfiguration) {
        this.projectBaseInfoConfiguration = projectBaseInfoConfiguration;
    }

    public MavenCoordinateConfiguration getMavenCoordinateConfiguration() {
        return mavenCoordinateConfiguration;
    }

    public void setMavenCoordinateConfiguration(MavenCoordinateConfiguration mavenCoordinateConfiguration) {
        this.mavenCoordinateConfiguration = mavenCoordinateConfiguration;
    }

    @Override
    public String toString() {
        return "Context{" +
                "extraProperties=" + extraProperties +
                ", autoDelimitKeyWords=" + autoDelimitKeyWords +
                ", configProperties=" + configProperties +
                ", fileEncoding='" + fileEncoding + '\'' +
                ", customProperties=" + customProperties +
                ", beginningDelimiter='" + beginningDelimiter + '\'' +
                ", endingDelimiter='" + endingDelimiter + '\'' +
                ", jdbcConnectionConfiguration=" + jdbcConnectionConfiguration +
                ", javaTypeResolverConfiguration=" + javaTypeResolverConfiguration +
                ", javaModelGeneratorConfiguration=" + javaModelGeneratorConfiguration +
                ", keyGeneratorConfiguration=" + keyGeneratorConfiguration +
                ", sqlMapGeneratorConfiguration=" + sqlMapGeneratorConfiguration +
                ", javaClientGeneratorConfiguration=" + javaClientGeneratorConfiguration +
                ", serviceGeneratorConfiguration=" + serviceGeneratorConfiguration +
                ", serviceImplGeneratorConfiguration=" + serviceImplGeneratorConfiguration +
                ", controllerGeneratorConfiguration=" + controllerGeneratorConfiguration +
                ", commentGeneratorConfiguration=" + commentGeneratorConfiguration +
                ", pluginConfigurations=" + pluginConfigurations +
                ", tableConfigurations=" + tableConfigurations +
                '}';
    }
}
