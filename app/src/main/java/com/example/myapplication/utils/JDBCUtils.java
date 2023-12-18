package com.example.myapplication.utils;


import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUtils {



    static {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static final String DB_URL = "jdbc:mysql://124.220.236.116:3306/test?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "Ll2622191252.";

    public static Connection getConn() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            // 记录日志或者根据实际情况进行异常处理
            e.printStackTrace();
            throw e; // 根据实际情况决定是否抛出异常
        }
        return conn;
    }


    public static void close(Connection conn, PreparedStatement ps, ResultSet rs){
        try {
            if (conn != null){
                conn.close();
            }
            if (ps != null){
                ps.close();
            }
            if (rs != null){
                rs.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}


