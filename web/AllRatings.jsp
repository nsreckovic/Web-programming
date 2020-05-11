<%--
  Created by IntelliJ IDEA.
  User: Nikola
  Date: 29-Apr-20
  Time: 21:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>All ratings</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div id="rate-again">
    <c:choose>
        <c:when test="${empty requestScope.ratings}">
            <div id="noRatingsInfo" class="container">
                <h1 class="heading">
                    There are currently <span class="text-primary">no ratings</span>.
                </h1>
                <br>
                If you want to rate assistants go <a href="/">here</a>.
            </div>
        </c:when>

        <c:otherwise>
            <div class="container">
                <h1 class="heading">
                    Average assistant
                    <span class="text-primary">ratings</span>:
                </h1>

                <br>

                <table class="table-fill">
                    <thead>
                    <tr>
                        <th>Assistant</th>
                        <th>Average rating</th>
                    </tr>
                    </thead>
                        <tbody class="table-hover">
                        <c:forEach items="${requestScope.ratings}" var="rating">
                            <c:choose>
                                <c:when test="${rating.name == 'ALEKSANDAR'}">
                                    <tr>
                                        <td>${rating.name}</td>
                                        <td>
                                            <fmt:formatNumber pattern="0.00" value="${0}"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td>${rating.name}</td>
                                        <td>
                                            <fmt:formatNumber pattern="0.00" value="${rating.average}"/>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        </tbody>
                </table>

                <br>

                Wanna rate again? Go <a href="/">here</a>.
            </div>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>
</html>
</html>
