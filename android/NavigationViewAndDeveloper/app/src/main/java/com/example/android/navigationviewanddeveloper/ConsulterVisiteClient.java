package com.example.android.navigationviewanddeveloper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import arrayAdapters.ArrayAdapterVisite;
import beans.Localite;
import beans.Logement;
import beans.Utilisateur;
import beans.Visite;
import databases.CallBack;
import databases.Links;


public class ConsulterVisiteClient extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Links links = new Links();
    private OnFragmentInteractionListener mListener;

    public ConsulterVisiteClient() {
    }

    public static ConsulterVisiteClient newInstance(String param1, String param2) {
        ConsulterVisiteClient fragment = new ConsulterVisiteClient();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       final FrameLayout frameLayout = (FrameLayout)inflater.inflate
               (R.layout.fragment_consulter_visite, container, false);

        String username = getArguments().getString("username");


        getString(new CallBack() {
            @Override
            public void onSuccess(ArrayList<Visite> visites) {

                //ListView des visites du client
                ArrayAdapterVisite arrayAdapterV = new ArrayAdapterVisite(getContext(),visites);
                final ListView listView =(ListView)frameLayout.findViewById(R.id.mylistview);

                listView.setAdapter(arrayAdapterV);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                        //Dialog pour modifer la visite ou l'annuler
                        final Dialog modifierVisite =new Dialog(getContext());
                        final TextView etat = (TextView)view.findViewById(R.id.etat);

                        modifierVisite.setTitle("Modifier visite");
                        modifierVisite.setContentView(R.layout.modifier_visite);

                        //bouton pour la boite dialogue modifierVisite
                        final Button fermer =(Button)modifierVisite.findViewById(R.id.fermer);
                        final Button annulerVisite=(Button)modifierVisite.findViewById(R.id.annulerVisite);

                        //input dateheure pour afficher date et heure choisit pour etre modifié
                        final TextInputLayout dateheure=(TextInputLayout)modifierVisite.findViewById(R.id.dateheure);


                        //afficher la date de la visite concerné dans l'input dateheure
                        final Visite visite =(Visite)adapterView.getItemAtPosition(position);
                        Date dateAffiche =new Date(visite.getVisite_date());
                        Time heureAffiche =new Time(visite.getVisite_heure());

                        dateheure.getEditText().setText(dateAffiche+" a "+heureAffiche);
                        dateheure.setEnabled(false);

                        Visite visiteAnnuler=(Visite)listView.getSelectedItem();

                        //pour annuler une visite
                        annulerVisite.setOnClickListener(new View.OnClickListener() {
                            @Override public void onClick(View view) {
                                Links links =new Links();
                                etat.setText("annule");

                                //TODO requete pour annuler la visite
                                StringRequest stringRequest= new StringRequest(Request.Method.POST,links.getAnnulerVisite(),
                                        new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(),"error "+error,Toast.LENGTH_LONG).show();
                                    }}){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        HashMap<String,String> params = new HashMap<String ,String >();

                                        params.put("id",visite.getId()+"");

                                        return params;
                                    }
                                };
                                RequestQueue requestQueue =Volley.newRequestQueue(getContext());
                                requestQueue.add(stringRequest);

                                modifierVisite.dismiss();
                            }});

                        //fermer boite dialogue ModifierVisite
                        fermer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                modifierVisite.dismiss();
                            }});

                            if(etat.getText().toString().equals("encours"))
                            modifierVisite.show();

                    }
                });
            }
            //ignore those methods
            @Override public void onSuccessL(ArrayList<Logement> logements) {}
            @Override public void onSuccesPrendreVisite(Visite visite) {}
            @Override public void onSuccesConsulterCompte(Utilisateur utilisateur, Localite localite) {}

        },username);

        return frameLayout;
    }
    public void onButtonPressed(int position) {
        if (mListener != null) {
            mListener.onFragmentInteraction(position);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    " must implement OnFragmentInteractionListener");}}

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int position);
    }

    /*********************************Requete pour trouver les visites d'un client************************/
    public void getString(final CallBack callBack,String client){

        JSONArray jsonArray= new JSONArray();
        HashMap<String ,String > params = new HashMap<String, String>();
        params.put("client",client);
        JSONObject jsonObject=new JSONObject(params);
        try {
            jsonArray.put(1,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //The volley request
        JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Request.Method.POST, links.getConsulterVisiteURL(),
                jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Visite> visites = new ArrayList<Visite>();
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject jsonObject1 = response.getJSONObject(i);
                        int id=jsonObject1.getInt("id") ;
                        String username_client=jsonObject1.getString("username_client") ;
                        String username_agent=jsonObject1.getString("username_agent") ;
                        String logement=jsonObject1.getString("logement");
                        long visite_date=jsonObject1.getLong("visite_date") ;
                        long visite_heure =jsonObject1.getLong("visite_heure");
                        int preavis =jsonObject1.getInt("preavis");
                        String etat =jsonObject1.getString("etat");
                        Visite visite =new Visite(id,username_client,username_agent,logement,visite_date,visite_heure,preavis,etat);
                        visites.add(visite);

                        Log.i("Vis"," a : "+visites.get(i).getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callBack.onSuccess(visites);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue =Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

    }

    /*********************************Pour afficher la boite de dialogue du choix de l'heure************************/
    public void createHourdialog(final TextInputLayout dateheure){

        final String modifierDate = dateheure.getEditText().getText().toString();
        final Dialog hourPicker = new Dialog(getContext());
        hourPicker.setTitle("Heure");
        hourPicker.setContentView(R.layout.changer_heure_dialog);
        final TextView hourDisplay =(TextView)hourPicker.findViewById(R.id.affichage);
        final NumberPicker numberPicker = (NumberPicker)hourPicker.findViewById(R.id.hourPicker);
        numberPicker.setMinValue(8);
        numberPicker.setMaxValue(16);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override public void onValueChange(NumberPicker numberPicker, int oldN, int newN) {
                if(newN<10)
                    hourDisplay.setText("0"+newN+":00");
                else
                    hourDisplay.setText(newN+":00");
            }});

        Button set =(Button)hourPicker.findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                dateheure.getEditText().setText(modifierDate+" à "+hourDisplay.getText());
                hourPicker.dismiss();

            }});
        Button cancel=(Button)hourPicker.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                hourPicker.dismiss();
            }});
        hourPicker.show();
    }

}
