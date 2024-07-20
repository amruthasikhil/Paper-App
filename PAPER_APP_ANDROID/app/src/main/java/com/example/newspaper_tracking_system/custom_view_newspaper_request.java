package com.example.newspaper_tracking_system;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class custom_view_newspaper_request extends BaseAdapter {
    SharedPreferences sh;
    ArrayList<String> paper_req_id,edition,language,request_date,status,start_date;
    Button b1,b2,b3;
    private Context context;
    public custom_view_newspaper_request(Context applicationContext, ArrayList<String> paper_req_id,  ArrayList<String> edition,  ArrayList<String>language, ArrayList<String>request_date, ArrayList<String>status, ArrayList<String>start_date) {
        this.context=applicationContext;
        this.paper_req_id=paper_req_id;
        this.edition=edition;
        this.language=language;
        this.request_date=request_date;
        this.status=status;
        this.start_date=start_date;



    }
    @Override
    public int getCount() {
        return paper_req_id.size();
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
            gridView = inflator.inflate(R.layout.custom_view_newspaper_request, null);
        }
        else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView38);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView39);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView40);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView41);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView42);
        TextView tv6 = (TextView) gridView.findViewById(R.id.textView37);

        tv1.setText(edition.get(i));
        tv2.setText(language.get(i));
        tv3.setText(request_date.get(i));
        tv4.setText(status.get(i));
        if(status.get(i).equalsIgnoreCase("approved")){
            tv5.setText(start_date.get(i));
            tv5.setVisibility(View.VISIBLE);
            tv6.setVisibility(View.VISIBLE);
        }
        else {
            tv5.setVisibility(View.INVISIBLE);
            tv6.setVisibility(View.INVISIBLE);
        }

        return gridView;
    }
}
