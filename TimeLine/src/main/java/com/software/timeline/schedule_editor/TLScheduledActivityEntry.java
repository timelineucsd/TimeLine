package com.software.timeline.schedule_editor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.software.timeline.R;
import com.software.timeline.misc.TLApp;
import com.software.timeline.timelogger.TLTimeLoggerActiity;

import java.util.Map;

/**
 * Created by ubicomp_lab on 12/5/2015.
 */

public class TLScheduledActivityEntry extends Activity {

    public String actName;
    public int actID;
    public String dayval;
    String username;
    String userPID;
    int clab ;
    int coffice;
    int cdiscussion;
    int clecture;
    String startTime;
    String endTime;
    int endTime_minute;
    int endTime_hour;
    int startTime_hour;
    int startTime_minute;
    int count;
    public void add_entry(){

        Map<String, ?> all = getSharedPreferences(TLApp.USER_SHARED_PREFS, MODE_PRIVATE).getAll();
        System.out.println("Shared Preferences" + all);
        username = (String) all.get("name");
        userPID = (String) all.get("pid");
        try {
            clab = (Integer)all.get("count_lab");
            coffice = (Integer)all.get("count_office");
            cdiscussion = (Integer)all.get("count_discussion");
            clecture = (Integer)all.get("count_lecture");
        }
        catch (Exception e)
        {
            clab = 0;
            coffice = 0;
            cdiscussion = 0;
            clecture = 0;
        }

        System.out.println("From shared preferences" + clab + " "+coffice +  " "+ cdiscussion+ " "+clecture);
        //Select activity
        Spinner mSpinnerDropdown_act = (Spinner) findViewById(R.id.spinner3);
        final String[] items_act = new String[]{"Lab Hours", "Office Hours","Discussion Hours","Lecture Hours"};
        ArrayAdapter<String> adapter_act = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_act);
        mSpinnerDropdown_act.setAdapter(adapter_act);

        mSpinnerDropdown_act.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                 actName = items_act[pos];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Nothing from spinner item was selected");
            }

        });

        //Select day
        Spinner mSpinnerDropdown_day = (Spinner) findViewById(R.id.spinner4);
        final String[] items_day = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday" , "Friday", "Saturday"};
        ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_day);
        mSpinnerDropdown_day.setAdapter(adapter_day);

        mSpinnerDropdown_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                dayval = items_day[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Nothing from spinner item was selected");
            }

        });



        Button submit = (Button) findViewById(R.id.button2);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View w) {
                //Select StartTime

                Log.d("Activity name:", actName);
                if (actName.equalsIgnoreCase("Lab Hours")) {
                    System.out.println("Inside lab hours IF condition");
                    actID = 2;
                    clab += 1;
                    Log.d("Lab hours:", clab + "");
                    count = clab;
                }
                else if (actName.equalsIgnoreCase("Office Hours")) {
                    System.out.println("Inside office hours IF condition");
                    actID = 3;
                    coffice += 1;
                    count = coffice;
                }
                else if (actName.equalsIgnoreCase("Discussion Hours")) {
                    actID = 5;
                    cdiscussion += 1;
                    count = cdiscussion;
                }
                else{
                    actID = 7;
                    clecture += 1;
                    count = clecture;
                }

                TimePicker start_time = (TimePicker) findViewById(R.id.timePicker4);
                start_time.clearFocus();
                startTime_hour = start_time.getCurrentHour();
                startTime_minute = start_time.getCurrentMinute();
                startTime = startTime_hour + ":" + startTime_minute; //in millis

                //Select EndTime
                TimePicker end_time = (TimePicker) findViewById(R.id.timePicker5);
                end_time.clearFocus();
                endTime_hour = end_time.getCurrentHour();
                endTime_minute = end_time.getCurrentMinute();
                endTime = endTime_hour + ":" + endTime_minute; //in millis

                long min_millis = startTime_minute * 60 * 1000;
                long hour_millis = startTime_hour * 3600 * 1000;
                long starttime_millis = min_millis + hour_millis;

                long min_millis_end = endTime_minute * 60 * 1000;
                long hour_millis_end = endTime_hour * 3600 * 1000;
                long endtime_millis = min_millis_end + hour_millis_end;

                System.out.println("Stime in millis=" + starttime_millis);
                System.out.println("Etime in millis=" + endtime_millis);
                System.out.println("Difference" + (endtime_millis - starttime_millis));

                //   ParseObject object_act = new ParseObject("Activities");
                ParseObject object = new ParseObject("Schedule");
                // object.put("activityId",actID);
                //object.put("activityCount", count);
                //object.put("dayOfWeek", dayval);//day
                //object.put("startTime", startTime);//starttime
                // object.put("endTime", endTime);//endtime
                object.put("pid", userPID);
                object.put("activityId", actID);
                object.put("activityCount", count);
                object.put("StartTime", startTime);
                object.put("EndTime", endTime);
                object.put("dayOfWeek", dayval);
                object.put("startTime", starttime_millis);
                object.put("endTime", endtime_millis);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            // Success!
                            System.out.println("data put done");
                            Toast.makeText(getApplicationContext(), "Activity registered.", Toast.LENGTH_LONG).show();


                        } else {
                            // Failure!
                            Log.e(SyncStateContract.Constants.DATA, "Failure");
                            System.out.println("data put not done " + e);
                        }
                    }
                });

                SharedPreferences.Editor editor = getSharedPreferences(TLApp.USER_SHARED_PREFS, MODE_PRIVATE).edit();
                editor.putInt("count_lab", clab);
                editor.putInt("count_discussion", cdiscussion);
                editor.putInt("count_office", coffice);
                editor.putInt("count_lecture", clecture);
                editor.commit();

                System.out.println("From shared preferences later" + clab + " " + coffice + " " + cdiscussion + " " + clecture);

            }
        });

        Button done = (Button) findViewById(R.id.button3);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        count =0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity_entry);
        add_entry();
    }
}
