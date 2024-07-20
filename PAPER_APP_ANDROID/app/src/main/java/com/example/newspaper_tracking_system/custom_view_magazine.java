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

public class custom_view_magazine extends BaseAdapter {
    SharedPreferences sh;
    ArrayList<String>mag_id,mag_name,pr_lid,language,price,provider;
    Button b1,b2,b3;
    private Context context;
    public custom_view_magazine(Context applicationContext, ArrayList<String> mag_id,  ArrayList<String> mag_name,  ArrayList<String>pr_lid, ArrayList<String>language, ArrayList<String>price, ArrayList<String>provider) {
        this.context=applicationContext;
        this.mag_id=mag_id;
        this.mag_name=mag_name;
        this.pr_lid=pr_lid;
        this.language=language;
        this.price=price;
        this.provider=provider;



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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (view == null) {
            gridView = new View(context);
            gridView = inflator.inflate(R.layout.custom_view_magazine, null);
        }
        else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView14);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView16);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView18);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView20);

        final Button b1 = (Button) gridView.findViewById(R.id.button5);




        tv1.setText(mag_name.get(i));
        tv2.setText(provider.get(i));
        tv3.setText(language.get(i));
        tv4.setText(price.get(i));


        b1.setTag(mag_id.get(i));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("mag_id",mag_id.get(i));
                ed.putString("amount",price.get(i));
                ed.putString("reqtype","Magazine");
                ed.commit();
                Intent ij=new Intent(context,Payment.class);
                ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ij);




            }
        });




        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);



        return gridView;
    }
}
