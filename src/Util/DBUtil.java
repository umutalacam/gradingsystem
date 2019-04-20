package Util;

import com.mysql.jdbc.*;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private static final String DB_URL = "jdbc:mysql://localhost/grading_system";
    private static final String USER = "root";
    private static final String PASS = "wryslx3nks";

    public static final int SUCCESS = 10;
    public static final int FAIL = 0;



    public static Connection connect(){
        Connection conn = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.print("Connecting to database...");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected successfully!");
            return conn;
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
            return null;
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
            return null;
        }
    }

}
