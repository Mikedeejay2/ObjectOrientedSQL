package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.database.SQLDatabase;

public class SQLTable implements SQLTableInterface
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

    @Override
    public boolean renameTable(String newName)
    {
        String command = "ALTER TABLE `" + name + "` RENAME TO `" + newName + "`";
        System.out.println(command);
        int code = database.executeUpdate(command);
        this.name = newName;
        return code != -1;
    }

    @Override
    public SQLColumn getColumn(String columnName)
    {
        return null;
    }

    @Override
    public SQLColumn getColumn(int index)
    {
        return null;
    }

    @Override
    public SQLColumn[] getColumns()
    {
        return new SQLColumn[0];
    }

    @Override
    public SQLDatabase getDatabase()
    {
        return database;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public SQLTableType getType()
    {
        return type;
    }
}
