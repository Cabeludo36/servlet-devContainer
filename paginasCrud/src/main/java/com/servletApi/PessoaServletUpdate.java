package com.servletApi;

import java.io.PrintWriter;

import com.controllers.PessoaController;
import com.dto.PessoaDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PessoaServletUpdate", urlPatterns = {"/api/pessoassr/update/*"})
public class PessoaServletUpdate extends HttpServlet {

    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        try (PrintWriter out = res.getWriter()) {
            long id = 0;
            var msg = "";

            // Verifica se o ID foi passado na URL
            try {
                String idStr = req.getPathInfo().split("/")[1];
                id = Long.parseLong(idStr);
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                msg = "ID inválido!";
            }
            
            // Verifica se o nome e a idade foram passados no body da requisição
            String nome = req.getParameter("nome");
            String idade = req.getParameter("idade");

            PessoaDTO pessoa = new PessoaDTO(nome, Integer.parseInt(idade));


            // Atualiza a pessoa
            var sucesso = PessoaController.atualizarPessoaAsync(id, pessoa);
            if (sucesso) {
                // Responde com sucesso e status de OK
                res.setStatus(HttpServletResponse.SC_OK);
                msg = "Pessoa atualizada com sucesso!";
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                msg = "Pessoa não encontrada ou dados inválidos!";
            }

            req.setAttribute("msg", msg);
            RequestDispatcher rd = req.getRequestDispatcher("/ssrPessoa.jsp");
            rd.forward(req, res);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
        }
    }
}