package com.software.timeline.timelogger;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.software.timeline.R;
import com.software.timeline.misc.TLApp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TLTimeLoggerService extends Service {
    public TLTimeLoggerService() {
    }

    android.os.Handler handler = new android.os.Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run()
        {
            String pid = getSharedPreferences(TLApp.USER_SHARED_PREFS, MODE_PRIVATE).getString("pid", "");
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Date startTime = c.getTime();
            Calendar c2 = Calendar.getInstance();
            Date endTime = c2.getTime();
            notifyIfTimeExceeded(pid, startTime, endTime);
            handler.postDelayed(this, 30*60*1000);
        }
    };
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        handler.postDelayed(runnable, 30 * 60 * 1000);
    }

    private  void notifyIfTimeExceeded(final String PID, final Date begin_date, final Date final_date) {
        System.out.println("Entered get");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TimeLog");
        query.whereEqualTo("pid", PID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    System.out.println("Objects retrieved: " + objects.size());
                    for (int i = 0; i < objects.size(); i++) {

                        System.out.println("inside for");
                        day = objects.get(i).getDate("Day");
                        System.out.println("Object Day: " + day);

                        if (!(day.before(begin_date) || day.after(final_date))) {

                            System.out.println("date in range");
                            start_time = objects.get(i).getInt("startTime");
                            end_time = objects.get(i).getInt("endTime");
                            String each_day_1 = "" + day;
                            String only_day = each_day_1.substring(0, 3);
                            aid = objects.get(i).getInt("activityId");
                            System.out.println("Object startTime: " + start_time + " end time: " + end_time + " day: " + only_day + " aid: " + aid);

                            switch (aid) {
                                case 1:
                                    aid_1 += end_time - start_time;
                                    break;
                                case 2:
                                    aid_2 += end_time - start_time;
                                    break;
                                case 3:
                                    aid_3 += end_time - start_time;
                                    break;
                                case 4:
                                    aid_4 += end_time - start_time;
                                    break;
                                case 5:
                                    aid_5 += end_time - start_time;
                                    break;
                                case 6:
                                    aid_6 += end_time - start_time;
                                    break;
                                case 7:
                                    aid_7 += end_time - start_time;
                                    break;
                            }
                        }
                    }
                    if ((aid_1 + aid_2 + aid_3 + aid_4 + aid_5 + aid_6 + aid_7) > (1000 * 60 * 60 * 24 * 10))    //exceeding 8 hours
                    {
                        sendWarningNotification("Your have exceeded your allocated hours");
                    }
                    else if ((aid_1 + aid_2 + aid_3 + aid_4 + aid_5 + aid_6 + aid_7) > (1000 * 60 * 60 * 24 * 8))    //exceeding 8 hours
                    {
                        sendWarningNotification("Your are about to exceed your allocated hours");
                    }
                } else {
                    Log.e(SyncStateContract.Constants.DATA, "Failure");
                    System.out.println("data get not done " + e);
                }
            }
        });
    }

    private void sendWarningNotification(String s)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Time Logger")
                        .setContentText(s);

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public static int aid;
    public static int aid_count;
    private static long start_time;
    private static long end_time;
    public static long aid_1;
    public static long aid_2;
    public static long aid_3;
    public static long aid_4;
    public static long aid_5;
    public static long aid_6;
    public static long aid_7;
    private static Date day;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
