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

public class custom_view_magazine_request extends BaseAdapter {
    SharedPreferences sh;
    ArrayList<String> mag_id,mag_name,pr_lid,language,price,provider,status,req_date,start_date;
    Button b1,b2,b3;
    private Context context;
    public custom_view_magazine_request(Context applicationContext, ArrayList<String> mag_id,  ArrayList<String> mag_name,  ArrayList<String>pr_lid, ArrayList<String>language, ArrayList<String>price, ArrayList<String>provider, ArrayList<String>status, ArrayList<String>req_date, ArrayList<String>start_date) {
        this.context=applicationContext;
        this.mag_id=mag_id;
        this.mag_name=mag_name;
        this.pr_lid=pr_lid;
        this.language=language;
        this.price=price;
        this.provider=provider;
        this.status=status;
        this.req_date=req_date;
        this.start_date=start_date;


    }
    @Override
    public int getCount() {
        return mag_id.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (view == null) {
            gridView = new View(context);
            gridView = inflator.inflate(R.layout.custom_view_magazine_request, null);
        }
        else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textViewmag);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textViewpro);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textViewlan);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textViewprice);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView22);

        final Button b1 = (Button) gridView.findViewById(R.id.button6);
        b1.setTag(mag_id.get(i));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed=sh.edit();
                ed.commit();


                String ip = sh.getString("ip", "");
                String url = "http://" + ip + ":5000/user_remove_magazine_request";


                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                        Toast.makeText(context, "Removed....", Toast.LENGTH_SHORT).show();
                                        Intent ij=new Intent(context,UHOME.class);
                                        ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(ij);


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


                        params.put("mag_req_id",b1.getTag().toString() );


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




        tv1.setText(mag_name.get(i));
        tv2.setText(provider.get(i));
        tv3.setText(language.get(i));
        tv4.setText(price.get(i));
        tv5.setText(req_date.get(i));

        String st=status.get(i);



        if(st.equalsIgnoreCase("Approved")){
//            tv6.setText(start_date.get(i));

            b1.setVisibility(View.INVISIBLE);


        }
        else {



            b1.setVisibility(View.VISIBLE);

        }

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);



        return gridView;
    }
}
