package com.csnz.filter;/*
 *
 *@create 2021-05-02-23:19
 */

import com.csnz.pojo.User;
import com.csnz.util.Constants;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        //过滤器 从session中获取用户 判断是否登录
        if (user == null) {
            //证明未登录 或 已注销
            response.sendRedirect(request.getContextPath()+"/error.jsp");
        }else{
            filterChain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }
}
