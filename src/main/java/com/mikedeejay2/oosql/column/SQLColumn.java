package com.mikedeejay2.oosql.column;

import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.execution.SQLExecutor;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraints;
import com.mikedeejay2.oosql.column.key.SQLForeignKeyMeta;
import com.mikedeejay2.oosql.column.key.SQLPrimaryKeyMeta;
import com.mikedeejay2.oosql.misc.index.SQLIndexInfoMeta;
import com.mikedeejay2.oosql.sqlgen.SQLGenerator;
import com.mikedeejay2.oosql.table.SQLTable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLColumn implements SQLColumnInterface, SQLColumnMetaData {
    protected final SQLDatabase database;
    protected final SQLExecutor executor;
    protected final SQLGenerator generator;
    protected final SQLTable table;
    protected String columnName;

    public SQLColumn(SQLTable parentTable, String columnName) {
        this.table = parentTable;
        this.database = parentTable.getDatabase();
        this.executor = table.getExecutor();
        this.generator = table.getGenerator();
        this.columnName = columnName;
    }

    @Override
    public SQLTable getParentTable() {
        return table;
    }

    @Override
    public SQLDatabase getDatabase() {
        return database;
    }

    @Override
    public SQLDataType getDataType() {
        int type = getMetaInt(SQLColumnMeta.DATA_TYPE);
        return SQLDataType.fromNative(type);
    }

    @Override
    public String getName() {
        return columnName;
    }

    @Override
    public boolean addConstraint(SQLConstraintData constraint) {
        String command = generator.addConstraints(table.getName(), getInfo(), constraint);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean addConstraints(SQLConstraintData... constraints) {
        String command = generator.addConstraints(table.getName(), getInfo(), constraints);
        int[] codes = executor.executeUpdate(command.split("\n"));
        boolean success = true;
        for(int code : codes)
        {
            success &= code != -1;
        }
        return success;
    }

    @Override
    public boolean addConstraints(SQLConstraints constraints) {
        if(constraints == null) return false;
        return addConstraints(constraints.get());
    }

    @Override
    public boolean renameColumn(String newName) {
        return table.renameColumn(columnName, newName);
    }

    @Override
    public boolean removeConstraint(SQLConstraint constraint) {
        String command = generator.dropConstraint(table.getName(), getInfo(), constraint);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean removeConstraints(SQLConstraint... constraints) {
        String command = generator.dropConstraints(table.getName(), getInfo(), constraints);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public int[] getSizes() {
        int size1 = getMetaInt(SQLColumnMeta.COLUMN_SIZE);
        int size2 = getMetaInt(SQLColumnMeta.DECIMAL_DIGITS);
        if(size1 != 0 && size2 != 0) return new int[]{size1, size2};
        else if(size1 != 0) return new int[]{size1};
        else if(size2 != 0) return new int[]{size2};
        return new int[0];
    }

    @Override
    public Object getMetaObject(SQLColumnMeta metaDataType) {
        try {
            ResultSet result = database.getMetaData().getColumns(null, null, table.getName(), columnName);
            if(result.next()) {
                return result.getObject(metaDataType.asIndex());
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public <R> R getMetaObject(SQLColumnMeta metaDataType, Class<R> type) {
        try {
            ResultSet result = database.getMetaData().getColumns(null, null, table.getName(), columnName);
            if(result.next()) {
                return result.getObject(metaDataType.asIndex(), type);
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String getMetaString(SQLColumnMeta metaDataType) {
        return getMetaObject(metaDataType, String.class);
    }

    @Override
    public int getMetaInt(SQLColumnMeta metaDataType) {
        Integer value = getMetaObject(metaDataType, Integer.class);
        return value == null ? 0 : value;
    }

    @Override
    public short getMetaShort(SQLColumnMeta metaDataType) {
        Short value = getMetaObject(metaDataType, Short.class);
        return value == null ? 0 : value;
    }

    @Override
    public long getMetaLong(SQLColumnMeta metaDataType) {
        Long value = getMetaObject(metaDataType, Long.class);
        return value == null ? 0 : value;
    }

    @Override
    public boolean isNotNull() {
        return getMetaString(SQLColumnMeta.IS_NULLABLE).equals("NO");
    }

    @Override
    public boolean isPrimaryKey() {
        try {
            ResultSet resultPrimary = database.getMetaData().getPrimaryKeys(null, null, table.getName());
            while(resultPrimary.next()) {
                String curName = resultPrimary.getString(SQLColumnMeta.COLUMN_NAME.asIndex());
                if(columnName.equals(curName)) {
                    return true;
                }
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isForeignKey() {
        return getForeignKeyMetaString(SQLForeignKeyMeta.PKCOLUMN_NAME) != null;
    }

    @Override
    public boolean hasDefault() {
        return getMetaObject(SQLColumnMeta.COLUMN_DEF) != null;
    }

    @Override
    public boolean autoIncrements() {
        return getMetaString(SQLColumnMeta.IS_AUTOINCREMENT).equals("YES");
    }

    @Override
    @Deprecated
    public boolean isUnique() {
        return (table.indexInfoExists(columnName) && !table.getIndexInfoMetaBoolean(columnName, SQLIndexInfoMeta.NON_UNIQUE))
            || isPrimaryKey();
    }

    @Override
    public boolean hasConstraint(SQLConstraint constraint) {
        switch(constraint) {
            case NOT_NULL:
                return isNotNull();
            case UNIQUE:
                return isUnique();
            case PRIMARY_KEY:
                return isPrimaryKey();
            case FOREIGN_KEY:
                return isForeignKey();
            case DEFAULT:
                return hasDefault();
            case AUTO_INCREMENT:
                return autoIncrements();
            default:
                return false;
        }
    }

    @Override
    public SQLColumnInfo getInfo() {
        return new SQLColumnInfo(getDataType(), getName(), getSizes(), getConstraints());
    }

    @Override
    public SQLConstraints getConstraints() {
        SQLConstraints constraints = new SQLConstraints();
        boolean isNotNull = isNotNull();
        boolean isUnique = isUnique();
        boolean isPrimaryKey = isPrimaryKey();
        boolean isForeignKey = isForeignKey();
        boolean hasDefault = hasDefault();
        boolean autoIncrements = autoIncrements();

        if(isNotNull && !isPrimaryKey) constraints.addNotNull();
        if(isUnique && !isPrimaryKey) constraints.addUnique();
        if(isPrimaryKey) constraints.addPrimaryKey();
        if(isForeignKey) constraints.addForeignKey(getReferenceTableName(), getReferenceColumnName());
        if(hasDefault) constraints.addDefault(String.valueOf(getDefault()));
        if(autoIncrements) constraints.addAutoIncrement();

        return constraints;
    }

    @Override
    public Object getDefault() {
        return getMetaObject(SQLColumnMeta.COLUMN_DEF);
    }

    @Override
    public <R> R getDefault(Class<R> type) {
        return getMetaObject(SQLColumnMeta.COLUMN_DEF, type);
    }

    @Override
    public String getDefaultString() {
        return getDefault(String.class);
    }

    @Override
    public int getDefaultInt() {
        return getDefault(Integer.class);
    }

    @Override
    public float getDefaultFloat() {
        return getDefault(Float.class);
    }

    @Override
    public double getDefaultDouble() {
        return getDefault(Double.class);
    }

    @Override
    public boolean getDefaultBoolean() {
        return getDefault(Boolean.class);
    }

    @Override
    public String getReferenceTableName() {
        return getForeignKeyMetaString(SQLForeignKeyMeta.PKTABLE_NAME);
    }

    @Override
    public SQLTable getReferenceTable() {
        return database.getTable(getReferenceTableName());
    }

    @Override
    public String getReferenceColumnName() {
        return getForeignKeyMetaString(SQLForeignKeyMeta.PKCOLUMN_NAME);
    }

    @Override
    public SQLColumn getReferenceColumn() {
        return getReferenceTable().getColumn(getReferenceColumnName());
    }

    public SQLExecutor getExecutor() {
        return executor;
    }

    public SQLGenerator getGenerator() {
        return generator;
    }

    @Override
    public Object getForeignKeyMetaObject(SQLForeignKeyMeta metaDataType) {
        try {
            ResultSet resultForeign = database.getMetaData().getImportedKeys(null, null, table.getName());
            while(resultForeign.next()) {
                String curName = resultForeign.getString(SQLForeignKeyMeta.FKCOLUMN_NAME.asIndex());
                if(columnName.equals(curName)) {
                    return resultForeign.getObject(metaDataType.asIndex());
                }
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public <R> R getForeignKeyMetaObject(SQLForeignKeyMeta metaDataType, Class<R> type) {
        try {
            ResultSet resultForeign = database.getMetaData().getImportedKeys(null, null, table.getName());
            while(resultForeign.next()) {
                String curName = resultForeign.getString(SQLForeignKeyMeta.FKCOLUMN_NAME.asIndex());
                if(columnName.equals(curName)) {
                    return resultForeign.getObject(metaDataType.asIndex(), type);
                }
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String getForeignKeyMetaString(SQLForeignKeyMeta metaDataType) {
        return getForeignKeyMetaObject(metaDataType, String.class);
    }

    @Override
    public int getForeignKeyMetaInt(SQLForeignKeyMeta metaDataType) {
        Integer value = getForeignKeyMetaObject(metaDataType, Integer.class);
        return value == null ? 0 : value;
    }

    @Override
    public short getForeignKeyMetaShort(SQLForeignKeyMeta metaDataType) {
        Short value = getForeignKeyMetaObject(metaDataType, Short.class);
        return value == null ? 0 : value;
    }

    @Override
    public Object getPrimaryKeyMetaObject(SQLPrimaryKeyMeta metaDataType) {
        try {
            ResultSet result = database.getMetaData().getPrimaryKeys(null, null, table.getName());
            while(result.next()) {
                String curName = result.getString(SQLPrimaryKeyMeta.COLUMN_NAME.asIndex());
                if(columnName.equals(curName)) {
                    return result.getObject(metaDataType.asIndex());
                }
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public <R> R getPrimaryKeyMetaObject(SQLPrimaryKeyMeta metaDataType, Class<R> type) {
        try {
            ResultSet result = database.getMetaData().getPrimaryKeys(null, null, table.getName());
            while(result.next()) {
                String curName = result.getString(SQLPrimaryKeyMeta.COLUMN_NAME.asIndex());
                if(columnName.equals(curName)) {
                    return result.getObject(metaDataType.asIndex(), type);
                }
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String getPrimaryKeyMetaString(SQLPrimaryKeyMeta metaDataType) {
        return getPrimaryKeyMetaObject(metaDataType, String.class);
    }

    @Override
    public int getPrimaryKeyMetaInt(SQLPrimaryKeyMeta metaDataType) {
        Integer value = getPrimaryKeyMetaObject(metaDataType, Integer.class);
        return value == null ? 0 : value;
    }

    @Override
    public short getPrimaryKeyMetaShort(SQLPrimaryKeyMeta metaDataType) {
        Short value = getPrimaryKeyMetaObject(metaDataType, Short.class);
        return value == null ? 0 : value;
    }
}
