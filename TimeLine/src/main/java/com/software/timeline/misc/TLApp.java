package com.software.timeline.misc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.util.Log;

import com.parse.*;
import com.software.timeline.database.TLParseDatabase;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Prateek on 11/20/2015.
 */
public class TLApp extends Application
{

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        TLParseDatabase.setUpParse();
    }

    public static Context getAppContext()
    {
        return mAppContext;
    }

    public static void addTimeLog(String PID, int AID, long startTime, long endTime, String day)
    {
        System.out.println("Entered final push");
        ParseObject object = new ParseObject("TimeLog");
        object.put("pid", PID);
        object.put("activityId", AID);
        object.put("startTime", startTime);
        object.put("endTime", endTime);
        object.put("day", day);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {

                if (e == null) {
                    // Success!
                    System.out.println("data put done");

                } else {
                    // Failure!
                    Log.e(SyncStateContract.Constants.DATA, "Failure");
                    System.out.println("data put not done "+e);
                }
            }
        });
    }

    public static void getActivities(final String PID) {
        System.out.println("Entered get");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
        query.whereEqualTo("pid", PID);
        query.findInBackground(new FindCallback<ParseObject>()
        {
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    System.out.println("Objects retrieved: "+ objects.size());
                    for (int i=0; i < objects.size(); i++){

                        System.out.println("inside for");

                        if((aid==objects.get(i).getInt("activityId")) && (aid_count==objects.get(i).getInt("activityCount"))){

                            System.out.println("conditions matched");
                            start_time=objects.get(i).getInt("startTime");
                            end_time=objects.get(i).getInt("endTime");
                            day=objects.get(i).getString("day");
                            System.out.println("Object startTime: " + start_time + " end time: " + end_time + " day: " + day);
                            addTimeLog(PID,aid,start_time,end_time,day);
                            break;
                        }
                    }

                } else {
                    Log.e(SyncStateContract.Constants.DATA, "Failure");
                    System.out.println("data get not done " + e);
                }
            }
        });
    }


    private static Context mAppContext;
    public static final String USER_SHARED_PREFS = "com.software.timeline.user";
    private static TLApp mParseDBHandler;
    public static int alert = 0;
    public static int aid;
    public static int aid_count;
    private static long start_time;
    private static long end_time;
    private static String day;
}
