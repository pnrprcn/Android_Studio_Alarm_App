package com.example.myapplication2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class AddalarmActivity extends AppCompatActivity {

    Button btnSave;
    Button btnNotSave;
    TimePicker timepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addalarm);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnNotSave =(Button)findViewById(R.id.btnNotSave);
        timepicker =(TimePicker)findViewById(R.id.timepicker);


        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("button save clicked");
                timepicker.getHour();
                //System.out.println(timepicker.getHour());
                //System.out.println(timepicker.getMinute());
                Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
                resultIntent.putExtra("hour", timepicker.getHour());
                resultIntent.putExtra("min", timepicker.getMinute());
                setResult(Activity.RESULT_OK, resultIntent);
                AddalarmActivity.super.finish();
            }
        });

        btnNotSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("button Notsave clicked");
                AddalarmActivity.super.finish();
            }
        });
    }



}
