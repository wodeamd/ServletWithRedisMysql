package com.fei.repo;

import com.fei.model.User;

import java.sql.*;

/**
 * Created by freddy on 4/2/2017.
 */
public class Mysql {

    private Connection getDbConnection() throws Exception{
        try {
            // Grab login info for MySQL from the credentials node
            String hostname = System.getenv("dbhost");
            String user = System.getenv("user");
//            String password = System.getenv("password");
//            String port = System.getenv("port");

//            String hostname = "172.16.51.58";
//            String user = "root";
//            String password = "111111";
//            String port = "3306";

            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" + hostname + ":" + "3306" + "/";
            Connection conn = DriverManager.getConnection(url, user, null);
            conn.createStatement().execute("create database if not exists " + "user");

            System.out.println("Checked db.");
            conn.createStatement().execute("CREATE TABLE if not exists user.user" + " (name varchar(255), email varchar(255), country varchar(100))");
            System.out.println("Checked table.");
            conn.close();

            String dbUrl = url + "user";


            System.out.println("Connecting to mysql:" + dbUrl);

            return DriverManager.getConnection(dbUrl, user, null);
        } catch (Exception e) {
            System.out.println("Caught error: ");
            e.printStackTrace();
            throw e;
        }
    }


    public User getUser(String name) {
        Connection conn = null;
        Statement stmt = null;
        User result = null;
        try{
            conn = getDbConnection();

            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT name, email, country FROM user where name=\"" + name + "\"";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                result = new User();
                result.setName(rs.getString("name"));
                result.setEmail(rs.getString("email"));
                result.setCountry(rs.getString("country"));
                System.out.println("Find user:" + result.getName() + " from mysql db.");
            }
            rs.close();
            stmt.close();
            conn.close();

        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try

        return result;

    }

    public void addUser(User user) {

        Connection conn = null;
        Statement stmt = null;
        try{
            conn = getDbConnection();

            stmt = conn.createStatement();
            String sql;
            sql = "INSERT INTO user(name,email,country) " +
                    "VALUES (\"" + user.getName() + "\",\"" + user.getEmail() + "\",\"" + user.getCountry() + "\")";
            System.out.println("Will execute sql:" + sql);

            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();

        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try

    }
}
