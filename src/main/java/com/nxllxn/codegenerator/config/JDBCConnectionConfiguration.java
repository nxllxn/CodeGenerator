package com.nxllxn.codegenerator.config;

import com.nxllxn.codegenerator.config.xml.XmlConstantsRegistry;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Properties;

public class JDBCConnectionConfiguration extends AbstractConfiguration implements Configuration {
    /**
     * 驱动程序全限定类名称
     */
    private String driverClassName;

    /**
     * 连接数据库的url
     */
    private String connectionUrl;

    /**
     * 数据库登陆用户名
     */
    private String userName;

    /**
     * 数据库登陆密码
     */
    private String password;

    /**
     * 额外参数，比如allowMultipleQuery
     */
    private Properties props;

    /**
     * 构造函数
     *
     * @param systemProperties 系统环境变量中的属性
     * @param extraProperties  用户自定义属性
     * @param configProperties 从配置资源中加载的属性
     */
    public JDBCConnectionConfiguration(Properties systemProperties, Properties extraProperties, Properties configProperties) {
        super(systemProperties, extraProperties, configProperties);
    }

    @Override
    public void fromXmlNode(Node node) {
        Properties attributes = parseProperties(node);

        String driverClassName = attributes.getProperty(XmlConstantsRegistry.NODE_NAME_DRIVER_CLASS_NAME);
        String connectionUrl = attributes.getProperty(XmlConstantsRegistry.NODE_NAME_CONNECTION_URL);
        String userName = attributes.getProperty(XmlConstantsRegistry.NODE_NAME_USER_NAME);
        String password = attributes.getProperty(XmlConstantsRegistry.NODE_NAME_USER_PWD);


        this.driverClassName = driverClassName;
        this.connectionUrl = connectionUrl;

        if (!StringUtils.isBlank(userName)){
            this.userName = userName;
        }

        if (!StringUtils.isBlank(password)){
            this.password = password;
        }

        this.props = parseCustomProperties(node);
    }

    @Override
    public Element toXmlNode() {
        return null;
    }

    @Override
    public void validate() {

    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    @Override
    public String toString() {
        return "JDBCConnectionConfiguration{" +
                "driverClassName='" + driverClassName + '\'' +
                ", connectionUrl='" + connectionUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", props=" + props +
                '}';
    }
}
