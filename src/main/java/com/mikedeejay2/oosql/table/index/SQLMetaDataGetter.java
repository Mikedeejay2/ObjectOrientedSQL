package com.mikedeejay2.oosql.table.index;

public interface SQLMetaDataGetter<T>
{
    Object getMetaObject(T metaDataType);

    <R> R getMetaObject(T metaDataType, Class<R> type);

    String getMetaString(T metaDataType);

    int getMetaInt(T metaDataType);

    short getMetaShort(T metaDataType);

    long getMetaLong(T metaDataType);
}
