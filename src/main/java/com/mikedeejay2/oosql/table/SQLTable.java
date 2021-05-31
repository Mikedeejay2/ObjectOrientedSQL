package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.column.SQLColumnMeta;
import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.execution.SQLExecutor;
import com.mikedeejay2.oosql.sqlgen.SQLGenerator;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLTable implements SQLTableInterface, SQLTableMetaData
{
    protected final SQLDatabase database;
    protected final SQLExecutor executor;
    protected final SQLGenerator generator;
    protected String tableName;

    public SQLTable(SQLDatabase database, String tableName)
    {
        this.database = database;
        this.executor = database.getExecutor();
        this.generator = database.getGenerator();
        this.tableName = tableName;
    }

    @Override
    public boolean renameTable(String newName)
    {
        String command = generator.renameTable(tableName, newName);
        int code = database.getExecutor().executeUpdate(command);
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
        SQLColumn[] columns = new SQLColumn[getColumnsAmount()];
        try
        {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, null);
            while(result.next())
            {
                String columnName = result.getString(SQLColumnMeta.COLUMN_NAME.asIndex());
                SQLColumn column = new SQLColumn(this, columnName);
                columns[result.getRow() - 1] = column;
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
        return columns;
    }

    @Override
    public String[] getColumnNames()
    {
        String[] columns = new String[getColumnsAmount()];
        try
        {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, null);
            while(result.next())
            {
                String columnName = result.getString(SQLColumnMeta.COLUMN_NAME.asIndex());
                columns[result.getRow() - 1] = columnName;
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
        return columns;
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
    public boolean addColumn(SQLColumnInfo info)
    {
        String command = generator.addColumn(tableName, info);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean addColumns(SQLColumnInfo... info)
    {
        String command = generator.addColumns(tableName, info);
        int code = executor.executeUpdate(command);
        return code != -1;
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
            ResultSetMetaData resultMeta = result.getMetaData();
            return resultMeta.getColumnCount();
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
            String command = generator.countRows(tableName);
            ResultSet result = executor.executeQuery(command);
            if(result.next())
            {
                return result.getInt(1);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public SQLTableInfo getInfo()
    {
        return null;
    }

    @Override
    public SQLColumnInfo[] getColumnInfos()
    {
        SQLColumn[] columns = getColumns();
        SQLColumnInfo[] infos = new SQLColumnInfo[columns.length];
        for(int i = 0; i < columns.length; ++i)
        {
            infos[i] = columns[i].getInfo();
        }
        return infos;
    }
}
