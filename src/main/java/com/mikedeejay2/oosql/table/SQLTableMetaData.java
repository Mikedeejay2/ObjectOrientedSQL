package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumn;

public interface SQLTableMetaData
{
    SQLColumn[] getColumns();

    SQLTableType getTableType();

    String getMeta(SQLTableMeta metaType);

    boolean columnExists(String columnName);

    boolean columnExists(SQLColumn column);

    int getColumnsAmount();

    int getRowsAmount();
}
