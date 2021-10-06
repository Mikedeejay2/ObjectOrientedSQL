package com.mikedeejay2.oosql.connector.data;

import com.mikedeejay2.oosql.connector.SQLConnection;
import com.mikedeejay2.oosql.misc.SQLType;

public abstract class SQLConnectionData {
    protected final SQLType type;
    protected String dbName;

    public SQLConnectionData(String dbName, SQLType type) {
        this.dbName = dbName;
        this.type = type;
    }

    public String getDBName() {
        return dbName;
    }

    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

    public SQLType getType() {
        return type;
    }

    public SQLConnection createConnection() {
        return type.createConnection(this);
    }
}
