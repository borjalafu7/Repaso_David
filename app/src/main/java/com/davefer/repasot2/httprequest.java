package com.davefer.repasot2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class httprequest extends AppCompatActivity {

    private final static String URL_COMUNIDADES = "https://onthestage.es/restapi/v1/allcomunidades";
    TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_httprequest);

        tvResultado = findViewById(R.id.tvResultado);

        getComunidades();
    }

    private void getComunidades(){
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_COMUNIDADES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String s ="";
                try{
                    JSONArray datos = response.getJSONArray("DATA");
                    for(int i = 0;i<datos.length();i++){
                        JSONObject comunidad = datos.getJSONObject(i);
                        s +="\n" + comunidad.getString("descripcion");
                    }
                }catch (JSONException e){

                }
                tvResultado.setText(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tst","Error de Volley");
            }
        });

        queue.add(request);
    }
}