package com.mikedeejay2.oosql.misc.constraint;

public enum SQLConstraint {
    // Ensures that a column can not contain a NULL value
    NOT_NULL      ("NOT NULL"      , false, false, true , false),
    // Ensures that each value in a column are different
    UNIQUE        ("UNIQUE"        , false, false, false, false),
    // Defines the column that uniquely identifies each row. NOT NULL and UNIQUE
    PRIMARY_KEY   ("PRIMARY KEY"   , true , false, false, false),
    // Defines a record in another table
    FOREIGN_KEY   ("FOREIGN KEY"   , true , false, false, true),
    // Ensures that all records in a column pass a condition
    CHECK         ("CHECK"         , true , true , false, false),
    // Sets a default value for a column when no value is specified
    DEFAULT       ("DEFAULT"       , false, true , false, false),
    // Whether to auto increment the column
    AUTO_INCREMENT("AUTO_INCREMENT", false, false, true , false),
    ;

    private final String value;
    private final boolean tableConstraint;
    private final boolean useParams;
    private final boolean dataConstraint;
    private final boolean reference;

    SQLConstraint(String value, boolean tableConstraint, boolean useParams, boolean dataConstraint, boolean reference) {
        this.value = value;
        this.tableConstraint = tableConstraint;
        this.useParams = useParams;
        this.dataConstraint = dataConstraint;
        this.reference = reference;
    }

    public String get() {
        return value;
    }

    public boolean isTableConstraint() {
        return tableConstraint;
    }

    public boolean useParams() {
        return useParams;
    }

    public boolean isDataConstraint() {
        return dataConstraint;
    }

    public boolean useReference() {
        return reference;
    }
}
