package com.nxllxn.codegenerator.config.xml;

/**
 * Xml配置文件相关配置常量定义
 */
public class XmlConstantsRegistry {
    /**
     * 默认的xml文件publicId
     */
    public static final String DEFAULT_XML_PUBLIC_ID = "";

    /**
     * 额外配置文件资源路径
     */
    public static final String EXTRA_PROPERTIES_RESOURCE = "resource";

    /**
     * 额外配置文件url路径
     */
    public static final String EXTRA_PROPERTIES_URL = "url";

    /**
     * 属性节点名称
     */
    public static final String NODE_NAME_PROPERTY = "property";

    /**
     * 是否自动用分隔符包裹关键字
     */
    public static final String NODE_NAME_AUTO_DELIMIT_KEYWORDS = "autoDelimitKeywords";

    /**
     * java文件编码
     */
    public static final String NODE_NAME_JAVA_FILE_ENCODING = "javaFileEncoding";

    /**
     * 开始分隔符
     */
    public static final String NODE_NAME_BEGINNING_DELIMITER = "beginningDelimiter";

    /**
     * 结束分隔符
     */
    public static final String NODE_NAME_ENDING_DELIMITER = "endingDelimiter";


    /**
     * 属性资源文件节点名称
     */
    public static final String NODE_NAME_PROPERTIES = "properties";

    /**
     * JDBC配置节点名称
     */
    public static final String NODE_NAME_JDBC_CONNECTION = "jdbcConnection";

    /**
     * JDBC驱动类名称（全限定类名）
     */
    public static final String NODE_NAME_DRIVER_CLASS_NAME = "driverClassName";

    /**
     * JDBC连接url
     */
    public static final String NODE_NAME_CONNECTION_URL = "connectionURL";

    /**
     * JDBC连接使用登陆用户名
     */
    public static final String NODE_NAME_USER_NAME = "userName";

    /**
     * JDBC连接使用登陆密码
     */
    public static final String NODE_NAME_USER_PWD = "password";

    /**
     * jdbc type <--> java type 类型处理器
     */
    public static final String NODE_NAME_JAVA_TYPE_RESOLVER = "javaTypeResolver";

    /**
     * java实体类代码生成器
     */
    public static final String NODE_NAME_JAVA_MODEL_GENERATOR = "javaModelGenerator";

    /**
     * java sql Xml 文件代码生成器
     */
    public static final String NODE_NAME_SQL_MAP_GENERATOR = "sqlMapGenerator";

    /**
     * java Mapper文件代码生成器
     */
    public static final String NODE_NAME_JAVA_CLIENT_GENERATOR = "javaClientGenerator";

    public static final String NODE_NAME_KEY_DEFINITION_GENERATOR = "keyDefinitionGenerator";
    public static final String NODE_NAME_PROJECT_BASE_INFO = "projectBaseInfo";
    public static final String ATTR_NAME_PROJECT_NAME = "projectName";
    public static final String ATTR_NAME_SOURCE_DIRECTORY = "sourceDirectory";
    public static final String ATTR_NAME_RESOURCE_DIRECTORY = "resourcesDirectory";
    public static final String ATTR_NAME_ROOT_PACKAGE = "rootPackage";

    public static final String NODE_NAME_MAVEN_COORDINATE = "mavenCoordinate";
    public static final String ATTR_NAME_GROUP_ID = "groupId";
    public static final String ATTR_NAME_ARTIFACT_ID = "artifactId";
    public static final String ATTR_NAME_VERSION= "version";

    public static final String NODE_NAME_SERVICE_GENERATOR = "serviceGenerator";
    public static final String NODE_NAME_SERVICE_IMPL_GENERATOR = "serviceImplGenerator";
    public static final String NODE_NAME_CONTROLLER_GENERATOR = "controllerGenerator";
    public static final String NODE_NAME_APPLICATION_GENERATOR = "applicationGenerator";
    public static final String NODE_NAME_CONFIG_GENERATOR = "configGenerator";
    public static final String NODE_NAME_POM_GENERATOR = "pomGenerator";
    public static final String NODE_NAME_EXCEPTION_HANDLER_GENERATOR = "exceptionHandlerGenerator";

    /**
     * 代码注视代码生成器
     */
    public static final String NODE_NAME_COMMENT_GENERATOR = "commentGenerator";

    /**
     * 用户自定义插件
     */
    public static final String NODE_NAME_PLUGIN = "plugin";

    /**
     * 数据库表配置相关节点
     */
    public static final String NODE_NAME_TABLE = "table";

    /**
     * 表名-->实体名重命名规则节点
     */
    public static final String NODE_NAME_TABLE_RENAMING = "tableRenaming";

    /**
     * generated key sql 配置节点
     */
    public static final String NODE_NAME_GENERATED_KEY_SQL = "generatedKeySql";

    /**
     * 列重命名节点
     */
    public static final String NODE_NAME_COLUMN_RENAMING = "columnRenaming";

    /**
     * 列覆盖节点
     */
    public static final String NODE_NAME_COLUMN_OVERRIDE = "columnOverride";

    /**
     * 列忽略
     */
    public static final String NODE_NAME_COLUMN_IGNORE = "columnIgnore";

    /**
     * 类型，通常是一个类的全限定类名
     */
    public static final String ATTR_NAME_TYPE = "type";

    /**
     * 文件存储的目标目录
     */
    public static final String ATTR_NAME_TARGET_DIRECTORY = "targetDirectory";

    /**
     * 文件存储的目标包
     */
    public static final String ATTR_NAME_TARGET_PACKAGE = "targetPackage";

    /**
     * 表名称
     */
    public static final String ATTR_NAME_TABLE_NAME = "tableName";

    /**
     * 实体名称
     */
    public static final String ATTR_NAME_ENTITY_NAME = "entityName";

    /**
     * 待匹配字符串
     */
    public static final String ATTR_NAME_SEARCH_FOR = "searchFor";

    /**
     * 用于替换的字符串
     */
    public static final String ATTR_NAME_REPLACE_WITH = "replaceWith";

    /**
     * 列名称
     */
    public static final String ATTR_NAME_COLUMN = "column";

    /**
     * sql声明
     */
    public static final String ATTR_NAME_SQL_STATEMENT = "sqlStatement";

    /**
     * 属性名称
     */
    public static final String ATTR_NAME_PROPERTY = "property";

    /**
     * java类型
     */
    public static final String ATTR_NAME_JAVA_TYPE = "javaType";

    /**
     * jdbc类型
     */
    public static final String ATTR_NAME_JDBC_TYPE = "jdbcType";

    /**
     * 类型处理器
     */
    public static final String ATTR_NAME_TYPE_HANDLER = "typeHandler";

    public static final String XML_PUBLIC_ID = "publicId";
    public static final String XML_SYSTEM_ID = "systemId";
    public static final String MYBATIS_MAPPER_XML_NAME_SPACE_PACKAGE = "systemId";

    public static final String XML_NAMESPACE = "namespace";
    public static final String XML_XSI_NAMESPACE = "xsiNamespace";
}
