package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.misc.SQLConstraint;

public class SQLTableInfo
{
    private final String tableName;
    private final SQLColumnInfo[] columns;
    private final SQLConstraint[] constraints;
    private final String[] constraintExtra;

    public SQLTableInfo(String tableName, SQLColumnInfo[] columns, SQLConstraint[] constraints, String[] constraintExtra) {
        this.tableName = tableName;
        this.columns = columns;
        this.constraints = constraints;
        this.constraintExtra = constraintExtra;
    }

    public SQLConstraint[] getConstraints()
    {
        return constraints;
    }

    public String[] getConstraintExtra()
    {
        return constraintExtra;
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
