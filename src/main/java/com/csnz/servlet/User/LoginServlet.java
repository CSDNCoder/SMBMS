package com.csnz.servlet.User;/*
 *
 *@create 2021-04-29-10:48
 */

import com.csnz.pojo.User;
import com.csnz.service.User.UserService;
import com.csnz.service.User.UserServiceImpl;
import com.csnz.util.Constants;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//处理登录请求的servlet
public class LoginServlet extends HttpServlet {
    //控制层 调用业务层代码 进行判断
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取登录页面传来的信息
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        //和数据库中的密码进行对比，调用业务层
        UserService userService = new UserServiceImpl();
        //把登录的人的信息查到
        User user = userService.login(userCode, userPassword);
        //判断
        if (user == null || !user.getUserPassword().equals(userPassword)) {
            //查无此人
            //转发回登录页面 提示 用户名或密码错误
            req.setAttribute("error","用户名或密码错误");

            req.getRequestDispatcher("login.jsp").forward(req,resp);

        }else{
            //查有此人
                //将用户的信息放在session中
                req.getSession().setAttribute(Constants.USER_SESSION,user);
                //跳转到主页
                resp.sendRedirect("jsp/frame.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
