package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraints;

public class SQLColumnInfo
{
    protected final SQLDataType type;
    protected final String name;
    protected final int[] sizes;
    protected final SQLConstraints constraints;

    public SQLColumnInfo(SQLDataType type, String name, int[] sizes, SQLConstraints constraints)
    {
        this.type = type;
        this.name = name;
        this.sizes = sizes;
        this.constraints = constraints;
    }

    public SQLColumnInfo(SQLDataType type, String name, int size, SQLConstraints constraints)
    {
        this(type, name, new int[]{size}, constraints);
    }

    public SQLColumnInfo(SQLDataType type, String name, SQLConstraints constraints)
    {
        this(type, name, new int[]{}, constraints);
    }

    public SQLColumnInfo(SQLDataType type, String name, int size)
    {
        this(type, name, new int[]{size}, null);
    }

    public SQLColumnInfo(SQLDataType type, String name)
    {
        this(type, name, new int[]{}, null);
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

    public SQLConstraints getConstraints()
    {
        return constraints;
    }
}
