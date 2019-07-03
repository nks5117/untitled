import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        BaseDao baseDao = new BaseDao();
        ArrayList<ArrayList<String>> result = baseDao.exceuteQuery("SELECT * FROM websites;", null);

        for (ArrayList<String> line : result) {
            for (String s : line) {
                System.out.print(s + "\t");
            }
            System.out.println();
        }
    }
}


