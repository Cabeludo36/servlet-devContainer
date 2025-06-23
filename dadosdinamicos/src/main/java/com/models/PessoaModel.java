package com.models;

import java.util.ArrayList;
import java.util.List;

import com.dto.PessoaDTO;

public final class PessoaModel {

    // lista estática de pessoas
    // inicializada com alguns dados de exemplo
    // não irá mudar durante a execução do programa
    private static List<PessoaDTO> pessoas = new ArrayList<PessoaDTO>() {
        {
            add(new PessoaDTO("João", 30));
            add(new PessoaDTO("Maria", 25));
            add(new PessoaDTO("José", 40));
        }
    };

    // constructor privado para evitar instância
    private PessoaModel() {}

    // método para listar as pessoas
    public static List<PessoaDTO> getPessoas() {
        // delay para simular uma operação de longa duração
        try {
            Thread.sleep(2000); // 2 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // retorna pessoas
        return pessoas;
    }
}
