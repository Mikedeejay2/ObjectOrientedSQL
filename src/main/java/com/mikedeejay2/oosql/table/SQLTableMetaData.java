package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;

public interface SQLTableMetaData
{
    SQLTableType getTableType();

    String getMeta(SQLTableMeta metaType);

    boolean columnExists(String columnName);

    boolean columnExists(SQLColumn column);

    int getColumnsAmount();

    int getRowsAmount();

    SQLTableInfo getInfo();

    SQLColumnInfo[] getColumnInfos();

    boolean hasCheck();

    SQLConstraintData[] getConstraints();

    String[] getConstraintParams();

    boolean exists();
}
