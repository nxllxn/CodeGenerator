package com.nxllxn.codegenerator.codegen.generator.key;

import com.nxllxn.codegenerator.codegen.generator.util.CodeGenerateUtil;
import com.nxllxn.codegenerator.codegen.java.Field;
import com.nxllxn.codegenerator.codegen.java.FullyQualifiedJavaType;
import com.nxllxn.codegenerator.codegen.java.TopLevelClass;
import com.nxllxn.codegenerator.codegen.java.Visibility;
import com.nxllxn.codegenerator.jdbc.IntrospectedColumn;
import com.nxllxn.codegenerator.jdbc.IntrospectedTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体类对应的属性字符串常量定义的Key类生成服务实现类
 *
 * @author wenchao
 */
public class KeyCodeGenerateServiceImpl implements KeyCodeGenerateService {
    @Override
    public TopLevelClass generateKeyDefinitionClass(IntrospectedTable introspectedTable, String keyDefinitionClassPackage) {
        TopLevelClass keyDefinitionClass = new TopLevelClass();

        keyDefinitionClass.setPackageName(keyDefinitionClassPackage);
        keyDefinitionClass.setVisibility(Visibility.PUBLIC);
        keyDefinitionClass.setFinal(true);
        keyDefinitionClass.setType(introspectedTable.generateKeyDefinitionClassType());

        keyDefinitionClass.addTypeComment(introspectedTable.getEntityName() + "实体类Key常量定义类");
        keyDefinitionClass.addTypeComment("");
        keyDefinitionClass.addTypeComment("@author %UNKNOWN_AUTHOR%");

        return keyDefinitionClass;
    }

    @Override
    public List<Field> generateKeyDefinitions(IntrospectedTable introspectedTable) {
        List<Field> keyDefinitionFields = new ArrayList<>();

        Field keyDefinitionField;
        for (IntrospectedColumn introspectedColumn:introspectedTable.getAllColumnsIncludeGenerated()){
            keyDefinitionField = new Field();

            keyDefinitionField.setVisibility(Visibility.PUBLIC);
            keyDefinitionField.setStatic(true);
            keyDefinitionField.setFinal(true);
            keyDefinitionField.setType(FullyQualifiedJavaType.getStringInstance());
            keyDefinitionField.setName(CodeGenerateUtil.assembleConstantName(introspectedColumn.getPropertyName()));
            keyDefinitionField.setInitStrValue(CodeGenerateUtil.getUnderScoreString(introspectedColumn.getPropertyName()));

            keyDefinitionField.addElementComment(introspectedColumn.getRemarks());

            keyDefinitionFields.add(keyDefinitionField);
        }

        return keyDefinitionFields;
    }
}
