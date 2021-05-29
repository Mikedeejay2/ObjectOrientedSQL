package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;

public class SQLColumnInfo
{
    protected final SQLDataType type;
    protected final String name;
    protected final int[] sizes;
    protected final SQLConstraint[] constraints;
    protected final String[] extra;

    public SQLColumnInfo(SQLDataType type, String name, int[] sizes, SQLConstraint[] constraints, String... extra)
    {
        this.type = type;
        this.name = name;
        this.sizes = sizes;
        this.constraints = constraints;
        this.extra = extra;
    }

    public SQLColumnInfo(SQLDataType type, String name, int size, SQLConstraint[] constraints, String... extra)
    {
        this(type, name, new int[]{size}, constraints, extra);
    }

    public SQLColumnInfo(SQLDataType type, String name, SQLConstraint[] constraints, String... extra)
    {
        this(type, name, new int[]{}, constraints, extra);
    }

    public SQLColumnInfo(SQLDataType type, String name, int size, SQLConstraint constraint, String... extra)
    {
        this(type, name, new int[]{size}, new SQLConstraint[]{constraint}, extra);
    }

    public SQLColumnInfo(SQLDataType type, String name, SQLConstraint constraint, String... extra)
    {
        this(type, name, new int[]{}, new SQLConstraint[]{constraint}, extra);
    }

    public SQLColumnInfo(SQLDataType type, String name, int size, String... extra)
    {
        this(type, name, new int[]{size}, new SQLConstraint[]{}, extra);
    }

    public SQLColumnInfo(SQLDataType type, String name, String... extra)
    {
        this(type, name, new int[]{}, new SQLConstraint[]{}, extra);
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

    public String[] getExtra()
    {
        return extra;
    }
}
