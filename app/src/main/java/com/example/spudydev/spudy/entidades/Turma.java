package com.example.spudydev.spudy.entidades;

import android.support.annotation.NonNull;

import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.MD5;
import com.example.spudydev.spudy.usuario.dominio.Usuario;
import com.example.spudydev.spudy.usuario.professor.dominio.Professor;

import java.util.ArrayList;
import java.util.HashMap;


public class Turma {

    private String nome;
    private String cargaHoraria;
    private String cargaHorariaDiaria;
    private Professor professor;

    public String getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getCargaHorariaDiaria() {
        return cargaHorariaDiaria;
    }

    public void setCargaHorariaDiaria(String cargaHorariaDiaria) {
        this.cargaHorariaDiaria = cargaHorariaDiaria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    //Esta chamada vai ser feita pelo PROFESSOR

    private HashMap<String, Object> toMapTurma(){
        HashMap<String, Object> hashMapTurma = new HashMap<>();
        hashMapTurma.put("nomeTurma", getNome());
        hashMapTurma.put("cargaHorariaDiaria", getCargaHorariaDiaria());

        return hashMapTurma;
    }
    //Professor Cria turma
    public String criarTurma(String nomedaturma, String cargaHorariaDiaria){
        //Adicionar verificação turma com mesmo nome
        this.setNome(nomedaturma);
        this.setCargaHorariaDiaria(cargaHorariaDiaria);

        String uid = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getUid();
        String codigoTurma = MD5.hashCodigoTurma(uid, nomedaturma);

        //Salvando a turma na árvore turma
        AcessoFirebase.getFirebase().child("turma").child(codigoTurma).setValue(this.toMapTurma());
        //Salvando a turma na árvore professor
        AcessoFirebase.getFirebase().child("professor").child(uid).child("turmasMinistradas").child(codigoTurma).setValue(codigoTurma);

        return codigoTurma;
    }
    //Professor Criar turma
    public void adicionarTurma(String codigoTurma){
        //Adicionar verificação se aluno já esta na turma
        String uid = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getUid();
        AcessoFirebase.getFirebase().child("aluno").child(uid).child("turmas").child(codigoTurma).setValue(codigoTurma);
        AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("alunosDaTurma").child(uid).setValue(uid);
    }
}