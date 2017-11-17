package com.nxllxn.codegenerator.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nxllxn.codegenerator.config.JDBCConnectionConfiguration;
import com.nxllxn.codegenerator.config.MavenCoordinateConfiguration;
import com.nxllxn.codegenerator.config.ProjectBaseInfoConfiguration;
import com.nxllxn.codegenerator.exception.EmptyRequestBodyException;
import com.nxllxn.codegenerator.exception.ParamMissingException;
import com.nxllxn.codegenerator.service.CodeGeneratedService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Properties;

@RestController
public class CodeGeneratorController {
    private CodeGeneratedService codeGeneratedService;

    @Autowired
    public CodeGeneratorController(CodeGeneratedService codeGeneratedService) {
        this.codeGeneratedService = codeGeneratedService;
    }

    @PostMapping("/generateCode")
    public void generateCode(@RequestBody String requestParamJsonStr,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) throws SQLException {
        if (StringUtils.isBlank(requestParamJsonStr)) {
            throw new EmptyRequestBodyException();
        }

        JSONObject requestParamJsonObj = JSON.parseObject(requestParamJsonStr);
        if (requestParamJsonObj == null || requestParamJsonObj.isEmpty()) {
            throw new EmptyRequestBodyException();
        }

        String dbHost = requestParamJsonObj.getString("db_host");
        if (StringUtils.isBlank(dbHost)){
            throw new ParamMissingException("抱歉，数据库服务器主机地址或IP不能为空！");
        }

        String dbName = requestParamJsonObj.getString("db_name");
        if (StringUtils.isBlank(dbName)){
            throw new ParamMissingException("抱歉，待操作数据库名称不能为空！");
        }

        String dbUserName = requestParamJsonObj.getString("db_user_name");
        String dbPassword = requestParamJsonObj.getString("db_password");

        String projectName = requestParamJsonObj.getString("project_name");
        if (StringUtils.isBlank(projectName)){
            throw new ParamMissingException("抱歉，项目名称不能为空！");
        }

        String rootPackage = requestParamJsonObj.getString("root_package");
        if (StringUtils.isBlank(rootPackage)){
            throw new ParamMissingException("抱歉，根包名不能为空！");
        }

        String sourceDirectory = requestParamJsonObj.getString("source_directory");
        String resourceDirectory = requestParamJsonObj.getString("resource_directory");

        String groupId = requestParamJsonObj.getString("group_id");
        String artifactId = requestParamJsonObj.getString("artifact_id");
        String version = requestParamJsonObj.getString("version");

        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration(
                new Properties(), new Properties(), new Properties()
        );
        jdbcConnectionConfiguration.setConnectionUrl(
                String.format("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf8&noAccessToProcedureBodies=true", dbHost, dbName));
        jdbcConnectionConfiguration.setUserName(dbUserName);
        jdbcConnectionConfiguration.setPassword(dbPassword);

        ProjectBaseInfoConfiguration projectBaseInfoConfiguration = new ProjectBaseInfoConfiguration(
                new Properties(), new Properties(), new Properties()
        );
        projectBaseInfoConfiguration.setProjectName(projectName);
        projectBaseInfoConfiguration.setSourceDirectory(sourceDirectory);
        projectBaseInfoConfiguration.setResourceDirectory(resourceDirectory);
        projectBaseInfoConfiguration.setRootPackage(rootPackage);

        MavenCoordinateConfiguration mavenCoordinateConfiguration = new MavenCoordinateConfiguration(
                new Properties(), new Properties(), new Properties()
        );
        mavenCoordinateConfiguration.setGroupId(groupId);
        mavenCoordinateConfiguration.setArtifactId(artifactId);
        mavenCoordinateConfiguration.setVersion(version);

        OutputStream responseOutputStream = null;
        try {
            responseOutputStream = httpServletResponse.getOutputStream();

            setChineseHeader(httpServletRequest, httpServletResponse, projectName + ".zip");

            codeGeneratedService.generateCode(
                    projectBaseInfoConfiguration, mavenCoordinateConfiguration,
                    jdbcConnectionConfiguration, responseOutputStream);

            responseOutputStream.flush();
        } catch (IOException ok) {
            //it's safe to go,this is not gonna happen!
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                responseOutputStream.close();
            } catch (IOException ok) {
                //it's safe to go
            }
        }
    }

    /**
     * 根据具体浏览器设置必要的Header，浏览器才会做出相应的反应（比如保存文件到本地）
     */
    private void setChineseHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        String userAgent = request.getHeader("User-Agent");

        if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }

        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream; charset=utf-8");
    }
}
