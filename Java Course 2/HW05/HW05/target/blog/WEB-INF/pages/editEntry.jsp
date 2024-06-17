<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Edit entry</title>
        <style><%@include file="/WEB-INF/pages/css/main.css"%></style>
        <meta name='viewport' content='width=device-width, initial-scale=1'>
    </head>
    <body>
        <div class="title-container">
          <div>
              <p><b>User</b>:</p>
              <p>${sessionScope['current.user.fn']} ${sessionScope['current.user.ln']}</p>
          </div>
          <h1 class="title">Edit entry</h1>
        </div>
        <hr>
        <form  method="POST" action="/blog/servleti/editEntry?id=${entry.id}" accept-charset="UTF-8">
            <p style="color:red;">${error}</p>
            <div class="entry-form">
                <label for="title">Title:</label>
                <input name="title" required type="text" value="${entry.title}">
            </div>
            <br>
            <div class="entry-form">
                <label for="text">Write blog:</label>
                <textarea name="text" rows="25" cols="80">${entry.text}</textarea>
            </div>
            <br>
            <div class="register-buttons">
              <input type="submit" value="Edit">
              <a style="margin-left: 10px;" href="/blog/servleti/author/${entry.creator.nick}"><button type="button">Go back</button></a>
             </div>
        </form>
    </body>
</html>