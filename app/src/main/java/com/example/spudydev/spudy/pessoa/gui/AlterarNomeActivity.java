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

public class AlterarNomeActivity extends AppCompatActivity {
    private EditText edt_alterarNome;
    private Button btn_alterarNome;
    private VerificaConexao verificaConexao;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String tipoConta = getIntent().getStringExtra("tipoConta");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_nome);

        edt_alterarNome = (EditText) findViewById(R.id.edt_AlterarNome);
        btn_alterarNome = (Button) findViewById(R.id.btn_AlterarNome);
        verificaConexao = new VerificaConexao(this);

        btn_alterarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampo()) {
                        alterarNome();
                        Toast.makeText(AlterarNomeActivity.this, "Nome alterado com sucesso.", Toast.LENGTH_SHORT).show();
                        abrirTelaMeuPerfilActivity();
                    }
                } else {
                    Toast.makeText(AlterarNomeActivity.this, R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean verificaCampo(){
        if (edt_alterarNome.getText().toString().isEmpty()){
            edt_alterarNome.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }
        return true;
    }
    private void alterarNome() {
        AcessoFirebase.getFirebase().child("pessoa").child(user.getUid()).child("nome").setValue(edt_alterarNome.getText().toString());
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
