<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Color Chooser</title>
        <style>
            body {
                background-color: ${pickedBgColor};
            }
        </style>
    </head>
    <body>
        <h1>Choose background color:</h1>
        <a href="/webapp2/setcolor?color=white">WHITE</a>
        <a href="/webapp2/setcolor?color=red">RED</a>
        <a href="/webapp2/setcolor?color=green">GREEN</a>
        <a href="/webapp2/setcolor?color=cyan">CYAN</a>
    </body>
</html>