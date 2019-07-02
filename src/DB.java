import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DB {
    public static String JDBC_DRIVER;
    public static String URL;
    public static String USERNAME;
    public static String PASSWORD;

    static
    {
        try {
            FileInputStream fileInputStream = new FileInputStream("db.properties");
            Properties properties = new Properties();
            properties.load(fileInputStream);
            JDBC_DRIVER = properties.getProperty("JDBC_DRIVER");
            URL = properties.getProperty("DB_URL");
            USERNAME = properties.getProperty("DB_USERNAME");
            PASSWORD = properties.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperty("jdbc.drivers", JDBC_DRIVER);
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("jdbc.drivers"));
    }
}