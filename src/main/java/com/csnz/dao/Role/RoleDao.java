package com.csnz.dao.Role;/*
 *
 *@create 2021-05-03-23:30
 */

import com.csnz.pojo.Role;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
public interface RoleDao {
    //获取角色列表
    public abstract List<Role> getRoleList(Connection conn)throws SQLException;
}
