package com.mikedeejay2.oosql;

import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.connector.data.MySQLConnectionData;
import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.table.SQLTable;
import com.mikedeejay2.oosql.table.SQLTableType;
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
        System.out.println("Connected to Database");

        if(database.exists())
        {
            database.wipeDatabase();
        }
        else
        {
            database.createDatabase();
        }
        database.reconnect(true);
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
    public void testGetDBType()
    {
        assertNotNull(database.getConnectionType());
    }

    @Test
    @Order(1)
    public void testGetDBName()
    {
        assertNotNull(database.getName());
    }

    @Test
    @Order(1)
    public void testGetDBConnectionInfo()
    {
        assertNotNull(database.getConnectionData());
    }

    @Test
    @Order(1)
    public void testGetDBConnection()
    {
        assertNotNull(database.getConnection());
    }

    @Test
    @Order(2)
    public void testCreateTable()
    {
        database.createTable(
            "test_table",
            new SQLColumnInfo(SQLDataType.INTEGER, "id", new SQLConstraint[]{SQLConstraint.PRIMARY_KEY, SQLConstraint.AUTO_INCREMENT}),
            new SQLColumnInfo(SQLDataType.VARCHAR, "name", 20, SQLConstraint.NOT_NULL),
            new SQLColumnInfo(SQLDataType.VARCHAR, "username", 20, new SQLConstraint[]{SQLConstraint.UNIQUE, SQLConstraint.DEFAULT}, "ARandomUser")
                            );

        assertTrue(database.tableExists("test_table"));
    }

    @Test
    @Order(3)
    public void testTableExists()
    {
        assertTrue(database.tableExists("test_table"));
        assertFalse(database.tableExists("nonexistent_table"));
    }

    @Test
    @Order(3)
    public void testTablesAmount()
    {
        assertTrue(database.getTablesAmount(SQLTableType.TABLE) != 0);
    }

    @Test
    @Order(3)
    public void testRenameTable()
    {
        SQLTable table = database.getTable("test_table");
        table.renameTable("renamed_table");

        assertTrue(database.tableExists("renamed_table"));
        assertFalse(database.tableExists("test_table"));

        table.renameTable("test_table");

        assertTrue(database.tableExists("test_table"));
        assertFalse(database.tableExists("renamed_table"));
    }

    @Test
    @Order(3)
    public void testTableType()
    {
        SQLTable table = database.getTable("test_table");
        assertSame(table.getTableType(), SQLTableType.TABLE);
    }

    @Test
    @Order(3)
    public void testColumnExists()
    {
        SQLTable table = database.getTable("test_table");
        String name = "username";
        SQLColumn column = table.getColumn(name);

        String fakeName = "nonexistent_name";
        SQLColumn fakeCol = new SQLColumn(table, fakeName);

        assertTrue(table.columnExists(name));
        assertTrue(table.columnExists(column));
        assertFalse(table.columnExists(fakeName));
        assertFalse(table.columnExists(fakeCol));
    }

    @Test
    @Order(3)
    public void testGetColumnSize()
    {
        SQLTable table = database.getTable("test_table");
        String name = "username";
        SQLColumn column = table.getColumn(name);
        int[] sizes = column.getSizes();
        assertNotNull(sizes);
        assertEquals(1, sizes.length);
        assertEquals(20, sizes[0]);
    }

    @Test
    @Order(3)
    public void testGetDataType()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn column1 = table.getColumn("name");
        SQLDataType type1 = column1.getDataType();
        assertNotNull(type1);
        assertSame(type1, SQLDataType.VARCHAR);

        SQLColumn column2 = table.getColumn("id");
        SQLDataType type2 = column2.getDataType();
        assertNotNull(type2);
        assertSame(type2, SQLDataType.INTEGER);
    }

    @Test
    @Order(3)
    public void testIsNullable()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn column1 = table.getColumn("name");
        SQLColumn column2 = table.getColumn("username");
        assertFalse(column1.isNullable());
        assertTrue(column2.isNullable());
    }

    @Test
    @Order(3)
    public void testIsPrimaryKey()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn column1 = table.getColumn("name");
        SQLColumn column2 = table.getColumn("id");
        assertFalse(column1.isPrimaryKey());
        assertTrue(column2.isPrimaryKey());
    }

    @Test
    @Order(3)
    public void testIsForeignKey()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn column1 = table.getColumn("id");
        assertFalse(column1.isForeignKey());
    }

    @Test
    @Order(3)
    public void testHasDefault()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn column1 = table.getColumn("name");
        SQLColumn column2 = table.getColumn("username");
        assertFalse(column1.hasDefault());
        assertTrue(column2.hasDefault());
    }

    @Test
    @Order(3)
    public void testAutoIncrements()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn column1 = table.getColumn("username");
        SQLColumn column2 = table.getColumn("id");
        assertFalse(column1.autoIncrements());
        assertTrue(column2.autoIncrements());
    }
}
