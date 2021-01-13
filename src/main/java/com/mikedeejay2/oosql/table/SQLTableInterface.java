package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.SQLObject;
import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.database.SQLDatabase;

public interface SQLTableInterface extends SQLObject
{
    boolean renameTable(String newName);

    SQLColumn getColumn(String columnName);

    SQLColumn getColumn(int index);

    SQLColumn[] getColumns();

    SQLDatabase getDatabase();

    String getTableName();

    SQLTableType getTableType();

    String getTableMeta(SQLTableMeta metaType);
}
