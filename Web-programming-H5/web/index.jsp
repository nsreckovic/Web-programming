<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>JSP Example</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="css/style.css">
</head>

<body>

    <section id="data-form" class="py-3">
        <div class="container">
            <h1 class="heading">
                Add <span class="text-primary">Data</span>
            </h1>
            <p>Please fill out the form below:</p>
            <form method="post" action="/">
                <div class="form-group">
                    <label for="scope">Scope:</label>
                    <select id="scope" name="scope">
                        <option value="REQUEST">REQUEST</option>
                        <option value="SESSION">SESSION</option>
                        <option value="APPLICATION">APPLICATION</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="key">Key:</label>
                    <input type="text" name="key" id="key" required="required">
                </div>
                <div class="form-group">
                    <label for="value">Value:</label>
                    <input type="text" name="value" id="value" required="required"/>
                </div>
                <button type="submit" class="btn">Submit</button>
            </form>
        </div>

        <br><br>

        <%
        if (request.getAttribute("requestMap") == null) request.setAttribute("requestMap", new HashMap<>());
        if (session.getAttribute("sessionMap") == null) session.setAttribute("sessionMap", new HashMap<>());
        if (application.getAttribute("applicationMap") == null) application.setAttribute("applicationMap", new HashMap<>());

        String scope = request.getParameter("scope");
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        String jsessionid = request.getSession().getId();

        HashMap requestMap = (HashMap) request.getAttribute("requestMap");
        HashMap sessionMap = (HashMap) session.getAttribute("sessionMap");
        HashMap applicationMap = (HashMap) application.getAttribute("applicationMap");

        if (key != null) {
            switch (scope) {
                case "REQUEST":     requestMap.put(key, value);     break;
                case "SESSION":     sessionMap.put(key, value);     break;
                case "APPLICATION": applicationMap.put(key, value); break;
            }
        }

        if (!requestMap.isEmpty()) { %>
            <div class="container">
                <h2><span class="text-primary">Request</span> data</h2>
                <table class="table-fill">
                    <thead>
                        <tr>
                            <th>Key</th>
                            <th>Value</th>
                        </tr>
                    </thead>
                    <tbody class="table-hover">
                        <%
                        Iterator it = requestMap.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next(); %>
                            <tr>
                                <td><%= pair.getKey() %></td>
                                <td><%= pair.getValue() %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <br>
        <% } %>


        <% if (!sessionMap.isEmpty()) { %>
            <div class="container">
                <h2><span class="text-primary">Session</span> data</h2>
                <table class="table-fill">
                    <thead>
                    <tr>
                        <th>Key</th>
                        <th>Value</th>
                        <th>SessionID</th>
                    </tr>
                    </thead>
                    <tbody class="table-hover">
                        <%
                        Iterator it = sessionMap.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next(); %>
                            <tr>
                                <td><%= pair.getKey() %></td>
                                <td><%= pair.getValue() %></td>
                                <td><%= jsessionid %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <br>
        <% } %>


        <% if (!applicationMap.isEmpty()) { %>
            <div class="container">
                <h2><span class="text-primary">Application</span> data</h2>
                <table class="table-fill">
                    <thead>
                    <tr>
                        <th>Key</th>
                        <th>Value</th>
                    </tr>
                    </thead>
                    <tbody class="table-hover">
                        <%
                        Iterator it = applicationMap.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next(); %>
                            <tr>
                                <td><%= pair.getKey() %></td>
                                <td><%= pair.getValue() %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <br>
        <% } %>

    </section>
</body>
</html>