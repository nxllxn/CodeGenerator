package com.nxllxn.codegenerator.service;

import com.nxllxn.codegenerator.config.JDBCConnectionConfiguration;
import com.nxllxn.codegenerator.config.MavenCoordinateConfiguration;
import com.nxllxn.codegenerator.config.ProjectBaseInfoConfiguration;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public interface CodeGeneratedService {
    /**
     * 代码生成接口
     *
     * @param projectBaseInfoConfiguration 项目基本信息
     * @param mavenCoordinateConfiguration 项目maven坐标信息
     * @param jdbcConnectionConfiguration  jdbc连接信息
     * @param outputStream                 HttpServletResponse输出流
     */
    void generateCode(ProjectBaseInfoConfiguration projectBaseInfoConfiguration,
                      MavenCoordinateConfiguration mavenCoordinateConfiguration,
                      JDBCConnectionConfiguration jdbcConnectionConfiguration,
                      OutputStream outputStream) throws IOException,SQLException;
}
