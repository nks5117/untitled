
import java.sql.*;


public class Main {
    public static void main(String[] args){

        try{
            Connection connection = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM websites;");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String url = resultSet.getString("url");
                System.out.println(id+"\t"+name+"\t"+url);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}


