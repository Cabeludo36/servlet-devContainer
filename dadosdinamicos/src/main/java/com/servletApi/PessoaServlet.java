package com.servletApi;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import com.controllers.PessoaController;
import com.dto.PessoaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

            // mapeia valores para json
            String json = mapper.writeValueAsString(
                // faz mapeamento dinamico das nossas propriedades
                // dessa forma não precisamos criar uma classe apenas para
                // satisfazer um retorno com o atributo "podeBeber"
                // fazemos isso com o metodo .stream().map() onde haverá 
                // um loop por cada pessoa criando um objeto dinamico
                // com a propriedade "podeBeber"
                // !Nota: poderiamos ter feito isso com uma class ou record tambem!
                pessoas.stream().map(pessoa -> { 
                    // map para fazer mapeamento dinamico
                    ObjectNode obj = mapper.createObjectNode();

                    // adiciona as propriedades
                    obj.put("nome", pessoa.getNome());
                    obj.put("idade", pessoa.getIdade());
                    obj.put("podeBeber", PessoaController.calcularPodeBeber(pessoa.getIdade()));

                    // retorna da função map o objeto de resposta da pessoa
                    return obj;
                })
                // coleta dados do map como lista e fecha o mapeamento para JSON
                .collect(Collectors.toList())
            ); // converte a lista de pessoas para JSON no ultimo )

            out.print(json); // escreve o JSON na resposta
            out.flush(); // garante que todos os dados sejam enviados ao cliente
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
