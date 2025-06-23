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

    public static boolean calcularPodeBeber(int idade) {
        // Verifica se a idade é maior ou igual a 18
        return idade >= 18;
    }
}
