package com.mikedeejay2.oosql.database;

import com.mikedeejay2.oosql.table.SQLTable;
import com.mikedeejay2.oosql.table.SQLTableType;

import java.sql.DatabaseMetaData;

public interface SQLDatabaseMetaData {
    DatabaseMetaData getMetaData();

    String[] getCatalogs();

    SQLTable[] getTables(SQLTableType... types);

    String[] getTableNames(SQLTableType... types);

    boolean tableExists(String tableName);

    boolean tableExists(SQLTable table);

    int getTablesAmount(SQLTableType... types);

    boolean isEmpty();

    boolean exists();
}
