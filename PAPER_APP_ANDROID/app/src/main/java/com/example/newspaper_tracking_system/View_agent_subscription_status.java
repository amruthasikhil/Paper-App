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

public class View_agent_subscription_status extends AppCompatActivity implements View.OnClickListener {
    ListView l1;
    SharedPreferences sh;
    ArrayList<String>agentname,agent_lid,place,district,pin,pic,stat;
    FloatingActionButton f1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_agent_subscription_status);
        l1=(ListView)findViewById(R.id.subscription_agent);
        f1=(FloatingActionButton)findViewById(R.id.floatingActionButton5);
        f1.setOnClickListener(this);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ip = sh.getString("ip", "");
        String url = "http://" + ip + ":5000/user_view_subscriber_status";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                JSONArray js= jsonObj.getJSONArray("data");

                                agentname=new ArrayList<>();
                                agent_lid=new ArrayList<>();
                                place=new ArrayList<>();
                                district=new ArrayList<>();
                                pin=new ArrayList<>();
                                pic=new ArrayList<>();
                                stat=new ArrayList<>();


                                for(int i=0;i<js.length();i++){
                                    JSONObject u=js.getJSONObject(i);
                                    agentname.add(u.getString("agent_name"));
                                    agent_lid.add(u.getString("agent_login_id"));
                                    place.add(u.getString("agent_place"));
                                    district.add(u.getString("agent_district"));
                                    pin.add(u.getString("agent_pin"));
                                    pic.add(u.getString("agent_image"));
                                    stat.add(u.getString("status"));


                                }

                                l1.setAdapter(new custom_view_agent_subscription_status(getApplicationContext(),agentname,agent_lid,place,district,pin,pic,stat));

                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getApplicationContext(),view_agent.class));

    }
}
