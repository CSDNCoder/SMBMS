package com.csnz.service.Bill;/*
 *
 *@create 2021-05-11-15:46
 */

import com.csnz.dao.BaseDao;
import com.csnz.dao.Bill.BillDaoImpl;
import com.csnz.pojo.Bill;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImpl implements BillService {
    private BillDaoImpl billDao;
    public BillServiceImpl(){
        this.billDao = new BillDaoImpl();
    }

    //根据 商品名称、供应商id、是否付款 查询订单总数
    @Override
    public int getBillCount(String queryProductName, int queryProviderId, int queryIsPayment) {
        Connection conn = null;
        int billCount = 0;
        try {
            conn = BaseDao.getConnection();
            conn.setAutoCommit(false);
            billCount = billDao.getBillCount(conn, queryProductName, queryProviderId, queryIsPayment);
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
            conn.rollback();
        }finally {
            BaseDao.closeResource(conn,null,null);
            return billCount;
        }
    }

    //根据 商品名称、供应商id、是否付款 查询订单列表
    @Override
    public List<Bill> getBillList(String queryProductName, int queryProviderId, int queryIsPayment, int currentPageNo, int pageSize) {
        Connection conn = null;
        List<Bill> billList = null;
        try {
            conn = BaseDao.getConnection();
            conn.setAutoCommit(false);
            System.out.println("enter BillServiceImpl...");
            billList = billDao.getBillList(conn, queryProductName, queryProviderId, queryIsPayment,currentPageNo,pageSize);
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
            conn.rollback();
        }finally {
            BaseDao.closeResource(conn,null,null);
            return billList;
        }
    }
    //添加订单
    @Override
    public boolean addBill(Bill bill) {
        boolean flag = false;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            conn.setAutoCommit(false);
            flag = billDao.addBill(conn, bill);
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
            conn.rollback();
        }finally {
            BaseDao.closeResource(conn,null,null);
            return flag;
        }
    }


    //删除订单
    @Override
    public boolean deleteBill(int billId) {
        boolean flag = false;
        Connection conn = null;
        try {
            conn  = BaseDao.getConnection();
            conn.setAutoCommit(false);
            flag = billDao.deleteBill(conn, billId);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();
        }finally {
            BaseDao.closeResource(conn,null,null);
            return flag;
        }
    }
    //根据订单id 获取订单信息
    @Override
    public Bill findByBillId(int billId){
        Connection conn = null;
        Bill bill = null;
        try {
            conn = BaseDao.getConnection();
            conn.setAutoCommit(false);
            bill = billDao.findByBillId(conn, billId);
        }catch (Exception e){
            e.printStackTrace();
            conn.rollback();
        }finally {
            BaseDao.closeResource(conn,null,null);
            return bill;
        }
    }

    //修改订单信息
    @Override
    public boolean modifyBill(int billId, Bill bill) {
        boolean flag = false;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            conn.setAutoCommit(false);
            flag = billDao.modifyBill(conn, billId, bill);
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
            conn.rollback();
        }finally {
            BaseDao.closeResource(conn,null,null);
            return flag;
        }
    }
}
