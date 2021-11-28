package com.mikedeejay2.oosql.table;

import com.mikedeejay2.oosql.column.SQLColumn;
import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.column.SQLColumnMeta;
import com.mikedeejay2.oosql.database.SQLDatabase;
import com.mikedeejay2.oosql.execution.SQLExecutor;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraints;
import com.mikedeejay2.oosql.sqlgen.SQLGenerator;
import com.mikedeejay2.oosql.misc.index.SQLIndexInfoMeta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLTable implements SQLTableInterface, SQLTableMetaData {
    protected final SQLDatabase database;
    protected final SQLExecutor executor;
    protected final SQLGenerator generator;
    protected String tableName;

    public SQLTable(SQLDatabase database, String tableName) {
        this.database = database;
        this.executor = database.getExecutor();
        this.generator = database.getGenerator();
        this.tableName = tableName;
    }

    @Override
    public boolean rename(String newName) {
        boolean success = database.renameTable(tableName, newName);
        this.tableName = newName;
        return success;
    }

    @Override
    public SQLColumn getColumn(String columnName) {
        if(!columnExists(columnName)) return null;
        return new SQLColumn(this, columnName);
    }

    @Override
    public SQLColumn getColumn(int index) {
        return getColumn(getColumnName(index));
    }

    @Override
    public String getColumnName(int index) {
        ++index;
        try {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, null);
            result.absolute(index);
            return result.getString(SQLColumnMeta.COLUMN_NAME.asIndex());
        }
        catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public SQLColumn[] getColumns() {
        SQLColumn[] columns = new SQLColumn[getColumnsAmount()];
        try {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, null);
            while(result.next()) {
                String columnName = result.getString(SQLColumnMeta.COLUMN_NAME.asIndex());
                SQLColumn column = new SQLColumn(this, columnName);
                columns[result.getRow() - 1] = column;
            }
        }
        catch(SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return columns;
    }

    @Override
    public String[] getColumnNames() {
        String[] columns = new String[getColumnsAmount()];
        try {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, null);
            while(result.next()) {
                String columnName = result.getString(SQLColumnMeta.COLUMN_NAME.asIndex());
                columns[result.getRow() - 1] = columnName;
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return columns;
    }

    @Override
    public SQLDatabase getDatabase() {
        return database;
    }

    @Override
    public String getName() {
        return tableName;
    }

    @Override
    public boolean addColumn(SQLColumnInfo info) {
        String command = generator.addColumn(tableName, info);
        int[] codes = executor.executeUpdate(command.split("\n"));
        boolean success = true;
        for(int code : codes) {
            success &= code != -1;
        }
        return success;
    }

    @Override
    public boolean addColumns(SQLColumnInfo... info) {
        String command = generator.addColumns(tableName, info);
        int[] codes = executor.executeUpdate(command.split("\n"));
        boolean success = true;
        for(int code : codes) {
            success &= code != -1;
        }
        return success;
    }

    @Override
    public boolean removeColumn(SQLColumn column) {
        return removeColumn(column.getName());
    }

    @Override
    public boolean removeColumn(String columnName) {
        String command = generator.dropColumn(tableName, columnName);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean removeColumn(int index) {
        return removeColumn(getColumnName(index));
    }

    @Override
    public boolean renameColumn(String columnName, String newName) {
        return renameColumn(getColumn(columnName), newName);
    }

    @Override
    public boolean renameColumn(SQLColumn column, String newName) {
        String command = generator.renameColumn(tableName, column.getInfo(), newName);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean renameColumn(int index, String newName) {
        return renameColumn(getColumn(index), newName);
    }

    @Override
    public boolean addConstraint(SQLConstraintData constraint) {
        String command = generator.addConstraint(tableName, null, constraint);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean addConstraints(SQLConstraintData... constraints) {
        String command = generator.addConstraints(tableName, null, constraints);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean addConstraints(SQLConstraints constraints) {
        if(constraints == null) return false;
        return addConstraints(constraints.get());
    }

    @Override
    public boolean removeConstraint(SQLConstraint constraint) {
        String command = generator.dropConstraint(tableName, null, constraint);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean removeConstraints(SQLConstraint... constraints) {
        String command = generator.dropConstraints(tableName, null, constraints);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean clear() {
        String command = generator.truncateTable(tableName);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean insertRow(Object... values) {
        String command = generator.insertRow(tableName, values);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public boolean insertRow(String[] columns, Object[] values) {
        String command = generator.insertRow(tableName, columns, values);
        int code = executor.executeUpdate(command);
        return code != -1;
    }

    @Override
    public SQLTableType getTableType() {
        String tableTypeStr = getMetaString(SQLTableMeta.TABLE_TYPE);
        SQLTableType tableType = SQLTableType.valueOf(tableTypeStr.replace(" ", "_"));
        return tableType;
    }

    @Override
    public boolean columnExists(String columnName) {
        try {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, columnName);
            return result.next();
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean columnExists(SQLColumn column) {
        return columnExists(column.getName());
    }

    @Override
    public int getColumnsAmount() {
        try {
            ResultSet result = database.getMetaData().getColumns(null, null, tableName, null);
            result.last();
            return result.getRow();
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getRowsAmount() {
        try {
            String command = generator.countRows(tableName);
            ResultSet result = executor.executeQuery(command);
            if(result.next()) {
                return result.getInt(1);
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public SQLTableInfo getInfo() {
        return new SQLTableInfo(tableName, getConstraints(), getColumnInfos());
    }

    @Override
    public SQLColumnInfo[] getColumnInfos() {
        SQLColumn[] columns = getColumns();
        SQLColumnInfo[] infos = new SQLColumnInfo[columns.length];
        for(int i = 0; i < columns.length; ++i) {
            infos[i] = columns[i].getInfo();
        }
        return infos;
    }

    @Override
    @Deprecated
    public boolean hasCheck() {
        // TODO: How do we get the check constraint?
        return false;
    }

    @Override
    @Deprecated
    public SQLConstraints getConstraints() {
        SQLConstraints constraints = new SQLConstraints();
        if(hasCheck()) {
            constraints.addCheck(getCheck());
        }
        return constraints;
    }

    @Override
    public boolean exists() {
        return database.tableExists(tableName);
    }

    @Override
    @Deprecated
    public String getCheck() {
        // TODO: How do we get the check condition?
        return null;
    }

    @Override
    public Object getMetaObject(SQLTableMeta metaDataType) {
        try {
            ResultSet result = database.getMetaData().getTables(null, null, tableName, null);
            if(result.next()) {
                return result.getObject(metaDataType.asIndex());
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public <R> R getMetaObject(SQLTableMeta metaDataType, Class<R> type) {
        try {
            ResultSet result = database.getMetaData().getTables(null, null, tableName, null);
            if(result.next()) {
                return result.getObject(metaDataType.asIndex(), type);
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String getMetaString(SQLTableMeta metaDataType) {
        return getMetaObject(metaDataType, String.class);
    }

    @Override
    public int getMetaInt(SQLTableMeta metaDataType) {
        return getMetaObject(metaDataType, Integer.class);
    }

    @Override
    public short getMetaShort(SQLTableMeta metaDataType) {
        return getMetaObject(metaDataType, Short.class);
    }

    @Override
    public long getMetaLong(SQLTableMeta metaDataType) {
        return getMetaObject(metaDataType, Long.class);
    }

    public SQLExecutor getExecutor() {
        return executor;
    }

    public SQLGenerator getGenerator() {
        return generator;
    }

    @Override
    public Object getIndexInfoMetaObject(String keyName, SQLIndexInfoMeta metaDataType) {
        try {
            ResultSet result = database.getMetaData().getIndexInfo(null, null, tableName, false, true);
            while(result.next()) {
                if(keyName.equals(result.getString(SQLIndexInfoMeta.INDEX_NAME.asIndex()))) {
                    return result.getObject(metaDataType.asIndex());
                }
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public <R> R getIndexInfoMetaObject(String keyName, SQLIndexInfoMeta metaDataType, Class<R> type) {
        try {
            ResultSet result = database.getMetaData().getIndexInfo(null, null, tableName, false, true);
            while(result.next()) {
                if(keyName.equals(result.getString(SQLIndexInfoMeta.INDEX_NAME.asIndex()))) {
                    return result.getObject(metaDataType.asIndex(), type);
                }
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String getIndexInfoMetaString(String keyName, SQLIndexInfoMeta metaDataType) {
        return getIndexInfoMetaObject(keyName, metaDataType, String.class);
    }

    @Override
    public int getIndexInfoMetaInt(String keyName, SQLIndexInfoMeta metaDataType) {
        return getIndexInfoMetaObject(keyName, metaDataType, Integer.class);
    }

    @Override
    public short getIndexInfoMetaShort(String keyName, SQLIndexInfoMeta metaDataType) {
        return getIndexInfoMetaObject(keyName, metaDataType, Short.class);
    }

    @Override
    public long getIndexInfoMetaLong(String keyName, SQLIndexInfoMeta metaDataType) {
        return getIndexInfoMetaObject(keyName, metaDataType, Long.class);
    }

    @Override
    public boolean getIndexInfoMetaBoolean(String keyName, SQLIndexInfoMeta metaDataType) {
        return getIndexInfoMetaObject(keyName, metaDataType, Boolean.class);
    }

    @Override
    public boolean indexInfoExists(String keyName) {
        try {
            ResultSet result = database.getMetaData().getIndexInfo(null, null, tableName, false, true);
            while(result.next()) {
                if(keyName.equals(result.getString(SQLIndexInfoMeta.INDEX_NAME.asIndex()))) {
                    return true;
                }
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
