package com.mikedeejay2.oosql.misc.index;

public interface SQLIndexInfoMetaGetter<T> {
    Object getIndexInfoMetaObject(String keyName, T metaDataType);

    <R> R getIndexInfoMetaObject(String keyName, T metaDataType, Class<R> type);

    String getIndexInfoMetaString(String keyName, T metaDataType);

    int getIndexInfoMetaInt(String keyName, T metaDataType);

    short getIndexInfoMetaShort(String keyName, T metaDataType);

    long getIndexInfoMetaLong(String keyName, T metaDataType);

    boolean getIndexInfoMetaBoolean(String keyName, T metaDataType);

    boolean indexInfoExists(String keyName);
}
