package com.example.spudydev.spudy.usuario.professor.gui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.activity.CriarTurmaActivity;
import com.example.spudydev.spudy.activity.DisciplinaActivity;
import com.example.spudydev.spudy.infraestrutura.gui.LoginActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.perfil.negocio.DadosMenuLateral;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainProfessorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DadosMenuLateral dadosMenuDAO = new DadosMenuLateral();
    //Listview
    private ArrayList<String> listaTurmasMinistradasAux;
    private ListView lvTurmaProfessor;
    //FimListView
    private  AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.sp_navigation_drawer_open, R.string.sp_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //Chamando a classe para setar nome e email do nav_header_menu_professor
        dadosMenuDAO.resgatarUsuario(navigationView, user);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_meu_perfil:
                        abrirTelaMeuPerfilProfessorActivity();
                        return true;
                    case R.id.nav_turmas:
                        //Activity de turmas
                        Toast.makeText(MainProfessorActivity.this, "Em construção", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_disciplinas:
                        //Activity de turmas
                        Toast.makeText(MainProfessorActivity.this, "Em construção", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_sair:
                        //sair
                        sair();
                        return true;
                    default:
                        return false;
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabButtonCriarTurma);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainProfessorActivity.this, CriarTurmaActivity.class);
                startActivity(intent);
            }
        });


        lvTurmaProfessor = findViewById(R.id.lvTurmasProfessor);
        //teste

        carregaTurmaMinistrada();

        //teste

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_professor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void sair() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Sair");
        //define a mensagem
        builder.setMessage("Tem certeza que deseja sair?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1) {
                FirebaseAuth.getInstance().signOut();
                abrirTelaLoginActivity();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
    //Carregando turmas
    private void carregaTurmaMinistrada(){
        //Pega UID do user
        final String uid = AcessoFirebase.getUidUsuario();
        //Lista para salvar as turmas que o professor é responsável
        AcessoFirebase.getFirebase().addValueEventListener(new ValueEventListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> turmasMinistradas = (HashMap<String,Object>) dataSnapshot.child("professor").child(uid).child("turmasMinistradas").getValue();
                listaTurmasMinistradasAux = new ArrayList<>();
                for (Object i: turmasMinistradas.values()){
                    if (!i.toString().equals("0")){
                        //Aqui ele esta com todas as turmas do professor, pegar o cardview e seta o texto
                        listaTurmasMinistradasAux.add(dataSnapshot.child("turma").child(i.toString()).child("nomeTurma").getValue(String.class).toUpperCase()+"\nCódigo da turma: "+i.toString());
                    }
                }
                setListViewTurmas(listaTurmasMinistradasAux);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Seta no listview
    public void setListViewTurmas(ArrayList<String> turmasMinistradas){
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, turmasMinistradas);
        lvTurmaProfessor.setAdapter(itemsAdapter);
    }


    //Intent perfil professor
    public void abrirTelaMeuPerfilProfessorActivity(){
        Intent intentAbrirTelaMeuPerfilProfessorAcitivty = new Intent(MainProfessorActivity.this, MeuPerfilProfessorActivity.class);
        startActivity(intentAbrirTelaMeuPerfilProfessorAcitivty);
    }
    public void abrirTelaDisciplinaActivity(){
        Intent intentAbrirTelaDisciplinaAcitivty = new Intent(MainProfessorActivity.this, DisciplinaActivity.class);
        startActivity(intentAbrirTelaDisciplinaAcitivty);
    }
    //Intent singOut
    public void abrirTelaLoginActivity(){
        Intent intentAbrirTelaLoginAcitivty = new Intent(MainProfessorActivity.this, LoginActivity.class);
        startActivity(intentAbrirTelaLoginAcitivty);
        finish();
    }
}

