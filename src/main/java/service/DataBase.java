package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {

    private static DataBase db;
    private static Connection connection;

    public static DataBase getInstance() {
        DataBase localInstance = db;
        if (localInstance == null) {
            synchronized (DataBase.class) {
                localInstance = db;
                if (localInstance == null) {
                    db = new DataBase();
                    getConnection();
                }
            }
        }
        return db;
    }


    public static Connection getConnection() {
        if (db == null) {
            getInstance();
        }

        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://127.0.0.1:5432/BlockChain";
                connection = DriverManager.getConnection(url, "ali", "testpass");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        return connection;
    }


}
