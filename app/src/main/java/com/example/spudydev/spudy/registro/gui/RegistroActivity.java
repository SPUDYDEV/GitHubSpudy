package com.example.spudydev.spudy.registro.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.registro.negocio.VerificaConexao;
import com.example.spudydev.spudy.usuario.aluno.dominio.Aluno;
import com.example.spudydev.spudy.usuario.dominio.Usuario;
import com.example.spudydev.spudy.usuario.professor.dominio.Professor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegistroActivity extends AppCompatActivity {

    private EditText nomeReg;
    private EditText emailReg;
    private EditText dataNascimentoReg;
    private EditText instituicaoReg;
    private Spinner tipodecontaReg;
    private EditText senhaReg;
    private EditText confirmaSenhaReg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nomeReg = findViewById(R.id.edtNomeRegistro);
        emailReg = findViewById(R.id.edtEmailRegistro);
        dataNascimentoReg = findViewById(R.id.edtDataRegistro);
        instituicaoReg = findViewById(R.id.edtInstituicaoRegistro);
        tipodecontaReg = findViewById(R.id.spnTipoDeConta);
        Button btnConfirmaReg = findViewById(R.id.btnConfirmRegistro);
        senhaReg = findViewById(R.id.edtSenhaReg);
        confirmaSenhaReg = findViewById(R.id.edtConfirmaSenha);
        dataNascimentoReg = findViewById(R.id.edtDataRegistro);

        //Conexao
        final VerificaConexao verificaConexao = new VerificaConexao(this);

        //criando máscara para data de nascimento
        Auxiliar.criarMascara(dataNascimentoReg);

        btnConfirmaReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (validarCampos()) {
                        if (senhaReg.getText().toString().equals(confirmaSenhaReg.getText().toString())) {
                            cadastrarUsuario();
                        } else {
                            senhaReg.setError(getString(R.string.sp_excecao_senhas_iguais));
                            confirmaSenhaReg.setError(getString(R.string.sp_excecao_senhas_iguais));
                        }
                    }
                } else {
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_conexao_falha));
                }
            }
        });
    }
    private boolean validarCampos(){

        boolean validacao = true;

        if (nomeReg.getText().toString().isEmpty() || nomeReg.getText().toString().trim().length() == 0){
            nomeReg.setError(getString(R.string.sp_excecao_nome_invalido));
            validacao = false;
        }

        if ((!Auxiliar.verificaExpressaoRegularEmail(emailReg.getText().toString())) ||
                emailReg.getText().toString().trim().length() == 0){
            emailReg.setError(getString(R.string.sp_excecao_email));
            validacao = false;
        }

        if (dataNascimentoReg.getText().toString().isEmpty() || dataNascimentoReg.getText().toString().trim().length() == 0){
            dataNascimentoReg.setError(getString(R.string.sp_excecao_data_nascimento));
            validacao = false;
        }

        if (instituicaoReg.getText().toString().isEmpty() || instituicaoReg.getText().toString().trim().length() == 0){
            instituicaoReg.setError(getString(R.string.sp_excecao_instituicao));
            validacao = false;
        }

        if (tipodecontaReg.getSelectedItem().equals("Selecione o tipo de conta")){
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_tipo_conta));
            validacao = false;
        }

        if (senhaReg.getText().toString().isEmpty() || senhaReg.getText().toString().trim().length() == 0){
            senhaReg.setError(getString(R.string.sp_excecao_senha));
            validacao = false;
        }

        if (confirmaSenhaReg.getText().toString().isEmpty() || confirmaSenhaReg.getText().toString().trim().length() == 0){
            confirmaSenhaReg.setError(getString(R.string.sp_excecao_senha));
            validacao = false;
        }

        return validacao;
    }

    private void cadastrarUsuario(){

        final FirebaseAuth autenticacao = AcessoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                emailReg.getText().toString(),
                senhaReg.getText().toString()
        ).addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    cadastrar();
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_usuario_cadastrado));
                    abrirTelaLoginUsuario();
                }else{
                     try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        senhaReg.setError(getString(R.string.sp_excecao_senha));
                        confirmaSenhaReg.setError(getString(R.string.sp_excecao_senha));
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        emailReg.setError(getString(R.string.sp_excecao_email));
                    }catch (FirebaseAuthUserCollisionException e){
                        emailReg.setError(getString(R.string.sp_excecao_email_cadastrado));
                    }catch (Exception e){
                        Auxiliar.criarToast(RegistroActivity.this, getString(R.string.sp_excecao_cadastro));
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    //Criando usuario
    public Usuario criaUsuario(){
        Usuario usuario = new Usuario();
        usuario.setEmail(emailReg.getText().toString());
        usuario.setInstituicao(instituicaoReg.getText().toString());
        usuario.setTipoConta(tipodecontaReg.getSelectedItem().toString());
        return  usuario;
    }

    //Criando pessoa
    public Pessoa criaPessoa(Usuario usuario){
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nomeReg.getText().toString());
        pessoa.setDataNascimento(dataNascimentoReg.getText().toString());
        pessoa.setUsuario(usuario);
        return pessoa;
    }

    //Criando aluno
    public Aluno criaAluno(Pessoa pessoa){
        Aluno aluno = new Aluno();
        aluno.setPessoa(pessoa);
        return aluno;
    }

    //Criando Professor
    public Professor criaProfessor(Pessoa pessoa){
        Professor professor = new Professor();
        professor.setPessoa(pessoa);
        return professor;
    }


    //Cadastrar usuario
    public void cadastrar(){
        Usuario usuario = criaUsuario();
        Pessoa pessoa = criaPessoa(usuario);
        String uidUsuario = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getUid();

        //Salvando Pessoa e usuario
        AcessoFirebase.getFirebase().child("usuario").child(uidUsuario).setValue(usuario.toMapUsuario());
        AcessoFirebase.getFirebase().child("pessoa").child(uidUsuario).setValue(pessoa.toMapPessoa());
        //Salvando Aluno OU Professor
        if (usuario.getTipoConta().equals("Aluno")){
            AcessoFirebase.getFirebase().child("aluno").child(uidUsuario).child("turmas").child("SENTINELA").setValue("0");
        }else{
            AcessoFirebase.getFirebase().child("professor").child(uidUsuario).child("turmasMinistradas").child("SENTINELA").setValue("0");
        }
    }

    public void abrirTelaLoginUsuario(){
        Intent intentAbrirTelaRegistroConfirm = new Intent(RegistroActivity.this, ConfirmRegistroActivity.class);
        startActivity(intentAbrirTelaRegistroConfirm);
        finish();
    }
}