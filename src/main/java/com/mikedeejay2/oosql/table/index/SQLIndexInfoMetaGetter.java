package com.mikedeejay2.oosql.table.index;

public interface SQLIndexInfoMetaGetter<T>
{
    Object getIndexInfoMetaObject(T metaDataType);

    <R> R getIndexInfoMetaObject(T metaDataType, Class<R> type);

    String getIndexInfoMetaString(T metaDataType);

    int getIndexInfoMetaInt(T metaDataType);

    short getIndexInfoMetaShort(T metaDataType);

    long getIndexInfoMetaLong(T metaDataType);
}
