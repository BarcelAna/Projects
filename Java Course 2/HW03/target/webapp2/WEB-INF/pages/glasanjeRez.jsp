<%@ page import="java.util.*, hr.fer.oprpp2.beans.Band" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    Map<String, Band> map = (HashMap<String, Band>)request.getServletContext().getAttribute("results");
    List<Band> winners = new ArrayList<>();
    Comparator<Band> comparator = new Comparator<Band>() {
         @Override
         public int compare(Band b1, Band b2) {
             return Integer.compare(b2.getVotes(), b1.getVotes());
         }
    };
    TreeSet<Band> sortedBands = new TreeSet<>(comparator);
    sortedBands.addAll(map.values());
    Iterator it = sortedBands.iterator();
    int maxVotes = ((Band)(it.next())).getVotes();
    for(Band b : map.values()) {
       if(b.getVotes()==maxVotes) {
         winners.add(b);
       }
    }
%>
<html>
    <head>
        <title>Rezultati glasanja</title>
        <style type="text/css">
            table.rez td {text-align: center;}
        </style>
    </head>
    <body>
        <h1>Rezultati glasanja</h1>
        <p>Ovo su rezultati glasanja</p>
        <table border="1" class="rez" cellspacing="0">
            <thead><tr><th>Bend</th><th>Broj glasova</th></thead>
            <tbody>
                <c:forEach items="${results}" var="r">
                    <tr>
                        <th>${r.value.name}</th>
                        <th>${r.value.votes}</th>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <br>
        <h2>Grafički prikaz rezultata</h2>
        <img alt="Pie-chart" src="/webapp2/glasanje-grafika" width="400" height="400"/>
        <br>
        <h2>Rezultati u XLS formatu</h2>
        <p>Rezultati u XLS formatu dostupni su <a href="/webapp2/glasanje-xls">ovdje</a></p>
        <br>
        <h2>Razno</h2>
        <p>Primjeri pjesama pobjedničkih bendova:</p>
        <ul>
            <c:forEach items="<%= winners %>" var="w">
                <li><a href="${w.url}" target="_blank">${w.name}</a></li>
            </c:forEach>
        </ul>
    </body>
</html>