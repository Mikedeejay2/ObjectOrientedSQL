package com.mikedeejay2.oosql.sqlgen;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.misc.SQLDataType;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraint;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraintData;
import com.mikedeejay2.oosql.misc.constraint.SQLConstraints;
import com.mikedeejay2.oosql.table.SQLTableInfo;

import java.util.*;

public class SimpleSQLGenerator implements SQLGenerator
{
    @Override
    public String createTable(SQLTableInfo info)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE `")
            .append(info.getTableName())
            .append("`");

        SQLColumnInfo[] columns = info.getColumns();

        if(columns.length > 0) builder.append(" (");

        List<Map.Entry<SQLConstraintData, SQLColumnInfo>> endConstraints = new ArrayList<>();
        SQLConstraints tableConstraints = info.getConstraints();
        if(tableConstraints != null)
        {
            SQLConstraintData[] tableConstraintData = tableConstraints.get();
            if(tableConstraintData != null)
            {
                for(SQLConstraintData curTableData : tableConstraintData)
                {
                    endConstraints.add(new AbstractMap.SimpleEntry<>(curTableData, null));
                }
            }
        }

        for(int index = 0; index < columns.length; ++index)
        {
            SQLColumnInfo curInfo = columns[index];
            String name = curInfo.getName();
            SQLConstraints constraints = curInfo.getConstraints();
            int[] sizes = curInfo.getSizes();
            SQLDataType type = curInfo.getType();

            builder.append("`")
                .append(name)
                .append("` ")
                .append(type.getName());

            if(sizes != null && sizes.length > 0)
            {
                builder.append(getSizesStr(sizes));
            }

            if(constraints != null)
            {
                for(SQLConstraintData constraint : constraints.get())
                {
                    if(constraint.isTableConstraint())
                    {
                        endConstraints.add(new AbstractMap.SimpleEntry<>(constraint, curInfo));
                        continue;
                    }

                    builder.append(" ");
                    String constraintStr = constraint.get();
                    builder.append(constraintStr);

                    if(constraint.isCheck())
                    {
                        builder.append(" ");
                        builder.append(getExtraStr(constraint.getCheckCondition()));
                    }
                    else if(constraint.isDefault())
                    {
                        builder.append(" ");
                        builder.append(getExtraStr(constraint.getDefaultValue()));
                    }
                }
            }

            if(index != columns.length - 1) builder.append(", ");
        }

        for(Map.Entry<SQLConstraintData, SQLColumnInfo> entry : endConstraints)
        {
            SQLConstraintData curConstraint = entry.getKey();
            SQLColumnInfo curInfo = entry.getValue();
            String data = null;
            if(curConstraint.isCheck())
            {
                data = curConstraint.getCheckCondition();
            }
            else if(curConstraint.isDefault())
            {
                data = curConstraint.getDefaultValue();
            }
            else
            {
                data = "`" + curInfo.getName() + "`";
            }
            String name = curConstraint.get();
            builder.append(", ")
                .append(name)
                .append("(")
                .append(data)
                .append(")");
        }

        if(columns.length > 0) builder.append(")");

        builder.append(";");

        return builder.toString();
    }

    @Override
    public String dropTable(String tableName)
    {
        return "DROP TABLE `" + tableName + "`;";
    }

    @Override
    public String renameTable(String tableName, String newName)
    {
        return "ALTER TABLE `" + tableName + "` RENAME TO `" + newName + "`;";
    }

    @Override
    public String countRows(String tableName)
    {
        return "SELECT COUNT(*) FROM `" + tableName + "`;";
    }

    @Override
    public String dropDatabase(String databaseName)
    {
        return "DROP DATABASE `" + databaseName + "`;";
    }

    @Override
    public String createDatabase(String databaseName)
    {
        return "CREATE DATABASE `" + databaseName + "`;";
    }

    @Override
    public String renameDatabase(String databaseName, String newName)
    {
        return "RENAME DATABASE `" + databaseName + "` TO `" + newName + "`;";
    }

    @Override
    public String addColumn(String tableName, SQLColumnInfo info)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE `")
            .append(tableName)
            .append("` ADD `")
            .append(info.getName())
            .append("` ")
            .append(info.getType().getName());

        int[] sizes = info.getSizes();
        if(sizes != null && sizes.length > 0)
        {
            builder.append(getSizesStr(sizes));
        }

        builder.append(";");

        SQLConstraints constraints = info.getConstraints();
        if(constraints != null)
        {
            SQLConstraintData[] constraintData = constraints.get();
            if(constraintData.length > 0)
            {
                builder.append("\n");
                builder.append(addConstraints(tableName, info, constraints));
            }
        }

        return builder.toString();
    }

    @Override
    public String addColumns(String tableName, SQLColumnInfo... info)
    {
        StringBuilder builder = new StringBuilder();
        for(SQLColumnInfo cur : info)
        {
            builder.append(addColumn(tableName, cur));
        }
        return builder.toString();
    }

    @Override
    public String addConstraint(String tableName, SQLColumnInfo info, SQLConstraintData constraint)
    {
        return null;
    }

    private String setDataType(String tableName, SQLColumnInfo info)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE `")
            .append(tableName)
            .append("` MODIFY COLUMN `")
            .append(info.getName())
            .append("` ");
        return builder.toString();
    }

    @Override
    public String dropConstraint(String tableName, SQLColumnInfo info, SQLConstraintData constraint)
    {
        return null;
    }

    @Override
    public String addConstraints(String tableName, SQLColumnInfo info, SQLConstraintData... constraints)
    {
        return null;
    }

    @Override
    public String dropConstraints(String tableName, SQLColumnInfo info, SQLConstraintData... constraints)
    {
        return null;
    }

    @Override
    public String addConstraints(String tableName, SQLColumnInfo info, SQLConstraints constraints)
    {
        return null;
    }

    @Override
    public String dropConstraints(String tableName, SQLColumnInfo info, SQLConstraints constraints)
    {
        return null;
    }

    @Override
    public String dropColumn(String tableName, String columnName)
    {
        return "ALTER TABLE `" + tableName + "` DROP COLUMN `" + columnName + "`;";
    }

    @Override
    public String renameColumn(String tableName, SQLColumnInfo info, String newName)
    {
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
        if(sizes != null && sizes.length > 0)
        {
            builder.append(getSizesStr(sizes));
        }

        if(info.getConstraints() != null)
        {
            for(SQLConstraintData cur : info.getConstraints().get())
            {
                if(!cur.isDataConstraint()) continue;
                builder.append(" ")
                    .append(cur.get());
            }
        }
        builder.append(";");

        return builder.toString();
    }

    private String getExtraStr(String extraStr)
    {
        StringBuilder builder = new StringBuilder();
        boolean encase;
        try
        {
            Double.parseDouble(extraStr);
            encase = false;
        }
        catch(NumberFormatException e)
        {
            encase = true;
        }
        if(encase) builder.append("'");
        builder.append(extraStr);
        if(encase) builder.append("'");
        return builder.toString();
    }

    private String getSizesStr(int[] sizes)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for(int sizeI = 0; sizeI < sizes.length; ++sizeI)
        {
            builder.append(sizes[sizeI]);
            if(sizeI != sizes.length - 1) builder.append(", ");
        }
        builder.append(")");
        return builder.toString();
    }
}
