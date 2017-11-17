# CodeGenerator 基于数据库表结构信息的SpringBoot项目逆向代码生成工具
此项目为逆向代码生成工具，用于根据数据中的表结构信息生成包含基础CRUD接口的SpringBoot项目;

## 待读取的表结构信息
* 数据库表
    * 表名--用于生成实体类名称，以及衍生类名称
    * 评论信息--用于生成类评论信息
* 数据库表列
    * 列名--用于生成实体属性名称
    * 类型--用于解析实体属性类型
    * 是否为主键--主键列用于控制一些基于主键的查询操作
    * 是否可为空--是否可为空用于组装数据落地前的完整性校验
    * 是否为唯一索引--用于相关列的唯一性校验
    * 是否为外键--如果当前列是外键，实体类将包含一个依赖于的表结构的实体属性
    * 是否被引用作为外键--如果当前列被引用作为外键，那么实体类将包含一个依赖当前列的表结构的实体属性集合
    * 评论信息--用于作为属性评论信息

## 生成的代码模块（此处假定表结构信息为（record（record_id,record_name,record_blob）））
* 实体类(Record)
    * 主键属性(recordId)
    * 基本属性(recordName)
    * blob属性(recordBlob)
    * 基于外键的其他实体属性或实体属性集合(晚点详细描述)
    * equals方法（return obj == this || （obj instanceof Record） && obj.hashCode() == this.hashCode();）
    * hashCode方法(java.util.Objects.hash(Object ... args))
    * JSONObject toJson 当前实体的序列化方法
    * Record fromJson(JSONObject fromJsonObj) 当前实体的反序列化方法
    * toString方法
* 实体类的属性字符串常量key定义类(RecordKey)
    * 每个属性对应的字符串key常量(public static fianl String RECORD_ID = "record_id";)
* mybatis mapper 接口（RecordMapper）
    * 使用@Mapper，@Reponsitory两个注解进行注解
    * addNewRecord(Record record)--添加一条记录
    * addNewRecordBatch(List<Record> records)--批量添加记录
    * removeRecordByRecordId(Integer recordId)--根据主键查询指定记录，如果recode_id是自增主键
    * removeRecordByRecordName(String recordName)--根据唯一索引查询指定记录，如果record_name是唯一索引列
    * modifyRecordByRecordId(Integer oldRecordId，Record newRecord)--根据指定主键修改指定记录，如果recode_id是自增主键
    * queryRecordByRecordId(Integer recordId)--根据指定主键查询指定记录，如果recode_id是自增主键
    * queryRecordByRecordName(String recordName)--根据指定主键查询指定记录，不包含blob属性，如果recode_id是自增主键
    * queryRecordByRecordIdWithBlob(Integer recordId)--根据指定主键查询指定记录，包含blob属性，如果recode_id是自增主键
    * queryRecordByRecordIdWithOnlyBlob(Integer recordId)--根据指定主键查询指定记录，仅仅包含blob属性，如果recode_id是自增主键
    * queryAllRecords()--查询全部记录
    * queryAllRecordsWithPage(int offset,int limit)--查询全部记录，包含分页操作
    * queryRecordCountByRecordName(String recordName)--查询指定唯一索引对应的记录数目，如果record_name是唯一索引列
    * 基于外键的级联查询--稍后详细描述
* mybatis sql xml文件(RecordMapper.xml)
    * 上述所有Mapper接口的实现
    * addNewRecord&addNewRecordBatch两个添加数据的方法，默认都会使用usrGeneratedKey返回自赠主键
* 实体的CRUD service 接口(RecordService)
    * 同Mapper接口
    * queryRecordCountByRecordName(String recordName)调整为IsSpecRecordWithRecordNameExists(String recordName)
* 实体的CRUD service接口实现类serviceImpl(RecordServiceImpl)
    * 使用@Service注解进行注解
    * 上述所有service接口的实现
    * 根据具体需要对底层Mapper Component进行注入
* 实体类的Restful类型的CRUD web接口Controller（RecordController）
    * 使用@RestController注解进行注解
    * 根据具体需要对底层Service Component进行注入
    * Restful类型的CRUD接口，同service接口
    * 增加，删除，修改方法使用Post请求，并使用@PostMapping进行注解,value等同于方法名称，使用@RequestBody注解接受全部请求体数据
    * 查询接口使用Get请求，并使用@GetMapping进行注解，value等同于方法名称，使用@RequestParam注解逐个接收请求参数
* 基于RestControllerAdvice和ExceptionHandler的全局的异常处理机制相关代码
    * WellFormedException 异常信息接口
        * String getResponseCode();--获取异常信息状态码
        * String getResponseMsg();--获取异常信息内容描述
        * WellFormedException fromPrimitiveException(Exception primitiveException);--将原始异常信息转换为包含状态码和描述信息的异常类
    * AbstractException extends WellFormedException 抽象实现类
    * Code 异常信息状态码
    * EmptyRequestBodyException--请求体为空异常
    * ParamErrorException--请求参数错误异常
    * ParamMissingException--请求参数缺失异常
    * InternalServerException--服务器内部错误（用于未处理到的异常）
    * GlobalExceptionHandler--基于@RestControllerAdvice和@ExceptionHandler的全局异常处理
* SpringBoot项目基础配置
    * application.yml
    * application-dev.yml
    * application-test.yml
    * application-pro.yml
* SpringBoot项目启动类Application
    * main
    * druidDataSource
    * txManager(TransactionManager)
* SpringBoot项目基础Maven配置文件pom.xml
    * 基础依赖
        * org.apache.commons, commons-lang3, 3.4
        * com.alibaba, fastjson, 1.2.37
        * org.springframework.boot, spring-boot-starter
        * org.springframework.boot, spring-boot-starter-log4j2
        * com.fasterxml.jackson.dataformat, jackson-dataformat-yaml
        * org.springframework.boot, spring-boot-starter-web
        * org.springframework.boot, spring-boot-starter-test
        * junit, junit, 4.12
        * org.springframework.boot, spring-boot-starter-jdbc
        * mysql, mysql-connector-java, 5.1.40
        * com.alibaba, druid, 1.0.29
        * org.mybatis.spring.boot, mybatis-spring-boot-starter, 1.3.0

## How To Use
1. 克隆仓库https://github.com/nxllxn/CodeGenerator.git
2. 将当前项目添加为Maven项目
3. 添加配置文件Configuration.xml，提供必要配置，包括项目基本信息以及maven坐标等信息
4. 如果需要生成代码到压缩文件，那么使用ZipFileWriter将生成的代码写入到Zip文件输出流中，如果需要生成代码到当前项目，那么使用ProjectFileWriter将生成的代码按照层级机构写入到当前项目中。

### 配置文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE context
        PUBLIC ""
        "com/nxl/codegenerator/config/xml/confguration-1.0.dtd">

<context>
    <!--此处从jdbc.properties配置文件中加载数据库配置信息-->
    <properties resource="jdbc.properties"/>
    
    <!--也可以在此处使用property节点直接配置 property节点配置的属性优先级较高，这是由解析顺序决定的-->
    <property name="jdbc.driverClassName" value="驱动名称"/>
    <property name="jdbc.connectionURL" value="数据库连接URL"/>
    <property name="jdbc.userName" value="数据库访问用户名"/>
    <property name="jdbc.password" value="数据库访问密码"/>
    
    <!--项目基本信息 包括项目名称，源码目录，资源文件目录-->
    <projectBaseInfo
            projectName="GeneratedProject"
            sourceDirectory="src/main/java"
            resourcesDirectory="src/main/resources"
            rootPackage="com.foo.kie"/>

    <!--项目maven坐标 包括groupId，artifactId，以及Version-->
    <mavenCoordinate
            groupId="com.foo"
            artifactId="kie"
            version="1.0.0-SNAPSHOT"/>

    <!--代码生成使用的数据库配置信息-->
    <jdbcConnection
            driverClassName="${jdbc.driverClassName}"
            connectionURL="${jdbc.connectionURL}"
            userName="${jdbc.userName}"
            password="${jdbc.password}">
        <property name="allowMultipleQuery" value="true"/>
    </jdbcConnection>
</context>
```

### 示例代码
```java
public class Test {
    public static void main(String[] args) throws Exception {
        writeToZipFile();

        //writeToCurrentProject();
    }

    /**
     * 生成项目代码并将代码按照指定层级结构写入到当前项目中
     */
    private static void writeToCurrentProject() throws Exception {
        InputStream inputStream = CodeGeneratorController.class.getClassLoader().getResourceAsStream("Configuration.xml");

        Context context = (Context) new ConfigurationParser().parseConfiguration(
                inputStream
        );

        DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(
                getDatabaseMetadata(context), new JavaTypeResolver(), context
        );

        List<IntrospectedTable> introspectedTables = databaseIntrospector.introspectedTables();

        List<AbstractCodeGenerator> abstractCodeGenerators = context.getCodeGenerators();

        List<GeneratedFile> generatedFiles = new ArrayList<>();
        for (AbstractCodeGenerator codeGenerator : abstractCodeGenerators) {
            codeGenerator.setIntrospectedTables(introspectedTables);
            codeGenerator.setContext(context);

            generatedFiles.addAll(codeGenerator.generate());
        }

        FileWriter fileWriter = new ProjectFileWriter(generatedFiles);
        fileWriter.write();
    }

    /**
     * 生成项目代码并将代码写入到Zip文件中
     * @throws Exception SQLException
     */
    private static void writeToZipFile() throws Exception {
        InputStream inputStream = CodeGeneratorController.class.getClassLoader().getResourceAsStream("Configuration.xml");

        Context context = (Context) new ConfigurationParser().parseConfiguration(
                inputStream
        );

        DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(
                getDatabaseMetadata(context), new JavaTypeResolver(), context
        );

        List<IntrospectedTable> introspectedTables = databaseIntrospector.introspectedTables();

        List<AbstractCodeGenerator> abstractCodeGenerators = context.getCodeGenerators();

        List<GeneratedFile> generatedFiles = new ArrayList<>();
        for (AbstractCodeGenerator codeGenerator : abstractCodeGenerators) {
            codeGenerator.setIntrospectedTables(introspectedTables);
            codeGenerator.setContext(context);

            generatedFiles.addAll(codeGenerator.generate());
        }

        File zipFile = new File("/*此处填写输出zip文件地址*/");
        zipFile.getParentFile().mkdirs();
        zipFile.createNewFile();

        FileWriter zipFileWriter = new ZipFileWriter(
                generatedFiles,
                context.getProjectBaseInfoConfiguration().getProjectName(),
                new FileOutputStream(zipFile)
        );
        zipFileWriter.write();
    }

    private static DatabaseMetaData getDatabaseMetadata(Context configuration) throws SQLException {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = configuration.getJdbcConnectionConfiguration();

        Connection connection = new JDBCConnectionFactory(jdbcConnectionConfiguration).openConnection();

        return connection.getMetaData();
    }
}
```


## 约定
### 实体类序列化反序列化接口
所有实体类君实现接口Serializer或者继承这个接口的抽象实现AbstractSerializer
#### 接口类Serializer
```java
public interface Serializer<T> { 
    T fromJson(JSONObject fromJsonObj);

    JSONObject toJson();
}
```
#### 抽象实现类
```java
public abstract class AbstractSerializer<T> implements Serializer<T> { 
    public static JSONObject serialize(Serializer instance) { 
        if (instance == null){
            return null;
        }
        
        return instance.toJson();
    } 

    public static <T extends Serializer<T>> T antiSerialize(JSONObject fromJsonObject, Class<T> cls) { 
        try {
            T instance = cls.newInstance();
            instance.fromJson(fromJsonObject);
            return instance;
        } catch (IllegalAccessException ok) {
            //it`s safe to go
        } catch (InstantiationException  ok){
            //it`s safe to go,no need to process
        }
        
        return null;
    } 

    public static JSONArray serializeBatch(Collection<? extends Serializer> instances) { 
        JSONArray toJsonObjs = new JSONArray();
        
        if (instances == null){
            return toJsonObjs;
        }
        
        Iterator<? extends Serializer> iterator = instances.iterator();
        while (iterator.hasNext()){
            toJsonObjs.add(iterator.next().toJson());
        }
        
        return toJsonObjs;
    } 

    public static <T extends Serializer<T>> List<T> antiSerializeBatch(JSONArray fromJsonObjects, Class<T> cls) { 
        List<T> instances = new ArrayList<>();
        
        if (fromJsonObjects == null){
            return instances;
        }
        
        Iterator iterator = fromJsonObjects.iterator();
        T instance;
        while (iterator.hasNext()){
            try {
                instance = cls.newInstance();
                instance.fromJson((JSONObject) iterator.next());
                instances.add(instance);
            } catch (IllegalAccessException ok) {
                //it`s safe to go
            } catch (InstantiationException  ok){
                //it`s safe to go,no need to process
            }
        }
        
        return instances;
    } 
} 
```

### Controller接口抽象实现类
所有Controller接口类均继承于这个类，此类中提供四个默认实现：
* String responseSuccess():简单的构造一个状态码为00的结果Json字符串
* String responseJson(JSON jsonObj):使用00作为返回状态码，指定Json数据作为content组装结果Json字符串。
* String responseJson(Serializer entity):使用00作为状态码，entity序列化得到的Json对象作为content组装结果Json字符串。
* String responseJson(List<? extends Serializer> entities):使用00作为状态码，entities序列化得到的Json数组作为content组装结果Json字符串。
```java
package com.foo.kie.controller;

import com.alibaba.fastjson.JSON;
import com.foo.kie.entities.Serializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.foo.kie.entities.AbstractSerializer;
import java.util.List;
import com.foo.kie.entities.Serializer;
import com.alibaba.fastjson.JSONObject;

public class AbstractController { 
    private static final String RESPONSE_CODE = "response_code";

    private static final String CONTENT = "content";

    protected static final int DEFAULT_PAGE_SIZE = 200;

    protected String responseSuccess() { 
        JSONObject responseJsonObj = new JSONObject();
        responseJsonObj.put(RESPONSE_CODE,"00");
        return JSONObject.toJSONString(responseJsonObj,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    } 

    protected String responseJson(JSON jsonObj) { 
        JSONObject responseJsonObj = new JSONObject();
        responseJsonObj.put(RESPONSE_CODE,"00");
        responseJsonObj.put(CONTENT,jsonObj);
        return JSONObject.toJSONString(responseJsonObj,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    } 

    protected String responseJson(Serializer entity) { 
        JSONObject responseJsonObj = new JSONObject();
        responseJsonObj.put(RESPONSE_CODE,"00");
        responseJsonObj.put(CONTENT,entity == null ? null : entity.toJson());
        return JSONObject.toJSONString(responseJsonObj,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    } 

    protected String responseJson(List<? extends Serializer> entities) { 
        JSONObject responseJsonObj = new JSONObject();
        responseJsonObj.put(RESPONSE_CODE,"00");
        responseJsonObj.put(CONTENT,AbstractSerializer.serializeBatch(entities));
        return JSONObject.toJSONString(responseJsonObj,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    } 
} 
```

