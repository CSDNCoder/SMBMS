<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page import="com.csnz.util.Constants" %>
<%@ page import="com.csnz.pojo.User" %>
<%@include file="/jsp/common/head.jsp"%>

<div class="right">
    <img class="wColck" src="../images/clock.jpg" alt=""/>
    <div class="wFont">
        <h2>${ userSession.userName }</h2>
        <p>欢迎来到潮汕奴仔超市管理系统!</p>
    </div>
</div>
</section>
<%@include file="/jsp/common/foot.jsp" %>
