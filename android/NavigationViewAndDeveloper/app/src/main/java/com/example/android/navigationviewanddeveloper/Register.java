package com.example.android.navigationviewanddeveloper;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
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

import databases.InputFilterRegex;
import databases.Links;

public class Register extends AppCompatActivity {


    private TextInputLayout username,nom,prenom,pass,passconf,email,numTel;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        username=(TextInputLayout)findViewById(R.id.username);
        nom=(TextInputLayout)findViewById(R.id.nom);
        prenom=(TextInputLayout)findViewById(R.id.prenom);
        pass=(TextInputLayout)findViewById(R.id.pass);
        passconf=(TextInputLayout)findViewById(R.id.passconf);
        email=(TextInputLayout)findViewById(R.id.email);
        numTel=(TextInputLayout)findViewById(R.id.numTel);

        username.getEditText().setFilters(new InputFilter[]{new InputFilterRegex("^[aA-zZ]\\w{0,20}$")});
        nom.getEditText().setFilters(new InputFilter[]{new InputFilterRegex("^[a-zA-Z]*$")});
        prenom.getEditText().setFilters(new InputFilter[]{new InputFilterRegex("^[a-zA-Z]*$")});

        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Links links = new Links();

                final String usernameS =username.getEditText().getText().toString();
                final String nomS=nom.getEditText().getText().toString() ;
                final String prenomS=prenom.getEditText().getText().toString() ;
                final String passS=pass.getEditText().getText().toString() ;
                final String passconfS=passconf.getEditText().getText().toString();
                final String emailS =email.getEditText().getText().toString();
                final String numTelS=numTel.getEditText().getText().toString();
                if(!passS.equals(passconfS)){
                    pass.setErrorEnabled(true);
                    pass.setError("Password don't match");
                    username.setErrorEnabled(false);
                }else{
                    if(usernameS.length()<6){
                        username.setErrorEnabled(true);
                        username.setError("username must above 6 caracters ");
                        pass.setErrorEnabled(false);
                    }else {

                        pass.setErrorEnabled(false);
                        username.setErrorEnabled(false);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, links.getRegisterURL(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), "response : " + response, Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "error ==> " + error, Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("username", usernameS);
                                params.put("nom", nomS);
                                params.put("prenom", prenomS);
                                params.put("pass", passS);
                                //params.put("passcon", passconfS);
                                params.put("email", emailS);
                                params.put("numTel", numTelS);

                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);
                    }
                }



            }
        });



    }

}


