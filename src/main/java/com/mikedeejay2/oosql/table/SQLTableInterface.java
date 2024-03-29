package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.SQLObject;
import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraints;
import com.mikedeejay2.oosql.row.SQLRow;

public interface SQLTableInterface extends SQLObject {
    SQLColumn[] getColumns();

    String[] getColumnNames();

    boolean rename(String newName);

    SQLColumn getColumn(String columnName);

    SQLColumn getColumn(int index);

    String getColumnName(int index);

    SQLDatabase getDatabase();

    String getName();

    boolean addColumn(SQLColumnInfo info);

    boolean addColumns(SQLColumnInfo... info);

    boolean removeColumn(SQLColumn column);

    boolean removeColumn(String columnName);

    boolean removeColumn(int index);

    boolean renameColumn(String columnName, String newName);

    boolean renameColumn(SQLColumn column, String newName);

    boolean renameColumn(int index, String newName);

    boolean addConstraint(SQLConstraintData constraint);

    boolean addConstraints(SQLConstraintData... constraints);

    boolean addConstraints(SQLConstraints constraints);

    boolean removeConstraint(SQLConstraint constraint);

    boolean removeConstraints(SQLConstraint... constraints);

    boolean clear();

    boolean insertRow(Object... values);

    boolean insertRow(String[] columns, Object[] values);

    boolean deleteRow(String condition);

//    boolean deleteRow(Object primaryKeyValue); TODO

//    boolean deleteRow(SQLRow<?> row); TODO

    boolean deleteRow(int index);
}
