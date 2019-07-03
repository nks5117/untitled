import java.io.FileInputStream;
import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;


public class BaseDao {
    private static String JDBC_DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static final String configFile = "db.properties";
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    static
    {
        init();
    }


    public static void init() {
        try {
            InputStream inputStream = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(inputStream);
            JDBC_DRIVER = properties.getProperty("JDBC_DRIVER");
            URL = properties.getProperty("DB_URL");
            USERNAME = properties.getProperty("DB_USERNAME");
            PASSWORD = properties.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperty("jdbc.drivers", JDBC_DRIVER);
    }

    private void getConnection() {
        connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeAll() {
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int exceuteUpdate(String preparedSql, Object[] param) {
        getConnection();
        int num = -1;

        try {
            preparedStatement = connection.prepareStatement(preparedSql);
            if (param != null && param.length > 0 ) {
                for (int i = 0; i < param.length; i++) {
                    preparedStatement.setObject(i + 1, param[i]);
                }
            }
            num = preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        closeAll();
        return num;
    }

    public ArrayList<ArrayList<String>> exceuteQuery (String preparedSql, Object[] param){
        ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
        getConnection();
        try {
            preparedStatement = connection.prepareStatement(preparedSql);
            int count = preparedStatement.getParameterMetaData().getParameterCount();
            if(param != null && param.length > 0 ){
                for(int i = 0; i < param.length; i++){
                    preparedStatement.setObject(i + 1,param[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            int culumnCount = resultSetMetaData.getColumnCount();

            ArrayList<String> line = new ArrayList<String>(culumnCount);

            for (int i = 0; i < culumnCount; i++){
                line.add(resultSetMetaData.getColumnName(i + 1));
            }
            table.add(line);

            while(resultSet.next()){
                line = new ArrayList<String>(culumnCount);
                for (int i = 0; i < culumnCount; i++){
                    line.add(resultSet.getString(i + 1));
                }
                table.add(line);
            }
            return table;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        System.out.println(System.getProperty("jdbc.drivers"));
    }
}