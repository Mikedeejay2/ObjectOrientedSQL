package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.column.SQLColumnMeta;
import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.misc.SQLDataType;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLTable implements SQLTableInterface
{
    protected final SQLDatabase database;
    protected String tableName;
    protected SQLTableType type;

    public SQLTable(SQLDatabase database, String tableName, SQLTableType type)
    {
        this.database = database;
        this.tableName = tableName;
        this.type = type;
    }

    @Override
    public boolean renameTable(String newName)
    {
        String command = "ALTER TABLE `" + tableName + "` RENAME TO `" + newName + "`";
        System.out.println(command);
        int code = database.executeUpdate(command);
        this.tableName = newName;
        return code != -1;
    }

    @Override
    public SQLColumn getColumn(String columnName)
    {
        try
        {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, columnName);
            if(result.next())
            {
                SQLDataType dataType = SQLDataType.fromNative(result.getInt(SQLColumnMeta.DATA_TYPE.toString()));
                SQLColumn column = new SQLColumn(this, columnName, dataType);
                return column;
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public SQLColumn getColumn(int index)
    {
        ++index;
        try
        {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, null);
            result.absolute(index);
            String columnName = result.getString(SQLColumnMeta.COLUMN_NAME.toString());
            return getColumn(columnName);
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public SQLColumn[] getColumns()
    {
        List<SQLColumn> columns = new ArrayList<>();
        try
        {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, null);
            while(result.next())
            {
                String columnName = result.getString(SQLColumnMeta.COLUMN_NAME.toString());
                SQLDataType dataType = SQLDataType.valueOf(result.getString(SQLColumnMeta.DATA_TYPE.toString()));
                SQLColumn column = new SQLColumn(this, columnName, dataType);
                columns.add(column);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
        return columns.toArray(new SQLColumn[0]);
    }

    @Override
    public SQLDatabase getDatabase()
    {
        return database;
    }

    @Override
    public String getTableName()
    {
        return tableName;
    }

    @Override
    public SQLTableType getType()
    {
        return type;
    }
}
