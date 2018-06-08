package com.example.android.navigationviewanddeveloper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import beans.Localite;
import beans.Logement;
import beans.Utilisateur;
import beans.Visite;
import databases.CallBack;
import databases.InputFilterRegex;
import databases.Links;


public class ConsulterCompte extends Fragment  {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;


    private Button send;
    private TextInputLayout nom;
    private TextInputLayout prenom;
    private TextInputLayout username;
    private TextInputLayout email;
    private TextInputLayout numTel;
    private TextInputLayout localiteInput;

    public ConsulterCompte() {
    }

    public static ConsulterCompte newInstance(String param1, String param2) {
        ConsulterCompte fragment = new ConsulterCompte();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_consulter_compte, container, false);


        final String usernameI = getArguments().getString("username");
        final String type = getArguments().getString("type");


        nom = (TextInputLayout) frameLayout.findViewById(R.id.nom);
        prenom = (TextInputLayout) frameLayout.findViewById(R.id.prenom);
        username = (TextInputLayout) frameLayout.findViewById(R.id.username);
        email = (TextInputLayout) frameLayout.findViewById(R.id.email);
        numTel = (TextInputLayout) frameLayout.findViewById(R.id.numTel);
        localiteInput = (TextInputLayout) frameLayout.findViewById(R.id.localite);


        nom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                nom.getEditText().setFilters(new InputFilter[]{
                        new InputFilterRegex("^[a-zA-Z]*$")});
            }
        });

        prenom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                prenom.getEditText().setFilters(new InputFilter[]{
                        new InputFilterRegex("^[a-zA-Z]*$")});
            }
        });

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                username.getEditText().setFilters(new InputFilter[]{
                        new InputFilterRegex("^[aA-zZ]\\w{5,19}$")});
            }
        });




        send = (Button) frameLayout.findViewById(R.id.send);

        if (type.equals("Client")) {
            localiteInput.setVisibility(View.GONE);
        }


        getCompte(new CallBack() {
            @Override public void onSuccess(ArrayList<Visite> visites) {}
            @Override public void onSuccessL(ArrayList<Logement> logements) {}
            @Override public void onSuccesPrendreVisite(Visite visite) {}
            @Override
            public void onSuccesConsulterCompte(Utilisateur utilisateur, Localite localite) {
                nom.getEditText().setText(utilisateur.getNom());
                prenom.getEditText().setText(utilisateur.getPrenom());
                username.getEditText().setText(utilisateur.getUsername());
                email.getEditText().setText(utilisateur.getEmail());
                numTel.getEditText().setText(utilisateur.getNumTel());




                if (type.equals("Agent"))
                    localiteInput.getEditText().setText(localite.getAddress());
                    localiteInput.setEnabled(false);
            }
        }, usernameI, type);

        send.setOnClickListener(new View.OnClickListener() {
            Links links = new Links();
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                final String nom1 = nom.getEditText().getText().toString();
                final String prenom1 = prenom.getEditText().getText().toString();
                final String username1 = username.getEditText().getText().toString();
                final String email1 = email.getEditText().getText().toString();
                final String numTel1 = numTel.getEditText().getText().toString();


                if(username1.length()<6){
                    username.setErrorEnabled(true);
                    username.setError("Username must be above 6 caracters");

                }else{
                    username.setErrorEnabled(false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, links.getModifierCompteURL(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getContext(), "" + response, Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}})
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {



                        Map<String, String> params = new HashMap<String, String>();

                        params.put("originalUser",usernameI);
                        params.put("nom", nom1);
                        params.put("prenom", prenom1);
                        params.put("username", username1);
                        params.put("email", email1);
                        params.put("numTel", numTel1);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);


                if(type.equals("Client")){
                    Intent intent =new Intent(getContext(),HomeClient.class);
                    intent.putExtra("username",username1);
                    startActivity(intent);
                }else{
                    Intent intent =new Intent(getContext(),HomeAgent.class);
                    intent.putExtra("username",username1);
                    startActivity(intent);
                }
                    getActivity().finishAffinity();

            }
            }
        });
        return frameLayout;
    }
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
    public void getCompte(final CallBack callBack, final String username, final String type) {
        Links links = new Links();
        JSONObject request = new JSONObject();

        try {
            request.put("username", username);
            request.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, links.getConsulterCompteURL(), request
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonUser) {
                Utilisateur utilisateur;

                Localite localite = null;

                try {


                    String nom = jsonUser.getString("nom");
                    String prenom = jsonUser.getString("prenom");
                    String username = jsonUser.getString("username");
                    String password = jsonUser.getString("password");
                    String email = jsonUser.getString("email");
                    String numTel = jsonUser.getString("numTel");
                    String etat = jsonUser.getString("etat");
                    String type = jsonUser.getString("type");
                    utilisateur = new Utilisateur(username, password, nom, prenom, email, numTel, etat, type);

                    if (type.equals("Agent")) {
                        JSONObject loc = jsonUser.getJSONObject("localite");
                        int id = loc.getInt("id");
                        String address = loc.getString("address");
                        localite = new Localite(id, address);
                    }

                    callBack.onSuccesConsulterCompte(utilisateur, localite);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }
}