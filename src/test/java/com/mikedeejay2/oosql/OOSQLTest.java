package com.mikedeejay2.oosql;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.connector.data.MySQLConnectionData;
import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class OOSQLTest
{
    private static SQLDatabase database;

    @BeforeAll
    public static void connectDB()
    {
        database = new SQLDatabase(new MySQLConnectionData(
            "test",
            "localhost",
            3306,
            "root",
            null));

        database.connect(true);

        if(database.exists())
        {
            database.wipeDatabase();
        }
        else
        {
            database.createDatabase();
        }
        database.reconnect(true);
        System.out.println("Connected to Database");
    }

    @AfterAll
    public static void disconnectDB()
    {
        database.disconnect(true);
        System.out.println("Disconnected from Database");
    }

    @Test
    @Order(0)
    public void testConnection()
    {
        assertTrue(database.isConnected());
    }

    @Test
    @Order(1)
    public void testCreateTable()
    {
        database.createTable(
            "test_table",
            new SQLColumnInfo(SQLDataType.INTEGER, "id", new SQLConstraint[]{SQLConstraint.PRIMARY_KEY, SQLConstraint.AUTO_INCREMENT}),
            new SQLColumnInfo(SQLDataType.INTEGER, "name", 20, SQLConstraint.NOT_NULL),
            new SQLColumnInfo(SQLDataType.VARCHAR, "username", 20, SQLConstraint.UNIQUE)
                            );
    }

    @Test
    @Order(2)
    public void testGetDBType()
    {
        assertNotNull(database.getConnectionType());
    }
}
