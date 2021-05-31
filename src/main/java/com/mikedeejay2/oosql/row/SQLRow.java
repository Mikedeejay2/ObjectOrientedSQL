package com.mikedeejay2.oosql.row;

import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.table.SQLTable;

public class SQLRow<T>
{
    protected final SQLDatabase database;
    protected final SQLTable table;
    protected T primaryKey;

    public SQLRow(SQLTable parentTable, T primaryKey)
    {
        this.table = parentTable;
        this.database = parentTable.getDatabase();
        this.primaryKey = primaryKey;
    }

    public T getPrimaryKey()
    {
        return primaryKey;
    }
}
