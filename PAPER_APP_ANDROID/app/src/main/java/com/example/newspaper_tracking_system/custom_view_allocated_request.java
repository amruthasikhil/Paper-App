package com.example.newspaper_tracking_system;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class custom_view_allocated_request extends BaseAdapter {
    SharedPreferences sh;
    ArrayList<String> name,provider,language,price,alocid,status,user_stat;
    Button b1,b2;
    private Context context;
    public custom_view_allocated_request(Context applicationContext, ArrayList<String> name,  ArrayList<String> provider,  ArrayList<String>language, ArrayList<String>price, ArrayList<String>alocid, ArrayList<String>status, ArrayList<String>user_stat) {
        this.context=applicationContext;
        this.name=name;
        this.provider=provider;
        this.language=language;
        this.price=price;
        this.alocid=alocid;
        this.status=status;
        this.user_stat=user_stat;




    }
    @Override
    public int getCount() {
        return alocid.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (view == null) {
            gridView = new View(context);
            gridView = inflator.inflate(R.layout.custom_view_allocated_request, null);
        }
        else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView70);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView72);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView74);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView78);
        final TextView tv6 = (TextView) gridView.findViewById(R.id.textView85);
        tv6.setVisibility(View.INVISIBLE);

        b1=(Button)gridView.findViewById(R.id.button12);
        b2=(Button)gridView.findViewById(R.id.button13);
        if (status.get(i).equalsIgnoreCase("Allocated")) {
            b1.setVisibility(View.VISIBLE);
            b2.setVisibility(View.VISIBLE);
        }
        else {
            b1.setVisibility(View.GONE);
            b2.setVisibility(View.GONE);
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed=sh.edit();
                ed.commit();


                String ip = sh.getString("ip", "");
                String url = "http://" + ip + ":5000/paper_boy_delivered_alocation";


                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                        Toast.makeText(context, "Delivered...", Toast.LENGTH_SHORT).show();
                                        tv6.setText("Delivered");
                                        tv6.setVisibility(View.VISIBLE);
                                        b1.setVisibility(View.INVISIBLE);
                                        b2.setVisibility(View.INVISIBLE);

//                                        Intent ij=new Intent(context,Paperboy_home.class);
//                                        ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        context.startActivity(ij);


                                    } else {
                                        Toast.makeText(context, "Cannot do the operation", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(context, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(context, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                        Map<String, String> params = new HashMap<String, String>();


                        params.put("aloc_id",alocid.get(i) );


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
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed=sh.edit();
                ed.commit();


                String ip = sh.getString("ip", "");
                String url = "http://" + ip + ":5000/paper_boy_not_delivered_alocation";


                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                        Toast.makeText(context, "Not delivered...", Toast.LENGTH_SHORT).show();
                                        tv6.setText("UnDelivered");
                                        tv6.setVisibility(View.VISIBLE);

                                        b1.setVisibility(View.INVISIBLE);
                                        b2.setVisibility(View.INVISIBLE);

//                                        Intent ij=new Intent(context,Paperboy_home.class);
//                                        ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        context.startActivity(ij);


                                    } else {
                                        Toast.makeText(context, "Cannot do the operation", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(context, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(context, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                        Map<String, String> params = new HashMap<String, String>();


                        params.put("aloc_id",alocid.get(i) );


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
        });

        tv1.setText(name.get(i));
        tv2.setText(provider.get(i));
        tv3.setText(language.get(i));
        tv5.setText(user_stat.get(i));







        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);




        return gridView;
    }
}
