package com.example.android.navigationviewanddeveloper;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import arrayAdapters.ArrayAdapterVisiteAgent;
import beans.Localite;
import beans.Logement;
import beans.Utilisateur;
import beans.Visite;
import databases.CallBack;
import databases.Links;


public class ConsulterVisiteAgent extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ConsulterVisiteAgent() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ConsulterVisiteAgent newInstance(String param1, String param2) {
        ConsulterVisiteAgent fragment = new ConsulterVisiteAgent();
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

        String username_agent =getArguments().getString("username");

        final FrameLayout frameLayout = (FrameLayout)inflater.inflate(
                R.layout.fragment_consulter_visite_agent, container, false);

        final ListView agentListView=frameLayout.findViewById(R.id.agentListView);
            getVisitesAgent(new CallBack() {
                @Override
                public void onSuccess(ArrayList<Visite> visites) {

                    /***************************ETABLIR PREAVIS****************************************************/
                    ArrayAdapterVisiteAgent arrayAdapterVisiteAgent =new ArrayAdapterVisiteAgent(getContext(),visites);
                    agentListView.setAdapter(arrayAdapterVisiteAgent);

                    agentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            final RatingBar preavisO =(RatingBar)view.findViewById(R.id.preavisO);
                            final Visite visite= (Visite)adapterView.getItemAtPosition(position);
                    /**********************ETABLIR PREAVIS DIALOGUE********************************************/
                            final Dialog etablirPreavisDialog = new Dialog(getContext());
                            etablirPreavisDialog.setTitle("etablir preavis");
                            etablirPreavisDialog.setContentView(R.layout.dialog_etablir_preavis);

                            //RatingBar
                            final RatingBar preavis=(RatingBar)etablirPreavisDialog.findViewById(R.id.preavis);
                            final TextView preavisTitle=(TextView)etablirPreavisDialog.findViewById(R.id.preavisTitle);
                            //Spinner for etat visite
                            final Spinner etatSpinner=(Spinner)etablirPreavisDialog.findViewById(R.id.etatSpinner) ;
                            ArrayAdapter<CharSequence> spinnerAdapter =ArrayAdapter.createFromResource(getContext(),R.array.etat,android.
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
                            Button send=(Button)etablirPreavisDialog.findViewById(R.id.send);
                            send.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View view) {
                                    preavisO.setRating(preavis.getRating());

                                    Links links=new Links();
                                    //requete pour envoyer le preavis etablit
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                            links.getValiderVisiteURL(), new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(),""+error,Toast.LENGTH_LONG).show();
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

                                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                    requestQueue.add(stringRequest);

                                    etablirPreavisDialog.dismiss();
                                }});
                            Button annuler=(Button)etablirPreavisDialog.findViewById(R.id.annuler);
                            annuler.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View view) {
                                    preavisO.setRating(0);

                                    etablirPreavisDialog.dismiss();
                                }});

                            etablirPreavisDialog.show();
                        }
                    });
                }
                @Override public void onSuccessL(ArrayList<Logement> logements) {}
                @Override public void onSuccesPrendreVisite(Visite visite) {}
                @Override public void onSuccesConsulterCompte(Utilisateur utilisateur, Localite localite) {}
            },username_agent);

        return frameLayout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void getVisitesAgent(final CallBack callBack,String username_agent) {
        Links links = new Links();
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("username_agent",username_agent);

            jsonArray.put(1,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, links.getConsulterVisiteAgent(), jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Visite> visites = new ArrayList<Visite>();
                        Visite visite=null;
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonResponse=response.getJSONObject(i);
                                int id =jsonResponse.getInt("id");
                                String username_client=jsonResponse.getString("username_client");
                                String username_agent=jsonResponse.getString("username_agent");
                                String logement=jsonResponse.getString("logement");
                                long visite_date=jsonResponse.getLong("visite_date");
                                long visite_heure=jsonResponse.getLong("visite_heure");
                                int preavis=jsonResponse.getInt("preavis");
                                String etat=jsonResponse.getString("etat") ;
                                visite=new Visite
                                        (id,username_client,username_agent,logement,visite_date,visite_heure,preavis,etat);
                                visites.add(visite);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    callBack.onSuccess(visites);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}});
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

}