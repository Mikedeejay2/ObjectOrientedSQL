package com.mikedeejay2.oosql.connector;

import java.sql.Connection;

public interface SQLConnection {
    boolean connect(boolean throwErrors);

    boolean disconnect(boolean throwErrors);

    Connection getConnection();

    boolean isConnected();
}
