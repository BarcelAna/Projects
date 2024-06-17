<%@ page import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Trenutno vrijeme</title>
        <style>
            body {
                background-image: url(${bgUrl})
            }
        </style>
    </head>
    <body>
        <p>Trenutno vrijeme je: ${currentTime}
        <a href="/webapp2/prikaziOdabir">Choose background</a>
    </body>
</html>