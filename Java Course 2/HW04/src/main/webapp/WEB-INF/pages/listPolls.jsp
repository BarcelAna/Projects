<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.oprpp2.jmbag0036529634.model.Poll"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Polls</title>
    </head>
    <body>
        <h1>Polls</h1>
        <ol>
            <c:forEach items="${polls}" var="poll">
                <li>
                    <a href="/voting-app/servleti/glasanje?pollID=${poll.id}">${poll.title}</a>
                </li>
            </c:forEach>
        </ol>
    </body>
</html>