package com.example.android.navigationviewanddeveloper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import arrayAdapters.ArrayAdapterGrid;
import beans.Localite;
import beans.Logement;
import beans.Utilisateur;
import beans.Visite;
import databases.CallBack;
import databases.Links;

public class ChoisirLogementA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_logement);



        String localite= getIntent().getStringExtra("localite");
        String type= getIntent().getStringExtra("typeLogement");
        int budget=getIntent().getIntExtra("budget",0);
        final String username = getIntent().getStringExtra("username");


        final GridView gridView=findViewById(R.id.grid_layout);
        /**********************************FOR DRAWING THE GRID VIEW**********************************/
        getVisites(new CallBack() {

            @Override
            public void onSuccessL(ArrayList<Logement> logements) {

                ArrayAdapterGrid arrayAdapter =new ArrayAdapterGrid(getApplicationContext(),logements);
                gridView.setAdapter(arrayAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        //todo : add Activity ChoisirLogementModifier

                        Logement logement =(Logement)adapterView.getItemAtPosition(i);
                       Intent intent =new Intent(getApplicationContext(),ChoisirLogementModfier.class);

                       //parametres pour prendre une visite
                       intent.putExtra("username_client",username);
                       intent.putExtra("idLogement",logement.getId());
                       startActivity(intent);
                    }
                });
            }
            @Override
            public void onSuccesPrendreVisite(Visite visite) {}

            @Override
            public void onSuccesConsulterCompte(Utilisateur utilisateur, Localite localite) {

            }
            @Override
            public void onSuccess(ArrayList<Visite> visites) {}
        },type,localite,budget);
    }




    /***********************************************FETCH DATA FROM DBB*************************************/
    public void getVisites(final CallBack callBack, String type , String localite , int budget){

        final Links links =new Links();


        final HashMap<String,String> params = new HashMap<String, String>();
        params.put("type",type);
        params.put("localite",localite);
        params.put("budget",String.valueOf(budget));

        JSONObject jsonObject = new JSONObject(params);
        JSONArray jsonArray =new JSONArray();

        try {
            jsonArray.put(1,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<Logement> logements =new ArrayList<Logement>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, links.getRechercherURL(), jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Logement logement;
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject json = response.getJSONObject(i);

                                String id=json.getString("id");
                                String address=json.getString("address");
                                String type=json.getString("type");
                                int prix=json.getInt("prix");
                                String logement_picture=json.getString("logement_picture");
                                String agent_service=json.getString("agent_service");

                                JSONObject jsonLocalite = json.getJSONObject("localite");

                                Localite localite = new Localite(jsonLocalite.getInt("id"),
                                        jsonLocalite.getString("address"));
                               logement=new Logement(id,address,localite,type,prix,logement_picture,agent_service);

                               logements.add(logement);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callBack.onSuccessL(logements);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }
}
