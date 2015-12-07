package com.software.timeline.timelogger;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.software.timeline.R;
import com.software.timeline.graphicalsummary.TLGraphicalSummary;
import com.software.timeline.login.TimeLineActivity;
import com.software.timeline.misc.TLApp;
import com.software.timeline.notifications.TLConfirmationReceiver;
import com.software.timeline.signup.TLJobActivities;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TLTimeLoggerActiity extends Activity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences shared = getSharedPreferences(TLApp.USER_SHARED_PREFS, MODE_PRIVATE);
        pid = (shared.getString("pid", ""));
        System.out.println("PID: " + pid);

        setContentView(R.layout.time_logger);
        if (TLApp.alert==1){
            System.out.println("fired fetching");
            TLApp.alert=0;
            TLApp.getActivities(pid);
            finish();
        }

        Intent intent = new Intent(this, TLTimeLoggerService.class);
        startService(intent);
        initComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timelogger_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menuLogout:
                logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser()
    {
        getSharedPreferences(TLApp.USER_SHARED_PREFS, MODE_PRIVATE).edit().clear().commit();
        Intent intent  = new Intent(this, TimeLineActivity.class);
        startActivity(intent);
        finish();
    }

    private void initComponents() {
        mHandler = new Handler();
        initSpinner();
        initTimer();
        updateTimerButton();
    }

    private void initTimer() {
        mTextViewTimer = (TextView) findViewById(R.id.textViewTimer);
    }

    private void updateTimerButton() {
        if (mButtonTimer == null)
            mButtonTimer = (Button) findViewById(R.id.buttonTimer);
        if (!isTimerRunning()) {
            mButtonTimer.setText("Start");
            System.out.println("Start printed");
        } else {
            mButtonTimer.setText("Stop");
            System.out.println("Stop printed");
        }
    }

    public void buttonTimerClicked(View view) {
        if (!isTimerRunning()) {
            setTimerRunning(true);
            mTimeElapsed = System.currentTimeMillis();
            start_time = mTimeElapsed;
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            Calendar dt = Calendar.getInstance();
            dt.clear();
            dt.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            day = dt.getTime();
            System.out.println("Date: " + day);
            mHandler.postDelayed(mRunnable, 1000L);
            mSpinnerDropdown.setEnabled(false);
        } else {
            end_time = System.currentTimeMillis();
            setTimerRunning(false);
            resetTimer();
            System.out.println("start time: " + start_time);
            System.out.println("end time: " + end_time);
            System.out.println("Button updated");
            TLApp.addTimeLog(pid, aid, start_time, end_time, day);
        }
        updateTimerButton();
    }

    private void resetTimer() {
        mHandler.removeCallbacks(mRunnable);
        mTimeElapsed = 0;
        mSpinnerDropdown.setEnabled(true);
        updateTimerText("00 : 00 : 00");
    }

    private void initSpinner() {
        mSpinnerDropdown = (Spinner) findViewById(R.id.spinnerActivities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, TLJobActivities.getActivities());
        mSpinnerDropdown.setAdapter(adapter);

        mSpinnerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                aid = pos + 1;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void buttonEditScheduleClicked(View view) {
        Intent intent = new Intent(this, TimeLineActivity.class);
        startActivity(intent);
        finish();
    }

    public void buttonGraphicalSummaryClicked(View view) {
        Intent intent = new Intent(this, TLGraphicalSummary.class);
        startActivity(intent);
    }

    public void buttonAlertClicked(View view)
    {
        sendWarningNotification("You are about to exceed your job limit");
    }

    public void buttonActivityReminderClicked(View view)
    {
        Intent intent = new Intent(this, TLConfirmationReceiver.class);
        intent.putExtra("classNow", false);
        intent.putExtra("activity", "Office Hours");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (1 * 100), pendingIntent);
    }

    public void buttonActivityConfirmationClicked(View view)
    {
        Intent intent = new Intent(this, TLConfirmationReceiver.class);
        intent.putExtra("classNow", true);
        intent.putExtra("activity", "Office Hours");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 2343243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (1 * 100), pendingIntent);
    }

    private void sendWarningNotification(String s)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.notification)
                        .setContentTitle("Time Logger")
                        .setContentText(s)
                        .setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public void setTimerRunning(boolean timerRunning) {
        mTimerRunning = timerRunning;
        System.out.println("mTimer in starttimer: "+mTimerRunning);
    }

    public boolean isTimerRunning() {
        System.out.println("mTimer in timer running: " + mTimerRunning);
        return mTimerRunning;
    }

    private void updateTimerText(String time) {
        mTextViewTimer.setText(time);
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTimerRunning()) {
                long seconds = (System.currentTimeMillis() - mTimeElapsed) / 1000;
                updateTimerText(String.format("%02d : %02d : %02d", seconds / 3600, seconds / 60, seconds % 60));
                mHandler.postDelayed(mRunnable, 1000L);
            }
        }

    };

    @Override
    public void onBackPressed()
    {
        if (mTimerRunning)
            moveTaskToBack(true);
        else
            super.onBackPressed();
    }

    private boolean mTimerRunning;
    private Button mButtonTimer;
    private TextView mTextViewTimer;
    private Handler mHandler;
    private Spinner mSpinnerDropdown;
    private long mTimeElapsed;
    private long start_time;
    private long end_time;
    private int aid;
    private Date day;
    private String pid;

}
