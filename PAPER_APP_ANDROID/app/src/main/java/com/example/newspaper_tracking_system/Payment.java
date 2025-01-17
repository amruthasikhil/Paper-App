package com.example.newspaper_tracking_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity implements View.OnClickListener {
    EditText e1,e2;
    Button b1;
    TextView t1;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        e1=(EditText)findViewById(R.id.editText4);
        e2=(EditText)findViewById(R.id.editText5);
        b1=(Button)findViewById(R.id.button9);
        t1=(TextView)findViewById(R.id.textView43);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        t1.setText(sh.getString("amount",""));
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String accno=e1.getText().toString();
        final String pin=e2.getText().toString();
        if(accno.equalsIgnoreCase("")){
            e1.setError("input Account Number");
        }
        if(pin.equalsIgnoreCase("")){
            e2.setError("input Pin");
        }
        else {
            if(sh.getString("reqtype","").equalsIgnoreCase("Magazine")) {
                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String ip = sh.getString("ip", "");
                String url = "http://" + ip + ":5000/user_sent_request";


                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                        Toast.makeText(getApplicationContext(), "Requested....", Toast.LENGTH_SHORT).show();

                                    } else if (jsonObj.getString("status").equalsIgnoreCase("no")) {

                                        Toast.makeText(getApplicationContext(), "Already requested", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Cannot do the operation", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        Map<String, String> params = new HashMap<String, String>();


                        params.put("mag_id", sh.getString("mag_id", ""));
                        params.put("lid", sh.getString("lid", ""));
                        params.put("agent_id", sh.getString("agent_id", ""));
                        params.put("amount", sh.getString("amount", ""));
                        params.put("accno", accno);
                        params.put("pin", pin);
                        params.put("type", sh.getString("reqtype", ""));


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
            if (sh.getString("reqtype","").equalsIgnoreCase("Newspaper")){
                String ip = sh.getString("ip", "");
                String url = "http://" + ip + ":5000/user_sent_request";


                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                            Toast.makeText(getApplicationContext(), "Requested....", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),UHOME.class));

                                        } else if (jsonObj.getString("status").equalsIgnoreCase("no")) {

                                            Toast.makeText(getApplicationContext(), "Already requested", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),UHOME.class));
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Cannot do the operation, Invalid Accno and Pin", Toast.LENGTH_LONG).show();
                                        }


                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        Map<String, String> params = new HashMap<String, String>();


                        params.put("edition_id",sh.getString("edition_id","") );
                        params.put("lid", sh.getString("lid",""));
                        params.put("agent_id", sh.getString("agent_id",""));
                        params.put("edition_language_id",sh.getString("edition_language_id",""));
                        params.put("amount", sh.getString("amount", ""));
                        params.put("accno", accno);
                        params.put("pin", pin);
                        params.put("type", sh.getString("reqtype", ""));

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
    }
}
