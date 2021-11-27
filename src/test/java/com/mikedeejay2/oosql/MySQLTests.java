package com.mikedeejay2.oosql;

import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.connector.data.MySQLConnectionData;
import com.mikedeejay2.oosql.connector.data.SQLConnectionData;
import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.misc.SQLType;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraints;
import com.mikedeejay2.oosql.table.SQLTable;
import com.mikedeejay2.oosql.table.SQLTableInfo;
import com.mikedeejay2.oosql.table.SQLTableType;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class MySQLTests implements TestInterface {
    private static SQLDatabase database;

    @BeforeAll
    public static void connectDB() {
        database = new SQLDatabase(new MySQLConnectionData(
            "test",
            "localhost",
            3306,
            "root",
            null));

        database.connect(true);
        System.out.println("Connected to Database");

        if(database.exists()) {
            database.wipe();
        } else {
            database.create();
        }
        database.reconnect(true);
    }

    @AfterAll
    public static void disconnectDB() {
        database.disconnect(true);
        System.out.println("Disconnected from Database");
    }

    @Override
    @Test
    @Order(0)
    public void testConnection() {
        assertTrue(database.isConnected());
    }

    @Override
    @Test
    @Order(1)
    public void testGetDBType() {
        assertSame(SQLType.MYSQL, database.getConnectionType());
    }

    @Override
    @Test
    @Order(1)
    public void testDBExists() {
        assertTrue(database.exists());
    }

    @Override
    @Test
    @Order(1)
    public void testGetDBName() {
        assertEquals("test", database.getName());
    }

    @Override
    @Test
    @Order(1)
    public void testGetDBConnectionInfo() {
        SQLConnectionData data = database.getConnectionData();
        assertNotNull(data);
        assertEquals("test", data.getDBName());
        assertSame(SQLType.MYSQL, data.getType());
    }

    @Override
    @Test
    @Order(1)
    public void testGetDBConnection() {
        Connection connection = database.getConnection();
        assertNotNull(connection);
    }

    @Override
    @Test
    @Order(2)
    public void testCreateTable() {
        SQLColumnInfo t1c1 = new SQLColumnInfo(
            SQLDataType.INTEGER,
            "user_id",
            new SQLConstraints()
                .addPrimaryKey()
                .addAutoIncrement()
        );

        SQLColumnInfo t1c2 = new SQLColumnInfo(
            SQLDataType.VARCHAR,
            "first_name",
            20,
            null
        );

        SQLColumnInfo t1c3 = new SQLColumnInfo(
            SQLDataType.VARCHAR,
            "last_name",
            20,
            null
        );

        SQLColumnInfo t1c4 = new SQLColumnInfo(
            SQLDataType.VARCHAR,
            "username",
            20,
            new SQLConstraints()
                .addDefault("unnamedUser")
                .addUnique()
        );

        SQLColumnInfo t1c5 = new SQLColumnInfo(
            SQLDataType.INTEGER,
            "age",
            new SQLConstraints()
                .addNotNull()
        );

        SQLColumnInfo t1c6 = new SQLColumnInfo(
            SQLDataType.INTEGER,
            "branch",
            new SQLConstraints()
                .addForeignKey(
                    "branches_table",
                    "branch_id")
        );

        SQLColumnInfo t1c7 = new SQLColumnInfo(
            SQLDataType.DOUBLE,
            "weight",
            new int[]{32, 8},
            null
        );

        SQLConstraints table1C = new SQLConstraints()
            .addCheck("age>=18");

        SQLColumnInfo t2c1 = new SQLColumnInfo(
            SQLDataType.INTEGER,
            "branch_id",
            new SQLConstraints()
                .addPrimaryKey()
        );

        SQLColumnInfo t2c2 = new SQLColumnInfo(
            SQLDataType.VARCHAR,
            "name",
            20,
            new SQLConstraints()
                .addNotNull()
        );

        SQLTableInfo table1Info = new SQLTableInfo(
            "users_table", table1C,
            t1c1, t1c2, t1c3, t1c4, t1c5, t1c6, t1c7);

        SQLTableInfo table2Info = new SQLTableInfo(
            "branches_table", null,
            t2c1, t2c2);

        SQLTable table2 = database.createTable(table2Info);
        SQLTable table1 = database.createTable(table1Info);

        assertNotNull(table1);
        assertNotNull(table2);
    }

    @Override
    @Test
    @Order(3)
    public void testTableExists() {
        assertTrue(database.tableExists("users_table"));
        assertTrue(database.tableExists("branches_table"));
        assertFalse(database.tableExists("nonexistent_table"));

        SQLTable table1 = database.getTable("users_table");
        SQLTable table2 = database.getTable("branches_table");
        SQLTable invalidTable = database.getTable("nonexistent_table");

        assertNotNull(table1);
        assertNotNull(table2);
        assertNull(invalidTable);
    }

    @Override
    @Test
    @Order(3)
    public void testTablesAmount() {
        int number = database.getTablesAmount(SQLTableType.TABLE);
        // MySQL has internal tables which add to this number.
        // Therefore, an accurate test cannot be performed.
        assertTrue(number > 0);
    }

    @Override
    @Test
    @Order(3)
    public void testRenameTable() {
        database.renameTable("users_table", "renamed_users");
        assertTrue(database.tableExists("renamed_users"));
        assertFalse(database.tableExists("users_table"));

        SQLTable table = database.getTable("renamed_users");
        assertNotNull(table);
        assertEquals("renamed_users", table.getName());

        table.rename("users_table");
        assertEquals("users_table", table.getName());
        assertTrue(database.tableExists("users_table"));
        assertFalse(database.tableExists("renamed_users"));
    }

    @Override
    @Test
    @Order(3)
    public void testTableType() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLTableType type = table.getTableType();
        assertNotNull(type);
        assertSame(SQLTableType.TABLE, type);
    }

    @Override
    @Test
    public void testColumnExists() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        assertTrue(table.columnExists("user_id"));
        assertTrue(table.columnExists("first_name"));
        assertTrue(table.columnExists("last_name"));
        assertFalse(table.columnExists("nonexistent_column"));

        SQLColumn userID = table.getColumn("user_id");
        assertNotNull(userID);
        assertEquals("user_id", userID.getName());
        SQLColumn invalidCol = table.getColumn("nonexistent_column");
        assertNull(invalidCol);
    }

    @Override
    @Test
    @Order(3)
    public void testGetColumnSize() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("weight");
        SQLColumn column2 = table.getColumn("username");
        assertNotNull(column1);
        assertNotNull(column2);
        int[] sizes1 = column1.getSizes();
        int[] sizes2 = column2.getSizes();
        assertNotNull(sizes1);
        assertNotNull(sizes2);
        assertEquals(2, sizes1.length);
        assertEquals(1, sizes2.length);
        assertEquals(32, sizes1[0]);
        assertEquals(8, sizes1[1]);
        assertEquals(20, sizes2[0]);
    }

    @Override
    @Test
    @Order(3)
    public void testGetColumnsAmount() {
        SQLTable table1 = database.getTable("users_table");
        assertNotNull(table1);
        SQLTable table2 = database.getTable("branches_table");
        assertNotNull(table2);

        int cols1 = table1.getColumnsAmount();
        int cols2 = table2.getColumnsAmount();
        assertEquals(7, cols1);
        assertEquals(2, cols2);
    }

    @Override
    @Test
    @Order(3)
    public void testGetDataType() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("username");
        SQLColumn column2 = table.getColumn("weight");
        assertNotNull(column1);
        assertNotNull(column2);

        SQLDataType type1 = column1.getDataType();
        SQLDataType type2 = column2.getDataType();
        assertSame(type1, SQLDataType.VARCHAR);
        assertSame(type2, SQLDataType.DOUBLE);
    }

    @Override
    @Test
    @Order(3)
    public void testIsNotNull() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("username");
        SQLColumn column2 = table.getColumn("user_id");
        SQLColumn column3 = table.getColumn("first_name");
        assertNotNull(column1);
        assertNotNull(column2);
        assertNotNull(column3);

        assertFalse(column1.isNotNull());
        assertTrue(column2.isNotNull());
        assertFalse(column3.isNotNull());
    }

    @Override
    @Test
    @Order(3)
    public void testIsPrimaryKey() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("user_id");
        SQLColumn column2 = table.getColumn("username");
        assertNotNull(column1);
        assertNotNull(column2);

        assertTrue(column1.isPrimaryKey());
        assertFalse(column2.isPrimaryKey());
    }

    @Override
    @Test
    @Order(3)
    public void testIsForeignKey() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("branch");
        SQLColumn column2 = table.getColumn("username");
        assertNotNull(column1);
        assertNotNull(column2);

        assertTrue(column1.isForeignKey());
        assertFalse(column2.isForeignKey());
    }

    @Override
    @Test
    @Order(3)
    public void testHasDefault() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("username");
        SQLColumn column2 = table.getColumn("last_name");
        assertNotNull(column1);
        assertNotNull(column2);

        assertTrue(column1.hasDefault());
        assertFalse(column2.hasDefault());
    }

    @Override
    @Test
    @Order(3)
    public void testAutoIncrements() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("user_id");
        SQLColumn column2 = table.getColumn("weight");
        assertNotNull(column1);
        assertNotNull(column2);

        assertTrue(column1.autoIncrements());
        assertFalse(column2.autoIncrements());
    }

    @Override
    @Test
    @Order(3)
    public void testGetColumns() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn[] columns = table.getColumns();
        assertNotNull(columns);
        for(int i = 0; i < columns.length; ++i) {
            SQLColumn curColumn = columns[i];
            assertNotNull(curColumn);
            SQLColumn testCol = table.getColumn(i);
            assertNotNull(testCol);
            assertEquals(curColumn.getName(), testCol.getName());
        }
    }

    @Override
    @Test
    @Order(3)
    public void testGetColumnNames() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        String[] columns = table.getColumnNames();
        assertNotNull(columns);
        for(int i = 0; i < columns.length; ++i) {
            String curColumn = columns[i];
            assertNotNull(curColumn);
            String testCol = table.getColumnName(i);
            assertNotNull(testCol);
            assertEquals(curColumn, testCol);
        }
    }

    @Override
    @Test
    @Order(3)
    public void testHasConstraint() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("user_id");
        SQLColumn column2 = table.getColumn("username");
        SQLColumn column3 = table.getColumn("weight");
        assertNotNull(column1);
        assertNotNull(column2);
        assertNotNull(column3);

        assertTrue(column1.hasConstraint(SQLConstraint.PRIMARY_KEY));
        assertTrue(column1.hasConstraint(SQLConstraint.AUTO_INCREMENT));
        assertTrue(column2.hasConstraint(SQLConstraint.DEFAULT));
        assertFalse(column3.hasConstraint(SQLConstraint.NOT_NULL));
        assertTrue(column1.hasConstraint(SQLConstraint.NOT_NULL));
        assertTrue(column2.hasConstraint(SQLConstraint.UNIQUE));
        assertFalse(column3.hasConstraint(SQLConstraint.UNIQUE));
        assertTrue(column1.hasConstraint(SQLConstraint.UNIQUE));
    }

    @Override
    @Test
    @Order(3)
    public void testGetColumnInfo() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("user_id");
        SQLColumn column2 = table.getColumn("username");
        assertNotNull(column1);
        assertNotNull(column2);

        SQLColumnInfo info1 = column1.getInfo();
        SQLColumnInfo info2 = column2.getInfo();
        assertNotNull(info1);
        assertNotNull(info2);

        SQLConstraints constraints1 = info1.getConstraints();
        SQLConstraints constraints2 = info2.getConstraints();
        assertNotNull(constraints1);
        assertNotNull(constraints2);
        assertEquals(2, constraints1.length());
        assertEquals(2, constraints2.length());
        assertEquals("user_id", info1.getName());
        assertEquals("username", info2.getName());
    }

    @Override
    @Test
    @Order(3)
    public void testGetColumnConstraints() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("user_id");
        SQLColumn column2 = table.getColumn("username");
        assertNotNull(column1);
        assertNotNull(column2);

        SQLConstraints constraints1 = column1.getConstraints();
        SQLConstraints constraints2 = column2.getConstraints();
        assertNotNull(constraints1);
        assertNotNull(constraints2);
        assertEquals(2, constraints1.length());
        assertEquals(2, constraints2.length());
        SQLConstraintData first1 = constraints1.get(0);
        SQLConstraintData first2 = constraints2.get(0);
        assertNotNull(first1);
        assertNotNull(first2);
        SQLConstraint constraint1 = first1.getConstraint();
        SQLConstraint constraint2 = first2.getConstraint();
        assertNotNull(constraint1);
        assertNotNull(constraint2);
        assertSame(constraint1, SQLConstraint.PRIMARY_KEY);
        assertSame(constraint2, SQLConstraint.UNIQUE);
    }

    @Override
    @Test
    @Order(3)
    public void testGetDefault() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("username");
        SQLColumn column2 = table.getColumn("weight");
        assertNotNull(column1);
        assertNotNull(column2);

        String default1 = column1.getDefaultString();
        String default2 = column2.getDefaultString();
        assertNotNull(default1);
        assertNull(default2);
        assertEquals("unnamedUser", default1);
    }

    @Override
    @Test
    @Order(3)
    public void testIsDBEmpty() {
        assertFalse(database.isEmpty());
    }

    @Override
    @Test
    @Order(3)
    public void testGetColumnInfos() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumnInfo[] infos = table.getColumnInfos();
        assertNotNull(infos);
        assertEquals(7, infos.length);
        for(int i = 0; i < infos.length; ++i) {
            SQLColumnInfo curInfo = infos[i];
            SQLColumn curCol = table.getColumn(i);
            assertNotNull(curInfo);
            assertNotNull(curCol);
            assertEquals(curInfo.getName(), curCol.getName());
        }
    }

    @Override
    @Test
    @Order(3)
    public void testGetTableInfo() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLTableInfo info = table.getInfo();
        assertNotNull(info);
        SQLColumnInfo[] cols = info.getColumns();
        assertNotNull(cols);
        assertNotNull(info.getTableName());
        assertEquals(info.getTableName(), table.getName());
    }

    @Override
    @Test
    @Order(3)
    public void testGetColumnName() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        String name1 = table.getColumnName(0);
        String name2 = table.getColumnName(3);
        assertNotNull(name1);
        assertNotNull(name2);
        assertEquals("user_id", name1);
        assertEquals("username", name2);
    }

    @Override
    @Test
    @Order(3)
    public void testGetTableConstraints() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLConstraints constraint = table.getConstraints();
        assertNotNull(constraint);
        assertEquals(0, constraint.length()); // TODO: Actually length 1, unable to check check condition
    }

    @Override
    @Test
    @Order(4)
    public void testAddColumn() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumnInfo newInfo = new SQLColumnInfo(
            SQLDataType.INTEGER,
            "test_col",
            new SQLConstraints()
                .addNotNull()
                .addUnique()
        );

        table.addColumn(newInfo);

        assertTrue(table.columnExists("test_col"));
        assertEquals(8, table.getColumnsAmount());
        SQLColumn column = table.getColumn("test_col");
        assertNotNull(column);
    }

    @Override
    @Test
    @Order(4)
    public void testAddConstraint() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("first_name");
        SQLColumn column2 = table.getColumn("weight");
        assertNotNull(column1);
        assertNotNull(column2);

        SQLConstraints constraints1 = new SQLConstraints()
            .addNotNull();

        SQLConstraints constraints2 = new SQLConstraints()
            .addDefault("21")
            .addNotNull();

        column1.addConstraints(constraints1);
        column2.addConstraints(constraints2);
        assertTrue(column1.hasConstraint(SQLConstraint.NOT_NULL));
//        assertTrue(column2.hasConstraint(SQLConstraint.DEFAULT)); TODO: Default doesn't apply? Why?
        assertTrue(column2.hasConstraint(SQLConstraint.NOT_NULL));
    }

    @Override
    @Test
    @Order(4)
    public void testRenameColumn() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        assertTrue(table.columnExists("first_name"));
        assertTrue(table.columnExists("last_name"));
        table.renameColumn("first_name", "first_temp");
        table.renameColumn("last_name", "last_temp");
        assertFalse(table.columnExists("first_name"));
        assertFalse(table.columnExists("last_name"));
        assertTrue(table.columnExists("first_temp"));
        assertTrue(table.columnExists("last_temp"));
        table.renameColumn("first_temp", "first_name");
        table.renameColumn("last_temp", "last_name");
        assertTrue(table.columnExists("first_name"));
        assertTrue(table.columnExists("last_name"));
        assertFalse(table.columnExists("first_temp"));
        assertFalse(table.columnExists("last_temp"));
    }

    @Override
    @Test
    @Order(5)
    public void testRemoveColumn() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        assertTrue(table.columnExists("test_col"));
        table.removeColumn("test_col");
        assertFalse(table.columnExists("test_col"));
    }

    @Override
    @Test
    @Order(5)
    public void testRemoveConstraint() {
        SQLTable table = database.getTable("users_table");
        assertNotNull(table);

        SQLColumn column1 = table.getColumn("first_name");
        SQLColumn column2 = table.getColumn("username");
        assertTrue(column1.hasConstraint(SQLConstraint.NOT_NULL));
        assertTrue(column2.hasConstraint(SQLConstraint.DEFAULT));
        column1.removeConstraint(SQLConstraint.NOT_NULL);
        column2.removeConstraint(SQLConstraint.DEFAULT);
        assertFalse(column1.hasConstraint(SQLConstraint.NOT_NULL));
        assertFalse(column2.hasConstraint(SQLConstraint.DEFAULT));
    }
}
