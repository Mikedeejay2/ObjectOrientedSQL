package com.mikedeejay2.oosql.sqlgen;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraints;
import com.mikedeejay2.oosql.table.SQLTableInfo;

import java.io.Serializable;
import java.util.*;

public class SimpleSQLGenerator implements SQLGenerator {
    @Override
    public String createTable(SQLTableInfo info) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE `")
            .append(info.getTableName())
            .append("`");

        SQLColumnInfo[] columns = info.getColumns();

        if(columns.length > 0) builder.append(" (");

        List<Map.Entry<SQLConstraintData, SQLColumnInfo>> endConstraints = new ArrayList<>();
        SQLConstraints tableConstraints = info.getConstraints();
        if(tableConstraints != null) {
            SQLConstraintData[] tableConstraintData = tableConstraints.get();
            if(tableConstraintData != null) {
                for(SQLConstraintData curTableData : tableConstraintData) {
                    endConstraints.add(new AbstractMap.SimpleEntry<>(curTableData, null));
                }
            }
        }

        for(int index = 0; index < columns.length; ++index) {
            SQLColumnInfo curInfo = columns[index];
            String name = curInfo.getName();
            SQLConstraints constraints = curInfo.getConstraints();
            int[] sizes = curInfo.getSizes();
            SQLDataType type = curInfo.getType();

            builder.append("`")
                .append(name)
                .append("` ")
                .append(type.getName());

            if(sizes != null && sizes.length > 0) {
                builder.append(getSizesStr(sizes));
            }

            if(constraints != null) {
                for(SQLConstraintData constraint : constraints.get()) {
                    if(constraint.isTableConstraint()) {
                        endConstraints.add(new AbstractMap.SimpleEntry<>(constraint, curInfo));
                        continue;
                    }

                    builder.append(" ");
                    String constraintStr = constraint.get();
                    builder.append(constraintStr);

                    if(constraint.isCheck()) {
                        builder.append(" ");
                        builder.append(getExtraStr(constraint.getCheckCondition()));
                    } else if(constraint.isDefault()) {
                        builder.append(" ");
                        builder.append(getExtraStr(constraint.getDefaultValue()));
                    }
                }
            }

            if(index != columns.length - 1) builder.append(", ");
        }

        for(Map.Entry<SQLConstraintData, SQLColumnInfo> entry : endConstraints) {
            SQLConstraintData curConstraint = entry.getKey();
            SQLColumnInfo curInfo = entry.getValue();
            String data = null;
            switch(curConstraint.getConstraint()) {
                case CHECK:
                    data = curConstraint.getCheckCondition();
                    break;
                case DEFAULT:
                    data = curConstraint.getDefaultValue();
                    break;
                case FOREIGN_KEY:
                    data = "`" +
                        curConstraint.getReferenceTable() + "`(`" +
                        curConstraint.getReferenceColumn() + "`)";
                    break;
                default:
                    data = "`" + curInfo.getName() + "`";
                    break;
            }
            String name = curConstraint.get();
            builder.append(", ")
                .append(name);
            if(curConstraint.isForeignKey()) {
                builder.append(" (`")
                    .append(curInfo.getName())
                    .append("`) REFERENCES ")
                    .append(data);
            } else {
                builder.append("(")
                    .append(data)
                    .append(")");
            }
        }

        if(columns.length > 0) builder.append(")");

        builder.append(";");

        return builder.toString();
    }

    @Override
    public String dropTable(String tableName) {
        return "DROP TABLE `" + tableName + "`;";
    }

    @Override
    public String renameTable(String tableName, String newName) {
        return "ALTER TABLE `" + tableName + "` RENAME TO `" + newName + "`;";
    }

    @Override
    public String countRows(String tableName) {
        return "SELECT COUNT(*) FROM `" + tableName + "`;";
    }

    @Override
    public String dropDatabase(String databaseName) {
        return "DROP DATABASE `" + databaseName + "`;";
    }

    @Override
    public String createDatabase(String databaseName) {
        return "CREATE DATABASE `" + databaseName + "`;";
    }

    @Override
    public String renameDatabase(String databaseName, String newName) {
        return "RENAME DATABASE `" + databaseName + "` TO `" + newName + "`;";
    }

    @Override
    public String addColumn(String tableName, SQLColumnInfo info) {
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE `")
            .append(tableName)
            .append("` ADD `")
            .append(info.getName())
            .append("` ")
            .append(info.getType().getName());

        int[] sizes = info.getSizes();
        if(sizes != null && sizes.length > 0) {
            builder.append(getSizesStr(sizes));
        }

        SQLConstraints constraints = info.getConstraints();
        StringBuilder constraintBuilder = new StringBuilder();
        if(constraints != null) {
            for(SQLConstraintData constraintData : constraints) {
                if(constraintData.isDataConstraint()) {
                    builder.append(" ")
                        .append(constraintData.get());
                } else {
                    constraintBuilder.append("\n")
                        .append(addConstraintInternal(tableName, info, constraintData));
                }
            }
        }

        builder.append(";");
        if(constraintBuilder.length() != 0) {
            builder.append(constraintBuilder);
        }

        return builder.toString();
    }

    @Override
    public String addColumns(String tableName, SQLColumnInfo... info) {
        StringBuilder builder = new StringBuilder();
        for(SQLColumnInfo cur : info) {
            builder.append(addColumn(tableName, cur));
        }
        return builder.toString();
    }

    @Override
    public String addConstraint(String tableName, SQLColumnInfo info, SQLConstraintData constraint) {
        if(constraint.isDataConstraint()) {
            SQLConstraints constraints = info.getConstraints();
            if(info.getConstraints() == null) {
                constraints = new SQLConstraints();
            }
            constraints = constraints.combine(new SQLConstraintData[]{constraint});
            SQLColumnInfo newInfo = new SQLColumnInfo(info.getType(), info.getName(), info.getSizes(), constraints);
            return setDataType(tableName, newInfo);
        }
        return addConstraintInternal(tableName, info, constraint);
    }

    @Override
    public String dropConstraint(String tableName, SQLColumnInfo info, SQLConstraint constraint) {
        if(constraint.isDataConstraint()) {
            SQLConstraints constraints = info.getConstraints();
            if(info.getConstraints() == null) {
                constraints = new SQLConstraints();
            }
            constraints.remove(constraint);
            SQLColumnInfo newInfo = new SQLColumnInfo(info.getType(), info.getName(), info.getSizes(), constraints);
            return setDataType(tableName, newInfo);
        }
        return removeConstraintInternal(tableName, info, constraint);
    }

    @Override
    public String addConstraints(String tableName, SQLColumnInfo info, SQLConstraintData... constraints) {
        StringBuilder builder = new StringBuilder();
        boolean flag = false;
        for(SQLConstraintData constraint : constraints) {
            if(constraint.isDataConstraint()) {
                SQLConstraints constraintss = info.getConstraints();
                if(info.getConstraints() == null) {
                    constraintss = new SQLConstraints();
                }
                constraintss = constraintss.combine(new SQLConstraintData[]{constraint});
                info = new SQLColumnInfo(info.getType(), info.getName(), info.getSizes(), constraintss);
                if(flag) builder.append("\n");
                builder.append(setDataType(tableName, info));
            } else {
                if(flag) builder.append("\n");
                builder.append(addConstraintInternal(tableName, info, constraint));
            }
            flag = true;
        }
        return builder.toString();
    }

    @Override
    public String dropConstraints(String tableName, SQLColumnInfo info, SQLConstraint... constraints) {
        StringBuilder builder = new StringBuilder();
        boolean flag = false;
        for(SQLConstraint constraint : constraints) {
            if(constraint.isDataConstraint()) {
                SQLConstraints constraintss = info.getConstraints();
                if(info.getConstraints() == null) {
                    constraintss = new SQLConstraints();
                }
                constraintss.remove(constraint);
                info = new SQLColumnInfo(info.getType(), info.getName(), info.getSizes(), constraintss);
                if(flag) builder.append("\n");
                builder.append(setDataType(tableName, info));
            } else {
                if(flag) builder.append("\n");
                builder.append(removeConstraintInternal(tableName, info, constraint));
            }
            flag = true;
        }
        return builder.toString();
    }

    @Override
    public String dropColumn(String tableName, String columnName) {
        return "ALTER TABLE `" + tableName + "` DROP COLUMN `" + columnName + "`;";
    }

    @Override
    public String renameColumn(String tableName, SQLColumnInfo info, String newName) {
        StringBuilder builder = new StringBuilder();

        builder.append("ALTER TABLE `")
            .append(tableName)
            .append("` CHANGE `")
            .append(info.getName())
            .append("` `")
            .append(newName)
            .append("` ")
            .append(info.getType().getName());

        int[] sizes = info.getSizes();
        if(sizes != null && sizes.length > 0) {
            builder.append(getSizesStr(sizes));
        }

        if(info.getConstraints() != null) {
            for(SQLConstraintData cur : info.getConstraints().get()) {
                if(!cur.isDataConstraint()) continue;
                builder.append(" ")
                    .append(cur.get());
            }
        }
        builder.append(";");

        return builder.toString();
    }

    @Override
    public String insertRow(String tableName, Object... values) {
        return "INSERT INTO `" + tableName + "` VALUES (" + formatValues(values) + ");";
    }

    @Override
    public String insertRow(String tableName, Map<String, Object> values) {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO `").append(tableName).append("` (");

        String[] columns = values.keySet().toArray(new String[0]);
        for(int i = 0; i < columns.length; ++i) {
            builder.append("`")
                .append(columns[i])
                .append("`");
            if(i != columns.length - 1) builder.append(", ");
        }

        builder.append(") VALUES (");

        Object[] valsArr = values.values().toArray(new Object[0]);
        builder.append(formatValues(valsArr));

        builder.append(");");
        return builder.toString();
    }

    private String setDataType(String tableName, SQLColumnInfo info) {
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE `")
            .append(tableName)
            .append("` MODIFY `")
            .append(info.getName())
            .append("` ")
            .append(info.getType().getName());

        int[] sizes = info.getSizes();
        if(sizes != null && sizes.length > 0) {
            builder.append(getSizesStr(info.getSizes()));
        }

        SQLConstraints constraints = info.getConstraints();
        if(constraints != null) {
            for(SQLConstraintData constraintData : constraints) {
                if(!constraintData.isDataConstraint()) continue;
                builder.append(" ")
                    .append(constraintData.get());
            }
        }

        builder.append(";");
        return builder.toString();
    }

    private String addConstraintInternal(String tableName, SQLColumnInfo info, SQLConstraintData constraint) {
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE `")
            .append(tableName)
            .append("` ");

        if(constraint.isDefault()) {
            builder
                .append("ALTER `")
                .append(info.getName())
                .append("` SET ");
        } else {
            builder.append("ADD ");
        }

        builder.append(constraint.get());
        switch(constraint.getConstraint()) {
            case DEFAULT:
                builder.append(" ")
                    .append(getExtraStr(constraint.getDefaultValue()));
                break;
            case CHECK:
                builder.append(" (")
                    .append(constraint.getCheckCondition())
                    .append(")");
                break;
            case PRIMARY_KEY:
            case UNIQUE:
                builder.append(" (`")
                    .append(info.getName())
                    .append("`)");
                break;
            case FOREIGN_KEY:
                builder.append(" (`")
                    .append(info.getName())
                    .append("`)")
                    .append(" REFERENCES `")
                    .append(constraint.getReferenceTable())
                    .append("`(`")
                    .append(constraint.getReferenceColumn())
                    .append("`)");
                break;
        }

        builder.append(";");
        return builder.toString();
    }

    private String removeConstraintInternal(String tableName, SQLColumnInfo info, SQLConstraint constraint) {
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE `")
            .append(tableName);

        switch(constraint) {
            case UNIQUE:
                builder.append("` DROP INDEX `").append(info.getName()).append("`");
                break;
            case FOREIGN_KEY:
            case CHECK:
                builder.append("` DROP `").append(info.getName()).append("`");
                break;
            case PRIMARY_KEY:
                builder.append("` DROP PRIMARY KEY");
                break;
            case DEFAULT:
                builder.append("` ALTER `").append(info.getName()).append("` DROP DEFAULT");
                break;
        }

        builder.append(";");
        return builder.toString();
    }

    private String getExtraStr(String extraStr) {
        StringBuilder builder = new StringBuilder();
        boolean encase;
        try {
            Double.parseDouble(extraStr);
            encase = false;
        }
        catch(NumberFormatException e) {
            encase = true;
        }
        if(encase) builder.append("'");
        builder.append(extraStr);
        if(encase) builder.append("'");
        return builder.toString();
    }

    private String getSizesStr(int[] sizes) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for(int sizeI = 0; sizeI < sizes.length; ++sizeI) {
            builder.append(sizes[sizeI]);
            if(sizeI != sizes.length - 1) builder.append(", ");
        }
        builder.append(")");
        return builder.toString();
    }

    private String formatValues(Object... values) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < values.length; ++i) {
            Object value = values[i];
            if(value == null) {
                builder.append("NULL");
            } else if(value instanceof String) {
                builder.append("'")
                    .append(value)
                    .append("'");
            }
            if(i != values.length - 1) builder.append(", ");
        }
        return builder.toString();
    }
}
