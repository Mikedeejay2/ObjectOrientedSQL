package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;

public interface SQLColumnMetaData
{
    SQLDataType getDataType();

    int[] getSizes();

    String getMetaString(SQLColumnMeta metaType);

    int getMetaInt(SQLColumnMeta metaType);

    boolean isNotNull();

    boolean isPrimaryKey();

    boolean isForeignKey();

    boolean hasDefault();

    boolean autoIncrements();

    boolean isUnique();

    boolean hasConstraint(SQLConstraint constraint);

    SQLColumnInfo getInfo();

    SQLConstraintData[] getConstraints();

    String[] getConstraintParams();

    String getDefault();
}
