package com.mikedeejay2.oosql.sqlgen;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.table.SQLTableInfo;

public class DebugSQLGenerator extends SimpleSQLGenerator
{
    public String print(String str)
    {
        System.out.println(str);
        return str;
    }

    @Override
    public String createTable(SQLTableInfo info)
    {
        return print(super.createTable(info));
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

    @Override
    public String renameDatabase(String databaseName, String newName)
    {
        return print(super.renameDatabase(databaseName, newName));
    }

    @Override
    public String addColumn(String tableName, SQLColumnInfo info)
    {
        return print(super.addColumn(tableName, info));
    }

    @Override
    public String addColumns(String tableName, SQLColumnInfo... info)
    {
        return print(super.addColumns(tableName, info));
    }

    @Override
    public String addConstraints(String tableName, SQLColumnInfo info, SQLConstraint... constraints)
    {
        return print(super.addConstraints(tableName, info, constraints));
    }
}
