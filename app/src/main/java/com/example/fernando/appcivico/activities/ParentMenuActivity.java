package com.example.fernando.appcivico.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.application.ApplicationAppCivico;

/**
 * Created by fernando on 17/10/16.
 */
public class ParentMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected NavigationView navigationView;
    protected ApplicationAppCivico applicationAppCivico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_default);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        applicationAppCivico = (ApplicationAppCivico) this.getApplication();


    }

    @Override
    protected void onResume() {
        View headerView = navigationView.getHeaderView(0);
        TextView txtemail = (TextView)headerView.findViewById(R.id.email_usuario_autenticado);
        TextView txtNome = (TextView)headerView.findViewById(R.id.nome_app_usuario_autenticado);

        navigationView.getMenu().clear();

        if (applicationAppCivico.usuarioAutenticado()) {
            navigationView.inflateMenu(R.menu.activity_main_drawer_autenticado);

            txtemail.setVisibility(View.VISIBLE);
            txtemail.setText(applicationAppCivico.getUsuarioAutenticado().getEmail());
            txtNome.setText(applicationAppCivico.getUsuarioAutenticado().getNomeUsuario());

        }else{
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            txtemail.setVisibility(View.GONE);
            txtNome.setText(this.getResources().getString(R.string.app_name));
        }
        super.onResume();
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
        getMenuInflater().inflate(R.menu.main, menu);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Intent i = new Intent(this,EscolherAcessoActivity.class);
            startActivity(i);
        }

        if (id == R.id.nav_busca_avancada) {
            Intent i = new Intent(this,BuscaAvancadaActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }

        if (id == R.id.nav_busca_por_localizacao) {
            Intent i = new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }

        if (id == R.id.nav_busca_minhas_avaliacoes) {
            Intent i = new Intent(this,MinhasAvaliacoesActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }

        if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setMessage("Deseja encerrar a sessão?")
                    .setCancelable(true)
                    .setPositiveButton(this.getString(R.string.confirmar),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            applicationAppCivico.removeDadosDeSessao();
                            Toast.makeText(ParentMenuActivity.this, "Sessão encerrada com sucesso",Toast.LENGTH_SHORT).show();

                            Intent refresh = new Intent(ParentMenuActivity.this, MainActivity.class);
                            startActivity(refresh);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                        }
                    })
                    .setNegativeButton(this.getString(R.string.cancelar),null);

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
