<html>
<body>
    <a href="index.jsp"><-- Back to Index</a>
    <%
        for(int i = 0; i < 5; i++) {
    %>
            <p class="green-text">Line <%= i + 1 %></p>
    <%
        }
    %>
</body>
</html>
