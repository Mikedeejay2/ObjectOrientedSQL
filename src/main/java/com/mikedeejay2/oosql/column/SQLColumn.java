package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.table.SQLTable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A column of a <tt>SQLTable</tt>.
 * <p>
 * Instances of <tt>SQLColumn</tt> should <b>ONLY</b> be obtained by using
 * {@link SQLTable#getColumn(String)} or similar.
 * <p>
 * Constructing this by using {@link SQLColumn#SQLColumn(SQLTable, String)} is unsafe and
 * could lead to errors. However, if <b>absolutely necessary</b>, a <tt>SQLColumn</tt> can
 * still be constructed independently from a table. All information provided to a constructor
 * must be accurate. If not, errors can and will occur.
 *
 * @see SQLTable
 * @author Mikedeejay2
 */
public class SQLColumn implements SQLColumnInterface
{
    // A reference to the parent database
    protected final SQLDatabase database;
    // A reference to the parent table
    protected final SQLTable table;
    // The name of the column
    protected String columnName;

    /**
     * Constructor for a column. Constructing this by using this constructor is unsafe and
     * could lead to errors. However, if <b>absolutely necessary</b>, a <tt>SQLColumn</tt> can
     * still be constructed independently from a table. All information provided to this constructor
     * must be accurate. If not, errors can and will occur.
     *
     * @param parentTable A reference to the parent table
     * @param columnName The name of the column
     */
    public SQLColumn(SQLTable parentTable, String columnName)
    {
        this.table = parentTable;
        this.database = parentTable.getDatabase();
        this.columnName = columnName;
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
        return columnName;
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
            ResultSet result = database.getMetaData().getColumns(null, null, table.getName(), columnName);
            return result.getString(metaType.asIndex());
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
            ResultSet result = database.getMetaData().getColumns(null, null, table.getName(), columnName);
            return result.getInt(metaType.asIndex());
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
            ResultSet resultCol = database.getMetaData().getColumns(null, null, table.getName(), columnName);
            return resultCol.getString(SQLColumnMeta.IS_NULLABLE.asIndex()).equals("YES");
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
                String curName = resultPrimary.getString(SQLColumnMeta.COLUMN_NAME.asIndex());
                if(columnName.equals(curName))
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
                String curName = resultForeign.getString(SQLColumnMeta.COLUMN_NAME.asIndex());
                if(columnName.equals(curName))
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
            ResultSet resultCol = database.getMetaData().getColumns(null, null, table.getName(), columnName);
            return resultCol.getString(SQLColumnMeta.COLUMN_DEF.asIndex()) != null;
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
            ResultSet resultCol = database.getMetaData().getColumns(null, null, table.getName(), columnName);
            return resultCol.getString(SQLColumnMeta.IS_AUTOINCREMENT.asIndex()).equals("YES");
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return false;
    }
}
