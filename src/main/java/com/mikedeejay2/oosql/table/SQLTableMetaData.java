package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.table.index.SQLMetaDataGetter;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraints;

public interface SQLTableMetaData extends SQLMetaDataGetter<SQLTableMeta>
{
    SQLTableType getTableType();

    boolean columnExists(String columnName);

    boolean columnExists(SQLColumn column);

    int getColumnsAmount();

    int getRowsAmount();

    SQLTableInfo getInfo();

    SQLColumnInfo[] getColumnInfos();

    boolean hasCheck();

    SQLConstraints getConstraints();

    boolean exists();

    String getCheck();
}
