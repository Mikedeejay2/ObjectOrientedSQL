package com.mikedeejay2.oosql;

import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.connector.data.MySQLConnectionData;
import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraints;
import com.mikedeejay2.oosql.table.SQLTable;
import com.mikedeejay2.oosql.table.SQLTableInfo;
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
    public void testDBExists()
    {
        assertTrue(database.exists());
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
            new SQLTableInfo(
                "test_table",
                null,
                new SQLColumnInfo(
                    SQLDataType.INTEGER, "id",
                    new SQLConstraints().addPrimaryKey().addAutoIncrement()),
                new SQLColumnInfo(
                    SQLDataType.VARCHAR, "name", 20,
                    new SQLConstraints().addNotNull()),
                new SQLColumnInfo(
                    SQLDataType.VARCHAR, "username", 20,
                    new SQLConstraints().addUnique().addDefault("ARandomUser"))
        ));

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
        assertTrue(column1.isNotNull());
        assertFalse(column2.isNotNull());
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

    @Test
    @Order(3)
    public void testGetColumns()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn[] columns = table.getColumns();
        assertNotNull(columns);
        assertEquals(columns.length, table.getColumnsAmount());
        for(SQLColumn column : columns)
        {
            assertNotNull(column);
            assertTrue(table.columnExists(column));
        }
    }

    @Test
    @Order(3)
    public void testGetColumnNames()
    {
        SQLTable table = database.getTable("test_table");
        String[] colNames = table.getColumnNames();
        assertNotNull(colNames);
        assertEquals(colNames.length, table.getColumnsAmount());
        for(String name : colNames)
        {
            assertNotNull(name);
            assertTrue(table.columnExists(name));
        }
    }

    @Test
    @Order(3)
    public void testHasConstraint()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn column1 = table.getColumn("username");
        SQLColumn column2 = table.getColumn("id");
        assertTrue(column2.hasConstraint(SQLConstraint.PRIMARY_KEY));
        assertFalse(column1.hasConstraint(SQLConstraint.NOT_NULL));
    }

    @Test
    @Order(3)
    public void testGetColumnInfo()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn column1 = table.getColumn("username");
        SQLColumn column2 = table.getColumn("id");
        SQLColumnInfo columnInfo1 = column1.getInfo();
        SQLColumnInfo columnInfo2 = column2.getInfo();
        assertNotNull(columnInfo1);
        assertNotNull(columnInfo2);
        assertEquals(columnInfo1.getName(), column1.getName());
        assertEquals(20, columnInfo1.getSizes()[0]);
        assertTrue(columnInfo2.getConstraints().length() > 0);
    }

    @Test
    @Order(3)
    public void testGetColumnConstraints()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn column1 = table.getColumn("name");
        SQLColumn column2 = table.getColumn("id");
        SQLConstraints col1Constraints = column1.getConstraints();
        SQLConstraints col2Constraints = column2.getConstraints();
        assertNotNull(col1Constraints);
        assertNotNull(col2Constraints);
        assertTrue(col1Constraints.length() > 0);
        assertTrue(col2Constraints.length() > 0);
        assertSame(col2Constraints.get(0).getConstraint(), SQLConstraint.PRIMARY_KEY);
        assertSame(col1Constraints.get(0).getConstraint(), SQLConstraint.NOT_NULL);
    }

    @Test
    @Order(3)
    public void testGetDefault()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn column = table.getColumn("username");
        String columnDefault = column.getDefault();
        assertNotNull(columnDefault);
        assertEquals(columnDefault, "ARandomUser");
    }

    @Test
    @Order(3)
    public void testIsDBEmpty()
    {
        assertFalse(database.isEmpty());
    }

    @Test
    @Order(3)
    public void testGetColumnInfos()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumnInfo[] infos = table.getColumnInfos();
        assertNotNull(infos);
        assertEquals(3, infos.length);
        assertEquals("id", infos[0].getName());
    }

    @Test
    @Order(3)
    public void testGetTableInfo()
    {
        SQLTable table = database.getTable("test_table");
        SQLTableInfo info = table.getInfo();
        assertNotNull(info);
        assertEquals("test_table", info.getTableName());
        assertEquals(3, info.getColumns().length);
    }

    @Test
    @Order(3)
    public void testGetColumnName()
    {
        SQLTable table = database.getTable("test_table");
        String columnName = table.getColumnName(0);
        assertNotNull(columnName);
        assertEquals(columnName, "id");
    }

    @Test
    @Order(3)
    public void testGetTableConstraints()
    {
        SQLTable table = database.getTable("test_table");
        SQLConstraints constraints = table.getConstraints();
        assertNotNull(constraints);
        assertEquals(constraints.length(), 0);
    }

    @Test
    @Order(4)
    public void testAddColumn()
    {
        SQLTable table = database.getTable("test_table");
        table.addColumn(new SQLColumnInfo(SQLDataType.VARCHAR, "password", 20));
        assertTrue(table.columnExists("password"));
        SQLColumn column = table.getColumn("password");
        assertNotNull(column);
        assertEquals("password", column.getName());
    }

    @Test
    @Order(4)
    public void testAddConstraint()
    {
        SQLTable table = database.getTable("test_table");
        SQLColumn column = table.getColumn("username");
        assertFalse(column.hasConstraint(SQLConstraint.NOT_NULL));
        column.addConstraint(SQLConstraintData.ofNotNull());
        assertTrue(column.hasConstraint(SQLConstraint.NOT_NULL));
    }

    @Test
    @Order(5)
    public void testRenameColumn()
    {
        SQLTable table = database.getTable("test_table");
        table.renameColumn("password", "newpass");
        assertTrue(table.columnExists("newpass"));
        assertFalse(table.columnExists("password"));
        table.renameColumn("newpass", "password");
        assertTrue(table.columnExists("password"));
        assertFalse(table.columnExists("newpass"));
    }

    @Test
    @Order(6)
    public void testRemoveColumn()
    {
        SQLTable table = database.getTable("test_table");
        table.removeColumn("password");
        assertFalse(table.columnExists("password"));
    }
}
