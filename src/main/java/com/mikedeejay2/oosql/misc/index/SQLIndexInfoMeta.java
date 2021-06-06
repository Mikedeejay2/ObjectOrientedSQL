package com.mikedeejay2.oosql.misc.index;

public enum SQLIndexInfoMeta
{
    // TABLE_CAT String => table catalog (may be null)
    TABLE_CAT("TABLE_CAT", 1),
    // TABLE_SCHEM String => table schema (may be null)
    TABLE_SCHEM("TABLE_SCHEM", 2),
    // TABLE_NAME String => table name
    TABLE_NAME("TABLE_NAME", 3),
    // NON_UNIQUE boolean => Can index values be non-unique. false when TYPE is tableIndexStatistic
    NON_UNIQUE("NON_UNIQUE", 4),
    // INDEX_QUALIFIER String => index catalog (may be null); null when TYPE is tableIndexStatistic
    INDEX_QUALIFIER("INDEX_QUALIFIER", 5),
    // INDEX_NAME String => index name; null when TYPE is tableIndexStatistic
    INDEX_NAME("INDEX_NAME", 6),
    // TYPE short => index type:
    //  * tableIndexStatistic - this identifies table statistics that are returned in conjuction with a table's index descriptions
    //  * tableIndexClustered - this is a clustered index
    //  * tableIndexHashed - this is a hashed index
    //  * tableIndexOther - this is some other style of index
    TYPE("TYPE", 7),
    // ORDINAL_POSITION short => column sequence number within index; zero when TYPE is tableIndexStatistic
    ORDINAL_POSITION("ORDINAL_POSITION", 8),
    // COLUMN_NAME String => column name; null when TYPE is tableIndexStatistic
    COLUMN_NAME("COLUMN_NAME", 9),
    // ASC_OR_DESC String => column sort sequence, "A" => ascending, "D" => descending, may be null if sort sequence is
    // not supported; null when TYPE is tableIndexStatistic
    ASC_OR_DESC("ASC_OR_DESC", 10),
    // CARDINALITY long => When TYPE is tableIndexStatistic, then this is the number of rows in the table; otherwise,
    // it is the number of unique values in the index.
    CARDINALITY("CARDINALITY", 11),
    // PAGES long => When TYPE is tableIndexStatisic then this is the number of pages used for the table, otherwise it
    // is the number of pages used for the current index.
    PAGES("PAGES", 12),
    // FILTER_CONDITION String => Filter condition, if any. (may be null)
    FILTER_CONDITION("FILTER_CONDITION", 13),
    ;

    private final String str;
    private final int index;

    SQLIndexInfoMeta(String str, int index)
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
