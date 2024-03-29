package com.patikadev.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    private Connection connect = null;

    public Connection connectDb(){
        try {
            this.connect = DriverManager.getConnection(Config.DB_URL,Config.DB_USER,Config.DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.connect;
    }

    public static Connection getInstance(){
        DbConnector db = new DbConnector();
        return db.connectDb();
    }
}
