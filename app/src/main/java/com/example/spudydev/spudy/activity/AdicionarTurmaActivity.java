package com.example.spudydev.spudy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.Turma;
import com.example.spudydev.spudy.usuario.aluno.gui.MainAlunoActivity;

public class AdicionarTurmaActivity extends AppCompatActivity {
    private EditText edtCodigoTurma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_adicionar_turma);

        Button btnAdicionarTurma = findViewById(R.id.btnAdicionarTurma);
        final EditText edtCodigoTurma = findViewById(R.id.edtCodigoTurma);

        btnAdicionarTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Turma turma = new Turma();
                turma.adicionarTurma(edtCodigoTurma.getText().toString());
                abrirTelaMainAlunoActivity();
            }
        });

    }

    public void abrirTelaMainAlunoActivity(){
        Intent intent = new Intent(this, MainAlunoActivity.class);
        startActivity(intent);
        finish();
    }
}

