package com.pruebas.springboot.di.app.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException; 

public class ConnectionManager {
    private static Connection connection;

    private static Connection init(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        connection = null;
        ConfigManager configManager = new ConfigManager();
        String hostName= configManager.getPropValues("hostname");
        String username= configManager.getPropValues("username");
        String passwd= configManager.getPropValues("passwd");
        String dbname= configManager.getPropValues("dbname");
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://"+hostName+"/"+dbname, username, passwd);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }


        if(connection!=null){
            System.out.println("Connections ready");

        }
        else{
            System.out.println("Error connections");
        }
        return connection;
    }
    public static Connection getConnection() throws SQLException{
        if (connection == null || connection.isClosed()){
            init();
        }
        return connection;
    }
    public static void main(String[] args) throws SQLException {
        if (new ConnectionManager().getConnection() != null) 
            System.out.println("CONNECTION OK!");
        else
            System.out.println("ERROR CONNECTION");
       
    }
}
