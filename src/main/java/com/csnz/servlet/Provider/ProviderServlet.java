package com.csnz.servlet.Provider;/*
 *
 *@create 2021-05-08-9:50
 */

import com.alibaba.fastjson.JSONArray;
import com.csnz.pojo.Provider;
import com.csnz.pojo.User;
import com.csnz.service.Provider.ProviderServiceImpl;
import com.csnz.util.Constants;
import com.csnz.util.PageSupport;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("query")){
            this.query(req,resp);
        }else if(method.equals("add")){
            this.add(req,resp);
        }else if(method.equals("delprovider")){
            this.deleteProvider(req,resp);
        }else if(method.equals("modify")){
            this.findById(req,resp);
        }else if(method.equals("modifyexe")){
            this.modify(req,resp);
        }else if(method.equals("view")){
            this.view(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    //查询 供应商 列表的方法
    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从providerlist.jsp中获取传来的数据
        String queryProCode = req.getParameter("queryProCode");
        String queryProName = req.getParameter("queryProName");
        String pageIndex = req.getParameter("pageIndex");
        List<Provider> providerList = null;
        int currentPageNo = 1;
        int pageSize = 5;
        //还需判断前端传来的数据是否为空
        if (queryProCode == null) {
            queryProCode = "";
        }
        if (queryProName == null ){
            queryProName = "";
        }
        if(pageIndex!=null){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        //获取符合条件的 信息数量
        int providerCounts = providerService.getProviderCounts(queryProCode, queryProName);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
    System.out.println("currentPageNo："+currentPageNo);


        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
    System.out.println("providerCounts:"+providerCounts);
        pageSupport.setTotalCount(providerCounts);
        //总共的页数
        int totalPageCount = pageSupport.getTotalPageCount();
        //总记录数
        int totalCount = pageSupport.getTotalCount();


        //获取符合条件的 信息
        providerList= providerService.getProviderList(queryProCode, queryProName, currentPageNo, pageSize);
        //将信息存入requset 使得在前端展示
        req.setAttribute("providerList",providerList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryProCode",queryProCode);
        req.setAttribute("queryProName",queryProName);
        //返回前端页面展示
        req.getRequestDispatcher("providerlist.jsp").forward(req,resp);
    }

    //添加 供应商方法
    public void add(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //从前端 获取供应商的信息
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");
        //下面的参数自己获取
        int createdBy = ((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId();

        //将信息封装成一个供应商对象
        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(createdBy);
        provider.setCreationDate(new Date());
        //调用service层方法
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        boolean flag = providerService.addProvider(provider);
        //如果成功 则重定向到providerlist.jsp页面
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
        }else{
            //失败 跳转到添加页面
            req.getRequestDispatcher("provideradd.jsp").forward(req,resp);
        }

    }
    //删除供应商的方法
    public void deleteProvider(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //从前端获取 要删除的供应商 的id
        String proid = req.getParameter("proid");
        int id = 0;
        try {
            id = Integer.parseInt(proid);
        }catch (Exception e){
            e.printStackTrace();
            id = 0;
        }
        //将信息存入一个map集合中 传给ajax
        HashMap<Object, Object> resultMap = new HashMap<>();
        if(id<=0){
            resultMap.put("delResult","notexist");
        }else{
            ProviderServiceImpl providerService = new ProviderServiceImpl();
            if(providerService.deleteProvider(id)){
                //如果删除成功
                resultMap.put("delResult","true");
            }else{
                resultMap.put("delResult","false");
            }
        }
        //将此map集合转换成json格式传递
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        //JSONArray 阿里巴巴的JSON工具类 用途就是：转换格式
        out.write(JSONArray.toJSONString(resultMap));
        out.flush();
        out.close();
    }
    //根据id查询供应商信息的方法
    public void findById(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        //从前端获取id
        String proid = req.getParameter("proid");
        int id = 0;
        try {
            id = Integer.parseInt(proid);
        }catch (Exception e){
            e.printStackTrace();
            id = 0;
        }
       if(id>0){
           ProviderServiceImpl providerService = new ProviderServiceImpl();
           Provider provider = providerService.findById(id);
           //设置id 让修改提交时可获取
           provider.setId(id);
           //将供应商信息存至 req
           req.setAttribute("provider",provider);
           //返回至前端展示页面
           req.getRequestDispatcher("providermodify.jsp").forward(req,resp);
       }
    }
    //修改供应商信息方法
    public void modify(HttpServletRequest req,HttpServletResponse resp) throws  IOException, ServletException {
        System.out.println("enter modify ...");
        //从前端获取 要修改的供应商的id信息
        String proId =  req.getParameter("proid");
        System.out.println("proId : ->"+proId.toString());
        int id = 0;
        try {
            id = Integer.parseInt(proId);
        }catch (Exception e){
            e.printStackTrace();
            id = 0;
        }
        //从前端获取供应商信息
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");
        //封装成一个对象
        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        //下面的参数不是由前端传来的
        provider.setModifyDate(new Date());
        provider.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        if(id>0){
            //执行更改
            ProviderServiceImpl providerService = new ProviderServiceImpl();
            if(providerService.modifyProvider(id, provider)){
                //如果修改成功 重定向到展示供应商列表页面
                resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
            }else{
                //修改失败 转发到此修改页面
                req.getRequestDispatcher("providermodify.jsp").forward(req,resp);
            }
        }
    }
    //查看 供应商信息方法
    public void view(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        //从前端获取供应商的id
        String proid = req.getParameter("proid");
        int id = 0;
        try {
            id = Integer.parseInt(proid);
        }catch (Exception e){
            e.printStackTrace();
            id = 0 ;
        }
        //根据id查询
        if(id >0){
            ProviderServiceImpl providerService = new ProviderServiceImpl();
            Provider provider = providerService.findById(id);
            //将此对象传到providerview.jsp进行展示
            req.setAttribute("provider",provider);
            //重定向到展示页
            req.getRequestDispatcher("providerview.jsp").forward(req,resp);
        }
    }
}
