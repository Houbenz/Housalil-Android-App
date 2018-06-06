package databases;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import beans.Visite;

/**
 * Created by Houbenz on 26/05/2018.
 */

public class DataFetchClient {


    private static final String SERVER_IP_ADDRESS="192.168.1.5";
    private StringRequest stringRequest;
    private JsonArrayRequest jsonArrayRequest;
    private JsonObjectRequest jsonObjectRequest;


/*********************************CONSULTERVISITE*****************************************/

    public ArrayList<Visite> consulterVisite(String client){
        String url = SERVER_IP_ADDRESS+":8080/housalil/client/consulterVisiteClient";
        final ArrayList<Visite> visites =new ArrayList<Visite>();


        //put the client username as parameters
        JSONArray jsonArray= new JSONArray();
        HashMap<String ,String > params = new HashMap<String, String>();
        params.put("client",client);
        JSONObject jsonObject=new JSONObject(params);
        try {
            jsonArray.put(1,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonArrayRequest =new JsonArrayRequest(Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i=0;i<response.length();i++){
                            try {


                                JSONObject jsonObject1 = response.getJSONObject(i);

                                int id=jsonObject1.getInt("id") ;
                                String username_client=jsonObject1.getString("username_client") ;
                                String username_agent=jsonObject1.getString("username_agent") ;
                                String logement=jsonObject1.getString("logement");
                                long date=jsonObject1.getLong("date") ;
                                long time =jsonObject1.getLong("time");
                                int preavis =jsonObject1.getInt("id");
                                String etat =jsonObject1.getString("etat");



                                Visite visite =new Visite(id,username_client,username_agent,logement,date,time,preavis,etat);
                                visites.add(visite);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

                );



        return visites;
    }
}
