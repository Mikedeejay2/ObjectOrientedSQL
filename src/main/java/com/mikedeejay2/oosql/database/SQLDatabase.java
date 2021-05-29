package com.mikedeejay2.oosql.database;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.connector.MySQLConnection;
import com.mikedeejay2.oosql.connector.SQLConnection;
import com.mikedeejay2.oosql.connector.SQLiteConnection;
import com.mikedeejay2.oosql.connector.data.MySQLConnectionData;
import com.mikedeejay2.oosql.connector.data.SQLConnectionData;
import com.mikedeejay2.oosql.connector.data.SQLiteConnectionData;
import com.mikedeejay2.oosql.execution.SQLExecutor;
import com.mikedeejay2.oosql.misc.SQLType;
import com.mikedeejay2.oosql.sqlgen.SQLGenerator;
import com.mikedeejay2.oosql.sqlgen.SimpleSQLGenerator;
import com.mikedeejay2.oosql.table.SQLTable;
import com.mikedeejay2.oosql.table.SQLTableMeta;
import com.mikedeejay2.oosql.table.SQLTableType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLDatabase implements SQLDatabaseInterface, SQLDatabaseMetaData
{
    protected SQLConnection connection;
    protected SQLConnectionData connectionData;
    protected SQLGenerator generator;
    protected SQLExecutor executor;

    public SQLDatabase(SQLConnectionData data)
    {
        this.connectionData = data;
        this.generator = new SimpleSQLGenerator();
        this.executor = new SQLExecutor();
    }

    @Override
    public void setInfo(SQLConnectionData data)
    {
        this.connectionData = data;
    }

    @Override
    public boolean connect(boolean throwErrors)
    {
        if(isConnected()) return false;
        switch(connectionData.getType())
        {
            case MYSQL:
                if(connection == null) this.connection = new MySQLConnection((MySQLConnectionData) connectionData);
                return connection.connect(throwErrors);
            case SQLITE:
                if(connection == null) this.connection = new SQLiteConnection((SQLiteConnectionData) connectionData);
                return connection.connect(throwErrors);
        }
        return false;
    }

    @Override
    public boolean disconnect(boolean throwErrors)
    {
        if(!isConnected()) return false;
        return connection.disconnect(throwErrors);
    }

    @Override
    public boolean isConnected()
    {
        return connection != null && connection.isConnected();
    }

    @Override
    public SQLConnection getSQLConnection()
    {
        return connection;
    }

    @Override
    public Connection getConnection()
    {
        return connection.getConnection();
    }

    @Override
    public SQLType getConnectionType()
    {
        return connectionData.getType();
    }

    @Override
    public String getName()
    {
        return connectionData.getDBName();
    }

    @Override
    public SQLConnectionData getConnectionData()
    {
        return connectionData;
    }

    @Override
    public SQLTable getTable(String tableName)
    {
        if(!tableExists(tableName)) return null;
        return new SQLTable(this, tableName);
    }

    @Override
    public SQLTable createTable(String tableName, SQLColumnInfo... info)
    {
        String command = generator.createTable(tableName, info);

        int code = executor.executeUpdate(command);
        if(code == -1) return null;

        SQLTable table = new SQLTable(this, tableName);
        return table;
    }

    @Override
    public boolean removeTable(String tableName)
    {
        String command = generator.dropTable(tableName);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public DatabaseMetaData getMetaData()
    {
        try
        {
            return this.getConnection().getMetaData();
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public SQLTable[] getTables(SQLTableType type)
    {
        List<SQLTable> tables = new ArrayList<>();
        try
        {
            ResultSet result = this.getMetaData().getTables(null, null, null, new String[]{type.get()});
            while(result.next())
            {
                String tableName = result.getString(SQLTableMeta.TABLE_NAME.asIndex());
                SQLTable table = new SQLTable(this, tableName);
                tables.add(table);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return tables.toArray(new SQLTable[0]);
    }

    @Override
    public boolean tableExists(String tableName)
    {
        try
        {
            ResultSet result = getMetaData().getTables(null, null, tableName, null);
            return result.next();
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tableExists(SQLTable table)
    {
        return tableExists(table.getName());
    }

    @Override
    public int getTablesAmount(SQLTableType... types)
    {
        String[] typesStrs = null;
        if(types != null)
        {
            typesStrs = new String[types.length];
            for(int i = 0; i < types.length; ++i)
            {
                SQLTableType curType = types[i];
                typesStrs[i] = curType.get();
            }
        }

        try
        {
            ResultSet result = this.getMetaData().getTables(null, null, null, typesStrs);
            result.last();
            return result.getRow();
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return 0;
    }

    public SQLGenerator getGenerator()
    {
        return generator;
    }

    public SQLExecutor getExecutor()
    {
        return executor;
    }
}
