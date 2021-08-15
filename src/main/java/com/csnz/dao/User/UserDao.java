package com.csnz.dao.User;/*
 *
 *@create 2021-04-29-9:44
 */

import com.csnz.pojo.Role;
import com.csnz.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//登录 判断 的接口
public interface UserDao {
    //得到要登录的用户信息
    public abstract User getLoginInfo(Connection conn,String userCode) throws SQLException;
    //修改密码
    public abstract int updatePassword(Connection conn,int id,String newPsd)throws SQLException;
    //根据用户名 或 角色 查询用户总数
    public abstract int getUserCounts(Connection conn,String username,int userRole)throws SQLException;
    //根据条件 查询 获取用户列表 userlist
    public abstract List<User> getUserList(Connection conn,String username,int userRole,int currentPageNo,int pageSize)throws SQLException;
    //用户管理模块中的 子模块—— 添加用户
    public abstract int addUser(Connection conn,User user)throws SQLException;
    //用户管理模块中的子模块 —— 删除用户
    public abstract boolean deleteUser(Connection conn,int userId)throws SQLException;
    //根据用户id 查询用户信息
    public abstract User findById(Connection conn,int userId)throws SQLException;
    //用户管理模块中的子模块 —— 更改用户信息
    public abstract boolean modify(Connection conn,int id,User user)throws SQLException;
}
