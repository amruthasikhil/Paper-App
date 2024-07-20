package com.example.newspaper_tracking_system;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class custom_view_payment_history extends BaseAdapter {
    SharedPreferences sh;
    ArrayList<String> type,amount,date,payid;
    private Context context;
    public custom_view_payment_history(Context applicationContext, ArrayList<String> type,  ArrayList<String> amount,  ArrayList<String>date, ArrayList<String>payid) {
        this.context=applicationContext;
        this.type=type;
        this.amount=amount;
        this.date=date;
        this.payid=payid;




    }
    @Override
    public int getCount() {
        return payid.size();
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
            gridView = inflator.inflate(R.layout.custom_view_payment_history, null);
        }
        else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView64);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView66);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView68);




        tv1.setText(type.get(i));
        tv2.setText(amount.get(i));
        tv3.setText(date.get(i));








        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);





        return gridView;
    }
}
