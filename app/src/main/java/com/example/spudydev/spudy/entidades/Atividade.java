package com.example.spudydev.spudy.entidades;

import java.util.HashMap;
import java.util.Map;

public class Atividade {
    private String nome;
    private HashMap<String ,Double> notas;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private HashMap<String, Double> getNotas() {
        return notas;
    }

    public void setNotas(HashMap<String, Double> notas) {
        this.notas = notas;
    }

    public Map<String, Object> toMapAtividade() {
        HashMap<String, Object> hashMapAtividade = new HashMap<>();
        hashMapAtividade.put("nome", getNome());
        hashMapAtividade.put("notas", getNotas());
        return hashMapAtividade;
    }
}
