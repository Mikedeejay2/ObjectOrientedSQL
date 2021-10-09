package com.mikedeejay2.oosql.connector.data;

import com.mikedeejay2.oosql.connector.MySQLConnection;
import com.mikedeejay2.oosql.connector.SQLConnection;
import com.mikedeejay2.oosql.misc.SQLType;

public class MySQLConnectionData extends SQLConnectionData {
    protected String host;
    protected int port;
    protected String username;
    protected String password;
    protected boolean useSSL;

    public MySQLConnectionData(String dbName, String host, int port, String username, String password) {
        super(dbName, SQLType.MYSQL);

        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.useSSL = false;
    }

    @Override
    public MySQLConnection createConnection() {
        return new MySQLConnection(this);
    }

    public MySQLConnection createConnection(boolean legacy) {
        return new MySQLConnection(this, legacy);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean useSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    @Override
    public String toString() {
        return dbName + ", " + username + ":" + password + ", " + host + ":" + port;
    }
}
