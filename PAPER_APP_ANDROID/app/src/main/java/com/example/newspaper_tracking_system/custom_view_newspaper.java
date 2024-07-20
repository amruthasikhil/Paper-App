package com.example.newspaper_tracking_system;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class custom_view_newspaper extends BaseAdapter {
    SharedPreferences sh;
    ArrayList<String> edition_id,place,pr_lid,provider,price,editon_language_id,edition_language;
    Button b1,b2,b3;
    private Context context;
    public custom_view_newspaper(Context applicationContext, ArrayList<String> edition_id,  ArrayList<String> place,  ArrayList<String>pr_lid, ArrayList<String>provider, ArrayList<String>price, ArrayList<String>editon_language_id, ArrayList<String>edition_language) {
        this.context=applicationContext;
        this.edition_id=edition_id;
        this.place=place;
        this.pr_lid=pr_lid;
        this.provider=provider;
        this.price=price;
        this.editon_language_id=editon_language_id;
        this.edition_language=edition_language;



    }

    @Override
    public int getCount() {
        return edition_id.size();
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
            gridView = inflator.inflate(R.layout.custom_view_newspaper, null);
        }
        else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView26);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView28);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView30);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView32);





        final Button b1 = (Button) gridView.findViewById(R.id.button7);


        b1.setTag(edition_id.get(i));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("edition_id",edition_id.get(i));
                ed.putString("amount",price.get(i));
                ed.putString("reqtype","Newspaper");
                ed.putString("edition_language_id",editon_language_id.get(i));
                ed.commit();
                Intent ij=new Intent(context,Payment.class);
                ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ij);


            }
        });

        tv1.setText(place.get(i));
        tv2.setText(provider.get(i));
        tv3.setText(price.get(i));
        tv4.setText(edition_language.get(i));
        return gridView;

    }
}
