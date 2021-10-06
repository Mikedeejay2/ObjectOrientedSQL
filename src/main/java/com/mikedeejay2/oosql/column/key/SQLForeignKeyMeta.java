package com.mikedeejay2.oosql.column.key;

public enum SQLForeignKeyMeta {
    // PKTABLE_CAT String => primary key table catalog (may be null)
    PKTABLE_CAT("PKTABLE_CAT", 1),
    // PKTABLE_SCHEM String => primary key table schema (may be null)
    PKTABLE_SCHEM("PKTABLE_SCHEM", 2),
    // PKTABLE_NAME String => primary key table name
    PKTABLE_NAME("PKTABLE_NAME", 3),
    // PKCOLUMN_NAME String => primary key column name
    PKCOLUMN_NAME("PKCOLUMN_NAME", 4),
    // FKTABLE_CAT String => foreign key table catalog (may be null) being exported (may be null)
    FKTABLE_CAT("FKTABLE_CAT", 5),
    // FKTABLE_SCHEM String => foreign key table schema (may be null) being exported (may be null)
    FKTABLE_SCHEM("FKTABLE_SCHEM", 6),
    // FKTABLE_NAME String => foreign key table name being exported
    FKTABLE_NAME("FKTABLE_NAME", 7),
    // FKCOLUMN_NAME String => foreign key column name being exported
    FKCOLUMN_NAME("FKCOLUMN_NAME", 8),
    // KEY_SEQ short => sequence number within foreign key( a value of 1 represents the first column of the foreign key,
    // a value of 2 would represent the second column within the foreign key).
    KEY_SEQ("KEY_SEQ", 9),
    // UPDATE_RULE short => What happens to foreign key when primary is updated:
    //  * importedNoAction - do not allow update of primary key if it has been imported
    //  * importedKeyCascade - change imported key to agree with primary key update
    //  * importedKeySetNull - change imported key to NULL if its primary key has been updated
    //  * importedKeySetDefault - change imported key to default values if its primary key has been updated
    //  * importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
    UPDATE_RULE("UPDATE_RULE", 10),
    // DELETE_RULE short => What happens to the foreign key when primary is deleted.
    //  * importedKeyNoAction - do not allow delete of primary key if it has been imported
    //  * importedKeyCascade - delete rows that import a deleted key
    //  * importedKeySetNull - change imported key to NULL if its primary key has been deleted
    //  * importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
    //  * importedKeySetDefault - change imported key to default if its primary key has been deleted
    DELETE_RULE("DELETE_RULE", 11),
    // FK_NAME String => foreign key name (may be null)
    FK_NAME("FK_NAME", 12),
    // PK_NAME String => primary key name (may be null)
    PK_NAME("PK_NAME", 13),
    // DEFERRABILITY short => can the evaluation of foreign key constraints be deferred until commit
    //  * importedKeyInitiallyDeferred - see SQL92 for definition
    //  * importedKeyInitiallyImmediate - see SQL92 for definition
    //  * importedKeyNotDeferrable - see SQL92 for definition
    DEFERRABILITY("DEFERRABILITY", 14),
    ;

    private final String str;
    private final int index;

    SQLForeignKeyMeta(String str, int index) {
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
