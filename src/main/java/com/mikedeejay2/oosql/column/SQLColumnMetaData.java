package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.misc.*;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraints;
import com.mikedeejay2.oosql.column.key.*;
import com.mikedeejay2.oosql.table.SQLTable;
import com.mikedeejay2.oosql.misc.SQLMetaDataGetter;

public interface SQLColumnMetaData extends SQLMetaDataGetter<SQLColumnMeta>,
    SQLPrimaryKeyMetaGetter<SQLPrimaryKeyMeta>,
    SQLForeignKeyMetaGetter<SQLForeignKeyMeta> {
    SQLDataType getDataType();

    int[] getSizes();

    boolean isNotNull();

    boolean isPrimaryKey();

    boolean isForeignKey();

    boolean hasDefault();

    boolean autoIncrements();

    boolean isUnique();

    boolean hasConstraint(SQLConstraint constraint);

    SQLColumnInfo getInfo();

    SQLConstraints getConstraints();

    Object getDefault();

    <R> R getDefault(Class<R> type);

    String getDefaultString();

    int getDefaultInt();

    float getDefaultFloat();

    double getDefaultDouble();

    boolean getDefaultBoolean();

    String getReferenceTableName();

    SQLTable getReferenceTable();

    String getReferenceColumnName();

    SQLColumn getReferenceColumn();
}
