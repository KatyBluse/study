package com.study.studymarket.common.util;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecursionUtil {

    private static int addNum = 0;
    private static int avg = 0;

    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/study";
        String username = "root";
        String password = "123456";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private static int insert(Recursion recursion) {
        Connection conn = getConn();
        int i = 0;
        String sql = "insert into recursion (pid) values(?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, recursion.getPid());
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    //查找最后一条记录
    public static Recursion findLast() {
        Connection conn = getConn();
        String sql = "select * from recursion order by id desc limit 1";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            Recursion recursion = new Recursion();
            if (rs.isAfterLast()==rs.isBeforeFirst()) {
                return null;
            }
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
                    if (i%2 ==0) {
                        int pid = rs.getInt(i);
                        System.out.println("pid:"+pid+"\r\n");
                        recursion.setPid(pid);
                    } else {
                        int id = rs.getInt(i);
                        System.out.println("id:"+id);
                        recursion.setId(id);

                    }
                }
            }
            return recursion;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * @param add  加几次
     *
     * */
    public static void createList(int add) {
        Recursion lastData = findLast();
        int lastNum = 0;
        if (lastData != null) {
            lastNum = lastData.getId();
        }
        if (addNum > add) {
            return ;
        }
        Recursion recursion = new Recursion();
        recursion.setPid(lastNum);
        insert(recursion);
        addNum++;
        createList(add);
    }

    public static void main(String[] args) {
//        avg=10;
        RecursionUtil.createList(10);
    }

}
