package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.misc.SQLDataType;

public interface SQLColumnMetaData
{
    SQLDataType getDataType();

    int[] getSizes();

    String getMetaString(SQLColumnMeta metaType);

    int getMetaInt(SQLColumnMeta metaType);

    boolean isNullable();

    boolean isPrimaryKey();

    boolean isForeignKey();

    boolean hasDefault();

    boolean autoIncrements();
}
