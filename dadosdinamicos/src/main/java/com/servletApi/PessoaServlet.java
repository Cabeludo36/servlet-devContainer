package com.servletApi;

import java.io.PrintWriter;
import java.util.List;

import com.controllers.PessoaController;
import com.dto.PessoaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PessoaServlet", urlPatterns = {"/api/pessoa"})
public class PessoaServlet extends HttpServlet {
    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        List<PessoaDTO> pessoas = PessoaController.getPessoasAsync();
        
        // utilizar o PrintWriter desta forma garante que o recurso será fechado corretamente
        // e evita vazamentos de recursos, mesmo que ocorra uma exceção.
        // O bloco try-with-resources é uma boa prática para garantir que o PrintWriter seja
        // fechado automaticamente após o uso, evitando a necessidade de fechá-lo manualmente.
        try(PrintWriter out = res.getWriter()) {
            ObjectMapper mapper = new ObjectMapper(); // objeto de mapeamento JSON
            String json = mapper.writeValueAsString(pessoas); // converte a lista de pessoas para JSON
            out.print(json); // escreve o JSON na resposta
            out.flush(); // garante que todos os dados sejam enviados ao cliente
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
