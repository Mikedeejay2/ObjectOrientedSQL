package com.mikedeejay2.oosql.execution;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface SQLExecutorInterface {
    int executeUpdate(String command);

    ResultSet executeQuery(String command);

    int executeUpdate(PreparedStatement statement);

    ResultSet executeQuery(PreparedStatement statement);

    PreparedStatement prepareStatement(String command);

    int[] executeUpdate(String... commands);

    ResultSet[] executeQuery(String... commands);

    int[] executeUpdate(PreparedStatement... statements);

    ResultSet[] executeQuery(PreparedStatement... statements);

    PreparedStatement[] prepareStatement(String... commands);
}
