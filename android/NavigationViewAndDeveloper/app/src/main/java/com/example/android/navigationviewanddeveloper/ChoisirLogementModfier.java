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
import android.util.Log;
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
import java.time.LocalTime;
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




        agentInput =(TextInputLayout)findViewById(R.id.agentInput);
        agentInput.setHint("Agent de visite");
        agentInput.setEnabled(false);

        dateHeureInput=(TextInputLayout)findViewById(R.id.dateHeureInput);
        dateHeureInput.setHint("date et heure de visite");
        dateHeureInput.setEnabled(false);

        // to get today's date


        Calendar cal =Calendar.getInstance();
        int hour =cal.get(Calendar.HOUR_OF_DAY);

        final java.sql.Date sDate;
        java.util.Date jDate;

        if(hour<16) {
            jDate = cal.getTime();
             sDate= new Date(jDate.getTime());

        }else{
            cal.add(Calendar.DATE,1);
             jDate = cal.getTime();
             sDate = new Date(jDate.getTime());
        }


 /************************************DRAW FETCHED DATA IN LAYOUT***************************************/

        prendreVisite(new CallBack() {
            @Override
            public void onSuccess(ArrayList<Visite> visites) {
                // to get today's date
                java.util.Date jDate =  Calendar.getInstance().getTime();
                final java.sql.Date sDate=new Date(jDate.getTime());
                Visite visite= visites.get(0);
                agentInput.getEditText().setText(visite.getUsername_agent());
                Date dateVisite = new Date(visite.getVisite_date());
                Time heureVisite = new Time(visite.getVisite_heure());
                dateHeureInput.getEditText().setText(dateVisite+" à "+heureVisite);
            }
            @Override public void onSuccessL(ArrayList<Logement> logements) {}
            @Override public void onSuccesPrendreVisite(Visite visite) {}
            @Override public void onSuccesConsulterCompte(Utilisateur utilisateur, Localite localite) {}
        }, username_client, idLogement, sDate);

        submit=(Button)findViewById(R.id.submit);
        /***************************************lorsque submit est appuyé**************************************************************/
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {


               String complet= dateHeureInput.getEditText().getText().toString();

               String dateS =complet.substring(0,10);
               String heureS=complet.substring(complet.length()-8);

               Time time = Time.valueOf(heureS);
               Date  date =Date.valueOf(dateS);

               ajouterRDV(username_client, idLogement, dateS,heureS);

                finishAffinity();

                Intent intent =new Intent(getApplicationContext(),HomeClient.class);
                intent.putExtra("username",username_client);

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

                //when client enters a date and presses ok it sends a request to get  the free hours on that date
                prendreVisite(new CallBack() {
                    @Override public void onSuccess(ArrayList<Visite> visites) {

                        ArrayList<String> heureLibre=new ArrayList<String>();
                        Time time =null;
                        Log.i("MSSGG"," leng "+visites.size());
                        Visite visite =null;

                        for(int i=0;i<visites.size();i++){
                            visite=visites.get(i);
                            time=new Time(visite.getVisite_heure());
                            heureLibre.add(time+"");
                        }

                        String modifierDate=String.valueOf(dateHeureInput.getEditText().getText());

                        createHourdialog(modifierDate,heureLibre);

                    }
                    @Override public void onSuccessL(ArrayList<Logement> logements) {}
                    @Override public void onSuccesPrendreVisite(Visite visite) {}
                    @Override public void onSuccesConsulterCompte(Utilisateur utilisateur, Localite localite) {}
                },username_client,idLogement,sDate);
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
    // TODO :Requete pour modifier date et heure avant envoi ,
    // TODO :neccessite d'envoyer une requete qui recurepe les heures libres d'une journée selectionné

    /*********************************Pour afficher la boite de dialogue du choix de l'heure************************/
    public void createHourdialog(final String modifierDate, final ArrayList<String> heureLibres ){
       final Dialog hourPicker = new Dialog(ChoisirLogementModfier.this);
        hourPicker.setTitle("Heure");
        hourPicker.setContentView(R.layout.changer_heure_dialog);
        final TextView hourDisplay =(TextView)hourPicker.findViewById(R.id.affichage);
        final NumberPicker numberPicker = (NumberPicker)hourPicker.findViewById(R.id.hourPicker);




        String [] tabheureLibres=new String[heureLibres.size()];
        tabheureLibres=heureLibres.toArray(tabheureLibres);

        numberPicker.setDisplayedValues(tabheureLibres);
        numberPicker.setWrapSelectorWheel(false);

        String minC =heureLibres.get(0).substring(0,2);
        String maxC =heureLibres.get(heureLibres.size()-1).substring(0,2);

        int min =Integer.parseInt(minC);
        int max =Integer.parseInt(maxC);

        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldN, int newN) {

                if(newN<10)
                    hourDisplay.setText("0"+newN+":00:00");
                else
                    hourDisplay.setText(newN+":00:00");
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
    public void prendreVisite(final CallBack callBack,String username_client , String idLogement , Date date){
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
                    public void onResponse(JSONArray jsonArrayResponse) {

                        //Changed recently
                        Visite visite=null;
                        for(int i =0; i<jsonArrayResponse.length();i++) {
                            try {

                                JSONObject response = jsonArrayResponse.getJSONObject(i);
                                int id = response.getInt("id");
                                String username_client = response.getString("username_client");
                                String username_agent = response.getString("username_agent");
                                String logement = response.getString("logement");
                                long visite_date = response.getLong("visite_date");
                                long visite_heure = response.getLong("visite_heure");
                                int preavis = response.getInt("preavis");
                                String etat = response.getString("etat");
                                visite = new Visite(id, username_client, username_agent, logement, visite_date, visite_heure, preavis, etat);

                                visites.add(visite);
                            } catch (JSONException e)
                            {e.printStackTrace();}
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
    public void ajouterRDV(String username_client , String idLogement , final String date, final String time){

        prendreVisite(new CallBack() {
            @Override
            public void onSuccess(ArrayList<Visite> visites) {
                Links links = new Links();
                Visite visite = visites.get(0);

                HashMap<String ,String > params = new HashMap<String ,String>();
                //params.put("id",""+visite.getId());
                params.put("username_client",visite.getUsername_client());
                params.put("username_agent",visite.getUsername_agent());
                params.put("logement",visite.getLogement());
                params.put("visite_date",date);
                params.put("visite_heure",time);
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
            , username_client, idLogement, Date.valueOf(date));
    }
}
