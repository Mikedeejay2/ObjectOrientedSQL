package com.mikedeejay2.oosql.connector.data;

import com.mikedeejay2.oosql.misc.SQLType;

import java.io.File;

public class SQLiteConnectionData extends SQLConnectionData
{
    protected File dbFile;

    public SQLiteConnectionData(String dbName, File dbFile)
    {
        super(dbName, SQLType.SQLITE);

        this.dbFile = dbFile;
    }

    public File getDbFile()
    {
        return dbFile;
    }

    public void setDbFile(File dbFile)
    {
        this.dbFile = dbFile;
    }
}
