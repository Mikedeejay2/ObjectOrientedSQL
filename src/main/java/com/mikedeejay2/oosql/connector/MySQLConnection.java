package com.mikedeejay2.oosql.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection implements SQLConnection
{
    protected Connection connection;
    protected String host;
    protected int port;
    protected String username;
    protected String password;
    protected String database;
    protected boolean useSSL;

    public MySQLConnection(String host, int port, String username, String password, String database)
    {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.useSSL = false;
    }

    @Override
    public synchronized boolean connect(boolean throwErrors)
    {
        try
        {
            if(isConnected()) return false;

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + useSSL,
                    username, password);

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

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getDatabase()
    {
        return database;
    }

    public void setDatabase(String database)
    {
        this.database = database;
    }

    public boolean isUseSSL()
    {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL)
    {
        this.useSSL = useSSL;
    }
}
