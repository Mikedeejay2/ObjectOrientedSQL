package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.misc.SQLConstraint;

public class SQLTableInfo
{
    private final String tableName;
    private final SQLColumnInfo[] columns;
    private final SQLConstraint[] constraints;
    private final String[] constraintParams;

    public SQLTableInfo(String tableName, SQLColumnInfo[] columns, SQLConstraint[] constraints, String[] constraintParams) {
        this.tableName = tableName;
        this.columns = columns;
        this.constraints = constraints;
        this.constraintParams = constraintParams;
    }

    public SQLConstraint[] getConstraints()
    {
        return constraints;
    }

    public String[] getConstraintParams()
    {
        return constraintParams;
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
