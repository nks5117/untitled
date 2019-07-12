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
 * @author 倪可塑
 * @version 1.0
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

    /**
     * 在加载 BaseDaoImpl 类时从文件 configFile
     * 中读取数据库设置，加载 JDBC 驱动
     */
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

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * 关闭 resultSet 和与该 resultSet 相关联的 Statement
     * 和 Connection（如有）
     * @param resultSet
     * @throws SQLException
     */
    @Override
    public void close(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            Statement statement = resultSet.getStatement();
            resultSet.close();
            close(statement);
        }
    }

    /**
     * 关闭 statement 和与该 statement 相关联的 Connection（如有）
     * @param statement
     * @throws SQLException
     */
    @Override
    public void close(Statement statement) throws SQLException {
        if (statement != null) {
            Connection connection = statement.getConnection();
            statement.close();
            close(connection);
        }
    }

    /**
     * 关闭 connection
     * @param connection
     * @throws SQLException
     */
    @Override
    public void close(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * 执行参数化 SQL 语句
     * @param preparedSql 参数化 SQL 语句
     * @param param 对应的参数
     * @return 受影响的行数
     */
    protected int executeUpdate(String preparedSql, Object[] param) {
        int num = 0;
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

    /**
     * 执行给定的 SQL 语句
     * @param sql
     * @return 受影响的行数
     */
    protected int executeUpdate(String sql) {
        return executeUpdate(sql, null);
    }

    /**
     * 向数据库中插入一个对象。insertToTable 方法根据给定的对象生
     * 成一个参数化 SQL 语句，其内容形如
     * "INSERT INTO table (columns) VALUE (values);"
     * 其中，columns 只会包含 t 中对应字段不为 null 的列，因此，
     * 参数 t 要满足：对于数据库中每个 NOT NULL 且没有默认值的列，
     * 对象 t 的相应字段都不应为 null。
     * @param table 要插入到的表
     * @param t 要插入的对象
     * @param <T> 类型 T 必须满足以下条件：
     *           1. 对于 T 的每个字段，都有对应的 get 方法
     *           2. T 的每个字段和方法都符合驼峰命名规范
     *           3. 对于 T 的每个字段，在数据库中都有对应名称的列，
     *           但字段名中的大写字母在数据库列名中为小写
     * @return 受影响的行数（应当是 1）
     */
    protected <T> int insertToTable(String table, T t) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<Object> param = new ArrayList<>();
        try {
            for (Field f : t.getClass().getDeclaredFields()) {
                Object obj = t.getClass().getDeclaredMethod("get"
                        + f.getName().substring(0, 1).toUpperCase()
                        + f.getName().substring(1), (Class<?>[]) null).invoke(t);
                if (obj != null) {
                    columns.add(f.getName());
                    param.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < columns.size(); i++) {
            sql.append("`" + columns.get(i).toLowerCase() + "`");
            if (i < columns.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(") VALUES (");
        for (int i = 0; i < columns.size(); i++) {
            sql.append("?");
            if (i < columns.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(");");

        return executeUpdate(sql.toString(), param.toArray());
    }

    /**
     * 向数据库中插入多个对象。
     * @param table 要插入到的表
     * @param t 要插入的对象
     * @param <T> 类型 T 必须满足以下条件：
     *           1. 对于 T 的每个字段，都有对应的 get 方法
     *           2. T 的每个字段和方法都符合驼峰命名规范
     *           3. 对于 T 的每个字段，在数据库中都有对应名称的列，
     *           但字段名中的大写字母在列名中为小写
     * @see BaseDaoImpl#insertToTable(String, T)
     * @return 受影响的行数
     */
    protected <T> int insertToTable(String table, T...t) {
        int num = 0;
        for (int i = 0; i < t.length; i++) {
            num += insertToTable(table, t[i]);
        }
        return num;
    }

    /**
     * 通过给定的 SQL 语句从数据库中获得一个对象列表
     * @param preparedSql 参数化 SQL 语句
     * @param param 参数列表
     * @param tClass 类型 T 的 Class
     * @param <T> 要获得的对象的类型，该类型 T 必须满足以下条件：
     *           1. 有一个无参构造器
     *           2. 对于 T 的每个字段，都有对应的接收 String 参数的 get 方法
     *           3. 对于 T 的每个字段，在 SQL 语句的查询结果中都有对应名称的列，但字段中的大写字母在数据库列名中为小写
     *           4. T 的每个字段和对应的 get 方法都使用驼峰命名法（如 userName 和 getUserName()）
     * @return 如果查询结果为空，返回一个长度为 0 的 ArrayList。
     */
    protected <T> ArrayList<T> executeQuery(String preparedSql, Object[] param, Class<T> tClass) {
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
            for (Field f : tClass.getDeclaredFields()) {
                columns.add(f.getName());
            }

            while (resultSet.next()) {
                T tmp = tClass.getConstructor((Class<?>[])null).newInstance();
                for (String column : columns) {
                    tClass.getMethod(
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

    /**
     * 通过给定的 SQL 语句从数据库中获得一个对象列表
     * @param sql SQL 语句
     * @param tClass 类型 T 的 Class
     * @param <T> 要获得的对象的类型，该类型 T 必须满足以下条件：
     *           1. 有一个无参构造器
     *           2. 对于 T 的每个字段，都有对应的接收 String 参数的 get 方法
     *           3. 对于 T 的每个字段，在 SQL 语句的查询结果中都有对应名称的列，但字段中的大写字母在数据库列名中为小写
     *           4. T 的每个字段和对应的 get 方法都使用驼峰命名法（如 userName 和 getUserName()）
     * @return 如果查询结果为空，返回一个长度为 0 的 ArrayList。
     */
    protected <T> ArrayList<T> executeQuery(String sql, Class<T> tClass) {
        return executeQuery(sql, null, tClass);
    }

    /**
     * 获取某个表中的全部数据
     * @param table 要获取数据的表
     * @param tClass 类型 T 的 Class
     * @param <T> 要获得的对象的类型，该类型 T 必须满足以下条件：
     *           1. 有一个无参构造器
     *           2. 对于 T 的每个字段，都有对应的接收 String 参数的 get 方法
     *           3. 对于 T 的每个字段，在 SQL 语句的查询结果中都有对应名称的列，但字段中的大写字母在数据库列名中为小写
     *           4. T 的每个字段和对应的 get 方法都使用驼峰命名法（如 userName 和 getUserName()）
     * @return 如果查询结果为空，返回一个长度为 0 的 ArrayList。
     */
    protected <T> ArrayList<T> getAllFromTable (String table, Class<T> tClass) {
        return executeQuery("SELECT * FROM " + table + ";", tClass);
    }
}