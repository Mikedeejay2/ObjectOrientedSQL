package com.mikedeejay2.oosql.sqlgen;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;
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

        List<Map.Entry<SQLConstraint, SQLColumnInfo>> tableConstraints = new ArrayList<>();
        if(info.getConstraints() != null)
        {
            for(SQLConstraint endConstraint : info.getConstraints())
            {
                tableConstraints.add(new AbstractMap.SimpleEntry<>(endConstraint, null));
            }
        }

        int extraIndex = 0;

        for(int index = 0; index < columns.length; ++index)
        {
            SQLColumnInfo curInfo = columns[index];
            String name = curInfo.getName();
            SQLConstraint[] constraints = curInfo.getConstraints();
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

            for(SQLConstraint constraint : constraints)
            {
                if(constraint.isTableConstraint())
                {
                    tableConstraints.add(new AbstractMap.SimpleEntry<>(constraint, curInfo));
                    continue;
                }

                builder.append(" ");
                String constraintStr = constraint.get();
                builder.append(constraintStr);

                if(constraint.useParams())
                {
                    String extraStr = curInfo.getConstraintParams()[extraIndex];
                    ++extraIndex;
                    builder.append(" ");
                    builder.append(getExtraStr(extraStr));
                }
            }

            if(index != columns.length - 1) builder.append(", ");
        }

        int tableIndex = 0;
        for(Map.Entry<SQLConstraint, SQLColumnInfo> entry : tableConstraints)
        {
            SQLConstraint constraint = entry.getKey();
            SQLColumnInfo curInfo = entry.getValue();
            String data;
            if(curInfo == null)
            {
                data = constraint.useParams() ? info.getConstraintParams()[tableIndex] : "`" + curInfo.getName() + "`";
                ++tableIndex;
            }
            else
            {
                data = constraint.useParams() ? curInfo.getConstraintParams()[extraIndex] : "`" + curInfo.getName() + "`";
                ++extraIndex;
            }
            String name = constraint.get();
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

        if(info.getConstraints() != null && info.getConstraints().length > 0)
        {
            builder.append("\n");
            builder.append(addConstraints(tableName, info, info.getConstraints()));
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
    public String addConstraints(String tableName, SQLColumnInfo info, SQLConstraint... constraints)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE `")
            .append(tableName)
            .append("` ");

        boolean flag = false;
        int extraIndex = 0;
        for(SQLConstraint constraint : constraints)
        {
            if(flag) builder.append("\n");
            if(constraint.isDataConstraint())
            {
                builder.append("MODIFY `")
                    .append(info.getName())
                    .append("` ")
                    .append(info.getType().getName());

                int[] sizes = info.getSizes();
                if(sizes != null && sizes.length > 0)
                {
                    builder.append(getSizesStr(sizes));
                }

                for(SQLConstraint cur : info.getConstraints())
                {
                    if(!cur.isDataConstraint()) continue;
                    builder.append(" ")
                        .append(cur.get());
                }

                builder.append(" ")
                    .append(constraint.get());

                if(constraint.useParams())
                {
                    String extraStr = info.getConstraintParams()[extraIndex];
                    ++extraIndex;
                    builder.append(" ");
                    builder.append(getExtraStr(extraStr));
                }
            }
            else
            {
                builder.append("ADD ")
                    .append(constraint.get())
                    .append(" (");

                if(constraint.useParams())
                {
                    String extraStr = info.getConstraintParams()[extraIndex];
                    ++extraIndex;
                    builder.append(" ");
                    builder.append(getExtraStr(extraStr));
                }
                else
                {
                    builder.append("`")
                        .append(info.getName())
                        .append("`");
                }
                builder.append(")");
            }
            flag = true;
            builder.append(";");
        }
        return builder.toString();
    }

    @Override
    public String dropConstraints(String tableName, SQLColumnInfo info, SQLConstraint... constraints)
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

        for(SQLConstraint cur : info.getConstraints())
        {
            if(!cur.isDataConstraint()) continue;
            builder.append(" ")
                .append(cur.get());
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
