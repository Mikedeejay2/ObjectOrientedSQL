package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.SQLObject;
import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;
import com.mikedeejay2.oosql.table.SQLTable;

public interface SQLColumnInterface extends SQLObject
{
    SQLTable getParentTable();

    SQLDatabase getDatabase();

    String getName();

    boolean addConstraint(SQLConstraintData constraint);

    boolean addConstraints(SQLConstraintData... constraints);

    boolean renameColumn(String newName);
}
