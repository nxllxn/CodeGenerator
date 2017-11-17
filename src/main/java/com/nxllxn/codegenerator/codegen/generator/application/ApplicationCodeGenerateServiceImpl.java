package com.nxllxn.codegenerator.codegen.generator.application;

import com.nxllxn.codegenerator.codegen.java.*;
import com.nxllxn.codegenerator.utils.AssembleUtil;

/**
 * Application 代码生成服务实现类
 *
 * @author wenchao
 */
public class ApplicationCodeGenerateServiceImpl implements ApplicationCodeGenerateService {
    @Override
    public TopLevelClass generateApplicationClass(String packageName) {
        TopLevelClass applicationClass = new TopLevelClass();

        applicationClass.setPackageName(packageName);

        applicationClass.setVisibility(Visibility.PUBLIC);
        applicationClass.setType(new FullyQualifiedJavaType(packageName + AssembleUtil.PACKAGE_SEPERATOR + "Application"));

        applicationClass.addAnnotation("@EnableTransactionManagement");
        applicationClass.addAnnotation("@SpringBootApplication");
        applicationClass.addAnnotation("@ServletComponentScan");

        applicationClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.boot.autoconfigure.SpringBootApplication"));
        applicationClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.boot.web.servlet.ServletComponentScan"));
        applicationClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker"));
        applicationClass.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.transaction.annotation.EnableTransactionManagement;"));

        return applicationClass;
    }

    @Override
    public Method generateMainMethod() {
        Method method = new Method();

        method.setVisibility(Visibility.PUBLIC);
        method.setStatic(true);
        method.setName("main");

        Parameter parameter = new Parameter();
        parameter.setType(new FullyQualifiedJavaType("String[]"));
        parameter.setName("args");
        method.addParameter(parameter);

        method.addBodyLine("SpringApplication.run(Application.class, args);");

        method.addExtraNonStaticImport(new FullyQualifiedJavaType("org.springframework.boot.SpringApplication"));

        return method;
    }
}
