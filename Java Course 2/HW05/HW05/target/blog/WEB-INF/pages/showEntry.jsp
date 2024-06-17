<%@ page contentType="text/html; charset=UTF-8" import="hr.fer.oprpp2.model.BlogUser" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    Object currentUserObj = session.getAttribute("current.user.nick");
    String currentUser = "";
    if(currentUserObj==null) {
        currentUser = "anonymous";
    } else {
        currentUser = (String) currentUserObj;
    }
%>
<html>
    <head>
        <title>${entry.title}</title>
        <style><%@include file="/WEB-INF/pages/css/main.css"%></style>
        <meta name='viewport' content='width=device-width, initial-scale=1'>
    </head>
    <body>
        <div class="title-container">
           <div>
                <p><b>User</b>:</p>
                <p>${sessionScope['current.user.fn']} ${sessionScope['current.user.ln']}</p>
           </div>
           <h1 class="title">${entry.title}</h1>
        </div>
        <hr>
        <h3>Author: ${entry.creator.nick}</h3>
        <div class="blog-content">${entry.text}</div>
        <p style="color: grey;">Created: ${entry.createdAt} </p>
        <p style="color: grey">Last modified: ${entry.lastModifiedAt} </p>
        <div class="buttons-container">
           <c:if test="${not empty sessionScope['current.user.nick'] && sessionScope['current.user.nick'].equals(user.nick)}">
                <a style="margin-left:20px;" href="/blog/servleti/author/${user.nick}/edit?id=${entry.id}"><img src="${pageContext.request.contextPath}/images/edit.png" alt="edit-icon" class="icon-small"></a>
           </c:if>
           <a href="/blog/servleti/author/${entry.creator.nick}"><button style="margin-left: 10px;" type="button">Go back</button></a>
        <div>
        <br>
        <br>
        <h2>Comments:</h2>
        <hr>
        <div class="comment">
            <form action="/blog/servleti/addComment?id=${entry.id}" method="POST">
                <p style="margin-left:10px;"><b>User:</b><%= currentUser %></p>
                <input style="margin-left: 30px;"type="text" name="new_comment" value="Add new comment..." style="width=400px;">
                <input style="margin:10px;" type="submit" value="Add comment">
                <input type="hidden" name="userEmail" value= <%= currentUser %> >
            </form>
        </div>
        <c:forEach items="${entry.comments}" var="com">
            <div class="comment">
                <img src="${pageContext.request.contextPath}/images/userIcon.png" alt="edit-icon" class="icon">
                <p style="margin-left:10px;"><b>User:</b>${com.usersEMail}</p>
                <p style="margin-left:30px;">${com.message}</p>
                <p style="color:grey; margin-left:30px">Created: ${com.postedOn}</p>
            </div>
        </c:forEach>
    </body>
</html>