package com.software.timeline.misc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.util.Log;

import com.parse.*;
import com.software.timeline.database.TLParseDatabase;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Prateek on 11/20/2015.
 */
public class TLApp extends Application
{

    public static long aid_1;
    public static long aid_2;
    public static long aid_3;
    public static long aid_4;
    public static long aid_5;
    public static long aid_6;
    public static long aid_7;

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

    public static void addTimeLog(String PID, int AID, long startTime, long endTime, Date day)
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
                            day=objects.get(i).getDate("day");
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

    public static void getWorkedHours(final String PID, final Date begin_date, final Date final_date) {
        System.out.println("Entered get");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TimeLog");
        query.whereEqualTo("pid", PID);
        query.findInBackground(new FindCallback<ParseObject>()
        {
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    System.out.println("Objects retrieved: "+ objects.size());
                    for (int i=0; i < objects.size(); i++){

                        System.out.println("inside for");
                        day=objects.get(i).getDate("Day");
                        System.out.println("Object Day: "+day);

                        if(!(day.before(begin_date) || day.after(final_date))){

                            System.out.println("date in range");
                            start_time=objects.get(i).getInt("startTime");
                            end_time=objects.get(i).getInt("endTime");
                            String each_day_1 = ""+day;
                            String only_day = each_day_1.substring(0, 3);
                            aid=objects.get(i).getInt("activityId");
                            System.out.println("Object startTime: " + start_time + " end time: " + end_time + " day: " + only_day + " aid: " + aid);

                            switch (aid){
                                case 1:
                                    aid_1+=end_time-start_time;
                                    break;
                                case 2:
                                    aid_2+=end_time-start_time;
                                    break;
                                case 3:
                                    aid_3+=end_time-start_time;
                                    break;
                                case 4:
                                    aid_4+=end_time-start_time;
                                    break;
                                case 5:
                                    aid_5+=end_time-start_time;
                                    break;
                                case 6:
                                    aid_6+=end_time-start_time;
                                    break;
                                case 7:
                                    aid_7+=end_time-start_time;
                                    break;
                            }
                        }
                    }
                    //TLGraphicalSummary.displayResults();
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
    private static Date day;
}
