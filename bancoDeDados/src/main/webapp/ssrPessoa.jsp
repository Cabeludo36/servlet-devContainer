<html>
<body>
    <a href="/bancoDeDados/index.jsp"><-- Back to Index</a>
    <br>
    <h1>Create Person</h1>
    <form action="/bancoDeDados/api/pessoassr" method="post">
        <label for="nome">Name:</label>
        <input type="text" id="nome" name="nome" required>
        <br>
        <label for="idade">Age:</label>
        <input type="number" id="idade" name="idade" required>
        <br>
        <button type="submit">Create</button>
    </form>
    <br>
    <%
        String msg = (String) request.getAttribute("msg");
        if (msg != null) {
    %>
        <div>
            <p><%= msg %></p>
        </div>
    <%
        }
    %>
    <button type="button" onclick="location.reload();">Refresh</button>
    <h1>List of People</h1>
    <ul>
        <%
            java.util.List<com.dto.PessoaDTO> pessoas = com.controllers.PessoaController.getPessoasAsync();
            for (var pessoa : pessoas) {
        %>
            <li>
                <div style="display:flex;">
                    <form action="/bancoDeDados/api/pessoassr/update/<%= pessoa.getId() %>" method="post">
                        <span>Name: <input type="text" name="nome" value="<%= pessoa.getNome() %>" required></span>
                        -
                        <span>Age: <input type="number" name="idade" value="<%= pessoa.getIdade() %>" required></span>
                        -
                        <span>Can Drink: <%= com.controllers.PessoaController.calcularPodeBeber(pessoa.getIdade()) ? "Yes" : "No" %></span>
                        -
                        <span>Lucky Number: <%= pessoa.getNumeroSorte() %></span>
                        -
                        <button type="submit">Update</button>
                    </form>
                    <form action="/bancoDeDados/api/pessoassr/delete/<%= pessoa.getId() %>" method="post" style="display:inline;">
                        <button type="submit">X</button>
                    </form>
                </div>
            </li>
        <%
            }
        %>
    </ul>
</body>
</html>
