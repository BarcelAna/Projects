<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Color Chooser</title>
        <style>
            body {
                background-color: {$ session.pickedBgColor $}
            }
        </style>
    </head>
    <body>
        <h1>Choose background color:</h1>
        <a href="/webapp2/setColor?color=#ffffff">WHITE</a>
        <a href="/webapp2/setColor?color=#ff0000">RED</a>
        <a href="/webapp2/setColor?color=#00ff00">GREEN</a>
        <a href="/webapp2/setColor?color=#00ffff">CYAN</a>
    </body>
</html>