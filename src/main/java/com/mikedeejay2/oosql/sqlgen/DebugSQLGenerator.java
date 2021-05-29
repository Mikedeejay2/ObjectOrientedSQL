package com.mikedeejay2.oosql.sqlgen;

import com.mikedeejay2.oosql.column.SQLColumnInfo;

import java.util.logging.Logger;

public class DebugSQLGenerator extends SimpleSQLGenerator
{
    public String print(String str)
    {
        System.out.println(str);
        return str;
    }

    @Override
    public String createTable(String tableName, SQLColumnInfo... info)
    {
        return print(super.createTable(tableName, info));
    }

    @Override
    public String dropTable(String tableName)
    {
        return print(super.dropTable(tableName));
    }

    @Override
    public String renameTable(String tableName, String newName)
    {
        return print(super.renameTable(tableName, newName));
    }

    @Override
    public String countRows(String tableName)
    {
        return print(super.countRows(tableName));
    }

    @Override
    public String dropDatabase(String databaseName)
    {
        return print(super.dropDatabase(databaseName));
    }

    @Override
    public String createDatabase(String databaseName)
    {
        return print(super.createDatabase(databaseName));
    }
}
