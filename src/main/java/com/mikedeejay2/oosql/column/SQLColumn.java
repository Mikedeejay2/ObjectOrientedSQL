package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.table.SQLTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLColumn implements SQLColumnInterface
{
    protected SQLDatabase database;
    protected SQLTable table;
    protected String name;

    public SQLColumn(SQLTable parentTable, String name)
    {
        this.table = parentTable;
        this.database = parentTable.getDatabase();
        this.name = name;
    }

    @Override
    public SQLTable getParentTable()
    {
        return table;
    }

    @Override
    public SQLDatabase getDatabase()
    {
        return database;
    }

    @Override
    public SQLDataType getDataType()
    {
        int type = getMetaInt(SQLColumnMeta.DATA_TYPE);
        return SQLDataType.fromNative(type);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public int[] getSizes()
    {
        int size1 = getMetaInt(SQLColumnMeta.COLUMN_SIZE);
        int size2 = getMetaInt(SQLColumnMeta.DECIMAL_DIGITS);
        if(size1 != 0 && size2 != 0) return new int[]{size1, size2};
        else if(size1 != 0) return new int[]{size1};
        else if(size2 != 0) return new int[]{size2};
        return new int[0];
    }

    @Override
    public String getMetaString(SQLColumnMeta metaType)
    {
        try
        {
            ResultSet result = database.getMetaData().getColumns(null, null, table.getName(), name);
            return result.getString(metaType.toString());
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public int getMetaInt(SQLColumnMeta metaType)
    {
        try
        {
            ResultSet result = database.getMetaData().getColumns(null, null, table.getName(), name);
            return result.getInt(metaType.toString());
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean isNullable()
    {

        try
        {
            ResultSet resultCol = database.getMetaData().getColumns(null, null, table.getName(), name);
            return resultCol.getString(SQLColumnMeta.IS_NULLABLE.toString()).equals("YES");
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isPrimaryKey()
    {
        try
        {
            ResultSet resultPrimary = database.getMetaData().getPrimaryKeys(null, null, table.getName());
            while(resultPrimary.next())
            {
                String curName = resultPrimary.getString(SQLColumnMeta.COLUMN_NAME.toString());
                if(name.equals(curName))
                {
                    return true;
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isForeignKey()
    {
        try
        {
            ResultSet resultForeign = database.getMetaData().getImportedKeys(null, null, table.getName());
            while(resultForeign.next())
            {
                String curName = resultForeign.getString(SQLColumnMeta.COLUMN_NAME.toString());
                if(name.equals(curName))
                {
                    return true;
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean hasDefault()
    {
        try
        {
            ResultSet resultCol = database.getMetaData().getColumns(null, null, table.getName(), name);
            return resultCol.getString(SQLColumnMeta.COLUMN_DEF.toString()) != null;
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean autoIncrements()
    {
        try
        {
            ResultSet resultCol = database.getMetaData().getColumns(null, null, table.getName(), name);
            return resultCol.getString(SQLColumnMeta.IS_AUTOINCREMENT.toString()).equals("YES");
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return false;
    }
}
