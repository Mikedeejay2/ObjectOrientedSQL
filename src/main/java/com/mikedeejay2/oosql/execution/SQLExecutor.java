package com.mikedeejay2.oosql.execution;

import com.mikedeejay2.oosql.connector.SQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLExecutor implements SQLExecutorInterface
{
    protected SQLConnection connection;

    @Override
    public int executeUpdate(String command)
    {
        try
        {
            PreparedStatement statement = prepareStatement(command);
            return statement.executeUpdate();
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
            return -1;
        }
    }

    @Override
    public ResultSet executeQuery(String command)
    {
        try
        {
            PreparedStatement statement = prepareStatement(command);
            return statement.executeQuery();

        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public int executeUpdate(PreparedStatement statement)
    {
        try
        {
            return statement.executeUpdate();
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
            return -1;
        }
    }

    @Override
    public ResultSet executeQuery(PreparedStatement statement)
    {
        try
        {
            return statement.executeQuery();
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public PreparedStatement prepareStatement(String command)
    {
        try
        {
            return this.getConnection().prepareStatement(command);
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }

    public Connection getConnection()
    {
        return connection.getConnection();
    }

    public SQLConnection getSQLConnection()
    {
        return connection;
    }

    public void setSQLConnection(SQLConnection connection)
    {
        this.connection = connection;
    }
}
