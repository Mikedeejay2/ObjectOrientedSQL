package com.mikedeejay2.oosql.misc;

public enum SQLConstraint
{
    // Ensures that a column can not contain a NULL value
    NOT_NULL      ("NOT NULL"      , false, false, true ),
    // Ensures that each value in a column are different
    UNIQUE        ("UNIQUE"        , false, false, false),
    // Defines the column that uniquely identifies each row. NOT NULL and UNIQUE
    PRIMARY_KEY   ("PRIMARY KEY"   , true , false, false),
    // Defines a record in another table
    FOREIGN_KEY   ("FOREIGN KEY"   , true , false, false),
    // Ensures that all records in a column pass a condition
    CHECK         ("CHECK"         , true , true , false),
    // Sets a default value for a column when no value is specified
    DEFAULT       ("DEFAULT"       , false, true , false),
    // Whether to auto increment the column
    AUTO_INCREMENT("AUTO_INCREMENT", false, false, true ),
    ;

    private final String value;
    private final boolean tableConstraint;
    private final boolean useParams;
    private final boolean dataConstraint;

    SQLConstraint(String value, boolean tableConstraint, boolean useParams, boolean dataConstraint)
    {
        this.value = value;
        this.tableConstraint = tableConstraint;
        this.useParams = useParams;
        this.dataConstraint = dataConstraint;
    }

    public String get()
    {
        return value;
    }

    public boolean isTableConstraint()
    {
        return tableConstraint;
    }

    public boolean useParams()
    {
        return useParams;
    }

    public boolean isDataConstraint()
    {
        return dataConstraint;
    }
}
