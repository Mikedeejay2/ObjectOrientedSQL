package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.SQLObject;
import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.database.SQLDatabase;

public interface SQLTableInterface extends SQLObject
{
    public boolean renameTable(String newName);

    public SQLColumn getColumn(String columnName);

    public SQLColumn getColumn(int index);

    public SQLColumn[] getColumns();

    public SQLDatabase getDatabase();

    public String getTableName();

    public SQLTableType getType();
}
