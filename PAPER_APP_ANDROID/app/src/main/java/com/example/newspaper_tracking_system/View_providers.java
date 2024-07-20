package com.example.newspaper_tracking_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class View_providers extends AppCompatActivity {
    ListView l1;
    Spinner s1,s2;
    SharedPreferences sh;
    String[] pap={"Edition","magazine"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_provider);
        l1=(ListView)findViewById(R.id.content);
        s1=(Spinner)findViewById(R.id.provider);
        s2=(Spinner)findViewById(R.id.paper);
        ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,pap);
        s2.setAdapter(ad);
    }
}
