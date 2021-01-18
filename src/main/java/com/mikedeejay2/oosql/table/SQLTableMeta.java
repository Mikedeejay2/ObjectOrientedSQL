package com.mikedeejay2.oosql.table;

public enum SQLTableMeta
{
    // String => table catalog (may be null)
    TABLE_CAT("TABLE_CAT", 1),
    // String => table schema (may be null)
    TABLE_SCHEM("TABLE_SCHEM", 2),
    // String => table name
    TABLE_NAME("TABLE_NAME", 3),
    // String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
    TABLE_TYPE("TABLE_TYPE", 4),
    // String => explanatory comment on the table
    REMARKS("REMARKS", 5),
    // String => the types catalog (may be null)
    TYPE_CAT("TYPE_CAT", 6),
    // String => the types schema (may be null)
    TYPE_SCHEM("TYPE_SCHEM", 7),
    // String => type name (may be null)
    TYPE_NAME("TYPE_NAME", 8),
    // String => name of the designated "identifier" column of a typed table (may be null)
    SELF_REFERENCING_COL_NAME("SELF_REFERENCING_COL_NAME", 9),
    // String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
    REF_GENERATION("REF_GENERATION", 10),
    ;

    private final String str;
    private final int index;

    SQLTableMeta(String str, int index)
    {
        this.str = str;
        this.index = index;
    }

    public String asString()
    {
        return str;
    }

    public int asIndex()
    {
        return index;
    }
}
