package com.controllers;

import java.util.List;

import com.dto.PessoaDTO;
import com.models.PessoaModel;

// 
public final class PessoaController {

    public static List<PessoaDTO> getPessoasAsync() {
        // Chama o método estático da PessoaModel para obter a lista de pessoas
        var pessoas = PessoaModel.getPessoas();

        return pessoas;
    }

    public static Boolean addPessoaAsync(PessoaDTO pessoa) {
        var resultadoValidacao = validarPessoa(pessoa);

        if (!resultadoValidacao) {
            return false; // Pessoa inválida
        }

        var sucesso = PessoaModel.addPessoa(pessoa);
        return sucesso;
    }

    public static boolean removePessoaAsync(long id) {
        // Chama o método estático da PessoaModel para remover a pessoa pelo ID
        return PessoaModel.removePessoa(id);
    }

    public static boolean calcularPodeBeber(int idade) {
        // Verifica se a idade é maior ou igual a 18
        return idade >= 18;
    }

    public static boolean atualizarPessoaAsync(long id, PessoaDTO pessoa) {
        // Verifica se a pessoa é válida
        if (!validarPessoa(pessoa)) {
            return false; // Pessoa inválida
        }

        // atualiza a pessoa pelo ID
        var sucesso = PessoaModel.updatePessoa(id, pessoa);
        return sucesso;
    }

    private static boolean validarPessoa(PessoaDTO pessoa) {
        // Verifica se o nome não é nulo ou vazio
        if (pessoa.getNome() == null || pessoa.getNome().isEmpty()) {
            return false;
        }
        // Verifica se a idade é maior ou igual a 0
        if (pessoa.getIdade() < 0) {
            return false;
        }
        return true;
    }
}
