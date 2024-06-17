<%@ page contentType="text/html; charset=UTF-8" import="hr.fer.oprpp2.model.BlogUser" pageEncoding="UTF-8" session="true"%>
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
            <h1 class="title">Blogs from ${user.nick}</h1>
         </div>
        <hr>
        <div class="log-in">
            <c:choose>
                <c:when test="${empty user.entries}" >
                    <h2>${user.nick} does not have entries yet.</h2>
                </c:when>
                <c:otherwise>
                 <h2>Blogs:</h2>
                 <c:forEach var="entry" items="${user.entries}">
                     <div class="register-buttons">
                         <a href="/blog/servleti/author/${user.nick}/${entry.id}"><p>${entry.title}</p></a>
                         <c:if test="${not empty sessionScope['current.user.nick'] && sessionScope['current.user.nick'].equals(user.nick)}">
                              <a style="margin-left:20px;" href="/blog/servleti/author/${user.nick}/edit?id=${entry.id}"><img src="${pageContext.request.contextPath}/images/edit.png" alt="edit-icon" class="icon-small"></a>
                         </c:if>
                     </div>
                 </c:forEach>
                  <br>
                  <br>
                </c:otherwise>
            </c:choose>
            <c:if test="${not empty sessionScope['current.user.nick'] && sessionScope['current.user.nick'].equals(user.nick)}">
                                   <a href="/blog/servleti/author/${user.nick}/new"><img src="${pageContext.request.contextPath}/images/add.png" alt="edit-icon" class="icon-small"></a>
            </c:if>
            <div class="register-buttons">
                <a href="/blog/servleti/main"><button style="margin-left: 10px;" type="button">Home</button></a>
            <div>
        </div>
    </body>
</html>