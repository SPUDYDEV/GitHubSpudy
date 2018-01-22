package com.example.spudydev.spudy.entidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 08/01/2018.
 */

public class Disciplina {

    private String nome;
    private ArrayList<Material> materiais;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Material> getMateriais() {
        return materiais;
    }

    public void setMateriais(ArrayList<Material> materiais) {
        this.materiais = materiais;
    }

    public Map<String, Object> toMapDisciplina() {
        HashMap<String, Object> hashMapDisciplina = new HashMap<>();
        hashMapDisciplina.put("nome", getNome());
        hashMapDisciplina.put("materiais", getMateriais());
        return hashMapDisciplina;
    }
}
