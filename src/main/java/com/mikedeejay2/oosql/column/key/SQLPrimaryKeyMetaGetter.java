package com.mikedeejay2.oosql.column.key;

public interface SQLPrimaryKeyMetaGetter<T> {
    Object getPrimaryKeyMetaObject(T metaDataType);

    <R> R getPrimaryKeyMetaObject(T metaDataType, Class<R> type);

    String getPrimaryKeyMetaString(T metaDataType);

    int getPrimaryKeyMetaInt(T metaDataType);

    short getPrimaryKeyMetaShort(T metaDataType);
}
