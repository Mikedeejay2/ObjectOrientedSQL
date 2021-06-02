package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraints;

public class SQLTableInfo
{
    private final String tableName;
    private final SQLColumnInfo[] columns;
    private final SQLConstraints constraints;

    public SQLTableInfo(String tableName, SQLConstraints constraints, SQLColumnInfo... columns) {
        this.tableName = tableName;
        this.constraints = constraints;
        this.columns = columns;
    }

    public SQLConstraints getConstraints()
    {
        return constraints;
    }

    public SQLColumnInfo[] getColumns()
    {
        return columns;
    }

    public String getTableName()
    {
        return tableName;
    }
}
