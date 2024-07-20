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

public class login extends AppCompatActivity implements View.OnClickListener
{
    EditText e1,e2;
    Button b1;
    SharedPreferences sh;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1=(EditText)findViewById(R.id.editText2);
        e2=(EditText)findViewById(R.id.editText3);
        t1=(TextView) findViewById(R.id.textView79);
        t1.setOnClickListener(this);
        b1=(Button)findViewById(R.id.button8);
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view==t1)
        {
            startActivity(new Intent(getApplicationContext(),registration.class));
        }
        String uname=e1.getText().toString();
        String passw=e2.getText().toString();

        int f=0;
        if (uname.equalsIgnoreCase(""))
        {
            f=1;
            e1.setError("enter data");
        }
        if (passw.equalsIgnoreCase(""))
        {
            f=1;
            e2.setError("enter data");
        }
        if (f==0)
        {
            final SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String ip = sh.getString("ip", "");
            String url = "http://" + ip + ":5000/and_login_post";

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                            //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                            // response
                            try
                            {
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj.getString("status").equalsIgnoreCase("ok"))
                                {
                                    String lid = jsonObj.getString("lid");
                                    SharedPreferences.Editor edt = sh.edit();
                                    edt.putString("lid", lid);
                                    edt.putString("type", jsonObj.getString("type"));
                                    edt.commit();
                                    String type = jsonObj.getString("type");


                                    if ((jsonObj.getString("type").equalsIgnoreCase("user"))||(jsonObj.getString("type").equalsIgnoreCase("subscriber")))
                                    {
//                                        startService(new Intent(login.this, LocationService.class));
                                        Intent i = new Intent(getApplicationContext(), UHOME.class);
                                        startActivity(i);
                                    }
                                    else
                                        {
//                                        startService(new Intent(login.this,notiservise.class));
                                        Intent ij = new Intent(getApplicationContext(), Paperboy_home.class);
                                        startActivity(ij);
                                        }
                                }
                                else
                                    {
                                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                                    }
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            // error
                            Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    )
            {
                @Override
                protected Map<String, String> getParams()
                {
                    SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("uname", e1.getText().toString());
                    params.put("pswd", e2.getText().toString());

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

//#################