package com.software.timeline.schedule_editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.software.timeline.R;
import com.software.timeline.misc.TLApp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TLScheduleEditorActivity extends Activity{

    public Object username;
    public Object userPID;
    public String actName;
    public int actID;
    public int fetched_activityID;
    public int fetched_activityCount;
    public int actualActCount;
    public String fetched_activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.schedule_editor);

        Log.d("edit schedule:", "activity entered");

        Map<String, ?> all = getSharedPreferences(TLApp.USER_SHARED_PREFS, MODE_PRIVATE).getAll();
        System.out.println("Shared Preferences" + all);
        username = all.get("name");
        userPID = all.get("pid");
        System.out.println(username + "    " + userPID);

        initSpinner();
    }

    public void displayList(String activityName,int activityID){

        final ListView listview = (ListView)findViewById(R.id.listView1);
        final List<String> listitems = new ArrayList<String>();
        final ArrayAdapter<String> listadapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, listitems);
        listview.setAdapter(listadapter);
        listitems.clear();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Activities");
        System.out.println(query);
        query.whereEqualTo("activityId", activityID);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                System.out.println("inside parse done function");
                if (e == null) {
                    System.out.println("Objects size: " + objects.size());
                    for (int i = 0; i < objects.size(); i++) {
                        System.out.println("inside for");
                        fetched_activityID = objects.get(i).getInt("activityId");
                        fetched_activityCount = objects.get(i).getInt("activityCount");
                        fetched_activityName = objects.get(i).getString("acitivityName");
                        System.out.println("From Activities Table:");
                        System.out.println("Act name:" + fetched_activityName + "Object activityID: " + fetched_activityID + " Object activityCount " + fetched_activityCount);

                        System.out.println("Before entering schedule Table:");
                        System.out.println(" Activity count: " + fetched_activityCount + " ActivityID " + fetched_activityID + "Activity Name:" + fetched_activityName);

                        ParseQuery<ParseObject> query1;
                        query1 = ParseQuery.getQuery("Schedule");
                        query1.whereEqualTo("pid", userPID);
                        query1.whereEqualTo("activityId", fetched_activityID);
                        System.out.println("calling query on schedule table");
                        query1.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    System.out.println("Objects retrieved: " + objects.size());
                                    for (int i = 0; i < objects.size(); i++) {

                                       // Date day = objects.get(i).getDate("Day");
                                        String dayval = objects.get(i).getString("dayOfWeek");
                                        String start_time = objects.get(i).getString("StartTime");
                                        String end_time = objects.get(i).getString("EndTime");
                                        actualActCount = objects.get(i).getInt("activityCount");
                                        System.out.println(fetched_activityName + "Object startTime: " + start_time + " end time: " + end_time);
                                        listitems.add("\nDay:" + dayval+ "\nStart Time " + start_time + "\nEnd Time" + end_time + "\nActivity Count" + actualActCount);
                                    }
                                    listadapter.notifyDataSetChanged();
                                    listview.invalidate();
                                } else {
                                    Log.e(SyncStateContract.Constants.DATA, "Failure");
                                    System.out.println("data get not done " + e);
                                }
                            }
                        });

                        System.out.println("Entering listitem listener");
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                System.out.println("inside list item listener");
                                System.out.println("item at list pos = " + listview.getItemAtPosition(position));

                                Intent i;
                                i = new Intent(TLScheduleEditorActivity.this, TLEditorActivity.class);
                                // String getrec = fetched_activityID+ " " + fetched_activityCount ;
                                //String getrec = listview.getItemAtPosition(position).toString();
                                Bundle bundle = new Bundle();
                                bundle.putInt("ActivityCount", actualActCount);
                                bundle.putInt("ActivityID", fetched_activityID);
                                bundle.putString("UserPID", (String) userPID);
                                bundle.putString("ActivityName", (String) fetched_activityName);

                                // bundle.putString("Tuple_Values", getrec);
                                System.out.println(bundle);
                                i.putExtras(bundle);
                                startActivity(i);

                            }

                        });


//                        listview.setOnLongClickListener(new AdapterView.OnItemLongClickListener() {
//                            @Override
//                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                                return false;
//                            }
//
//
//                        });

                        break;
                    }
                } else {
                    Log.e(SyncStateContract.Constants.DATA, "Failure");
                    System.out.println("couldn't fetch data from activities table" + e);
                }
            }
        });

    }

    private void initSpinner() {
        Spinner mSpinnerDropdown = (Spinner) findViewById(R.id.spinner);
        final String[] items = new String[]{"Select Activity","Lab Hours", "Office Hours","Discussion Hours","Lecture Hours"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mSpinnerDropdown.setAdapter(adapter);

        System.out.println("Entering spinneritem listener");
        mSpinnerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                actName = items[pos];
                if (actName.equalsIgnoreCase("Lab Hours")) {
                    actID = 2;
                } else if (actName.equalsIgnoreCase("Office Hours")){
                    actID = 3;
                } else if (actName.equalsIgnoreCase("Discussion Hours")){
                    actID = 5;
                } else if (actName.equalsIgnoreCase("Lecture Hours")){
                    actID = 7;
                }
                else{
                    actID = 0;
                }
                System.out.println("From spinner selection:");
                System.out.println("actname=" + actName + "actId=" + actID);
                displayList(actName, actID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Nothing from spinner item was selected");
            }

        });
    }
}
