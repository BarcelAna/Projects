<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Register</title>
        <style><%@include file="/WEB-INF/pages/css/main.css"%></style>
        <meta name='viewport' content='width=device-width, initial-scale=1'>
    </head>
    <body>
        <div class="title-container">
             <div>
                  <p><b>User</b>:</p>
                  <p>${sessionScope['current.user.fn']} ${sessionScope['current.user.ln']}</p>
             </div>
             <h1 class="title">Register to Blogs!</h1>
        </div>
        <hr>
            <div class="log-in">
                <div class="title-container">
                    <img src="../images/userIcon.png" alt="User-icon" class="icon">
                    <h2 style="margin-left:10px;">Register</h2>
                </div>
                <hr>
                <form class="login-form" method="POST" action="/blog/servleti/createUser" accept-charset="UTF-8">
                   <div>
                        <label for="firstName">First name: </label>
                        <input type="text" name="firstName" required value=${user.firstName}>
                        <br>
                        <br>
                        <label for="lastName">Last name: </label>
                        <input type="text" name="lastName" required value=${user.lastName} >
                        <br>
                        <br>
                        <label for="email">E-mail: </label>
                        <input type="text" name="email" required value=${user.email}>
                        <br>
                        <br>
                        <label for="nick">Nickname: </label>
                        <input type="text" name="nick" required>
                        <br>
                        <c:if test="${not empty error}">
                            <p style="color:red;">${error}</p>
                        </c:if>
                        <br>
                        <label for="pass">Password: </label>
                        <input type="password" name="pass" required>
                   </div>
                   <br>
                   <br>
                   <div class="register-buttons">
                        <input type="submit" value="Submit">
                        <a href="/blog/servleti/main"><button style="margin-left: 10px;" type="button">Home</button></a>
                   <div>
                </form>
            </div>
        </div>
    </body>
</html>