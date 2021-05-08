package com.example.myapplication2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.core.os.LocaleListCompat.create;
import static java.lang.Integer.numberOfLeadingZeros;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    TextClock textClock;
    Button btnAdd;
    TimePicker timePicker;
    PopupWindow popUp;
    ListView alarmlist;
    CheckBox cbAlarm;
    TextView txtAlarm;
    SwipeDetector swipeDetector;
    Context context;
    int min,hour;


    List<Boolean> cbox = new ArrayList();
    ArrayList<Times> arrayOfTimes = new ArrayList<Times>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout layout = new LinearLayout(this);
        btnAdd =(Button)findViewById(R.id.btnAdd);
        alarmlist = (ListView) findViewById(R.id.list);
        System.out.println("oncreate");

        swipeDetector = new SwipeDetector();


        MyAdapter adapter = new MyAdapter(this, arrayOfTimes);
        alarmlist.setAdapter(adapter);

        getAllAlarmsFromPreference();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button clicked");
                Intent activity2Intent = new Intent(getApplicationContext(), AddalarmActivity.class);
                startActivityForResult(activity2Intent,2);
            }
         });

        alarmlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Times obj= (Times)alarmlist.getItemAtPosition(position);
                adapter.remove(obj);
                alarmlist.requestLayout();
                adapter.notifyDataSetChanged();
                String[] tokens = obj.getTime().split(":");
                hour=parseInt(tokens[0]);
                min=parseInt(tokens[1]);

                CancelRepeatingAlarm(hour,min);

                return false;
            }
        });


    }

    protected void onPause() {
        super.onPause();
        putAllAlarmsInPreferences(arrayOfTimes);

    }

    public void putAllAlarmsInPreferences(ArrayList<Times> arrayOfTimes){
        SharedPreferences sharedPreferences = this.getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        int i=0;
        for(Times times:arrayOfTimes){
            editor.putString(String.valueOf(i),times.getTime());
            i++;
            //times.cb.booleanValue();
            //editor.putBoolean(String.valueOf(i),times.getBox().booleanValue());
            //i++;
        }
        editor.putInt("sizey",arrayOfTimes.size());

        editor.commit();
    }

    public void getAllAlarmsFromPreference(){
        SharedPreferences sharedPreferences = this.getPreferences(Activity.MODE_PRIVATE);
        int i=0;
        String clock;
        Boolean cb;
        int size=sharedPreferences.getInt("sizey",0);
        for(i=0;i<size;i++){
            clock = sharedPreferences.getString(String.valueOf(i)
            ,"00:00");
            //i++;
            //cb=sharedPreferences.getBoolean(String.valueOf(i),false);
            Times newTime= new Times(clock);

            arrayOfTimes.add(newTime);
        }




    }

    public void start(int hour, int min) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Date dat = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY, hour);
        cal_alarm.set(Calendar.MINUTE, min);
        cal_alarm.set(Calendar.SECOND, 0);
        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);

        manager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
    }

    public void CancelRepeatingAlarm(int hour, int min){
        //if it already exists, then replace it with this one
        Intent alertIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alertIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Date dat = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY, hour);
        cal_alarm.set(Calendar.MINUTE, min);
        cal_alarm.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, values);
        //alarmlist.setAdapter(adapter);

        MyAdapter adapter = new MyAdapter(this, arrayOfTimes);
        alarmlist.setAdapter(adapter);




        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (2) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    Integer returnhour = data.getIntExtra("hour",0);
                    Integer returnmin = data.getIntExtra("min",0);
                    System.out.println("activity worked");
                    Times newAlarm = new Times(returnhour+":"+returnmin);
                    adapter.add(newAlarm);
                    adapter.notifyDataSetChanged();
                    start(returnhour,returnmin);


                    for(Times times:arrayOfTimes){
                        System.out.println(times.getTime());
                        //System.out.println(times.getBox());

                    }

                }
                break;
            }
        }
    }



}
