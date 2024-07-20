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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class custom_view_my_classified_request extends BaseAdapter {
    SharedPreferences sh;
    ArrayList<String>provider_name,newsfile,description,date,status,amount;
    private Context context;
    public custom_view_my_classified_request(Context applicationContext, ArrayList<String> provider_name,  ArrayList<String> newsfile,  ArrayList<String>description, ArrayList<String>date, ArrayList<String>status, ArrayList<String>amount) {
        this.context=applicationContext;
        this.provider_name=provider_name;
        this.newsfile=newsfile;
        this.description=description;
        this.date=date;
        this.status=status;
        this.amount=amount;




    }
    @Override
    public int getCount() {
        return provider_name.size();
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
            gridView = inflator.inflate(R.layout.custom_view_my_classified_request, null);
        }
        else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView45);
        ImageView tv2 = (ImageView) gridView.findViewById(R.id.imageView3);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView49);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView51);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView53);

//        final Button b1 = (Button) gridView.findViewById(R.id.button5);
        Toast.makeText(context, newsfile.get(i), Toast.LENGTH_SHORT).show();
        SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(context);
        String url1="http://"+sh.getString("ip","")+":5000"
                +newsfile.get(i);
        Picasso.with(context).load(url1.toString()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                .transform(new CircleTransform()).error(R.drawable.ic_menu_gallery).into(tv2);




        tv1.setText(provider_name.get(i));

        tv3.setText(description.get(i));
        tv4.setText(status.get(i));
        tv5.setText(amount.get(i));






        tv1.setTextColor(Color.BLACK);
//        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);



        return gridView;
    }
}
