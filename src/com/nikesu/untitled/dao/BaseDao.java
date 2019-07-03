package com.nikesu.untitled.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface BaseDao {
    Connection getConnection() throws SQLException;
    void close(ResultSet resultSet) throws SQLException;
    void close(Statement statement) throws SQLException;
    void close(Connection connection) throws SQLException;
}
