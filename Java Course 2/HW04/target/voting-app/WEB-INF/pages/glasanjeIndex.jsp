<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="hr.fer.oprpp2.jmbag0036529634.model.Poll"%>
<%@page import="hr.fer.oprpp2.jmbag0036529634.model.PollOption"%>
<%@page import="java.util.List"%>

<html>
   <body>
       <h1>${poll.title}</h1>
       <p>${poll.message}</p>
       <ol>
            <c:forEach var="opt" items="${pollOptions}">
                <li>
                    <a href="/voting-app/servleti/dodaj_glas?optID=${opt.id}">${opt.optionTitle}</a>
                </li>
            </c:forEach>
       </ol>
   </body>
</html>