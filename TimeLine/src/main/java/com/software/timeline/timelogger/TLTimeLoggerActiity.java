package com.software.timeline.timelogger;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.software.timeline.R;
import com.software.timeline.login.TimeLineActivity;
import com.software.timeline.misc.TLApp;
import com.software.timeline.notifications.TLNotificationsActivity;
import com.software.timeline.signup.TLJobActivities;
import com.software.timeline.signup.TLSignUpActivity;

import java.util.Calendar;
import java.util.TimeZone;

public class TLTimeLoggerActiity extends Activity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_logger);
        if (TLApp.alert==1){
            System.out.println("fired fetching");
            TLApp.alert=0;
            TLApp.getActivities(pid);
        }
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
            day = "" + dt.getTime();
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
        Intent intent = new Intent(this, TLSignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void buttonAlertClicked(View view) {
        Intent intent = new Intent(this, TLNotificationsActivity.class);
        startActivity(intent);
        finish();
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
    private String day;
    private String pid="A53093508";

}
