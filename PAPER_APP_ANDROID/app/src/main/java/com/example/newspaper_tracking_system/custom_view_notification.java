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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class custom_view_notification extends BaseAdapter {
    SharedPreferences sh;
    ArrayList<String> provider_name,place,date,notification,noti_id;
    Button b1,b2,b3;
    private Context context;
    public custom_view_notification(Context applicationContext, ArrayList<String> provider_name,  ArrayList<String> place,  ArrayList<String>date, ArrayList<String>notification, ArrayList<String>noti_id) {
        this.context=applicationContext;
        this.provider_name=provider_name;
        this.place=place;
        this.date=date;
        this.notification=notification;
        this.noti_id=noti_id;



    }
    @Override
    public int getCount() {
        return noti_id.size();
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
            gridView = inflator.inflate(R.layout.custom_view_notification, null);
        }
        else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView56);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView58);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView60);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView62);



        tv1.setText(provider_name.get(i));
        tv2.setText(place.get(i));
        tv3.setText(date.get(i));
        tv4.setText(notification.get(i));







        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);




        return gridView;

    }
}
