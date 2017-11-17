package com.nxllxn.codegenerator.jdbc.java;

import com.nxllxn.codegenerator.config.JDBCConnectionConfiguration;
import com.nxllxn.codegenerator.jdbc.ConnectionFactory;
import com.nxllxn.codegenerator.message.Messages;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnectionFactory implements ConnectionFactory {
    private JDBCConnectionConfiguration jdbcConnectionConfiguration;

    public JDBCConnectionFactory(JDBCConnectionConfiguration jdbcConnectionConfiguration) {
        this.jdbcConnectionConfiguration = jdbcConnectionConfiguration;
    }

    @Override
    public Connection openConnection() throws SQLException{
        Properties props = new Properties();

        if (jdbcConnectionConfiguration.getProps() != null){
            props.putAll(jdbcConnectionConfiguration.getProps());
        }

        if (!StringUtils.isBlank(jdbcConnectionConfiguration.getUserName())) {
            props.setProperty("user", jdbcConnectionConfiguration.getUserName());
        }

        if (!StringUtils.isBlank(jdbcConnectionConfiguration.getPassword())) {
            props.setProperty("password", jdbcConnectionConfiguration.getPassword());
        }

        Driver driver = getDriver(jdbcConnectionConfiguration.getDriverClassName());
        Connection conn = driver.connect(jdbcConnectionConfiguration.getConnectionUrl(), props);

        if (conn == null) {
            throw new RuntimeException(Messages.forMessage(Messages.MessageKey.NEED_FOR_IMPLEMENT));
        }

        return conn;
    }

    public Driver getDriver(String driverClassName) {
        try {
            Class cls = Class.forName(driverClassName);

            return (Driver) cls.newInstance();
        }catch (InstantiationException | IllegalAccessException | ClassNotFoundException e){
            e.printStackTrace();

            throw new RuntimeException(Messages.forMessage(Messages.MessageKey.NEED_FOR_IMPLEMENT));
        }
    }
}
