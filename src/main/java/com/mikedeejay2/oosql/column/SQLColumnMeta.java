package com.mikedeejay2.oosql.column;

public enum SQLColumnMeta {
    // String => table catalog (may be null)
    TABLE_CAT("TABLE_CAT", 1),
    // String => table schema (may be null)
    TABLE_SCHEM("TABLE_SCHEM", 2),
    // String => table name
    TABLE_NAME("TABLE_NAME", 3),
    // String => column name
    COLUMN_NAME("COLUMN_NAME", 4),
    // int => SQL type from java.sql.Types
    DATA_TYPE("DATA_TYPE", 5),
    // String => Data source dependent type name, for a UDT the type name is fully qualified
    TYPE_NAME("TYPE_NAME", 6),
    // int => column size.
    COLUMN_SIZE("COLUMN_SIZE", 7),
    // is not used.
    BUFFER_LENGTH("BUFFER_LENGTH", 8),
    // int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
    DECIMAL_DIGITS("DECIMAL_DIGITS", 9),
    // int => Radix (typically either 10 or 2)
    NUM_PREC_RADIX("NUM_PREC_RADIX", 10),
    // int => is NULL allowed.
    // columnNoNulls - might not allow NULL values
    // columnNullable - definitely allows NULL values
    // columnNullableUnknown - nullability unknown
    NULLABLE("NULLABLE", 11),
    // String => comment describing column (may be null)
    REMARKS("REMARKS", 12),
    // String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
    COLUMN_DEF("COLUMN_DEF", 13),
    // int => unused
    SQL_DATA_TYPE("SQL_DATA_TYPE", 14),
    // int => unused
    SQL_DATETIME_SUB("SQL_DATETIME_SUB", 15),
    // int => for char types the maximum number of bytes in the column
    CHAR_OCTET_LENGTH("CHAR_OCTET_LENGTH", 16),
    // int => index of column in table (starting at 1)
    ORDINAL_POSITION("ORDINAL_POSITION", 17),
    // String => ISO rules are used to determine the nullability for a column.
    // YES --- if the column can include NULLs
    // NO --- if the column cannot include NULLs
    // empty string --- if the nullability for the column is unknown
    IS_NULLABLE("IS_NULLABLE", 18),
    // String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
    SCOPE_CATALOG("SCOPE_CATALOG", 19),
    // String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
    SCOPE_SCHEMA("SCOPE_SCHEMA", 20),
    // String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
    SCOPE_TABLE("SCOPE_TABLE", 21),
    // short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
    SOURCE_DATA_TYPE("SOURCE_DATA_TYPE", 22),
    // String => Indicates whether this column is auto incremented
    // YES --- if the column is auto incremented
    // NO --- if the column is not auto incremented
    // empty string --- if it cannot be determined whether the column is auto incremented
    IS_AUTOINCREMENT("IS_AUTOINCREMENT", 23),
    // String => Indicates whether this is a generated column
    // YES --- if this a generated column
    // NO --- if this not a generated column
    // empty string --- if it cannot be determined whether this is a generated column
    IS_GENERATEDCOLUMN("IS_GENERATEDCOLUMN", 24),
    ;

    private final String str;
    private final int index;

    SQLColumnMeta(String str, int index) {
        this.str = str;
        this.index = index;
    }

    public String asString() {
        return str;
    }

    public int asIndex() {
        return index;
    }
}
