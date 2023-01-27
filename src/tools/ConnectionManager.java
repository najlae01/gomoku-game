/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author MSI
 */
public class ConnectionManager {
    
/******************************************************************************
 * Connecting to the database using the Singleton design pattern *
 *****************************************************************************/
    
    private static Connection connection;
    private String url = "jdbc:mysql://localhost:3306/gomokudb";
    private String user = "root";
    private String password = "";

    private ConnectionManager() throws SQLException, ClassNotFoundException {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
            if(connection == null)
                    new ConnectionManager();
            return connection;
    }
}
