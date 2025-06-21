// pacote que contém a classe ServletHello
package com.servletApi;

// importações necessárias para o servlet
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletHello extends HttpServlet {

    // este metodo é chamado quando o servlet recebe uma requisição HTTP GET
    // onde HttpServletRequest req contém informações sobre a requisição
    // e HttpServletResponse res é usado para enviar uma resposta ao cliente
    @Override // sobrescreve metodo da classe base HttpServlet
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {

        res.setContentType("text/html"); // define o tipo de conteúdo da resposta como HTML
        res.setCharacterEncoding("UTF-8"); // define a codificação de caracteres da resposta como UTF-8
        
        try { // bloco try para capturar possíveis exceções de E/S
            PrintWriter out = res.getWriter(); // obtém um PrintWriter para escrever a resposta
            out.println("<html>"); // inicia o documento HTML
            out.println("<head><title>Servlet Hello</title></head>"); // define o título da página
            out.println("<body>"); // inicia o corpo do documento HTML
            out.println("<h1>Hello, Servlet!</h1>"); // escreve um cabeçalho no corpo do HTML
            out.println("<p>This is a simple servlet example.</p>"); // escreve um parágrafo no corpo do HTML
            out.println("</body>"); // fecha o corpo do documento HTML
            out.println("</html>"); // fecha o documento HTML
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
