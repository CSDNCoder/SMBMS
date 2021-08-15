package com.csnz.servlet.User;/*
 *
 *@create 2021-05-03-9:50
 */

import com.alibaba.fastjson.JSONArray;
import com.csnz.pojo.Role;
import com.csnz.pojo.User;
import com.csnz.service.Role.RoleServiceImpl;
import com.csnz.service.User.UserServiceImpl;
import com.csnz.util.Constants;
import com.csnz.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//实现Servlet复用

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savePwd")){
            this.savePwd(req,resp);
        }else if(method.equals("pwdmodify")){
            this.verifyPwd(req,resp);
        }else if(method.equals("query")){
            this.query(req,resp);
        }else if(method.equals("add")){
            this.add(req,resp);
        }else if(method.equals("getRoleList")){
            this.getRoleList(req,resp);
        }else if(method.equals("ifExist")){
            this.ifExist(req,resp);
        }else if(method.equals("deluser")){
            this.deleteUser(req,resp);
        }else if(method.equals("modify")){
            this.findById(req,resp);
        }else if(method.equals("modifyexe")){
            this.modify(req,resp);
        }else if(method.equals("view")){
            this.viewUser(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    //用户修改密码方法
    public void savePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从Session中获取ID
        Object obj = req.getSession().getAttribute(Constants.USER_SESSION);
        //获取前端页面传来的新密码
        String newpassword = req.getParameter("newpassword");
        //先判断不为空 再比较密码是否相等
        if(obj != null && newpassword != null){
            User user = (User) obj;
            UserServiceImpl userService = new UserServiceImpl();
            //修改密码并返回结果
            boolean flag = userService.updatePassword(user.getId(), newpassword);
            //如果密码修改成功 移除当前session
            if(flag){
                req.setAttribute("message","修改密码成功，请使用新密码登录!");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else{
                req.setAttribute("message","密码修改失败 新密码不符合规范");
            }
        }else{
            req.setAttribute("message","新密码不能为空!");
        }
        //修改完了 重定向到此修改页面
        req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);

    }
    //验证密码的方法
    public void verifyPwd(HttpServletRequest req, HttpServletResponse resp)throws  IOException{
        //依旧从session中取ID
        Object obj = req.getSession().getAttribute(Constants.USER_SESSION);
        //取 前端传来的旧密码
        String oldpassword = req.getParameter("oldpassword");
        //将结果存放在map集合中 让Ajax使用
        Map<String, String> resultMap = new HashMap<>();
        //下面开始判断 键都是用result 此处匹配js中的Ajax代码
        if(obj == null){
            //说明session被移除了 或未登录|已注销
            resultMap.put("result","sessionerror");
        }else if(oldpassword == null){
            //前端输入的密码为空
            resultMap.put("result","error");
        }else {
            //如果旧密码与前端传来的密码相同
            if(((User)obj).getUserPassword().equals(oldpassword)){
                resultMap.put("result","true");
            }else{
                //前端输入的密码和真实密码不相同
                resultMap.put("result","false");
            }
        }
        //上面已经封装好 现在需要传给Ajax 格式为json 所以我们得转换格式
        resp.setContentType("application/json");//将应用的类型变成json
        PrintWriter writer = resp.getWriter();
        //JSONArray 阿里巴巴的JSON工具类 用途就是：转换格式
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();

    }
    //查询用户列表的方法
    public void query(HttpServletRequest req, HttpServletResponse resp){
        //查询用户列表
            //从前端获取数据
            String queryUserName = req.getParameter("queryName");
            String temp = req.getParameter("queryUserRole");//值为0 、1、2、3
            String pageIndex = req.getParameter("pageIndex");
            int queryUserRole = 0;

            //获取用户列表
            UserServiceImpl userService = new UserServiceImpl();
            List<User> userList = null;

            int currentPageNo = 1;
            int pageSize = 5;

            if(queryUserName == null){
                queryUserName = "";
            }
            if(temp!=null && !temp.equals("")){
                queryUserRole = Integer.parseInt(temp);
            }
            if(pageIndex!=null){
                currentPageNo = Integer.parseInt(pageIndex);
            }
            //获取用户总数 分页：上一页 下一页
            int totalCount = userService.getUserCounts(queryUserName, queryUserRole);
            //总页数支持
            PageSupport pageSupport = new PageSupport();
                System.out.println("当前页："+currentPageNo);
            pageSupport.setCurrentPageNo(currentPageNo);
            pageSupport.setPageSize(pageSize);
                System.out.println("获取用户总数"+totalCount);
            pageSupport.setTotalCount(totalCount);
            //总共的页数
            int totalPageCount = pageSupport.getTotalPageCount();
            //控制首页和尾页
            //如果页数小于1，就显示第一页  页数大于 最后一页就 显示最后一页
            if(currentPageNo<1){
                currentPageNo =1;
            }else if(currentPageNo>totalPageCount){
                currentPageNo = totalPageCount;
            }
        System.out.println("返回UserList的数据测试"+queryUserName+":"+queryUserRole+":"+currentPageNo+":"+pageSize);
        //获取用户列表展示
            userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
            //将数据传给前端
        System.out.println(userList);
//        for (User user : userList) {
//            System.out.println(user.toString());
//        }
            req.setAttribute("userList",userList);

        RoleServiceImpl roleService = new RoleServiceImpl();
        //所有角色
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);

        //返回至前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //添加用户方法
    public void add(HttpServletRequest req, HttpServletResponse resp)throws IOException,ServletException {
        System.out.println("进入add方法");
        //从前端获取数据
        String addUserCode = req.getParameter("userCode");
//        System.out.println("\n前端输入的："+addUserCode+"\n");
        String addUserName = req.getParameter("userName");
        String addUserPassword = req.getParameter("userPassword");
        String addGender = req.getParameter("gender");
        String addBirthday = req.getParameter("birthday");
        String addPhone = req.getParameter("phone");
        String addAddress = req.getParameter("address");
        String addUserRole = req.getParameter("userRole");
        //对数据进行封装
        User user = new User();
        user.setUserCode(addUserCode);
        user.setUserName(addUserName);
        user.setUserPassword(addUserPassword);
        user.setGender(Integer.parseInt(addGender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(addBirthday));
        }catch (ParseException e){
            e.printStackTrace();
        }
        user.setPhone(addPhone);
        user.setAddress(addAddress);
        user.setUserRole(Integer.parseInt(addUserRole));
        //注意这两个参数不在表单的填写范围内
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setCreateDate(new Date());
//        System.out.println("封装好的："+user.getUserCode());
        //调用service执行添加方法
        UserServiceImpl userService = new UserServiceImpl();
        boolean flag  = userService.addUser(user);
        if(flag){
            //说明执行成功 网页重定向到 用户管理页面(即 查询全部用户列表)
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            //说明 添加失败 转发到此 添加页面
            req.getRequestDispatcher("useradd.jsp").forward(req,resp);
        }
    }
    //用户管理模块中 子模块(添加用户——表单中的用户角色下拉框)
    public void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Role> roleList = null;
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList1 = roleService.getRoleList();
        //把roleList1 转换为json对象输出
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(JSONArray.toJSONString(roleList1));
        out.flush();
        out.close();
    }
    //用户管理模块 子模块(验证用户编码是否已经存在)
    public void ifExist(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取前端输入 的用户编码
        String userCode = req.getParameter("userCode");
        //将结果存放在map集合中 让Ajax使用
        Map<String, String> resultMap = new HashMap<>();
        if(userCode == null || userCode.equals("")){
            System.out.println("前端未填写用户编码...");
            resultMap.put("userCode","NoWrite");
        }else{
            System.out.println("前端填写了用户编码...");
            UserServiceImpl userService = new UserServiceImpl();
            User isNullUser = userService.login(userCode, "");

            //判断是否已经存在这个用户编码
            boolean flag = isNullUser != null ? true : false;
            if(flag){
                //用户编码存在
                //将信息存入map中
                resultMap.put("userCode","exist");
            }
        }
        //上面已经封装好 现在需要传给Ajax 格式为json 所以我们得转换格式
        resp.setContentType("application/json");//将应用的类型变成json
        PrintWriter writer = resp.getWriter();
        //JSONArray 阿里巴巴的JSON工具类 用途就是：转换格式
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();

    }
    //用户管理模块中的子模块 —— 删除用户
    public void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //从前端获取 要删除的用户 的信息
        String userid = req.getParameter("uid");
        int delId = 0;
        //先转换
        try {
            delId= Integer.parseInt(userid);
        }catch (Exception e){
            e.printStackTrace();
            delId = 0;
        }
        //将结果存放在map集合中 让Ajax使用
        Map<String, String> resultMap = new HashMap<>();
        if(delId<=0){
            resultMap.put("delResult","notexist");
        }else {
            UserServiceImpl userService = new UserServiceImpl();
            if(userService.deleteUser(delId)){
                resultMap.put("delResult","true");
            }else {
                resultMap.put("delResult", "false");
            }
        }
        //上面已经封装好 现在需要传给Ajax 格式为json 所以我们得转换格式
        resp.setContentType("application/json");//将应用的类型变成json
        PrintWriter writer = resp.getWriter();
        //JSONArray 阿里巴巴的JSON工具类 用途就是：转换格式
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }
    //用户管理模块中的功能 —— 根据id查询用户信息
    public void findById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从前端获取 要修改的用户 的id
        String uId = req.getParameter("uid");
        int userId = 0;
        try {
            userId = Integer.parseInt(uId);
        }catch (Exception e){
            e.printStackTrace();
        }
        UserServiceImpl userService = new UserServiceImpl();
        //查询要更改的用户信息
        User user = userService.findById(userId);
        //将用户信息保存至 request中 让usermodify.jsp显示
        req.setAttribute("user",user);
        req.getRequestDispatcher("usermodify.jsp").forward(req,resp);
    }
    //用户管理模块中的子模块 —— 更改用户信息
    public void modify(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //从前端获取 要修改的用户 的id
        String uId = req.getParameter("uid");
        int userId = 0;
        try {
            userId = Integer.parseInt(uId);
        }catch (Exception e){
            e.printStackTrace();
        }
        //从修改信息的表单中封装信息
        User user = new User();
        user.setUserName(req.getParameter("userName"));
        user.setGender(Integer.parseInt(req.getParameter("gender")));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("birthday")));
        }catch (ParseException e){
            e.printStackTrace();
        }
        user.setPhone(req.getParameter("phone"));
        user.setAddress(req.getParameter("address"));
        user.setUserRole(Integer.parseInt(req.getParameter("userRole")));
        //注意这两个参数不在表单的填写范围内
        user.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        UserServiceImpl userService = new UserServiceImpl();
        if(userService.modify(userId,user)){
            //如果执行成功了 网页重定向到 用户管理页面(即 查询全部用户列表)
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            //说明 添加失败 转发到此 添加页面
            req.getRequestDispatcher("usermodify.jsp").forward(req,resp);
        }
    }
    //用户管理模块中的子模块 —— 查询用户信息
    public void viewUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从前端获取 要查询用户 的id
        String id = req.getParameter("uid");
        int userId = 0;
        try {
            userId = Integer.parseInt(id);
        }catch (Exception e){
            e.printStackTrace();
            userId = 0;
        }
        //调用 根据id查询用户信息的方法
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.findById(userId);
        //将此user发送到展示前端 的页面进行展示
        req.setAttribute("user",user);
        //跳转到前端 的展示页面
        req.getRequestDispatcher("userview.jsp").forward(req,resp);
    }
}
