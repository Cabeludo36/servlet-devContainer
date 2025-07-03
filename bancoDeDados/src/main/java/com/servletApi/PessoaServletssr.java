package com.servletApi;

import java.io.PrintWriter;

import com.controllers.PessoaController;
import com.dto.PessoaDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PessoaServletssr", urlPatterns = {"/api/pessoassr"})
public class PessoaServletssr extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {

        try (PrintWriter out = res.getWriter()) {
            String nome = req.getParameter("nome");
            String idade = req.getParameter("idade");

            PessoaDTO pessoa = new PessoaDTO(nome, Integer.parseInt(idade));

            // Adiciona a pessoa
            var sucesso = PessoaController.addPessoaAsync(pessoa);
            var msg = "";
            if (sucesso) {
                // Responde com sucesso e status de criado
                res.setStatus(HttpServletResponse.SC_CREATED);
                msg = "Pessoa adicionada com sucesso!";
                
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                msg = "Erro ao adicionar pessoa!";
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
