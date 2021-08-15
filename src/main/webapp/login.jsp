<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>系统登录 - 深职院卜蜂莲花超市管理系统</title>
    <link type="text/css" rel="stylesheet" href="css/style.css" />
    <script type="text/javascript">
	/* if(top.location!=self.location){
	      top.location=self.location;
	 } */
    </script>
</head>
<body class="login_bg">

<%--    <%--%>
<%--        Object attribute = request.getAttribute("error");--%>
<%--    %>--%>

    <section class="loginBox">
        <header class="loginHeader">
            <h1>潮汕奴仔超市管理系统</h1>
        </header>
        <section class="loginCont">
	        <form class="loginForm" action="${pageContext.request.contextPath}/login.do"  name="actionForm" id="actionForm"  method="post" >
<%--				<div class="info">${error}</div>--%>
				<div class="inputbox">
                    <label for="userCode">用户名</label>
					<input type="text" class="input-text" id="userCode" name="userCode" placeholder="请输入用户名" required/>
				</div>
				<div class="inputbox">
                    <label for="userPassword">密码</label>
                    <input type="password" id="userPassword" name="userPassword" placeholder="请输入密码" required/>
                </div>
                <div class="info" style="text-align: center;">${error}</div>
				<div class="subBtn">
					
                    <input type="submit" value="登录"/>
                    <input type="reset" value="重置"/>
                </div>	
			</form>
        </section>
    </section>
</body>
</html>
