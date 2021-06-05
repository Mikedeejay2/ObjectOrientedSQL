package com.mikedeejay2.oosql;

import org.junit.jupiter.api.*;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public interface TestInterface
{
    @Test
    void testConnection();

    @Test
    void testGetDBType();

    @Test
    void testDBExists();

    @Test
    void testGetDBName();

    @Test
    void testGetDBConnectionInfo();

    @Test
    void testGetDBConnection();

    @Test
    void testCreateTable();

    @Test
    void testTableExists();

    @Test
    void testTablesAmount();

    @Test
    void testRenameTable();

    @Test
    void testTableType();

    @Test
    void testColumnExists();

    @Test
    void testGetColumnSize();

    @Test
    void testGetColumnsAmount();

    @Test
    void testGetDataType();

    @Test
    void testIsNotNull();

    @Test
    void testIsPrimaryKey();

    @Test
    void testIsForeignKey();

    @Test
    void testHasDefault();

    @Test
    void testAutoIncrements();

    @Test
    void testGetColumns();

    @Test
    void testGetColumnNames();

    @Test
    void testHasConstraint();

    @Test
    void testGetColumnInfo();

    @Test
    void testGetColumnConstraints();

    @Test
    void testGetDefault();

    @Test
    void testIsDBEmpty();

    @Test
    void testGetColumnInfos();

    @Test
    void testGetTableInfo();

    @Test
    void testGetColumnName();

    @Test
    void testGetTableConstraints();

    @Test
    void testAddColumn();

    @Test
    void testAddConstraint();

    @Test
    void testRenameColumn();

    @Test
    void testRemoveColumn();

    @Test
    void testRemoveConstraint();
}
