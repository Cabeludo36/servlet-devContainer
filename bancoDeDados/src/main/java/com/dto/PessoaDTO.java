package com.dto;

public class PessoaDTO {
    private long id;
    private String nome;
    private int idade;

    public PessoaDTO(long id, String nome, int idade) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
    }

    public PessoaDTO(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    public PessoaDTO() {
        // Construtor padrão necessário para algumas operações de reflexão
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    // gera numero randomico de 1 a 100
    public int getNumeroSorte() {
        return (int) (Math.random() * 100);
    }
}
