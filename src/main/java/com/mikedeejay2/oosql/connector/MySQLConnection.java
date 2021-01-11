package com.mikedeejay2.oosql.connector;

import com.mikedeejay2.oosql.connector.data.MySQLConnectionData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection implements SQLConnection
{
    protected Connection connection;
    protected MySQLConnectionData data;

    public MySQLConnection(MySQLConnectionData connectionData)
    {
        this.data = connectionData;
    }

    @Override
    public synchronized boolean connect(boolean throwErrors)
    {
        try
        {
            if(isConnected()) return false;

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + data.getHost() + ":" + data.getPort() + "/" + data.getDBName() + "?useSSL=" + data.useSSL(),
                    data.getUsername(), data.getPassword());

            return true;
        }
        catch(Exception e)
        {
            if(throwErrors) e.printStackTrace();
            return false;
        }
    }

    @Override
    public synchronized boolean disconnect(boolean throwErrors)
    {
        if(!isConnected()) return false;
        try
        {
            connection.close();
            connection = null;
            return true;
        }
        catch(Exception e)
        {
            if(throwErrors) e.printStackTrace();
            return false;
        }
    }

    @Override
    public Connection getConnection()
    {
        return connection;
    }

    @Override
    public boolean isConnected()
    {
        try
        {
            return connection != null && !connection.isClosed();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public MySQLConnectionData getData()
    {
        return data;
    }
}
