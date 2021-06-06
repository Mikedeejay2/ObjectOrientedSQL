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

    @Override
    public int[] executeUpdate(String... commands)
    {
        int[] results = new int[commands.length];
        for(int i = 0; i < commands.length; ++i)
        {
            results[i] = executeUpdate(commands[i]);
        }
        return results;
    }

    @Override
    public ResultSet[] executeQuery(String... commands)
    {
        ResultSet[] results = new ResultSet[commands.length];
        for(int i = 0; i < commands.length; ++i)
        {
            results[i] = executeQuery(commands[i]);
        }
        return results;
    }

    @Override
    public int[] executeUpdate(PreparedStatement... statements)
    {
        int[] results = new int[statements.length];
        for(int i = 0; i < statements.length; ++i)
        {
            results[i] = executeUpdate(statements[i]);
        }
        return results;
    }

    @Override
    public ResultSet[] executeQuery(PreparedStatement... statements)
    {
        ResultSet[] results = new ResultSet[statements.length];
        for(int i = 0; i < statements.length; ++i)
        {
            results[i] = executeQuery(statements[i]);
        }
        return results;
    }

    @Override
    public PreparedStatement[] prepareStatement(String... commands)
    {
        PreparedStatement[] results = new PreparedStatement[commands.length];
        for(int i = 0; i < commands.length; ++i)
        {
            results[i] = prepareStatement(commands[i]);
        }
        return results;
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
