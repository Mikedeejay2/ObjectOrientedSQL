package com.mikedeejay2.oosql.misc;

import com.mikedeejay2.oosql.connector.MySQLConnection;
import com.mikedeejay2.oosql.connector.SQLConnection;
import com.mikedeejay2.oosql.connector.SQLiteConnection;
import com.mikedeejay2.oosql.connector.data.MySQLConnectionData;
import com.mikedeejay2.oosql.connector.data.SQLConnectionData;
import com.mikedeejay2.oosql.connector.data.SQLiteConnectionData;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

public enum SQLType {
    MYSQL(MySQLConnection.class, MySQLConnectionData.class),
    SQLITE(SQLiteConnection.class, SQLiteConnectionData.class),
    ;

    private final Class<? extends SQLConnection> connectClass;
    private final Class<? extends SQLConnectionData> dataClass;
    private final Constructor<? extends SQLConnection> constructor;

    SQLType(Class<? extends SQLConnection> connectClass, Class<? extends SQLConnectionData> dataClass) {
        this.connectClass = connectClass;
        this.dataClass = dataClass;
        this.constructor = getConstructor();
    }

    public SQLConnection createConnection(SQLConnectionData data) {
        try {
            return constructor.newInstance(data);
        } catch(ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private Constructor<? extends SQLConnection> getConstructor() {
        Constructor<? extends SQLConnection> constructor = null;
        try {
            constructor = connectClass.getConstructor(dataClass);
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        }
        return constructor;
    }
}
