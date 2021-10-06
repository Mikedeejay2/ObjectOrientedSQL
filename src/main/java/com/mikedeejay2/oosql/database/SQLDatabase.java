package com.mikedeejay2.oosql.database;

import com.mikedeejay2.oosql.connector.MySQLConnection;
import com.mikedeejay2.oosql.connector.SQLConnection;
import com.mikedeejay2.oosql.connector.SQLiteConnection;
import com.mikedeejay2.oosql.connector.data.MySQLConnectionData;
import com.mikedeejay2.oosql.connector.data.SQLConnectionData;
import com.mikedeejay2.oosql.connector.data.SQLiteConnectionData;
import com.mikedeejay2.oosql.execution.SQLExecutor;
import com.mikedeejay2.oosql.misc.SQLType;
import com.mikedeejay2.oosql.sqlgen.DebugSQLGenerator;
import com.mikedeejay2.oosql.sqlgen.SQLGenerator;
import com.mikedeejay2.oosql.table.SQLTable;
import com.mikedeejay2.oosql.table.SQLTableInfo;
import com.mikedeejay2.oosql.table.SQLTableMeta;
import com.mikedeejay2.oosql.table.SQLTableType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLDatabase implements SQLDatabaseInterface, SQLDatabaseMetaData
{
    protected String databaseName;
    protected SQLConnection connection;
    protected SQLConnectionData connectionData;
    protected SQLGenerator generator;
    protected SQLExecutor executor;

    public SQLDatabase(SQLConnectionData data)
    {
        this.connectionData = data;
        this.databaseName = connectionData.getDBName();
        this.generator = new DebugSQLGenerator();
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
        if(connection == null) this.connection = connectionData.createConnection();
        executor.setSQLConnection(connection);
        return connection.connect(throwErrors);
    }

    @Override
    public boolean disconnect(boolean throwErrors)
    {
        if(!isConnected()) return false;
        return connection.disconnect(throwErrors);
    }

    @Override
    public boolean reconnect(boolean throwErrors)
    {
        if(!isConnected()) return connect(throwErrors);
        return disconnect(throwErrors) && connect(throwErrors);
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
        return databaseName;
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
    public SQLTable createTable(SQLTableInfo info)
    {
        String command = generator.createTable(info);

        int code = executor.executeUpdate(command);
        if(code == -1) return null;

        SQLTable table = new SQLTable(this, info.getTableName());
        return table;
    }

    @Override
    public boolean dropTable(String tableName)
    {
        String command = generator.dropTable(tableName);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean wipeDatabase()
    {
        return dropDatabase() && createDatabase();
    }

    @Override
    public boolean createDatabase()
    {
        String command = generator.createDatabase(databaseName);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean dropDatabase()
    {
        String command = generator.dropDatabase(databaseName);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean exists()
    {
        for(String catalog : getCatalogs())
        {
            if(databaseName.equals(catalog)) return true;
        }
        return false;
    }

    @Override
    public boolean renameDatabase(String newName)
    {
        String command = generator.renameDatabase(databaseName, newName);
        int code = executor.executeUpdate(command);
        connectionData.setDBName(newName);
        this.databaseName = newName;
        return code != -1;
    }

    @Override
    public boolean renameTable(String tableName, String newName)
    {
        String command = generator.renameTable(tableName, newName);
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
    public String[] getCatalogs()
    {
        List<String> catalogsList = new ArrayList<>();
        try
        {
            ResultSet catalogs = getMetaData().getCatalogs();
            while(catalogs.next())
            {
                catalogsList.add(catalogs.getString(1));
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return catalogsList.toArray(new String[0]);
    }

    @Override
    public SQLTable[] getTables(SQLTableType... types)
    {
        String[] typeStrs = null;

        if(types != null)
        {
            typeStrs = new String[types.length];
            for(int i = 0; i < typeStrs.length; ++i)
            {
                typeStrs[i] = types[i].get();
            }
        }
        SQLTable[] tables = new SQLTable[getTablesAmount(types)];
        try
        {
            ResultSet result = this.getMetaData().getTables(null, null, null, typeStrs);
            while(result.next())
            {
                String tableName = result.getString(SQLTableMeta.TABLE_NAME.asIndex());
                SQLTable table = new SQLTable(this, tableName);
                tables[result.getRow() - 1] = table;
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return tables;
    }

    @Override
    public String[] getTableNames(SQLTableType... types)
    {
        String[] typeStrs = null;

        if(types != null)
        {
            typeStrs = new String[types.length];
            for(int i = 0; i < typeStrs.length; ++i)
            {
                typeStrs[i] = types[i].get();
            }
        }
        String[] tables = new String[getTablesAmount(types)];
        try
        {
            ResultSet result = this.getMetaData().getTables(null, null, null, typeStrs);
            while(result.next())
            {
                String tableName = result.getString(SQLTableMeta.TABLE_NAME.asIndex());
                tables[result.getRow() - 1] = tableName;
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return tables;
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

    @Override
    public boolean isEmpty()
    {
        return getTablesAmount() == 0;
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
