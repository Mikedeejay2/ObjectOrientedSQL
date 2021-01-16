package com.mikedeejay2.oosql.database;

import com.mikedeejay2.oosql.SQLObject;
import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.connector.SQLConnection;
import com.mikedeejay2.oosql.connector.data.SQLConnectionData;
import com.mikedeejay2.oosql.misc.SQLType;
import com.mikedeejay2.oosql.table.SQLTable;
import com.mikedeejay2.oosql.table.SQLTableType;

import java.sql.*;

interface SQLDatabaseInterface extends SQLObject
{
    void setInfo(SQLConnectionData data);

    boolean connect(boolean throwErrors);

    boolean disconnect(boolean throwErrors);

    boolean isConnected();

    SQLConnection getSQLConnection();

    Connection getConnection();

    SQLType getType();

    String getName();

    SQLConnectionData getConnectionData();

    SQLTable getTable(String tableName);

    SQLTable createTable(String tableName, SQLColumnInfo... info);

    boolean removeTable(String tableName);

    int executeUpdate(String command);

    ResultSet executeQuery(String command);

    int executeUpdate(PreparedStatement statement);

    ResultSet executeQuery(PreparedStatement statement);

    PreparedStatement prepareStatement(String command);

    DatabaseMetaData getMetaData();

    SQLTable[] getTables(SQLTableType type);

    boolean tableExists(String tableName);

    boolean tableExists(SQLTable table);

    int getTablesAmount();
}
