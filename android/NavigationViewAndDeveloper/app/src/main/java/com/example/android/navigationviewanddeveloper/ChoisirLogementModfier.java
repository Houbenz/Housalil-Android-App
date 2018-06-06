package com.example.android.navigationviewanddeveloper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import beans.Localite;
import beans.Logement;
import beans.Utilisateur;
import beans.Visite;
import databases.CallBack;
import databases.Links;

public class ChoisirLogementModfier extends AppCompatActivity {

    TextInputLayout agentInput;
    TextInputLayout dateHeureInput;
    Button submit,modifier;
    DatePickerDialog datePickerDialog;
    Date date ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_logement_modfier);

        final  String username_client=getIntent().getStringExtra("username_client");
        final String idLogement = getIntent().getStringExtra("idLogement");
        // to get today's date

         java.util.Date jDate =  Calendar.getInstance().getTime();
         final java.sql.Date sDate=new Date(jDate.getTime());
        agentInput =(TextInputLayout)findViewById(R.id.agentInput);
        agentInput.setHint("Agent de visite");
        agentInput.setEnabled(false);

        dateHeureInput=(TextInputLayout)findViewById(R.id.dateHeureInput);
        dateHeureInput.setHint("date et heure de visite");
        dateHeureInput.setEnabled(false);



        /************************************DRAW FETCHED DATA IN LAYOUT***************************************/

        prendreVisite(new CallBack() {
            @Override
            public void onSuccess(ArrayList<Visite> visites) {

                Visite visite= visites.get(0);
                agentInput.getEditText().setText(visite.getUsername_agent());
                Date dateVisite = new Date(visite.getVisite_date());
                Time heureVisite = new Time(visite.getVisite_heure());
                dateHeureInput.getEditText().setText(dateVisite+" à "+heureVisite);
            }
            @Override
            public void onSuccessL(ArrayList<Logement> logements) {}
            @Override
            public void onSuccesPrendreVisite(Visite visite) {}
            @Override
            public void onSuccesConsulterCompte(Utilisateur utilisateur, Localite localite) {

            }
        }, username_client, idLogement, sDate);

        submit=(Button)findViewById(R.id.submit);
        /***************************************lorsque submit est appuyé**************************************************************/
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                ajouterRDV(username_client, idLogement, sDate);
                finishAffinity();
                Intent intent =new Intent(getApplicationContext(),HomeClient.class);
                startActivity(intent);
            }
        });

        /***********************TRAITEMENT POUR MODIFIER*********************************/
        modifier =(Button)findViewById(R.id.modifier);
        //DateListener for DatePicker
        final DatePickerDialog.OnDateSetListener listener =new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar date = Calendar.getInstance();
                date.set(Calendar.YEAR,year);
                date.set(Calendar.MONTH,month);
                date.set(Calendar.DATE,day);
                Date sDate = new Date(date.getTime().getTime());

                    submit.setEnabled(true);
                    modifier.setEnabled(true);
                dateHeureInput.getEditText().setText(sDate.toString());

                String modifierDate=String.valueOf(dateHeureInput.getEditText().getText());
                createHourdialog(modifierDate);
            }
        };
        //Cancel Listener for DatePicker
        final DatePickerDialog.OnCancelListener cancelListener = new DatePickerDialog.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                modifier.setEnabled(true);
            }
        };

        /********************************************* lorsque modifier est appuyé******************/
        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar =Calendar.getInstance();
                int day    =calendar.get(Calendar.DAY_OF_MONTH);
                int month =calendar.get(Calendar.MONTH);
                int year  =calendar.get(Calendar.YEAR);
                modifier.setEnabled(false);
                //datePicker configuration
                datePickerDialog= new DatePickerDialog(ChoisirLogementModfier.this,R.style.AlertDialogStyle, listener,year,month,day);
                datePickerDialog.setOnCancelListener(cancelListener);

                Calendar calendar1 =Calendar.getInstance();
                calendar1.add(Calendar.DATE,1);
                long todayLong =calendar1.getTime().getTime();
                calendar.add(Calendar.MONTH,3);
                long dayMaxLong =calendar.getTime().getTime();
                datePickerDialog.getDatePicker().setMinDate(todayLong);
                datePickerDialog.getDatePicker().setMaxDate(dayMaxLong);
                datePickerDialog.show();
            }
        });
    }
    //TODO :Requete pour modifier date et heure avant envoi ,
    // TODO :neccessite d'envoyer une requete qui recurepe les heures libres d'une journée selectionné

    /*********************************Pour afficher la boite de dialogue du choix de l'heure************************/
    public void createHourdialog(final String modifierDate ){
       final Dialog hourPicker = new Dialog(ChoisirLogementModfier.this);
        hourPicker.setTitle("Heure");
        hourPicker.setContentView(R.layout.changer_heure_dialog);
        final TextView hourDisplay =(TextView)hourPicker.findViewById(R.id.affichage);
        final NumberPicker numberPicker = (NumberPicker)hourPicker.findViewById(R.id.hourPicker);
        numberPicker.setMinValue(8);
        numberPicker.setMaxValue(16);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldN, int newN) {
                if(newN<10)
                hourDisplay.setText("0"+newN+":00");
                else
                hourDisplay.setText(newN+":00");

            }
        });

        Button set =(Button)hourPicker.findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateHeureInput.getEditText().setText(modifierDate+" à "+hourDisplay.getText());
            hourPicker.dismiss();

            }
        });
        Button cancel=(Button)hourPicker.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hourPicker.dismiss();
            }
        });
        hourPicker.show();
    }

/*************************************TO CREATE A VISIT (AUTOMATIQUE) **********************************************************/
    public void prendreVisite(final CallBack callBack,String username_client , String idLogement , Date date ){
        Links links = new Links();
        HashMap<String ,String> params = new HashMap<String, String>();
        params.put("username_client",username_client);
        params.put("idLogement",idLogement);
        params.put("date",date.toString());
        final ArrayList<Visite> visites = new ArrayList<Visite>();
        JSONObject jsonRequest =new JSONObject(params);

        JSONArray jsonArray =new JSONArray();
        try {
            jsonArray.put(1,jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, links.getPrendreVisiteURL(), jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response1) {
                        try {
                            JSONObject response=response1.getJSONObject(0);
                        int id = response.getInt("id");
                        String username_client=response.getString("username_client") ;
                        String username_agent=response.getString("username_agent") ;
                        String logement=response.getString("logement");
                        long visite_date=response.getLong("visite_date") ;
                        long visite_heure =response.getLong("visite_heure");
                        int preavis =response.getInt("preavis");
                        String etat =response.getString("etat");
                         Visite  visite =new Visite(id,username_client,username_agent,logement,visite_date,visite_heure,preavis,etat);
                         visites.add(visite);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callBack.onSuccess(visites);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
/********************************AJOUTE UN RDV POUR LE CLIENT********************************/
    public void ajouterRDV(String username_client , String idLogement , Date date){
        prendreVisite(new CallBack() {
            @Override
            public void onSuccess(ArrayList<Visite> visites) {
                Links links = new Links();
                Visite visite = visites.get(0);
                HashMap<String ,String > params = new HashMap<String ,String>();
                params.put("id",""+visite.getId());
                params.put("username_client",visite.getUsername_client());
                params.put("username_agent",visite.getUsername_agent());
                params.put("logement",visite.getLogement());
                params.put("visite_date",""+visite.getVisite_date());
                params.put("visite_heure",""+visite.getVisite_heure());
                params.put("etat",""+visite.getEtat());
                params.put("preavis",""+visite.getPreavis());
            JSONObject jsonObject = new JSONObject(params);
                try {
                    jsonObject.put("visite",visite);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, links.getAjouterRDVURL()
                        , jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String ajout = response.getString("ajout");
                            Toast.makeText(getApplicationContext(),"response == "+ajout,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override public void onErrorResponse(VolleyError error) {}});
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(jsonObjectRequest);
            }
            @Override public void onSuccessL(ArrayList<Logement> logements) {}
            @Override public void onSuccesPrendreVisite(Visite visite) {}
            @Override public void onSuccesConsulterCompte(Utilisateur utilisateur,Localite localite) {}}
            , username_client, idLogement, date);
    }
}
