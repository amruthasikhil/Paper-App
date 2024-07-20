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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class custom_view_agent extends BaseAdapter {
    SharedPreferences sh;
    ArrayList<String>  name,place,district,pin,phone,email,image,lid;
    Button b1,b2,b3;
    private Context context;
    public custom_view_agent(Context applicationContext, ArrayList<String> name,  ArrayList<String> place,  ArrayList<String>district, ArrayList<String>pin, ArrayList<String>phone, ArrayList<String>email, ArrayList<String>image, ArrayList<String>lid) {
        this.context=applicationContext;
        this.name=name;
        this.place=place;
        this.district=district;
        this.pin=pin;
        this.phone=phone;
        this.email=email;
        this.image=image;
        this.lid=lid;



    }

    @Override
    public int getCount() {
        return lid.size();
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
            gridView = inflator.inflate(R.layout.custom_view_agent, null);
        }
        else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView2);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView8);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView9);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView10);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView11);
        TextView tv6 = (TextView) gridView.findViewById(R.id.textView12);
        final Button b3 = (Button) gridView.findViewById(R.id.button3);
//        final Button b5 = (Button) gridView.findViewById(R.id.button10);


        b3.setTag(lid.get(i));
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed=sh.edit();
                ed.commit();


                String ip = sh.getString("ip", "");
                String url = "http://" + ip + ":5000/user_sent_subscriber_request";


                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                        Toast.makeText(context, "Requested....", Toast.LENGTH_SHORT).show();
//                                        Intent ij = new Intent(context, view_agent.class);
//                                        ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        context.startActivity(ij);
                                    }else if(jsonObj.getString("status").equalsIgnoreCase("no")){
                                        Toast.makeText(context, "Already Requested....", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(jsonObj.getString("status").equalsIgnoreCase("1")){
                                        Toast.makeText(context, "Request Updated....", Toast.LENGTH_SHORT).show();
                                    }else {
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


                        params.put("agent_id",lid.get(i).toString() );
                        params.put("lid",sh.getString("lid","") );


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


        ImageView img=(ImageView)gridView.findViewById(R.id.imageView);

        tv1.setText(name.get(i));
        tv2.setText(place.get(i));
        tv3.setText(district.get(i));
        tv4.setText(pin.get(i));
        tv5.setText(phone.get(i));
        tv6.setText(email.get(i));
        SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(context);
        String url1="http://"+sh.getString("ip","")+":5000"
                +image.get(i);
        Toast.makeText(context, url1, Toast.LENGTH_SHORT).show();
        Picasso.with(context).load(url1.toString()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).error(R.drawable.ic_menu_gallery).into(img);







        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);
        tv6.setTextColor(Color.BLACK);



        return gridView;

    }
}
