package com.mikedeejay2.oosql.connector;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLiteConnection implements SQLConnection
{
    protected Connection connection;
    protected String database;
    protected File dbFile;

    public SQLiteConnection(String database, File dbFile)
    {
        this.database = database;
        this.dbFile = dbFile;
    }

    @Override
    public synchronized boolean connect(boolean throwErrors)
    {
        if(isConnected()) return false;
        try
        {

            if(!dbFile.exists())
            {
                dbFile.getParentFile().mkdirs();
                dbFile.createNewFile();
            }
        }
        catch(Exception e)
        {
            if(throwErrors) e.printStackTrace();
            return false;
        }

        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

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
            return false;
        }
    }
}
