package com.mikedeejay2.oosql.sqlgen;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;
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
    public String addConstraints(String tableName, SQLColumnInfo info, SQLConstraintData... constraints)
    {
        return print(super.addConstraints(tableName, info, constraints));
    }

    @Override
    public String addConstraint(String tableName, SQLColumnInfo info, SQLConstraintData constraint)
    {
        return print(super.addConstraint(tableName, info, constraint));
    }

    @Override
    public String dropConstraint(String tableName, SQLColumnInfo info, SQLConstraint constraint)
    {
        return print(super.dropConstraint(tableName, info, constraint));
    }

    @Override
    public String dropConstraints(String tableName, SQLColumnInfo info, SQLConstraint... constraints)
    {
        return print(super.dropConstraints(tableName, info, constraints));
    }

    @Override
    public String dropColumn(String tableName, String columnName)
    {
        return print(super.dropColumn(tableName, columnName));
    }

    @Override
    public String renameColumn(String tableName, SQLColumnInfo info, String newName)
    {
        return print(super.renameColumn(tableName, info, newName));
    }
}
