package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 数据库表结构相关配置，用于控制表结构到实体类的转换
 */
public class TableConfiguration extends AbstractConfiguration {
    /**
     * 指定的表名称 可以使用通配符
     */
    private String tableName;

    /**
     * 对应的实体类的名称
     */
    private String entityName;

    /**
     * tableName --> entityName重命名规则
     */
    private List<TableRenamingConfiguration> tableRenamingConfigurations;

    /**
     * generated key sql 语法sql
     */
    private GeneratedKeySqlConfiguration generatedKeySqlConfiguration;

    /**
     * 列名称到实体属性名称重命名规则
     */
    private List<ColumnRenamingConfiguration> columnRenamingConfigurations;

    /**
     * 列配置覆盖，包括属性名称覆盖，类型覆盖
     */
    private List<ColumnOverrideConfiguration> columnOverrideConfigurations;

    /**
     * 列忽略
     */
    private List<ColumnIgnoreConfiguration> columnIgnoreConfigurations;

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public TableConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);

        this.tableRenamingConfigurations = new ArrayList<>();
        this.columnRenamingConfigurations = new ArrayList<>();
        this.columnIgnoreConfigurations = new ArrayList<>();
        this.columnOverrideConfigurations = new ArrayList<>();
    }

    @Override
    public void fromXmlNode(Node element) {
        Properties properties = parseProperties(element);

        this.tableName = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_TABLE_NAME);
        this.entityName = properties.getProperty(XmlConstantsRegistry.ATTR_NAME_ENTITY_NAME);

        NodeList nodes = element.getChildNodes();

        Node node;
        for (int index = 0,length = nodes.getLength();index < length;index ++){
            node = nodes.item(index);

            //如果不是元素节点而是普通的文本节点，那么直接跳过
            if (node.getNodeType() != Node.ELEMENT_NODE){
                continue;
            }

            switch (node.getNodeName()){
                case XmlConstantsRegistry.NODE_NAME_TABLE_RENAMING:
                    TableRenamingConfiguration tableRenamingConfiguration = new TableRenamingConfiguration(
                            systemProperties,extraProperties,configProperties
                    );

                    tableRenamingConfiguration.fromXmlNode(node);

                    this.tableRenamingConfigurations.add(tableRenamingConfiguration);
                    break;
                case XmlConstantsRegistry.NODE_NAME_GENERATED_KEY_SQL:
                    GeneratedKeySqlConfiguration generatedKeySqlConfiguration = new GeneratedKeySqlConfiguration(
                            systemProperties,extraProperties,configProperties
                    );

                    generatedKeySqlConfiguration.fromXmlNode(node);

                    this.generatedKeySqlConfiguration = generatedKeySqlConfiguration;
                    break;
                case XmlConstantsRegistry.NODE_NAME_COLUMN_RENAMING:
                    ColumnRenamingConfiguration columnRenamingConfiguration = new ColumnRenamingConfiguration(
                            systemProperties,extraProperties,configProperties
                    );

                    columnRenamingConfiguration.fromXmlNode(node);

                    columnRenamingConfigurations.add(columnRenamingConfiguration);
                    break;
                case XmlConstantsRegistry.NODE_NAME_COLUMN_OVERRIDE:
                    ColumnOverrideConfiguration columnOverrideConfiguration = new ColumnOverrideConfiguration(
                            systemProperties,extraProperties,configProperties
                    );

                    columnOverrideConfiguration.fromXmlNode(node);

                    columnOverrideConfigurations.add(columnOverrideConfiguration);
                    break;
                case XmlConstantsRegistry.NODE_NAME_COLUMN_IGNORE:
                    ColumnIgnoreConfiguration columnIgnoreConfiguration = new ColumnIgnoreConfiguration(
                            systemProperties,extraProperties,configProperties
                    );

                    columnIgnoreConfiguration.fromXmlNode(node);

                    columnIgnoreConfigurations.add(columnIgnoreConfiguration);
                    break;
            }
        }

    }

    @Override
    public Element toXmlNode() {
        return null;
    }

    @Override
    public void validate() {

    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<TableRenamingConfiguration> getTableRenamingConfigurations() {
        return tableRenamingConfigurations;
    }

    public void setTableRenamingConfiguration(List<TableRenamingConfiguration> tableRenamingConfigurations) {
        this.tableRenamingConfigurations = tableRenamingConfigurations;
    }

    public GeneratedKeySqlConfiguration getGeneratedKeySqlConfiguration() {
        return generatedKeySqlConfiguration;
    }

    public void setGeneratedKeySqlConfiguration(GeneratedKeySqlConfiguration generatedKeySqlConfiguration) {
        this.generatedKeySqlConfiguration = generatedKeySqlConfiguration;
    }

    public List<ColumnRenamingConfiguration> getColumnRenamingConfigurations() {
        return columnRenamingConfigurations;
    }

    public void setColumnRenamingConfigurations(List<ColumnRenamingConfiguration> columnRenamingConfigurations) {
        this.columnRenamingConfigurations = columnRenamingConfigurations;
    }

    public List<ColumnOverrideConfiguration> getColumnOverrideConfigurations() {
        return columnOverrideConfigurations;
    }

    public void setColumnOverrideConfigurations(List<ColumnOverrideConfiguration> columnOverrideConfigurations) {
        this.columnOverrideConfigurations = columnOverrideConfigurations;
    }

    public List<ColumnIgnoreConfiguration> getColumnIgnoreConfigurations() {
        return columnIgnoreConfigurations;
    }

    public void setColumnIgnoreConfigurations(List<ColumnIgnoreConfiguration> columnIgnoreConfigurations) {
        this.columnIgnoreConfigurations = columnIgnoreConfigurations;
    }

    @Override
    public String toString() {
        return "TableConfiguration{" +
                "tableName='" + tableName + '\'' +
                ", entityName='" + entityName + '\'' +
                ", tableRenamingConfigurations=" + tableRenamingConfigurations +
                ", generatedKeySqlConfiguration=" + generatedKeySqlConfiguration +
                ", columnRenamingConfigurations=" + columnRenamingConfigurations +
                ", columnOverrideConfigurations=" + columnOverrideConfigurations +
                ", columnIgnoreConfigurations=" + columnIgnoreConfigurations +
                '}';
    }
}
