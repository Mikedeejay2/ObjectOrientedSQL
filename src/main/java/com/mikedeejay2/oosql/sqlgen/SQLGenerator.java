package com.mikedeejay2.oosql.sqlgen;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;
import com.mikedeejay2.oosql.table.SQLTableInfo;

public interface SQLGenerator {
    String createTable(SQLTableInfo info);

    String dropTable(String tableName);

    String renameTable(String tableName, String newName);

    String countRows(String tableName);

    String dropDatabase(String databaseName);

    String createDatabase(String databaseName);

    String renameDatabase(String databaseName, String newName);

    String addColumn(String tableName, SQLColumnInfo info);

    String addColumns(String tableName, SQLColumnInfo... info);

    String addConstraint(String tableName, SQLColumnInfo info, SQLConstraintData constraint);

    String dropConstraint(String tableName, SQLColumnInfo info, SQLConstraint constraint);

    String addConstraints(String tableName, SQLColumnInfo info, SQLConstraintData... constraints);

    String dropConstraints(String tableName, SQLColumnInfo info, SQLConstraint... constraints);

    String dropColumn(String tableName, String columnName);

    String renameColumn(String tableName, SQLColumnInfo info, String newName);
}
