package com.mikedeejay2.oosql.connector.data;

import com.mikedeejay2.oosql.connector.SQLiteConnection;
import com.mikedeejay2.oosql.misc.SQLType;

import java.io.File;

public class SQLiteConnectionData extends SQLConnectionData {
    protected File dbFile;

    public SQLiteConnectionData(String dbName, File dbFile) {
        super(dbName, SQLType.SQLITE);

        this.dbFile = dbFile;
    }

    @Override
    public SQLiteConnection createConnection() {
        return new SQLiteConnection(this);
    }

    public File getDbFile() {
        return dbFile;
    }

    public void setDbFile(File dbFile) {
        this.dbFile = dbFile;
    }

    @Override
    public String toString() {
        return dbName + ", " + dbFile.getPath();
    }
}
