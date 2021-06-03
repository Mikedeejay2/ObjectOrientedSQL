package com.mikedeejay2.oosql.misc.constraint;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SQLConstraints implements Iterable<SQLConstraintData>
{
    protected final List<SQLConstraintData> constraintList;

    public SQLConstraints()
    {
        this.constraintList = new ArrayList<>();
    }

    public SQLConstraintData[] get()
    {
        return constraintList.toArray(new SQLConstraintData[0]);
    }

    public SQLConstraintData get(int index)
    {
        return constraintList.get(index);
    }

    public List<SQLConstraintData> getList()
    {
        return constraintList;
    }

    public SQLConstraints clear()
    {
        constraintList.clear();
        return this;
    }

    public int length()
    {
        return constraintList.size();
    }

    public int size()
    {
        return constraintList.size();
    }

    public SQLConstraints add(SQLConstraintData constraint)
    {
        constraintList.add(constraint);
        return this;
    }

    public SQLConstraints remove(SQLConstraint constraint)
    {
        constraintList.removeIf(data -> data.getConstraint() == constraint);
        return this;
    }

    public SQLConstraints addNotNull()
    {
        return this.add(SQLConstraintData.ofNotNull());
    }

    public SQLConstraints addUnique()
    {
        return this.add(SQLConstraintData.ofUnique());
    }

    public SQLConstraints addPrimaryKey()
    {
        return this.add(SQLConstraintData.ofPrimaryKey());
    }

    public SQLConstraints addForeignKey(String referenceTable, String referenceColumn)
    {
        return this.add(SQLConstraintData.ofForeignKey(referenceTable, referenceColumn));
    }

    public SQLConstraints addCheck(String checkCondition)
    {
        return this.add(SQLConstraintData.ofCheck(checkCondition));
    }

    public SQLConstraints addDefault(String defaultValue)
    {
        return this.add(SQLConstraintData.ofDefault(defaultValue));
    }

    public SQLConstraints addAutoIncrement()
    {
        return this.add(SQLConstraintData.ofAutoIncrement());
    }

    @NotNull
    @Override
    public Iterator<SQLConstraintData> iterator()
    {
        return constraintList.iterator();
    }
}
