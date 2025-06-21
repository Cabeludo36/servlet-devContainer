package com.webinterface;

import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

public class ApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // This class can be extended to handle API requests
    // You can implement doGet, doPost, etc. methods here

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h1>API Endpoint</h1>");
        out.println("veja mais em: <a href='./api/getMessage'>./api/getMessage</a>");
                
    }

}
