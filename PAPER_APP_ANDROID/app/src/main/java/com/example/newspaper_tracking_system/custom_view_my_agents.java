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
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class custom_view_my_agents extends BaseAdapter {
    SharedPreferences sh;
    ArrayList<String> name,place,district,pin,phone,email,image,lid;
    Button b1,b2,b3;
    private Context context;
    public custom_view_my_agents(Context applicationContext, ArrayList<String> name,  ArrayList<String> place,  ArrayList<String>district, ArrayList<String>pin, ArrayList<String>phone, ArrayList<String>email, ArrayList<String>image, ArrayList<String>lid) {
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
            gridView = inflator.inflate(R.layout.custom_view_my_agents, null);
        }
        else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.agname);
        TextView tv2 = (TextView) gridView.findViewById(R.id.agplace);
        TextView tv3 = (TextView) gridView.findViewById(R.id.agdis);
        TextView tv4 = (TextView) gridView.findViewById(R.id.agpin);
        TextView tv5 = (TextView) gridView.findViewById(R.id.agphon);
        TextView tv6 = (TextView) gridView.findViewById(R.id.agemail);
        ImageView im=(ImageView) gridView.findViewById(R.id.agpic);
        final Button b1 = (Button) gridView.findViewById(R.id.button2);
        final Button b2 = (Button) gridView.findViewById(R.id.button4);
        final Button b3 = (Button) gridView.findViewById(R.id.button10);
        final Button b4 = (Button) gridView.findViewById(R.id.button14);
        b1.setTag(lid.get(i));
        b2.setTag(lid.get(i));
        b3.setTag(lid.get(i));
        b4.setTag(lid.get(i));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("agent_id",lid.get(i));
                ed.commit();
                Intent ij=new Intent(context,view_magazine_request.class);
                ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ij);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);

                SharedPreferences.Editor ed=sh.edit();
                ed.putString("agent_id",lid.get(i));
                ed.commit();
                Intent ij=new Intent(context,view_newspaper_requests.class);
                ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ij);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);

                SharedPreferences.Editor ed=sh.edit();
                ed.putString("agent_id",lid.get(i));
                ed.commit();
                Intent ij=new Intent(context,view_my_classified_request.class);
                ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ij);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);

                SharedPreferences.Editor ed=sh.edit();
                ed.putString("agent_id",lid.get(i));
                ed.commit();
                Intent ij=new Intent(context,Reply.class);
                ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ij);
            }
        });



        tv1.setText(name.get(i));
        tv2.setText(place.get(i));
        tv3.setText(district.get(i));
        tv4.setText(pin.get(i));
        tv5.setText(phone.get(i));
        tv6.setText(email.get(i));

        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
        String url1="http://"+sh.getString("ip","")+":5000"
                +image.get(i);
        Picasso.with(context).load(url1.toString()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).error(R.drawable.ic_menu_gallery).into(im);

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);
        tv6.setTextColor(Color.BLACK);





        return gridView;
    }
}
