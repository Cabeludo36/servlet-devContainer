package com.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dto.PessoaDTO;
import com.models.database.DatabaseConnection;

public final class PessoaModel {
    private static final int DELAY = 2000; // 2 segundos
    private static long idPessoaAtual = 3; // ID inicial para a próxima pessoa

    // lista estática de pessoas
    // inicializada com alguns dados de exemplo
    // não irá mudar durante a execução do programa
    private static List<PessoaDTO> pessoas = new ArrayList<PessoaDTO>() {
        {
            add(new PessoaDTO(1, "João", 30));
            add(new PessoaDTO(2, "Maria", 25));
            add(new PessoaDTO(3, "José", 40));
        }
    };

    // constructor privado para evitar instância
    private PessoaModel() {
    }

    // método para listar as pessoas
    public static List<PessoaDTO> getPessoas() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String sql = """
            SELECT 
                p.id,
                p.nome,
                p.idade
            FROM 
                pessoa p
            ORDER BY
                p.id DESC
            """;

        // retorna pessoas
        return DatabaseConnection.runQueryForList(sql, PessoaDTO.class);
    }

    // retorna true se a adição for bem-sucedida
    // delay para simular uma operação de longa duração
    public static boolean addPessoa(PessoaDTO pessoa) {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // incrementa o ID atual
        idPessoaAtual++;
        // adiciona um ID único à pessoa
        pessoa.setId(idPessoaAtual);
        return pessoas.add(pessoa);
    }

    // remove uma pessoa da lista pelo ID
    // retorna true se a remoção for bem-sucedida
    // delay para simular uma operação de longa duração
    public static boolean removePessoa(long id) {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // busca a pessoa pelo ID e remove
        return pessoas.removeIf(p -> p.getId() == id);
    }

    // atualiza uma pessoa na lista pelo ID
    // retorna true se a atualização for bem-sucedida
    // delay para simular uma operação de longa duração
    public static boolean updatePessoa(long id, PessoaDTO pessoaAtualizada) {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        // busca a pessoa pelo ID e atualiza
        Optional<PessoaDTO> pessoaOpt = pessoas.stream().filter(p -> p.getId() == id).findFirst();
        if (pessoaOpt.isPresent()) {
            PessoaDTO pessoa = pessoaOpt.get();
            pessoa.setNome(pessoaAtualizada.getNome());
            pessoa.setIdade(pessoaAtualizada.getIdade());
            return true;
        }
        return false;
    }
}