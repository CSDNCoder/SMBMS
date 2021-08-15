package com.csnz.dao;/*
 *
 *@create 2021-04-29-8:14
 操作数据库的公共类
 */

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    //静态代码块 类加载的时候初始化
    static {
        Properties properties = new Properties();
        //通过类加载器读取对应的资源
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        //properties读取文件内容
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }
    //获取数据库的连接
    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    //编写查询公共方法
    public static ResultSet executeQuery(Connection conn,String sql,PreparedStatement preparedStatement,Object[] params,ResultSet resultSet) throws SQLException {
        //预编译的sql在后面直接执行即可
        preparedStatement = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            //setObject 占位符从1开始 而数字从0开始
            preparedStatement.setObject(i+1,params[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    //编写增删改查公共方法
    public static int execute(Connection conn,String sql,PreparedStatement preparedStatement,Object[] params) throws SQLException {
        //预编译的sql在后面直接执行即可
        int updateRow;
        preparedStatement = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            //setObject 占位符从1开始 而数字从0开始
            preparedStatement.setObject(i+1,params[i]);
        }
        updateRow = preparedStatement.executeUpdate();
        return updateRow;
    }
    //释放资源
    public static boolean closeResource(Connection conn,PreparedStatement preparedStatement,ResultSet resultSet){
        boolean flag = true;
        if(conn!=null){
            try {
                conn.close();
                //GC回收
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if(preparedStatement!=null){
            try {
                preparedStatement.close();
                //GC回收
                preparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if(resultSet!=null){
            try {
                resultSet.close();
                //GC回收
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
