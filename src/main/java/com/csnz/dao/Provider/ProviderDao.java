package com.csnz.dao.Provider;/*
 *
 *@create 2021-05-08-10:27
 */

import com.csnz.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProviderDao {
    //根据供应商编码 或 供应商名称 查询供应商总数
    public abstract int getProviderCounts(Connection conn,String queryProCode,String queryProName)throws SQLException;

    //查询供应商数据列表
    public abstract List<Provider> getProviderList(Connection conn,String ProCode,String ProName,int currentPageNo, int pageSize)throws SQLException;

    //添加供应商的方法
    public abstract boolean addProvider(Connection conn,Provider provider)throws SQLException;

    //删除供应商的方法
    public abstract boolean deleteProvider(Connection conn,int providerId)throws SQLException;

    //根据供应商id查询供应商信息的方法
    public abstract Provider findById(Connection conn,int providerId)throws SQLException;

    //修改供应商信息方法
    public abstract boolean modifyProvider(Connection conn,int id,Provider provider)throws SQLException;
}
