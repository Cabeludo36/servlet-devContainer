package com.servletApi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import com.controllers.PessoaController;
import com.dto.PessoaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PessoaServlet", urlPatterns = {"/api/pessoa/*"})
public class PessoaServlet extends HttpServlet {
    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        List<PessoaDTO> pessoas = PessoaController.getPessoasAsync();
        
        try(PrintWriter out = res.getWriter()) {
            ObjectMapper mapper = new ObjectMapper();

            // mapeia valores para json
            String json = mapper.writeValueAsString(
                pessoas.stream().map(pessoa -> { 
                    ObjectNode obj = mapper.createObjectNode();

                    // adiciona as propriedades
                    obj.put("nome", pessoa.getNome());
                    obj.put("idade", pessoa.getIdade());
                    obj.put("podeBeber", PessoaController.calcularPodeBeber(pessoa.getIdade()));
                    obj.put("numeroSorte", pessoa.getNumeroSorte());
                    obj.put("id", pessoa.getId());

                    return obj;
                })
                .collect(Collectors.toList())
            ); 

            out.print(json);
            out.flush(); 
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        try (PrintWriter out = res.getWriter()) {
            ObjectMapper mapper = new ObjectMapper();

            PessoaDTO pessoa;
            try {
                var jsonNode = mapper.readTree(req.getReader());
                String nome = jsonNode.get("nome").asText();
                int idade = jsonNode.get("idade").asInt();
                pessoa = new PessoaDTO(nome, idade);
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"message\": \"Erro ao processar os dados!\"}");
                out.flush();
                return;
            }

            // Adiciona a pessoa
            var sucesso = PessoaController.addPessoaAsync(pessoa);
            var mensagem = "";
            if (sucesso) {
                // Responde com sucesso e status de criado
                res.setStatus(HttpServletResponse.SC_CREATED);
                mensagem = "Pessoa adicionada com sucesso!";
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mensagem = "Erro ao adicionar pessoa!";
            }
            out.print("{\"message\": \"" + mensagem + "\"}");
            out.flush();
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        long id = 0;
        var msg = "";

        try (PrintWriter out = res.getWriter()) {
            // Verifica se o ID foi passado na URL
            try {
                String idStr = req.getPathInfo().split("/")[1];
                id = Long.parseLong(idStr);
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                msg = "ID inválido!";
            }

            boolean sucesso = PessoaController.removePessoaAsync(id);

            if (sucesso) {
                res.setStatus(HttpServletResponse.SC_OK);
                msg = "Pessoa removida com sucesso!";
            } else {
                msg = "Pessoa não encontrada!"; 
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            out.print("{\"message\": \"" + msg + "\"}");
            out.flush();
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        long id = 0;
        var msg = "";

        ObjectMapper mapper = new ObjectMapper();

        

        try (PrintWriter out = res.getWriter()) {
            // Verifica se o ID foi passado na URL
            try {
                String idStr = req.getPathInfo().split("/")[1];
                id = Long.parseLong(idStr);
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                msg = "ID inválido!";
            }

            PessoaDTO pessoa;
            try {
                var jsonNode = mapper.readTree(req.getReader());
                String nome = jsonNode.get("nome").asText();
                int idade = jsonNode.get("idade").asInt();
                pessoa = new PessoaDTO(nome, idade);
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"message\": \"Erro ao processar os dados!\"}");
                out.flush();
                return;
            }

            boolean sucesso = PessoaController.atualizarPessoaAsync(id, pessoa);

            if (sucesso) {
                res.setStatus(HttpServletResponse.SC_OK);
                msg = "Pessoa atualizada com sucesso!";
            } else {
                msg = "Pessoa não encontrada ou dados inválidos!";
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            out.print("{\"message\": \"" + msg + "\"}");
            out.flush();
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
