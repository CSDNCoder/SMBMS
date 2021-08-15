package com.csnz.dao.Bill;/*
 *
 *@create 2021-05-11-15:45
 */

import com.csnz.pojo.Bill;
import com.csnz.util.Constants;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BillDao {
    //根据 商品名称、供应商id、是否付款 查询订单总数
    public abstract int getBillCount(Connection conn,String queryProductName,int queryProviderId,int queryIsPayment)throws  SQLException;
    //根据 商品名称、供应商id、是否付款 查询订单列表
    public abstract List<Bill> getBillList(Connection conn,String queryProductName,int queryProviderId,int queryIsPayment, int currentPageNo, int pageSize) throws SQLException;
    //添加订单
    public abstract boolean addBill(Connection conn,Bill bill)throws SQLException;
    //删除订单
    public abstract boolean deleteBill(Connection conn,int billId)throws SQLException;
    //根据订单id 获取订单信息
    public abstract Bill findByBillId(Connection conn,int billId)throws SQLException;
    //修改订单信息
    public abstract boolean modifyBill(Connection conn,int billId,Bill bill)throws SQLException;
}
