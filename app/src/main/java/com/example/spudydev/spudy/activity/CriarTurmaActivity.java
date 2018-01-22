package com.example.spudydev.spudy.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.Turma;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.MD5;
import com.example.spudydev.spudy.usuario.professor.gui.MainProfessorActivity;

public class CriarTurmaActivity extends AppCompatActivity {

    private EditText nomeTurma;
    private EditText cargaHorariaDiaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_criar_turma);

        nomeTurma = findViewById(R.id.edtNomeTurma);
        cargaHorariaDiaria = findViewById(R.id.edtCargaHorariaDiaria);

        Button btnCriarTurma = findViewById(R.id.btnCriarTurma);
        btnCriarTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Turma turma = new Turma();
                String codigoTurma = turma.criarTurma(nomeTurma.getText().toString(), cargaHorariaDiaria.getText().toString());
                Auxiliar.criarToast(CriarTurmaActivity.this,"Codigo: " + codigoTurma);
                abrirTelaMainProfessor();
            }
        });



        //criando turma
    }
    public void abrirTelaMainProfessor(){
        Intent abrirTelaMainProfessor = new Intent(CriarTurmaActivity.this, MainProfessorActivity.class);
        startActivity(abrirTelaMainProfessor);
        finish();

    }
}
