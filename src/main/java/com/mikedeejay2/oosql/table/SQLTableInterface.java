package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.SQLObject;
import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.database.SQLDatabase;

public interface SQLTableInterface extends SQLObject
{
    SQLColumn[] getColumns();

    String[] getColumnNames();

    boolean renameTable(String newName);

    SQLColumn getColumn(String columnName);

    SQLColumn getColumn(int index);

    SQLDatabase getDatabase();

    String getName();

    boolean addColumn(SQLColumnInfo info);

    boolean addColumns(SQLColumnInfo... info);
}
