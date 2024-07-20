package com.example.newspaper_tracking_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class view_allocated_requests extends AppCompatActivity {
    ListView l1;
    SharedPreferences sh;
    ArrayList<String> name,provider,language,price,alocid,status,user_stat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_allocated_requests);
        l1=(ListView)findViewById(R.id.alocate);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        String ip = sh.getString("ip", "");
        String url = "http://" + ip + ":5000/paperboy_view_paper_allocation_list";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js= jsonObj.getJSONArray("data");
                                name=new ArrayList<>();
                                provider=new ArrayList<>();
                                language=new ArrayList<>();
                                price=new ArrayList<>();
                                alocid=new ArrayList<>();
                                status=new ArrayList<>();
                                user_stat=new ArrayList<>();

                                for(int i=0;i<js.length();i++){
                                    JSONObject u=js.getJSONObject(i);
                                    name.add(u.getString("user_name"));
                                    provider.add(u.getString("provider_name"));
                                    language.add(u.getString("edition_language"));
                                    price.add(u.getString("allocation_id"));
                                    alocid.add(u.getString("allocation_id"));
                                    status.add(u.getString("dst"));
                                    user_stat.add(u.getString("usrst"));

                                }

                                l1.setAdapter(new custom_view_allocated_request(getApplicationContext(),name,provider,language,price,alocid,status,user_stat));

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
}
