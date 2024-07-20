package com.example.newspaper_tracking_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Paperboy_duties extends AppCompatActivity implements View.OnClickListener {
    Button bb1,bb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paperboy_duties);
        bb1=(Button)findViewById(R.id.button17);
        bb2=(Button)findViewById(R.id.button18);
        bb1.setOnClickListener(this);
        bb2.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if(view==bb1){

            Intent ii=new Intent(getApplicationContext(),view_allocated_requests.class);
            startActivity(ii);
        }
        if(view==bb2){
            Intent ii=new Intent(getApplicationContext(),login.class);
            startActivity(ii);
        }


    }



}
