// pacote que contém a classe ServletHello
package com.servletApi;

// importações necessárias para o servlet
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletHello extends HttpServlet {

    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {

        res.setContentType("text/html"); 
        res.setCharacterEncoding("UTF-8"); 
        
        try { 
            
            PrintWriter out = res.getWriter(); 
            out.println("<html>"); 
            out.println("<head><title>Servlet Hello</title></head>"); 
            out.println("<body>"); 
            out.println("<h1>Hello, Servlet!</h1>");
            // Loop para escrever 5 parágrafos no corpo do HTML 
            for (int i = 0; i < 5; i++) { 
                out.println("<p>This is a simple servlet example "+i+".</p>"); // escreve um parágrafo no corpo do HTML
            }
            out.println("</body>"); 
            out.println("</html>"); 
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
