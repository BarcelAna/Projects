<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Blogs</title>
        <style><%@include file="/WEB-INF/pages/css/main.css"%></style>
        <meta name='viewport' content='width=device-width, initial-scale=1'>
    </head>
    <body>
        <div class="title-container">
            <div>
                   <p><b>User</b>:</p>
                   <p>${sessionScope['current.user.fn']} ${sessionScope['current.user.ln']}</p>
             </div>
            <h1 class="title">Welcome to Blogs!</h1>
        </div>
        <hr>
        <div class="center-container">
            <div class="authors">
                <h2>Authors:</h2>
                <hr>
                <ul>
                    <c:forEach var="author" items="${authors}">
                        <li><a href="/blog/servleti/author/${author.nick}">${author.nick}</a></li>
                        <br>
                    </c:forEach>
                </ul>
            </div>
            <div class="log-in">
                <c:choose>
                    <c:when test="${empty sessionScope['current.user.id']}">
                       <h2>Log in</h2>
                       <form class="login-form" action="/blog/servleti/login" method="POST">
                           <div>
                               <label for="nick">Nickname: </label>
                               <input type="text" name="nick" value="${nick}" required>
                               <br>
                               <br>
                               <label for="pass">Password: </label>
                               <input type="password" name="pass" required>
                               <p style="color:red;">${error}</p>
                           </div>
                           <br>
                           <div class="login-form">
                              <input type="submit" value="Submit">
                              <p>Not registered yet? <a href="/blog/servleti/register">Register</a></p>
                           <div>
                       </form>
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/images/userIcon.png" alt="edit-icon" class="icon">
                        <p><b>First name</b>: ${sessionScope["current.user.fn"]}</p>
                        <p><b>Last name</b>: ${sessionScope["current.user.ln"]}</p>
                        <p><b>Nickname</b>: ${sessionScope["current.user.nick"]}</p>
                        <a href="/blog/servleti/logout"><button type="button">Log out</button></a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </body>
</html>