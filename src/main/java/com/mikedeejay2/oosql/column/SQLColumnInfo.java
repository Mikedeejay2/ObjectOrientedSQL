package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;

public class SQLColumnInfo
{
    protected final SQLDataType type;
    protected final String name;
    protected final int[] sizes;
    protected final SQLConstraint[] constraints;
    protected final String[] constraintParams;

    public SQLColumnInfo(SQLDataType type, String name, int[] sizes, SQLConstraint[] constraints, String... constraintParams)
    {
        this.type = type;
        this.name = name;
        this.sizes = sizes;
        this.constraints = constraints;
        this.constraintParams = constraintParams;
    }

    public SQLColumnInfo(SQLDataType type, String name, int size, SQLConstraint[] constraints, String... constraintParams)
    {
        this(type, name, new int[]{size}, constraints, constraintParams);
    }

    public SQLColumnInfo(SQLDataType type, String name, SQLConstraint[] constraints, String... constraintParams)
    {
        this(type, name, new int[]{}, constraints, constraintParams);
    }

    public SQLColumnInfo(SQLDataType type, String name, int size, SQLConstraint constraint, String... constraintParams)
    {
        this(type, name, new int[]{size}, new SQLConstraint[]{constraint}, constraintParams);
    }

    public SQLColumnInfo(SQLDataType type, String name, SQLConstraint constraint, String... constraintParams)
    {
        this(type, name, new int[]{}, new SQLConstraint[]{constraint}, constraintParams);
    }

    public SQLColumnInfo(SQLDataType type, String name, int size, String... constraintParams)
    {
        this(type, name, new int[]{size}, new SQLConstraint[]{}, constraintParams);
    }

    public SQLColumnInfo(SQLDataType type, String name, String... constraintParams)
    {
        this(type, name, new int[]{}, new SQLConstraint[]{}, constraintParams);
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

    public String[] getConstraintParams()
    {
        return constraintParams;
    }
}
