package com.example.android.navigationviewanddeveloper;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class HomeClient extends AppCompatActivity implements ConsulterVisiteClient.OnFragmentInteractionListener
        ,RechercherLogement.OnFragmentInteractionListener,ConsulterCompte.OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;

    final RechercherLogement rechercherLogement = new RechercherLogement();
    final ConsulterVisiteClient consulterVisiteClient =new ConsulterVisiteClient();
    final ConsulterCompte consulterCompte =new ConsulterCompte();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_client);


        setTitle("Espace Client");

        final String username = getIntent().getStringExtra("username");
        final FragmentManager fragmentManager=getSupportFragmentManager();


        drawerLayout =(DrawerLayout) findViewById(R.id.drawer_client);


        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar_client);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_openC,R.string.navigation_drawer_closeC);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();




        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view_client);


        Bundle bundle =new Bundle();

        bundle.putString("type","Client");
        bundle.putString("username",username);
        consulterVisiteClient.setArguments(bundle);
        rechercherLogement.setArguments(bundle);
        consulterCompte.setArguments(bundle);

        FragmentTransaction transactionOnlaunch=fragmentManager.beginTransaction();


        transactionOnlaunch=fragmentManager.beginTransaction();
        transactionOnlaunch.replace(R.id.content_frame_client,consulterVisiteClient,"consulterVisiteClient");
        //transactionOnlaunch.addToBackStack(null);
        transactionOnlaunch.commit();




        /*****************************THE NAVIGATION VIEW TO SWITCH BETWEEN TABS FOR CLIENT ********************************/
        NavigationView.OnNavigationItemSelectedListener listener =new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                FragmentTransaction fragmentTransaction;

                switch (item.getItemId()){
                    case R.id.consulterVisite:{
                        Toast.makeText(getApplicationContext(),"consulter visite",Toast.LENGTH_SHORT).show();

                        fragmentManager.popBackStack();
                        fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame_client,consulterVisiteClient,"consulterVisiteClient");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        return true;


                    }
                    case R.id.rechercherLogement:{
                        Toast.makeText(getApplicationContext(),"rechercher logement",Toast.LENGTH_SHORT).show();
                            fragmentManager.popBackStack();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame_client, rechercherLogement, "rechercherLogement");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            drawerLayout.closeDrawers();
                            return true;
                    }
                    case R.id.consulterCompte:{

                        Toast.makeText(getApplicationContext(),"consulter compte",Toast.LENGTH_SHORT).show();
                        fragmentManager.popBackStack();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame_client, consulterCompte, "consulterCompte");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        return true;
                    }
                    case R.id.deconnect:{
                        drawerLayout.closeDrawers();
                        disconnectionAlert();
                        return  true;
                    }
                }
                drawerLayout.closeDrawers();
                return true;
            }
        };

        navigationView.setNavigationItemSelectedListener(listener);
    }


    //Not used yet
    @Override
    public void onFragmentInteraction(int position) {

    }

    /***********************TO CREATE ALERT DIALOG******************************/
    public void disconnectionAlert() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this,R.style.AlertDialogStyle);
        builder.setTitle("Disconnection");
        builder.setMessage("would you like to disconnect ?").
                setCancelable(true)
                .setPositiveButton("disconnect",
                new DialogInterface.OnClickListener() {
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


    /******************************FOR APPBAR***********************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout_client:{
                disconnectionAlert();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu_client,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

