<%@ page import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String[] colors = new String[]{"red", "blue", "green", "orange", "pink", "cyan"};
    int colorIndex = new Random().nextInt(colors.length);
    String color = colors[colorIndex];
%>
<html>
    <head>
        <title>Funny story</title>
    </head>
    <body style="color: <%= color %>">
        <h1>Funny story</h1>
        <p>
            aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
            aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
            aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
            aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        </p>
    </body>
</html>