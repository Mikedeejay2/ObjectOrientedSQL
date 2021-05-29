package com.mikedeejay2.oosql.sqlgen;

import com.mikedeejay2.oosql.column.SQLColumnInfo;

public interface SQLGenerator
{
    String createTable(String tableName, SQLColumnInfo... info);

    String dropTable(String tableName);

    String renameTable(String tableName, String newName);

    String countRows(String tableName);

    String dropDatabase(String databaseName);

    String createDatabase(String databaseName);
}
