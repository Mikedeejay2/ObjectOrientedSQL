package com.mikedeejay2.oosql.sqlgen;

import com.mikedeejay2.oosql.column.SQLColumnInfo;
import com.mikedeejay2.oosql.misc.SQLConstraint;
import com.mikedeejay2.oosql.misc.SQLDataType;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleSQLGenerator implements SQLGenerator
{
    public SimpleSQLGenerator()
    {
    }

    @Override
    public String createTable(String tableName, SQLColumnInfo... info)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE `")
            .append(tableName)
            .append("`");

        if(info.length > 0) builder.append(" (");

        List<Map.Entry<SQLConstraint, SQLColumnInfo>> endConstraints = new ArrayList<>();

        int extraIndex = 0;

        for(int index = 0; index < info.length; ++index)
        {
            SQLColumnInfo curInfo = info[index];
            String name = curInfo.getName();
            SQLConstraint[] constraints = curInfo.getConstraints();
            int[] sizes = curInfo.getSizes();
            SQLDataType type = curInfo.getType();

            builder.append("`")
                .append(name)
                .append("` ")
                .append(type.getName());

            if(sizes.length > 0)
            {
                builder.append("(");
                for(int sizeI = 0; sizeI < sizes.length; ++sizeI)
                {
                    builder.append(sizes[sizeI]);
                    if(sizeI != sizes.length - 1) builder.append(", ");
                }
                builder.append(")");
            }

            for(SQLConstraint constraint : constraints)
            {
                if(constraint.atEnd())
                {
                    endConstraints.add(new AbstractMap.SimpleEntry<>(constraint, curInfo));
                    continue;
                }

                builder.append(" ");
                String constraintStr = constraint.get();
                builder.append(constraintStr);

                if(constraint.useExtra())
                {
                    String extraStr = curInfo.getExtra()[extraIndex];
                    ++extraIndex;
                    builder.append(" ");
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
                }
            }

            if(index != info.length - 1) builder.append(", ");
        }

        for(Map.Entry<SQLConstraint, SQLColumnInfo> entry : endConstraints)
        {
            SQLConstraint constraint = entry.getKey();
            SQLColumnInfo curInfo = entry.getValue();
            String data = constraint.useExtra() ? curInfo.getExtra()[extraIndex] : "`" + curInfo.getName() + "`";
            ++extraIndex;
            String name = constraint.get();
            builder.append(", ")
                .append(name)
                .append("(")
                .append(data)
                .append(")");
        }

        if(info.length > 0) builder.append(")");

        return builder.toString();
    }

    @Override
    public String dropTable(String tableName)
    {
        return "DROP TABLE `" + tableName + "`";
    }

    @Override
    public String renameTable(String tableName, String newName)
    {
        return "ALTER TABLE `" + tableName + "` RENAME TO `" + newName + "`";
    }

    @Override
    public String countRows(String tableName)
    {
        return "SELECT COUNT(*) FROM `" + tableName + "`";
    }
}
