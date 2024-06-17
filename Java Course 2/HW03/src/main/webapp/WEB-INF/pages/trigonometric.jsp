<%@ page import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Tablica</title>
        <style>
            table, th, td {
                border: 1px solid black;
            }
        </style>
    </head>
    <body>
        <table style='border: 1px solid black'>
            <tr><th>X</th><th>sin(x)</th><th>cos(x)</th></tr>
            <c:forEach items="${angles}" var="entry">
                <tr>
                    <td><b>${entry.key}</b></td>
                    <td>${entry.value.sin}</td>
                    <td>${entry.value.cos}</td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>