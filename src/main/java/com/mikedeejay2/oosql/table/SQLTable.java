package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.database.SQLDatabase;

public class SQLTable
{
    protected final SQLDatabase database;
    protected String name;
    protected SQLTableType type;

    public SQLTable(SQLDatabase database, String name, SQLTableType type)
    {
        this.database = database;
        this.name = name;
        this.type = type;
    }

    public SQLColumn[] getColumns()
    {
        return new SQLColumn[0];
    }

    public boolean renameTable(String newName)
    {
        String command = "ALTER TABLE `" + name + "` RENAME TO `" + newName + "`";
        System.out.println(command);
        int code = database.executeUpdate(command);
        this.name = newName;
        return code != -1;
    }

    public SQLColumn getColumn(String columnName)
    {
        return null;
    }

    public SQLColumn getColumn(int index)
    {
        return null;
    }

    public SQLDatabase getDatabase()
    {
        return database;
    }

    public String getName()
    {
        return name;
    }

    public SQLTableType getType()
    {
        return type;
    }
}
