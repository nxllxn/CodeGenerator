package com.nxllxn.codegenerator.jdbc;

/**
 * 数据库相关的常量定义
 */
public class SqlConstantRegistry {
    /**
     * 数据库表MetaData相关key
     */
    public static final String TABLE_CAT = "TABLE_CAT";
    public static final String TABLE_SCHEM = "TABLE_SCHEM";
    public static final String TABLE_NAME = "TABLE_NAME";
    public static final String TABLE_TYPE = "TABLE_TYPE";
    public static final String TABLE_TYPE_TABLE = "TABLE";
    public static final String TABLE_TYPE_VIEW = "VIEW";
    public static final String TABLE_TYPE_SYSTEM_TABLE = "SYSTEM TABLE";
    public static final String TABLE_TYPE_GLOBAL_TEMPORARY = "GLOBAL TEMPORARY";
    public static final String TABLE_TYPE_LOCAL_TEMPORARY = "LOCAL TEMPORARY";
    public static final String TABLE_TYPE_ALIAS = "ALIAS";
    public static final String TABLE_TYPE_SYNONYM = "SYNONYM";
    public static final String TABLE_REMARKS = "REMARKS";


    /**
     * 数据库列MetaData相关key
     */
    public static final String COLUMN_ATTACHED_TABLE_NAME = "TABLE_NAME";
    public static final String COLUMN_ATTACHED_TABLE_CAT = "TABLE_CAT";
    public static final String COLUMN_ATTACHED_TABLE_SCHEM = "TABLE_SCHEM";
    public static final String COLUMN_NAME = "COLUMN_NAME";
    public static final String COLUMN_DATA_TYPE = "DATA_TYPE";
    public static final String COLUMN_TYPE_NAME = "TYPE_NAME";
    public static final String COLUMN_SIZE = "COLUMN_SIZE";
    public static final String COLUMN_DECIMAL_DIGITS = "DECIMAL_DIGITS";
    public static final String COLUMN_NULLABLE = "NULLABLE";
    public static final String COLUMN_REMARKS = "REMARKS";
    public static final String COLUMN_DEFAULT_VALUE = "COLUMN_DEF";
    public static final String COLUMN_IS_NULLABLE = "IS_NULLABLE";
    public static final String COLUMN_IS_AUTOINCREMENT = "IS_AUTOINCREMENT";
    public static final String COLUMN_IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

    /**
     * 布尔值取值
     */
    public static final String UNKNOWN = "";
    public static final String YES = "YES";
    public static final String NO = "NO";

    /**
     * 索引信息相关常量
     */
    public static final String INDEX_TABLE_CAT = "TABLE_CAT";
    public static final String INDEX_TABLE_SCHEM = "TABLE_SCHEM";
    public static final String INDEX_TABLE_NAME = "TABLE_NAME";
    public static final String INDEX_NON_UNIQUE = "NON_UNIQUE";
    public static final String INDEX_QUALIFIER = "INDEX_QUALIFIER";
    public static final String INDEX_NAME = "INDEX_NAME";
    public static final String INDEX_TYPE = "INDEX_TYPE";
    /**
     * this identifies table statistics that are returned in conjuction with a table's index descriptions
     */
    public static final String INDEX_TYPE_STATISTIC = "tableIndexStatistic";
    /**
     * this is a clustered index
     */
    public static final String INDEX_TYPE_CLUSTERED = "tableIndexClustered";
    /**
     * this is a hashed index
     */
    public static final String INDEX_TYPE_HASHED = "tableIndexHashed";
    /**
     * this is some other style of index
     */
    public static final String INDEX_TYPE_OTHER = "tableIndexOther";
    /**
     * 缩索引所在列名称
     */
    public static final String INDEX_COLUMN_NAME = "COLUMN_NAME";

    /**
     * 外健相关常量
     */
    public static final String FOREIGN_KEY_PKTABLE_CAT = "PKTABLE_CAT";
    public static final String FOREIGN_KEY_PKTABLE_SCHEM = "PKTABLE_SCHEM";
    public static final String FOREIGN_KEY_PKTABLE_NAME = "PKTABLE_NAME";
    public static final String FOREIGN_KEY_PKCOLUMN_NAME = "PKCOLUMN_NAME";
    public static final String FOREIGN_KEY_FKTABLE_CAT = "FKTABLE_CAT";
    public static final String FOREIGN_KEY_FKTABLE_SCHEM = "FKTABLE_SCHEM";
    public static final String FOREIGN_KEY_FKTABLE_NAME = "FKTABLE_NAME";
    public static final String FOREIGN_KEY_FKCOLUMN_NAME = "FKCOLUMN_NAME";

    /**
     * 主键相关常量
     */
    public static final String PRIMARY_KEY_TABLE_CAT = "TABLE_CAT";
    public static final String PRIMARY_KEY_TABLE_SCHEM = "TABLE_SCHEM";
    public static final String PRIMARY_KEY_TABLE_NAME = "TABLE_NAME";
    public static final String PRIMARY_KEY_COLUMN_NAME = "COLUMN_NAME";
    public static final String PRIMARY_KEY_KEY_SEQ = "KEY_SEQ";
    public static final String PRIMARY_KEY_PK_NAME = "PK_NAME";


}
