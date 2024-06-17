<%@ page import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="hr.fer.oprpp2.jmbag0036529634.model.Poll"%>
<%@page import="hr.fer.oprpp2.jmbag0036529634.model.PollOption"%>

<%
   List<PollOption> winners = new ArrayList<>();
   List<PollOption> pollOptions = (List<PollOption>) request.getAttribute("results");

   int i = 0;
   PollOption current = pollOptions.get(i);
   Long maxVotes = current.getVotesCount();
   while(current.getVotesCount() == maxVotes) {
        winners.add(current);
        current = pollOptions.get(++i);
   }
%>
<html>
    <head>
        <title>Rezultatiii glasanja</title>
        <style type="text/css">
            table.rez td {text-align: center;}
        </style>
    </head>
    <body>
        <h1>Rezultati glasanja</h1>
        <p>Ovo su rezultati glasanja</p>

        <table border="1" class="rez" cellspacing="0">
            <tbody>
                <c:forEach items="${results}" var="opt">
                    <tr>
                        <th>${opt.optionTitle}</th>
                        <th>${opt.votesCount}</th>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <br>

        <h2>Grafički prikaz rezultata</h2>
        <img alt="Pie-chart" src="/voting-app/servleti/glasanje-grafika?pollID=${pollID}" width="400" height="400"/>

        <br>

        <h2>Rezultati u XLS formatu</h2>
        <p>Rezultati u XLS formatu dostupni su <a href="/voting-app/servleti/glasanje-xls?pollID=${pollID}">ovdje</a></p>

        <br>

        <h2>Razno</h2>
        <p>Primjeri pjesama pobjedničkih bendova:</p>
        <ul>
            <c:forEach items="<%= winners %>" var="w">
                <li><a href="${w.optionLink}" target="_blank">${w.optionTitle}</a></li>
            </c:forEach>
        </ul>
    </body>
</html>