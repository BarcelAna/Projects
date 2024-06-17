<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    Object startTimeObj = request.getServletContext().getAttribute("startT");
    Long startTime = (Long) startTimeObj;
    Long currentTime = System.currentTimeMillis();
    Long diff = currentTime - startTime;
    long hours = diff / (3600000);
    long minutes = (diff-hours*3600000) / (60000);
    long seconds = (diff - hours*3600000 - minutes*60000) / 1000;
    String runningFor = hours + " h " + minutes + " min " + seconds + " s";
%>
<html>
    <head>
        <title>App Info</title>
    </head>
    <body>
        <h1> This app is running for: <%= runningFor %> </h1>
    </body>
</html>