package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.column.SQLColumnInfo;

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
}
