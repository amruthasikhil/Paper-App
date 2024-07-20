package com.example.newspaper_tracking_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Update_status extends AppCompatActivity implements View.OnClickListener
{
    EditText e1,e2;
    Button b1;
    RadioButton r1,r2;
    TextView t1,t2,t3;
    SharedPreferences sh;
    String stat="";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);
        e1=(EditText)findViewById(R.id.editText6);
        e2=(EditText)findViewById(R.id.editText16);
        b1=(Button)findViewById(R.id.button16);
        r1=(RadioButton)findViewById(R.id.radioButton3);
        r2=(RadioButton)findViewById(R.id.radioButton4);
        t1=(TextView) findViewById(R.id.textView82);
        t2=(TextView) findViewById(R.id.textView83);
        t3=(TextView) findViewById(R.id.textView84);
        b1.setOnClickListener(this);
        final SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ip = sh.getString("ip", "");
        String url = "http://" + ip + ":5000/user_view_my_stat";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok"))
                            {

                                t1.setText("Updated Status:"+jsonObj.getString("stat"));
                                t2.setText("From Date     :"+jsonObj.getString("fro"));
                                t3.setText("To date       :"+jsonObj.getString("to"));

                            } else
                                {
                                Toast.makeText(getApplicationContext(), "No status Available", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e)
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
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
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
    public void onClick(View view)
    {
        if (e1.getText().toString().equalsIgnoreCase(""))
        {
            e1.setError("enter date");
        }
        if (e2.getText().toString().equalsIgnoreCase(""))
        {
            e2.setError("enter date");
        }
        if (r1.isChecked())
        {
            stat = "Available";
        }
        if (r2.isChecked())
        {
            stat = "Unavailable";
        }
        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String ip = sh.getString("ip", "");
            String url = "http://" + ip + ":5000/user_current_status";
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            try
                            {
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj.getString("status").equalsIgnoreCase("ok"))
                                {
                                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                                }
                                else
                                    {
                                    Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("lid", sh.getString("lid", ""));
                    params.put("from_date",e1.getText().toString());
                    params.put("to_date",e2.getText().toString());
                    params.put("stat",stat);
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

//###################