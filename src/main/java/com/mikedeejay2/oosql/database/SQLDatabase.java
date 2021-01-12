package com.mikedeejay2.oosql.database;

import com.mikedeejay2.oosql.SQLObject;
import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.connector.MySQLConnection;
import com.mikedeejay2.oosql.connector.SQLConnection;
import com.mikedeejay2.oosql.connector.SQLiteConnection;
import com.mikedeejay2.oosql.connector.data.MySQLConnectionData;
import com.mikedeejay2.oosql.connector.data.SQLConnectionData;
import com.mikedeejay2.oosql.connector.data.SQLiteConnectionData;
import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.misc.SQLType;
import com.mikedeejay2.oosql.table.SQLTable;
import com.mikedeejay2.oosql.table.SQLTableMeta;
import com.mikedeejay2.oosql.table.SQLTableType;

import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLDatabase implements SQLObject
{
    protected SQLConnection connection;
    protected SQLConnectionData connectionData;

    public SQLDatabase(SQLConnectionData data)
    {
        this.connectionData = data;
    }

    public void setInfo(SQLConnectionData data)
    {
        this.connectionData = data;
    }

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

    public boolean disconnect(boolean throwErrors)
    {
        if(!isConnected()) return false;
        return connection.disconnect(throwErrors);
    }

    public boolean isConnected()
    {
        return connection != null && connection.isConnected();
    }

    public SQLConnection getSQLConnection()
    {
        return connection;
    }

    public Connection getConnection()
    {
        return connection.getConnection();
    }

    public SQLType getType()
    {
        return connectionData.getType();
    }

    public String getDBName()
    {
        return connectionData.getDBName();
    }

    public SQLConnectionData getConnectionData()
    {
        return connectionData;
    }

    public SQLTable getTable(String tableName)
    {
        try
        {
            ResultSet result = getMetaData().getTables(null, null, tableName, null);
            if(result.next())
            {
                SQLTableType type  = SQLTableType.valueOf(result.getString(SQLTableMeta.TABLE_TYPE.toString()));
                SQLTable     table = new SQLTable(this, tableName, type);
                return table;
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return null;
    }

    public SQLTable createTable(String tableName, SQLColumnInfo... info)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE `")
               .append(tableName)
               .append("`");

        if(info.length > 0) builder.append(" (");

        List<Map.Entry<SQLConstraint, SQLColumnInfo>> endConstraints = new ArrayList<>();

        for(int index = 0; index < info.length; ++index)
        {
            SQLColumnInfo curInfo = info[index];
            String name = curInfo.getName();
            SQLConstraint[] constraints = curInfo.getConstraints();
            int[]       sizes = curInfo.getSizes();
            SQLDataType type  = curInfo.getType();

            builder.append("`")
                   .append(name)
                   .append("` ")
                   .append(type.getName());

            if(sizes.length > 0)
            {
                builder.append("(");
                for(int sizeI = 0; sizeI < sizes.length; ++sizeI)
                {
                    builder.append(sizes[sizeI]);
                    if(sizeI != sizes.length - 1) builder.append(", ");
                }
                builder.append(")");
            }

            for(SQLConstraint constraint : constraints)
            {
                if(constraint.atEnd())
                {
                    endConstraints.add(new AbstractMap.SimpleEntry<>(constraint, curInfo));
                    continue;
                }

                builder.append(" ");
                String constraintStr = constraint.get();
                builder.append(constraintStr);
            }

            if(index != info.length - 1) builder.append(", ");
        }

        for(Map.Entry<SQLConstraint, SQLColumnInfo> entry : endConstraints)
        {
            SQLConstraint constraint = entry.getKey();
            SQLColumnInfo curInfo = entry.getValue();
            String data = constraint.useExtra() ? curInfo.getExtra() : "`" + curInfo.getName() + "`";
            String name = constraint.get();
            builder.append(", ")
                   .append(name)
                   .append("(")
                   .append(data)
                   .append(")");
        }

        if(info.length > 0) builder.append(")");

        String command = builder.toString();

        System.out.println(command);

        int code = executeUpdate(command);
        if(code == -1) return null;

        SQLTable table = new SQLTable(this, tableName, SQLTableType.TABLE);
        return table;
    }

    public boolean removeTable(String tableName)
    {

        String command = "DROP TABLE `" + tableName + "`";
        System.out.println(command);
        int code = executeUpdate(command);
        return code != -1;
    }

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

    public ResultSet getTables(SQLTableType type)
    {
        try
        {
            return this.getMetaData().getTables(null, null, null, new String[]{type.get()});
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }
    }
}
