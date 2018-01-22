package com.example.spudydev.spudy.pessoa.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.registro.negocio.VerificaConexao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class AlterarInstituicaoActivity extends AppCompatActivity {

    private EditText edt_alterarInstituicao;
    private VerificaConexao verificaConexao;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String tipoConta = getIntent().getStringExtra("tipoConta");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_instituicao);

        edt_alterarInstituicao = findViewById(R.id.edt_AlterarInstituicao);
        Button btn_alterarInstituicao = findViewById(R.id.btn_AlterarInstituicao);
        verificaConexao = new VerificaConexao(this);

        btn_alterarInstituicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampo()) {
                        alterarInstituicao();
                        Toast.makeText(AlterarInstituicaoActivity.this, R.string.sp_alterar_instituicao_sucesso, Toast.LENGTH_SHORT).show();
                        abrirTelaMeuPerfilActivity();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean verificaCampo(){
        if (edt_alterarInstituicao.getText().toString().isEmpty()){
            edt_alterarInstituicao.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }
        return true;
    }
    private void alterarInstituicao() {
        AcessoFirebase.getFirebase().child("usuario").child(user.getUid()).child("instituicao").setValue(edt_alterarInstituicao.getText().toString());
    }

    public void abrirTelaMeuPerfilActivity (){
        if (tipoConta.equals("aluno")) {
            Intent intent = new Intent(this, MeuPerfilAlunoActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, MeuPerfilProfessorActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
