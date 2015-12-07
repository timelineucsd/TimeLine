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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.software.timeline.R;
import com.software.timeline.misc.TLApp;

import java.util.List;


/**
 * Created by ubicomp_lab on 12/4/2015.
 */
public class TLEditorActivity extends Activity {
    public String dayval;
    int actCount;
    int actID;
    int startTime_hour;
    int endTime_hour;
    int startTime_minute;
    int endTime_minute;
    String userPID;
    String actName;
    String new_startTime;
    String new_endTime;

    private void initTime(){
        TextView start = (TextView)findViewById(R.id.textView3);
        TextView end = (TextView)findViewById(R.id.textView2);
        start.setText("Start Time");
        end.setText("End Time");
    }

    private void updateTable() {

        Spinner mSpinnerDropdown = (Spinner) findViewById(R.id.spinner2);
        final String[] items = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday" , "Friday", "Saturday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mSpinnerDropdown.setAdapter(adapter);

        mSpinnerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                dayval = items[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Nothing from spinner item was selected");
            }

        });

        Button delete = (Button) findViewById(R.id.button4);
        delete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View w) {
                deletebuttonClicked();
            }

        });

        Button submit = (Button) findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View w) {

                TimePicker start_time = (TimePicker) findViewById(R.id.timePicker2);
                TimePicker end_time = (TimePicker) findViewById(R.id.timePicker3);
                start_time.clearFocus();
                end_time.clearFocus();
                startTime_hour = start_time.getCurrentHour();
                endTime_hour = end_time.getCurrentHour();
                startTime_minute = start_time.getCurrentMinute();//tomillis()- everything
                endTime_minute = end_time.getCurrentMinute();

                new_startTime = startTime_hour + ":" + startTime_minute;
                new_endTime = endTime_hour + ":" + endTime_minute;

                ParseQuery<ParseObject> query;
                query = ParseQuery.getQuery("Schedule");
                query.whereEqualTo("pid", userPID);
              /*  query.whereEqualTo("activityId", actID);
                query.whereEqualTo("activityCount", actCount);

                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
                        if (e == null) {
                            System.out.println("Objects retrieved: " + objects.size());
                            System.out.println("StartTime: " + new_startTime);
                            System.out.println("EndTime: " + new_endTime);
                            System.out.println("Day: " + dayval);
                            for (ParseObject value : objects) {
                                value.put("dayOfWeek", dayval);//day
                                value.put("startTime", new_startTime);//starttime
                                value.put("endTime", new_endTime);//endtime
                                value.saveInBackground();
                            }
                        } else {
                            Log.e(SyncStateContract.Constants.DATA, "Failure");
                            System.out.println("data get not done " + e);
                        }
                    }
                }); */

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        System.out.println("inside parse done function");
                        if (e == null) {
                            System.out.println("Objects size1: " + objects.size());
                            for (int i = 0; i < objects.size(); i++) {
                                if (objects.get(i).getInt("activityId") == actID) {
                                    System.out.println("number after first if :" + objects.get(i).getInt("activityId"));
                                    if (objects.get(i).getInt("activityCount") == actCount) {
                                        System.out.println("number after second if :" + objects.get(i).getInt("activityCount"));
                                        try {
                                            objects.get(i).delete();
                                            System.out.println("delete done");
                                            addRecord();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    public void addRecord(){
        System.out.println("put start");
        ParseObject obj = new ParseObject("Schedule");
        System.out.println("Object created");


        long min_millis = startTime_minute*60*1000;
        long hour_millis = startTime_hour*3600*1000;
        long starttime_millis = min_millis+hour_millis;

        long min_millis_end = endTime_minute*60*1000;
        long hour_millis_end = endTime_hour*3600*1000;
        long endtime_millis = min_millis_end+hour_millis_end;

        obj.put("pid", userPID);
        obj.put("activityId", actID);
        obj.put("activityCount", actCount);
        obj.put("StartTime", new_startTime);
        obj.put("EndTime", new_endTime);
        obj.put("dayOfWeek", dayval);
        obj.put("startTime",starttime_millis);
        obj.put("endTime",endtime_millis);
        System.out.println("Values: "+userPID+" "+actID+" "+actCount+" "+new_startTime+" "+new_endTime+" "+dayval);
        System.out.println("Before save");
        obj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    // Success!
                    System.out.println("data put done");
                    Toast.makeText(getApplicationContext(), "Activity updated.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(TLEditorActivity.this,TLScheduleEditorActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    // Failure!
                    Log.e(SyncStateContract.Constants.DATA, "Failure");
                    System.out.println("data put not done " + e);
                }
            }
        });
    }

    public void deletebuttonClicked(){
        ParseQuery<ParseObject> query;
        query = ParseQuery.getQuery("Schedule");
        query.whereEqualTo("pid", userPID);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                System.out.println("inside parse done function");
                if (e == null) {
                    System.out.println("Objects size1: " + objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        if (objects.get(i).getInt("activityId") == actID) {
                            System.out.println("number after first if :" + objects.get(i).getInt("activityId"));
                            if (objects.get(i).getInt("activityCount") == actCount) {
                                System.out.println("number after second if :" + objects.get(i).getInt("activityCount")+ " "+actCount);
                                try {
                                    objects.get(i).delete();
                                    System.out.println("delete done");
                                    SharedPreferences.Editor editor = getSharedPreferences(TLApp.USER_SHARED_PREFS, MODE_PRIVATE).edit();
                                    if (actID == 2){
                                        editor.putInt("count_lab", --actCount);
                                        System.out.println("Deleted actcount lab: "+ actCount);
                                    }
                                    else if (actID == 5){
                                        editor.putInt("count_discussion", --actCount);
                                        System.out.println("Deleted actcount discussion: " + actCount);
                                    }
                                    else if (actID == 3){
                                        editor.putInt("count_office", --actCount);
                                        System.out.println("Deleted actcount office: " + actCount);
                                    }
                                    else if (actID == 7){
                                        editor.putInt("count_lecture", --actCount);
                                        System.out.println("Deleted actcount lecture: " + actCount);
                                    }
                                    editor.commit();
                                    Toast.makeText(getApplicationContext(), "Activity deleted.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(TLEditorActivity.this,TLScheduleEditorActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        }
                    }
                }
            }
        });
    }
    public void initialize(){
        initTime();
        updateTable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_editor);
        Bundle bundle = getIntent().getExtras();
        actCount = bundle.getInt("ActivityCount");
        System.out.println("Act count: " + actCount);
        actID = bundle.getInt("ActivityID");
        userPID = bundle.getString("UserPID");
        actName = bundle.getString("ActivityName");
        initialize();
    }

}
