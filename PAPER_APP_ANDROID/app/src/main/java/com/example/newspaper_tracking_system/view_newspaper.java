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

public class view_newspaper extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    ListView l1;
    Spinner s1,s2;
    SharedPreferences sh;
    ArrayList<String>lid,prname;
    ArrayList<String>lang_id,lan;
    ArrayList<String> edition_id,place,pr_lid,provider,price,edition_lang_id,edition_language;
    String sel_lan="",sel_provider="";
    String prdit="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_newspaper);
        l1=(ListView)findViewById(R.id.paper1);
        s1=(Spinner)findViewById(R.id.provider2);
        s2=(Spinner)findViewById(R.id.lang2);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        String ip = sh.getString("ip", "");
        String url = "http://" + ip + ":5000/and_view_provider";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js= jsonObj.getJSONArray("data");
                                lid=new ArrayList<>();
                                prname=new ArrayList<>();

                                for(int i=0;i<js.length();i++){
                                    JSONObject u=js.getJSONObject(i);
                                    lid.add(u.getString("provider_login_id"));
                                    prname.add(u.getString("provider_name"));
                                }

                                ArrayAdapter<String> ad1=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,prname);
                                s1.setAdapter(ad1);

                            } else {
                                Toast.makeText(getApplicationContext(), "No Providers", Toast.LENGTH_LONG).show();
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


                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);


        String url2 = "http://" + ip + ":5000/user_view_edition_language";
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest2 = new StringRequest(Request.Method.POST, url2,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js= jsonObj.getJSONArray("data");
                                lang_id=new ArrayList<>();
                                lan=new ArrayList<>();

                                for(int i=0;i<js.length();i++){
                                    JSONObject u=js.getJSONObject(i);
                                    lang_id.add(u.getString("language_id"));
                                    lan.add(u.getString("edition_language"));
                                }

                                ArrayAdapter<String> ad2=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,lan);
                                s2.setAdapter(ad2);

                            } else {
                                Toast.makeText(getApplicationContext(), "No Languages", Toast.LENGTH_LONG).show();
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

                params.put("prdid",prdit);
                return params;
            }
        };


        postRequest2.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue2.add(postRequest2);

        String url1 = "http://" + ip + ":5000/user_view_editions";
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest1 = new StringRequest(Request.Method.POST, url1,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js= jsonObj.getJSONArray("data");
                                edition_id=new ArrayList<>();
                                place=new ArrayList<>();
                                pr_lid=new ArrayList<>();
                                provider=new ArrayList<>();
                                price=new ArrayList<>();
                                edition_lang_id=new ArrayList<>();
                                edition_language=new ArrayList<>();



                                for(int i=0;i<js.length();i++){
                                    JSONObject u=js.getJSONObject(i);
                                    edition_id.add(u.getString("edition_id"));
                                    place.add(u.getString("place"));
                                    pr_lid.add(u.getString("provider_login_id"));
                                    provider.add(u.getString("provider_name"));
                                    price.add(u.getString("price"));
                                    edition_lang_id.add(u.getString("language_id"));
                                    edition_language.add(u.getString("edition_language"));

                                }

                                l1.setAdapter(new custom_view_newspaper(getApplicationContext(),edition_id,place,pr_lid,provider,price,edition_lang_id,edition_language));


                            } else {
                                Toast.makeText(getApplicationContext(), "No Editions", Toast.LENGTH_LONG).show();
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


                return params;
            }
        };
        postRequest1.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(postRequest1);


        s1.setOnItemSelectedListener(this);
        s2.setOnItemSelectedListener(this);

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView==s1){
            sel_provider=lid.get(i);

        }
        if(adapterView==s2){
            sel_lan=lang_id.get(i);
        }
        String ip = sh.getString("ip", "");
        String url2 = "http://" + ip + ":5000/user_view_edition_extra";
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest2 = new StringRequest(Request.Method.POST, url2,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js= jsonObj.getJSONArray("data");
                                edition_id=new ArrayList<>();
                                place=new ArrayList<>();
                                pr_lid=new ArrayList<>();
                                provider=new ArrayList<>();
                                price=new ArrayList<>();
                                edition_lang_id=new ArrayList<>();
                                edition_language=new ArrayList<>();



                                for(int i=0;i<js.length();i++){
                                    JSONObject u=js.getJSONObject(i);
                                    edition_id.add(u.getString("edition_id"));
                                    place.add(u.getString("place"));
                                    pr_lid.add(u.getString("provider_login_id"));
                                    provider.add(u.getString("provider_name"));
                                    price.add(u.getString("price"));
                                    edition_lang_id.add(u.getString("language_id"));
                                    edition_language.add(u.getString("edition_language"));

                                }

                                l1.setAdapter(new custom_view_newspaper(getApplicationContext(),edition_id,place,pr_lid,provider,price,edition_lang_id,edition_language));



                            } else {
                                Toast.makeText(getApplicationContext(), "No Providers", Toast.LENGTH_LONG).show();
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
                params.put("lang",sel_lan);
                params.put("provider_login_id",sel_provider);

                return params;
            }
        };
        int MY_SOCKET_TIMEOUT_MS = 100000;
        postRequest2.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue2.add(postRequest2);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }
}
