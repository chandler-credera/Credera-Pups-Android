package com.example.crederapups;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AnimalsActivity extends AppCompatActivity {

    private Animal [] animals;
    private String bearerToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());



        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        bearerToken = preferences.getString(getString(R.string.token_key), "");
        String tokenExpiration = preferences.getString(getString(R.string.token_expiration_key), "");

        System.out.println("Token Expiration: " + tokenExpiration);

        try {
            SimpleDateFormat format = new SimpleDateFormat(getString(R.string.calendar_format));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(tokenExpiration));

            System.out.println("New Calendar Time: " + calendar.getTime());

            System.out.println(Calendar.getInstance().getTime());

            if(Calendar.getInstance().after(calendar)){
                System.out.println("Current Date is after expiration...");

                // send the request to get a new token
                JsonObjectRequest tokenRequest = getTokenRequest();
                requestQueue.add(tokenRequest);
            }
            else {
                System.out.println("Current Date is before expiration!");
            }

        }catch (Exception e){

        }

        // send the request to get animals

        JsonObjectRequest animalsRequest = getAnimalRequest();
        requestQueue.add(animalsRequest);




        // add the code here that will check to see if the bearer token is valid first
        //getAnimalRequest();



    }

    public JsonObjectRequest getAnimalRequest(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://api.petfinder.com/v2/animals?type=dog&limit=50&location=75001", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);

                        Gson gson = new Gson();
                        Animals tempAnimals = gson.fromJson(response.toString(), Animals.class);

                        System.out.println("Animals!!! " + tempAnimals);

                        for(Animal animal : tempAnimals.animals){
                            System.out.println(animal.name);
                            System.out.println(animal.url);
                        }

                        animals = tempAnimals.animals;

                        GridView gridView = findViewById(R.id.gridView);
                        gridView.setAdapter(new AnimalAdapter(getApplication(), animals));

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(AnimalsActivity.this, AnimalActivity.class);
                                intent.putExtra(getString(R.string.position_key), position);
                                startActivity(intent);
                            }
                        });

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        System.out.println(error);
                    }
                }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(getString(R.string.authorization), bearerToken);
                return params;
            }
        };

        return jsonObjectRequest;
    }

    public JsonObjectRequest getTokenRequest() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(getString(R.string.grant_type_key), getString(R.string.grant_type_value));
        params.put(getString(R.string.client_id_key), getString(R.string.client_id_value));
        params.put(getString(R.string.client_secret_key), getString(R.string.client_secret_value));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://api.petfinder.com/v2/oauth2/token", new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);

                        Gson gson = new Gson();
                        Token token = gson.fromJson(response.toString(), Token.class);

                        System.out.println(token.access_token);

                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.SECOND, token.expires_in);

                        System.out.println(calendar.getTime());

                        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(getString(R.string.token_key), "Bearer " + token.access_token);
                        editor.putString(getString(R.string.token_expiration_key), calendar.getTime().toString());
                        editor.apply();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        System.out.println(error);
                    }
                });

        return jsonObjectRequest;
    }
}
