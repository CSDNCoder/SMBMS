package com.csnz.service.User;/*
 *
 *@create 2021-04-29-10:23
 */

import com.csnz.dao.BaseDao;
import com.csnz.dao.User.UserDao;
import com.csnz.dao.User.UserDaoImpl;
import com.csnz.pojo.Role;
import com.csnz.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//用户登录的业务层实现类
public class UserServiceImpl implements UserService {
    //业务层肯定是调用dao层的
    private UserDao userDao;
    public UserServiceImpl(){
        userDao =new UserDaoImpl();
    }

    @Override
    //(String userCode, String passWord)两个参数对应是的首页传来的值
    //用户登录
    public User login(String userCode, String passWord) {
        Connection conn = null;
        User user = null;

        try {
            //调用 dao层操作数据库的公共类方法 获取数据库的连接
            conn = BaseDao.getConnection();
            //得到连接后 开始查询 通过业务层调用具体的数据库操作
            user = userDao.getLoginInfo(conn, userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            BaseDao.closeResource(conn,null,null);
        }
        return user;
    }

    //修改密码
    @Override
    public boolean updatePassword(int id, String passWord) {
        boolean flag = false;
        Connection conn = null;
        try {
            //获取连接
            conn = BaseDao.getConnection();
            //调用dao层 执行更新操作
            int i = userDao.updatePassword(conn, id, passWord);
            if (i > 0) {
                flag = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
            return flag;
        }
    }
    //用户管理——查询记录数
    @Override
    public int getUserCounts(String username, int userRole) {
        Connection conn = null;
        int userCounts = 0;
        try {
            //获取连接
            conn = BaseDao.getConnection();
            //执行sql语句
            userCounts = userDao.getUserCounts(conn, username, userRole);

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
            return userCounts;
        }
    }
    //根据条件 查询用户列表
    @Override
    public List<User> getUserList(String QueryUserName, int QueryUserRole, int currentPageNo, int pageSize) {
        Connection conn = null;
        List<User> userList = null;
        //获取数据库连接
        try {
            conn = BaseDao.getConnection();
            userList = userDao.getUserList(conn, QueryUserName, QueryUserRole, currentPageNo, pageSize);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
            return userList;
        }
    }

    //用户管理模块中的 子模块—— 添加用户
    @Override
    public boolean addUser(User user) {
        Connection conn = null;
        boolean flag = false;
        try {
            //获取数据库连接
            conn = BaseDao.getConnection();
            //开启JDBC事务管理
            conn.setAutoCommit(false);
            //Service层调用dao层的方法添加用户
            int updateRows = userDao.addUser(conn, user);
            conn.commit();
            if(updateRows > 0){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();
        }finally {
            //释放连接
            BaseDao.closeResource(conn,null,null);
            return flag;
        }
    }

    //用户管理模块中的子模块 —— 删除用户
    @Override
    public boolean deleteUser(int userId) {
        boolean flag = false;
        Connection conn = null;
        try {
            //获取数据库连接
            conn = BaseDao.getConnection();
            //开启事务
            conn.setAutoCommit(false);
            flag = userDao.deleteUser(conn, userId);
            //提交事务
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
            //事务回滚
            conn.rollback();
        }finally {
            //释放连接
            BaseDao.closeResource(conn,null,null);
            return flag;
        }
    }

    //根据id查询用户信息
    @Override
    public User findById(int userId) {
        User user = null;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            conn.setAutoCommit(false);
            user = userDao.findById(conn, userId);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }finally {
            BaseDao.closeResource(conn,null,null);
            return user;
        }
    }

    //用户管理模块中的子模块 —— 更改用户信息
    @Override
    public boolean modify(int id,User user) {
        Connection conn = null;
        boolean flag = false;
        try {
            conn = BaseDao.getConnection();
            //开启事务
            conn.setAutoCommit(false);
            flag = userDao.modify(conn, id,user);
            //提交事务
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            //事务回滚
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally {
            //释放资源
            BaseDao.closeResource(conn,null,null);
            return flag;
        }
    }

    @Test
    public void Test(){
        UserServiceImpl us = new UserServiceImpl();
        User admin = us.login("CSNZ", "1");
        System.out.println("管理员admin的密码:"+admin!=null?true:false);
    }
    @Test
    public void TestUserCounts(){
        UserServiceImpl us = new UserServiceImpl();
        int counts = us.getUserCounts(null, 0);
        System.out.println(counts);
    }
    @Test
    public void TestGetUserList(){
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = userService.getUserList("", 1, 1, 5);
        for (User user : userList) {
            System.out.println(user);
        }
    }

}
