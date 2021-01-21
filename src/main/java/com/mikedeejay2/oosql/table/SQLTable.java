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

    public SQLTable(SQLDatabase database, String tableName)
    {
        this.database = database;
        this.tableName = tableName;
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
        if(!columnExists(columnName)) return null;
        return new SQLColumn(this, columnName);
    }

    @Override
    public SQLColumn getColumn(int index)
    {
        ++index;
        try
        {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, null);
            result.absolute(index);
            String columnName = result.getString(SQLColumnMeta.COLUMN_NAME.asIndex());
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
                String columnName = result.getString(SQLColumnMeta.COLUMN_NAME.asIndex());
                SQLColumn column = new SQLColumn(this, columnName);
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
    public String getName()
    {
        return tableName;
    }

    @Override
    public SQLTableType getTableType()
    {
        String tableTypeStr = getMeta(SQLTableMeta.TABLE_TYPE);
        SQLTableType tableType = SQLTableType.valueOf(tableTypeStr);
        return tableType;
    }

    @Override
    public String getMeta(SQLTableMeta metaType)
    {
        try
        {
            ResultSet result = database.getMetaData().getTables(null, null, tableName, null);
            if(result.next())
            {
                return result.getString(metaType.asIndex());
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean columnExists(String columnName)
    {
        try
        {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, columnName);
            return result.next();
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean columnExists(SQLColumn column)
    {
        return columnExists(column.getName());
    }

    @Override
    public int getColumnsAmount()
    {
        try
        {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, null);
            ResultSetMetaData meta = result.getMetaData();
            return meta.getColumnCount();
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getRowsAmount()
    {
        try
        {
            ResultSet result = database.executeQuery("SELECT COUNT(*) FROM `" + tableName + "`");
            return result.getInt(1);
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return 0;
    }
}
