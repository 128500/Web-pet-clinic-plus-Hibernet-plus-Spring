<%--
  Created by IntelliJ IDEA.
  User: KUDIN ALEKSANDR
  Date: 26.08.2017
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session ="true" %>
<html>
<head>
    <title>VIEW MESSAGES</title>
    <link href="${pageContext.servletContext.contextPath}/views/css/viewMessagesStyle.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${pageContext.servletContext.contextPath}/views/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.servletContext.contextPath}/views/js/viewUserScript.js"></script>
</head>

<body>

<div id="header">
    <img src="${pageContext.servletContext.contextPath}/views/images/logo.png" alt="OMG" id="logo_img">
    <div id="header_sign">
        <div id="purple">WEB</div>
        <div id="gray">clinic</div>
    </div>
</div>

<div id="sidebar">
    <a href="${pageContext.servletContext.contextPath}/admin/view_users" id="refViewUser">&raquo; Return to main page</a>
    <a href="${pageContext.servletContext.contextPath}/user/create_account" id="refAddUser">&raquo; Add user into the database</a>
    <a href="${pageContext.servletContext.contextPath}/views/pages/user/find_user.jsp" id="refFindUser">&raquo; Find user or pet</a>
</div>


<div id="content">

<table>
    <%--@elvariable id="messages" type="java.util.ArrayList"--%>
    <c:forEach items="${messages}" var="message" varStatus="status">
        <tr><td>${message.getText()}</td></tr>
    </c:forEach>
</table>
</div>

<div id="footer">
    <p>FOOTER</p>
    <div id="hint"></div>
</div>
</body>
</html>