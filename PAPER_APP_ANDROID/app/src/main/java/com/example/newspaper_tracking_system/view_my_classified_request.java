package com.example.newspaper_tracking_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class view_my_classified_request extends AppCompatActivity {
    ListView l1;
    FloatingActionButton b1;
    SharedPreferences sh;
    ArrayList<String> provider_name,newsfile,description,date,status,amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_classified_request);
        l1=(ListView)findViewById(R.id.classfied_req);
        b1=(FloatingActionButton)findViewById(R.id.floatingActionButton3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),add_my_classifieds.class));
            }
        });
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        String ip = sh.getString("ip", "");
        String url = "http://" + ip + ":5000/user_view_classified_requests";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js= jsonObj.getJSONArray("data");
                                provider_name=new ArrayList<>();
                                newsfile=new ArrayList<>();
                                description=new ArrayList<>();
                                date=new ArrayList<>();
                                status=new ArrayList<>();
                                amount=new ArrayList<>();



                                for(int i=0;i<js.length();i++){
                                    JSONObject u=js.getJSONObject(i);
                                    provider_name.add(u.getString("provider_name"));
                                    newsfile.add(u.getString("news_file"));
                                    description.add(u.getString("description"));
                                    date.add(u.getString("date"));
                                    status.add(u.getString("status"));
                                    amount.add(u.getString("amount"));

                                }

                                l1.setAdapter(new custom_view_my_classified_request(getApplicationContext(),provider_name,newsfile,description,date,status,amount));


                            } else {
                                Toast.makeText(getApplicationContext(), "No Requests", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("lid",sh.getString("lid",""));
                params.put("agent_id",sh.getString("agent_id",""));
                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }
}
