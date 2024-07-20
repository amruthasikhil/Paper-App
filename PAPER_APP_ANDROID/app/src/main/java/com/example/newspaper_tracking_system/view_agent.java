package com.example.newspaper_tracking_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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

public class view_agent extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    ListView l1;
    ArrayList<String> name,place,district,pin,phone,email,image,lid;
    SharedPreferences sh;
    Spinner s1;
    String sel_dist="";
    String[] dist={"Thiruvananthapuram","Kollam","Alappuzha","Pathanamthitta","Kottayam","Idukki","Ernakulam","Thrissur","Palakkad","Malappuram","Kozhikode","Wayanad","Kannur","Kasaragod"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_agent);
        l1=(findViewById(R.id.agent));
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        s1=(Spinner)findViewById(R.id.spinner3);
        ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,dist);
        s1.setAdapter(ad);
        s1.setOnItemSelectedListener(this);

        String ip = sh.getString("ip", "");
        String url = "http://" + ip + ":5000/user_view_agent";
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
                                phone=new ArrayList<>();
                                place=new ArrayList<>();
                                district=new ArrayList<>();
                                pin=new ArrayList<>();
                                email=new ArrayList<>();
                                image=new ArrayList<>();
                                lid=new ArrayList<>();

                                for(int i=0;i<js.length();i++){
                                    JSONObject u=js.getJSONObject(i);
                                    name.add(u.getString("agent_name"));
                                    phone.add(u.getString("agent_contact_number"));
                                    place.add(u.getString("agent_place"));
                                    district.add(u.getString("agent_district"));
                                    pin.add(u.getString("agent_pin"));
                                    email.add(u.getString("agent_email_id"));
                                    image.add(u.getString("agent_image"));
                                    lid.add(u.getString("agent_login_id"));

                                }

                                l1.setAdapter(new custom_view_agent(getApplicationContext(),name,place,district,pin,phone,email,image,lid));

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        sel_dist=dist[i];
        String ip = sh.getString("ip", "");
        String url = "http://" + ip + ":5000/user_view_agent_by_district";
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
                                phone=new ArrayList<>();
                                place=new ArrayList<>();
                                district=new ArrayList<>();
                                pin=new ArrayList<>();
                                email=new ArrayList<>();
                                image=new ArrayList<>();
                                lid=new ArrayList<>();
                                for(int i=0;i<js.length();i++){
                                    JSONObject u=js.getJSONObject(i);
                                    name.add(u.getString("agent_name"));
                                    phone.add(u.getString("agent_contact_number"));
                                    place.add(u.getString("agent_place"));
                                    district.add(u.getString("agent_district"));
                                    pin.add(u.getString("agent_pin"));
                                    email.add(u.getString("agent_email_id"));
                                    image.add(u.getString("agent_image"));
                                    lid.add(u.getString("agent_login_id"));
                                }

                                l1.setAdapter(new custom_view_agent(getApplicationContext(),name,place,district,pin,phone,email,image,lid));

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
                params.put("dist",sel_dist);
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
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
