package com.nikesu.untitled.dao.impl;

import com.nikesu.untitled.dao.BaseDao;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.io.InputStream;
import java.util.Properties;
import java.util.ArrayList;

/**
 * BaseDaoImpl 提供了若干数据库增删改查方法
 */
public class BaseDaoImpl implements BaseDao {
    private static String JDBC_DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static final String configFile = "db.properties";

    static
    {
        init();
    }

    protected String capitalize(String x){
        char[] s = x.toCharArray();
        s[0] = Character.toUpperCase(s[0]);
        x = new String(s);
        return x;
    }

    private static void init() {
        try {
            InputStream inputStream = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(inputStream);
            JDBC_DRIVER = properties.getProperty("JDBC_DRIVER");
            URL = properties.getProperty("DB_URL");
            USERNAME = properties.getProperty("DB_USERNAME");
            PASSWORD = properties.getProperty("DB_PASSWORD");
            Class.forName(JDBC_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public void close(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            Statement statement = resultSet.getStatement();
            resultSet.close();
            close(statement);
        }
    }

    public void close(Statement statement) throws SQLException {
        if (statement != null) {
            Connection connection = statement.getConnection();
            statement.close();
            close(connection);
        }
    }

    public void close(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public int executeUpdate(String preparedSql, Object[] param) {
        int num = -1;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(preparedSql);
            if (param != null && param.length > 0 ) {
                for (int i = 0; i < param.length; i++) {
                    preparedStatement.setObject(i + 1, param[i]);
                }
            }
            num = preparedStatement.executeUpdate();
            close(preparedStatement);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return num;
    }

    public int executeUpdate(String sql) {
        return executeUpdate(sql, null);
    }


    public <T> ArrayList<T> executeQuery(String preparedSql, Object[] param, T t) {
        ArrayList<T> list = new ArrayList<>();

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(preparedSql);
            int count = preparedStatement.getParameterMetaData().getParameterCount();
            if(param != null && param.length > 0 ){
                for(int i = 0; i < param.length; i++){
                    preparedStatement.setObject(i + 1,param[i]);
                }
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<String> columns = new ArrayList<>();
            for (Field f : t.getClass().getDeclaredFields()) {
                columns.add(f.getName());
            }

            while (resultSet.next()) {
                T tmp = ((Class<T>)t.getClass()).getConstructor((Class<?>[])null).newInstance();
                for (String column : columns) {
                    t.getClass().getMethod(
                            "set"
                                    + column.substring(0,1).toUpperCase()
                                    + column.substring(1)
                            , String.class).invoke(tmp, resultSet.getString(column.toLowerCase()));
                }
                list.add(tmp);
            }
            close(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public <T> ArrayList<T> executeQuery(String sql, T t) {
        return executeQuery(sql, null, t);
    }
}