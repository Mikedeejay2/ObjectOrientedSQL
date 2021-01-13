package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.SQLObject;
import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.table.SQLTable;

public class SQLColumn implements SQLObject
{
    protected SQLTable table;
    protected SQLDataType type;
    protected String name;

    public SQLColumn(SQLTable parentTable, String name, SQLDataType type)
    {
        this.table = parentTable;
        this.type = type;
        this.name = name;
    }

    public SQLTable getParentTable()
    {
        return table;
    }

    public SQLTable getTable()
    {
        return table;
    }

    public SQLDataType getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public int[] getSizes()
    {
        return null;
    }

    public SQLConstraint[] getConstraints()
    {
        return null;
    }
}
