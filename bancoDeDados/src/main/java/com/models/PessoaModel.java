package com.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dto.PessoaDTO;
import com.models.database.DatabaseConnection;

public final class PessoaModel {
    private static final int DELAY = 2000; // 2 segundos


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
                 p.id
                ,p.nome
                ,p.idade
            FROM
                pessoa p
            ORDER BY
                p.id DESC
            """;
        
        return DatabaseConnection.runQueryForList(sql, PessoaDTO.class);
    }

    // retorna true se a adição afetou algo
    // delay para simular uma operação de longa duração
    public static boolean addPessoa(PessoaDTO pessoa) {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        String sql = """
            INSERT INTO pessoa 
            (nome, idade)
            VALUES 
            (?, ?)
            """;

        return DatabaseConnection.runCommand(sql, pessoa.getNome(), pessoa.getIdade()) > 0;
    }

    // remove uma pessoa da lista pelo ID
    // retorna true se a remoção afetou algo
    // delay para simular uma operação de longa duração
    public static boolean removePessoa(long id) {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        String sql = """
            DELETE FROM pessoa 
            WHERE id = ?
            """;
        return DatabaseConnection.runCommand(sql, id) > 0;
    }

    // atualiza uma pessoa na lista pelo ID
    // retorna true se a atualização afetou algo
    // delay para simular uma operação de longa duração
    public static boolean updatePessoa(long id, PessoaDTO pessoaAtualizada) {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        String sql = """
            UPDATE pessoa 
            SET nome = ?, idade = ?
            WHERE id = ?
            """;
        return DatabaseConnection.runCommand(sql, pessoaAtualizada.getNome(), pessoaAtualizada.getIdade(), id) > 0;
    }
}