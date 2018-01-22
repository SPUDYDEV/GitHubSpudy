package com.example.spudydev.spudy.pessoa.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.registro.negocio.VerificaConexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlterarSenhaActivity extends AppCompatActivity {

    private EditText edt_alterarSenhaAntiga;
    private EditText edt_alterarSenhaNova;
    private EditText edt_alterarSenhaNovaConfirma;
    private VerificaConexao verificaConexao;
    private boolean verifica;
    private String tipoConta = getIntent().getStringExtra("tipoConta");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        //Instatanciando os EditTexts
        edt_alterarSenhaAntiga = findViewById(R.id.edtAlterarSenhaAntiga);
        edt_alterarSenhaNova = findViewById(R.id.edtAlterarSenhaNova);
        edt_alterarSenhaNovaConfirma = findViewById(R.id.edtAlterarSenhaNovaConfirma);
        Button btn_alterarSenhaPerfil = findViewById(R.id.btn_AlterarSenhaPerfil);
        verificaConexao = new VerificaConexao(this);

        btn_alterarSenhaPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (verificaCampo()) {
                        alteraSenha();
                        Toast.makeText(AlterarSenhaActivity.this, "Senha alterada com sucesso.", Toast.LENGTH_SHORT).show();
                        abrirTelaMeuPerfil();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.sp_conexao_falha, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public boolean verificaCampo() {

        if (edt_alterarSenhaAntiga.getText().toString().isEmpty()){
            edt_alterarSenhaAntiga.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }

        if (edt_alterarSenhaNova.getText().toString().isEmpty()){
            edt_alterarSenhaNova.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }

        if (edt_alterarSenhaNovaConfirma.getText().toString().isEmpty()){
            edt_alterarSenhaNova.setError(getString(R.string.sp_excecao_campo_vazio));
            return false;
        }

        if (edt_alterarSenhaNova.getText().toString().length() < 6){
            edt_alterarSenhaNova.setError(getString(R.string.sp_excecao_senha));
            return false;
        }
        if (!edt_alterarSenhaNova.getText().toString().equals(edt_alterarSenhaNovaConfirma.getText().toString())){
            edt_alterarSenhaNova.setError(getString(R.string.sp_excecao_senhas_iguais));
            edt_alterarSenhaNovaConfirma.setError(getString(R.string.sp_excecao_senhas_iguais));
            return false;
        }
        return true;
    }

    private void alteraSenha() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getEmail() != null){
            AcessoFirebase.getFirebaseAutenticacao().signInWithEmailAndPassword(user.getEmail(),edt_alterarSenhaAntiga.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser();
                        user.updatePassword(edt_alterarSenhaNova.getText().toString());
                    }
                }
            });
        }
    }

    public void abrirTelaMeuPerfil (){
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
