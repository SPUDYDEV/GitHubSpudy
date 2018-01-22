package com.example.spudydev.spudy.usuario.professor.dominio;

import com.example.spudydev.spudy.entidades.Turma;
import com.example.spudydev.spudy.pessoa.dominio.Pessoa;

public class Professor {

    private Pessoa pessoa;
    private Turma turma;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Turma getTurma() { return turma; }

    public void setTurma(Turma turma) { this.turma = turma;  }
}
