package com.example.android.navigationviewanddeveloper;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeAgent extends AppCompatActivity implements ConsulterVisiteAgent.OnFragmentInteractionListener,
        ConsulterCompte.OnFragmentInteractionListener{
    DrawerLayout drawerLayout;
    String username_agent;

    final ConsulterCompte consulterCompte = new ConsulterCompte();
    final ConsulterVisiteAgent consulterVisiteAgent =new ConsulterVisiteAgent();
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_agent);


        setTitle("Espace Agent");


        drawerLayout =(DrawerLayout)findViewById(R.id.drawer_agent);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar_agent);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_openA,R.string.navigation_drawer_closeA);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView  = (NavigationView)findViewById(R.id.nav_view_agent);

        username_agent=getIntent().getStringExtra("username");
        final Bundle bundle =new Bundle();
        bundle.putString("username",username_agent);
        bundle.putString("type","Agent");
        consulterVisiteAgent.setArguments(bundle);
        consulterCompte.setArguments(bundle);

        final FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transactionOnlaunch=fragmentManager.beginTransaction();


        transactionOnlaunch=fragmentManager.beginTransaction();
        transactionOnlaunch.replace(R.id.content_frame_agent,consulterVisiteAgent);
       // transactionOnlaunch.addToBackStack(null);
        transactionOnlaunch.commit();






/********************************************THE NAVIGATION VIEW TO SWITCH BETWEEN TABS for Agent ***************/
        NavigationView.OnNavigationItemSelectedListener listener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {


                FragmentTransaction fragmentTransaction;



                item.setChecked(true);

                switch (item.getItemId()){

                    case R.id.consulterVisiteAgent:{
                        Toast.makeText(getApplicationContext(),"consulter visite agent",Toast.LENGTH_LONG);
                        fragmentManager.popBackStack();
                        fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame_agent,consulterVisiteAgent);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        return true;
                    }
                    case R.id.consulterCompteAgent:{


                        boolean ra =navigationView.getMenu().findItem(R.id.consulterCompteAgent).isChecked();

                        Toast.makeText(getApplicationContext(), ""+ra,Toast.LENGTH_LONG).show();

                        fragmentManager.popBackStack();
                        fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame_agent,consulterCompte,"consulterCompte");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        return true;
                    }
                    case R.id.deconnectAgent:{
                        disconnectionAlert();
                        drawerLayout.closeDrawers();
                        return true;
                    }
                }

                drawerLayout.closeDrawers();
                return false;
            }
        };
        navigationView.setNavigationItemSelectedListener(listener);
    }

    /***********************TO CREATE ALERT DISCONNECTION DIALOG******************************/
    public void disconnectionAlert() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this,R.style.AlertDialogStyle);
        builder.setTitle("Disconnection");
        builder.setMessage("would you like to disconnect ?")
                .setCancelable(true)
                .setPositiveButton("disconnect", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                }).setNegativeButton("cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface,int i){

                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
/*********************************FOR THE APPBAR WHEN AN ITEM IS CLICKED****************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case  R.id.logout_agent:
            {
                disconnectionAlert();
                return  true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
    /*********************************TO POPULATE THE APPBAR WITH ITEMS ****************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();

        inflater.inflate(R.menu.appbar_menu_agent,menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}