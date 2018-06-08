package com.example.android.navigationviewanddeveloper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import databases.Links;

public class EtablirPreavis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_etablir_preavis);

      /*  final RatingBar preavis=(RatingBar)findViewById(R.id.preavis);
        final TextView preavisTitle=(TextView)findViewById(R.id.preavisTitle);

        //Spinner for etat visite
        final Spinner etatSpinner=(Spinner)findViewById(R.id.etatSpinner) ;
        ArrayAdapter<CharSequence> spinnerAdapter =ArrayAdapter.createFromResource(getApplicationContext(),R.array.etat,android.
                R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etatSpinner.setAdapter(spinnerAdapter);

        etatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String etat=(String) adapterView.getItemAtPosition(position);

                if(etat.equals("termine")) {
                    preavis.setEnabled(true);
                    preavis.setVisibility(View.VISIBLE);
                    preavisTitle.setVisibility(View.VISIBLE);
                }
                else {
                    preavis.setEnabled(false);
                    preavis.setVisibility(View.INVISIBLE);
                    preavisTitle.setVisibility(View.INVISIBLE);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}});


        //Button send in dialogue
        Button send=(Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                preavisO.setRating(preavis.getRating());

                Links links=new Links();
                //requete pour envoyer le preavis etablit
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        links.getValiderVisiteURL(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),""+error,Toast.LENGTH_LONG).show();
                    }}){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params = new HashMap<String, String>();

                        int rating =(int)preavis.getRating();
                        if(rating==0)
                            params.put("preavis","null");
                        else
                            params.put("preavis",rating+"");


                        params.put("idVisite",visite.getId()+"");
                        params.put("etatVisite",etatSpinner.getSelectedItem().toString());
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }});

               */


               }
}
