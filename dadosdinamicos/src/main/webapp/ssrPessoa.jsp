<html>
<body>
    <a href="index.jsp"><-- Back to Index</a>

    <ul>
        <%
            // importando as classes necessárias com caminho completo Ex.: java.util.List
            // assim JSP entende qual classe usar
            java.util.List<com.dto.PessoaDTO> pessoas = com.controllers.PessoaController.getPessoasAsync();
            for (var pessoa : pessoas) {
        %>
            <li class="blue-text">
                <span>Name: <%= pessoa.getNome() %></span>
                -
                <span>Age: <%= pessoa.getIdade() %></span>
                - 
                <span>Can Drink: <%= com.controllers.PessoaController.calcularPodeBeber(pessoa.getIdade()) ? "Yes" : "No" %></span>

            </li>
        <%
            }
        %>
    </ul>
</body>
</html>
