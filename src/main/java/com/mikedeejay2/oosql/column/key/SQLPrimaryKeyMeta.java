package com.mikedeejay2.oosql.column.key;

public enum SQLPrimaryKeyMeta {
    // TABLE_CAT String => table catalog (may be null)
    TABLE_CAT("TABLE_CAT", 1),
    // TABLE_SCHEM String => table schema (may be null)
    TABLE_SCHEM("TABLE_SCHEM", 2),
    // TABLE_NAME String => table name
    TABLE_NAME("TABLE_NAME", 3),
    // COLUMN_NAME String => column name
    COLUMN_NAME("COLUMN_NAME", 4),
    // KEY_SEQ short => sequence number within primary key( a value of 1 represents the first column of the primary key, a value of 2 would represent the second column within the primary key).
    KEY_SEQ("KEY_SEQ", 5),
    // PK_NAME String => primary key name (may be null)
    PK_NAME("PK_NAME", 6),
    ;

    private final String str;
    private final int index;

    SQLPrimaryKeyMeta(String str, int index) {
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
