package com.example.android.navigationviewanddeveloper;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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


public class LoginActivity extends AppCompatActivity {


    Links links =new Links();
    private TextInputLayout inputTextusername;
    private TextInputLayout inputTextpassword;
    private Button connect;
    RelativeLayout registerLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputTextusername=(TextInputLayout)findViewById(R.id.InputTextusername);
        inputTextpassword=(TextInputLayout)findViewById(R.id.InputTextpassword);

        inputTextusername.setHint("Nom d'utilisateur");
        inputTextpassword.setHint("Mot de passe");



        //Networking inside OnClick button
        connect=(Button)findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final  String username = inputTextusername.getEditText().getText().toString();
               final  String password = inputTextpassword.getEditText().getText().toString();

              StringRequest  stringRequest = new StringRequest(Request.Method.POST, links.getLoginActivityURL(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent;
                        if (response.equals("Client")) {
                            Toast.makeText(getApplicationContext(),"im a client "+username,Toast.LENGTH_SHORT).show();
                            intent=new Intent(getApplicationContext(),HomeClient.class);
                            Log.i("MESSa","username = "+username);
                            intent.putExtra("username",username);
                            startActivity(intent);
                            finish();
                        } else {
                            if (response.equals("Agent")) {
                                Toast.makeText(getApplicationContext(),"im an agent",Toast.LENGTH_SHORT).show();
                                intent=new Intent(getApplicationContext(),HomeAgent.class);
                                intent.putExtra("username",username);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),"False",Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                      public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"error : "+error,Toast.LENGTH_SHORT).show();
                        Log.i("ERR","error ==> "+error);
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String ,String > params= new HashMap<String ,String>();
                        params.put("username",username);
                        params.put("password",password);
                        return params;
                    }
                };
                RequestQueue requestQueue =Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });

        registerLink=(RelativeLayout)findViewById(R.id.registerLink);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });


}
}
