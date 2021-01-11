package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.table.SQLTable;

public class SQLColumn
{
    protected SQLTable table;
    protected SQLDataType type;
    protected String name;
    protected int[] sizes;
    protected SQLConstraint[] constraints;

    public SQLColumn(SQLTable parentTable, SQLDataType type, String name, int[] sizes, SQLConstraint[] constraints)
    {
        this.table = parentTable;
        this.type = type;
        this.name = name;
        this.sizes = sizes;
        this.constraints = constraints;
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
        return sizes;
    }

    public SQLConstraint[] getConstraints()
    {
        return constraints;
    }
}
