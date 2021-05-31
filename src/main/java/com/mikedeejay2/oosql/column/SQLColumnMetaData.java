package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;

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

    SQLConstraint[] getConstraints();

    String[] getConstraintParams();

    String getDefault();
}
