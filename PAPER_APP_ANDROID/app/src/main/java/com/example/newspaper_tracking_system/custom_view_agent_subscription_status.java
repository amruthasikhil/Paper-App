package com.example.newspaper_tracking_system;

import android.content.Context;
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

public class custom_view_agent_subscription_status extends BaseAdapter {
    SharedPreferences sh;
    ArrayList<String>agentname,agent_lid,place,district,pin,pic,stat;
    private Context context;
    public custom_view_agent_subscription_status(Context applicationContext, ArrayList<String> agentname, ArrayList<String> agent_lid, ArrayList<String>place,ArrayList<String>district, ArrayList<String>pin, ArrayList<String>pic, ArrayList<String>stat) {
        this.context=applicationContext;
        this.agentname=agentname;
        this.agent_lid=agent_lid;
        this.place=place;
        this.district=district;
        this.pin=pin;
        this.district=district;
        this.pic=pic;
        this.stat=stat;




    }
    @Override
    public int getCount() {
        return agent_lid.size();
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
            gridView = inflator.inflate(R.layout.custom_view_agent_subscription_status, null);
        }
        else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView44);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView46);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView47);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView80);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView81);
        ImageView im=(ImageView)gridView.findViewById(R.id.imageView6);

        tv1.setText("Name     :"+agentname.get(i));
        tv2.setText("place    :"+place.get(i));
        tv3.setText("District :"+district.get(i));
        tv4.setText("Pin      :"+pin.get(i));
        tv5.setText("Status   :"+stat.get(i));




        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
        String url1="http://"+sh.getString("ip","")+":5000"
                +pic.get(i);
        Picasso.with(context).load(url1.toString()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).error(R.drawable.ic_menu_gallery).into(im);
        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);


        return gridView;
    }
}
