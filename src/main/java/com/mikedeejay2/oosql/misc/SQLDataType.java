package com.mikedeejay2.oosql.misc;

import java.math.BigDecimal;
import java.sql.*;

public enum SQLDataType {
    BIT("BIT", -7, Boolean.class),
    TINYINT("TINYINT", -6, Byte.class),
    SMALLINT("SMALLINT", 5, Short.class),
    INTEGER("INTEGER", 4, Integer.class),
    BIGINT("BIGINT", -5, Long.class),
    FLOAT("FLOAT", 6, Float.class),
    REAL("REAL", 7, Float.class),
    DOUBLE("DOUBLE", 8, Double.class),
    NUMERIC("NUMERIC", 2, BigDecimal.class),
    DECIMAL("DECIMAL", 3, BigDecimal.class),
    CHAR("CHAR", 1, String.class),
    VARCHAR("VARCHAR", 12, String.class),
    LONGVARCHAR("LONGVARCHAR", -1, String.class),
    DATE("DATE", 91, Date.class),
    TIME("TIME", 92, Time.class),
    TIMESTAMP("TIMESTAMP", 93, Timestamp.class),
    BINARY("BINARY", -2, Byte.class),
    VARBINARY("VARBINARY", -3, Byte.class, true),
    LONGVARBINARY("LONGVARBINARY", -4, Byte.class, true),
    NULL("NULL", 0, null),
    OTHER("OTHER", 1111, null),
    JAVA_OBJECT("JAVA_OBJECT", 2000, Object.class),
    DISTINCT("DISTINCT", 2001, String.class),
    STRUCT("STRUCT", 2002, Struct.class),
    ARRAY("ARRAY", 2003, Array.class),
    BLOB("BLOB", 2004, Blob.class),
    CLOB("CLOB", 2005, Clob.class),
    REF("REF", 2006, Ref.class),
    DATALINK("DATALINK", 70, String.class),
    BOOLEAN("BOOLEAN", 16, Boolean.class),
    ROWID("ROWID", -8, Integer.class),
    NCHAR("NCHAR", -15, Character.class),
    NVARCHAR("NVARCHAR", -9, Character.class, true),
    LONGNVARCHAR("LONGNVARCHAR", -16, Character.class, true),
    NCLOB("NCLOB", 2011, NClob.class),
    SQLXML("SQLXML", 2009, SQLXML.class),
    REF_CURSOR("REF_CURSOR", 2012, Ref.class),
    TIME_WITH_TIMEZONE("TIME_WITH_TIMEZONE", 2013, Time.class),
    TIMESTAMP_WITH_TIMEZONE("TIMESTAMP_WITH_TIMEZONE", 2014, Timestamp.class),
    ;

    private final String name;
    private final int nativeValue;
    private final Class<?> type;
    private final boolean array;

    SQLDataType(String value, int nativeValue, Class<?> type, boolean array) {
        this.name = value;
        this.nativeValue = nativeValue;
        this.type = type;
        this.array = array;
    }

    SQLDataType(String value, int nativeValue, Class<?> type) {
        this(value, nativeValue, type, false);
    }

    public String getName() {
        return name;
    }

    public int getNativeValue() {
        return nativeValue;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isArray() {
        return array;
    }

    public static SQLDataType fromNative(int nativeValue) {
        for(SQLDataType type : SQLDataType.values()) {
            if(type.nativeValue == nativeValue) return type;
        }
        return null;
    }
}
