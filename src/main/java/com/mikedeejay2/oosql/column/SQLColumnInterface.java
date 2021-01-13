package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.SQLObject;
import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.table.SQLTable;

public interface SQLColumnInterface extends SQLObject
{
    SQLTable getParentTable();

    SQLDatabase getDatabase();

    SQLDataType getDataType();

    String getName();

    int[] getSizes();

    String getMetaString(SQLColumnMeta metaType);

    int getMetaInt(SQLColumnMeta metaType);

    boolean isNullable();

    boolean isPrimaryKey();

    boolean isForeignKey();

    boolean hasDefault();

    boolean autoIncrements();
}
