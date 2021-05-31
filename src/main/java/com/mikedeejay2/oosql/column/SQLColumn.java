package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.table.SQLTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLColumn implements SQLColumnInterface, SQLColumnMetaData
{
    protected final SQLDatabase database;
    protected final SQLTable table;
    protected String columnName;

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
    public int getMetaInt(SQLColumnMeta metaType)
    {
        try
        {
            ResultSet result = database.getMetaData().getColumns(null, null, table.getName(), columnName);
            if(result.next())
            {
                return result.getInt(metaType.asIndex());
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean isNotNull()
    {
        return getMetaString(SQLColumnMeta.IS_NULLABLE).equals("NO");
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
        return getMetaString(SQLColumnMeta.COLUMN_DEF) != null;
    }

    @Override
    public boolean autoIncrements()
    {
        return getMetaString(SQLColumnMeta.IS_AUTOINCREMENT).equals("YES");
    }

    @Override
    public boolean isUnique()
    {
        // TODO: How do we get unique? It isn't found anywhere in metadata.
        return isPrimaryKey();
    }

    @Override
    public boolean hasConstraint(SQLConstraint constraint)
    {
        return false;
    }

    @Override
    public SQLColumnInfo getInfo()
    {
        return new SQLColumnInfo(getDataType(), getName(), getSizes(), getConstraints(), getConstraintParams());
    }

    @Override
    public SQLConstraint[] getConstraints()
    {
        List<SQLConstraint> constraints = new ArrayList<>();
        boolean isNotNull = isNotNull();
        boolean isUnique = isUnique();
        boolean isPrimaryKey = isPrimaryKey();
        boolean isForeignKey = isForeignKey();
        boolean hasDefault = hasDefault();
        boolean autoIncrements = autoIncrements();

        if(isNotNull && !isPrimaryKey) constraints.add(SQLConstraint.NOT_NULL);
        if(isUnique && !isPrimaryKey) constraints.add(SQLConstraint.UNIQUE);
        if(isPrimaryKey) constraints.add(SQLConstraint.PRIMARY_KEY);
        if(isForeignKey) constraints.add(SQLConstraint.FOREIGN_KEY);
        if(hasDefault) constraints.add(SQLConstraint.DEFAULT);
        if(autoIncrements) constraints.add(SQLConstraint.AUTO_INCREMENT);

        return constraints.toArray(new SQLConstraint[0]);
    }

    @Override
    public String[] getConstraintParams()
    {
        List<String> constraintParams = new ArrayList<>();
        String def = getDefault();
        if(def != null)
        {
            constraintParams.add(def);
        }
        return constraintParams.toArray(new String[0]);
    }

    @Override
    public String getDefault()
    {
        return getMetaString(SQLColumnMeta.COLUMN_DEF);
    }
}
