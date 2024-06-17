<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Create entry</title>
        <style><%@include file="/WEB-INF/pages/css/main.css"%></style>
        <meta name='viewport' content='width=device-width, initial-scale=1'>
    </head>
    <body>
        <div class="title-container">
            <div>
                   <p><b>User</b>:</p>
                   <p>${sessionScope['current.user.fn']} ${sessionScope['current.user.ln']}</p>
            </div>
            <h1 class="title">Write new blog entry</h1>
        </div>
        <hr>
        <form action="/blog/servleti/createEntry/${user.nick}" method="POST">
        <p style="color:red;">${error}</p>
         <div class="entry-form">
             <label for="title">Title:</label>
             <input name="title" required type="text" value="${title}">
         </div>
         <br>
        <div class="entry-form">
            <label for="text">Write blog:</label>
            <textarea name="text" rows="25" cols="80"></textarea>
        </div>
        <br>
         <div class="register-buttons">
              <input type="submit" value="Create">
              <a style="margin-left: 10px;" href="/blog/servleti/author/${user.nick}"><button type="button">Go back</button></a>
         </div>
       </form>
    </body>
</html>