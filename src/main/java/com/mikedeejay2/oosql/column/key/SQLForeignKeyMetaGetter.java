package com.mikedeejay2.oosql.column.key;

public interface SQLForeignKeyMetaGetter<T> {
    Object getForeignKeyMetaObject(T metaDataType);

    <R> R getForeignKeyMetaObject(T metaDataType, Class<R> type);

    String getForeignKeyMetaString(T metaDataType);

    int getForeignKeyMetaInt(T metaDataType);

    short getForeignKeyMetaShort(T metaDataType);
}
