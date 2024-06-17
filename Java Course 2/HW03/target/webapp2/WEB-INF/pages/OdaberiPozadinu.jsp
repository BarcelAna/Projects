<%@ page import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Odabir pozadine</title>
    </head>
    <body>
        <h1>Odaberi pozadinsku sliku</h1>
        <form method="GET" action="/webapp2/odaberiPozadinu">
        		<select name="url">
        			<option value="Barca.jpg">Barcelona grb</OPTION>
        			<option value="Gitara.png">Moja gitara</OPTION>
        		</select>
        		<br>
        		<br>
        		<input type="submit" value="Submit">
        </form>
    </body>
</html>