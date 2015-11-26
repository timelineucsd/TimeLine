package com.software.timeline.timelogger;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.software.timeline.R;

public class TLTimeLoggerActiity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_logger);
        initComponents();
    }

    private void initComponents()
    {
        mHandler = new Handler();
        initSpinner();
        initTimer();
        updateTimerButton();
    }

    private void initTimer()
    {
        mTextViewTimer = (TextView) findViewById(R.id.textViewTimer);
    }

    private void updateTimerButton()
    {
        if (mButtonTimer == null)
            mButtonTimer = (Button) findViewById(R.id.buttonTimer);
        if (!isTimerRunning())
        {
            mButtonTimer.setText("Start");
        }
        else
            mButtonTimer.setText("Stop");
    }

    public void buttonTimerClicked(View view)
    {
        if (!isTimerRunning())
        {
            setTimerRunning(true);
            mTimeElapsed = System.currentTimeMillis();
            mHandler.postDelayed(mRunnable, 1000L);
            mSpinnerDropdown.setEnabled(false);
        }
        else
        {
            setTimerRunning(false);
            resetTimer();
            mSpinnerDropdown.setEnabled(true);
        }
        updateTimerButton();
    }

    private void resetTimer()
    {
        mHandler.removeCallbacks(mRunnable);
        mTimeElapsed = 0;
        updateTimerText("00 : 00 : 00");
    }

    private void initSpinner()
    {
        mSpinnerDropdown = (Spinner)findViewById(R.id.spinnerActivities);
        String[] items = new String[]{"Piazza", "Class", "Email", "Discussion"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mSpinnerDropdown.setAdapter(adapter);
    }

    public void setTimerRunning(boolean timerRunning)
    {
        mTimerRunning = timerRunning;
    }

    public boolean isTimerRunning()
    {
        return mTimerRunning;
    }

    private void updateTimerText(String time)
    {
        mTextViewTimer.setText(time);
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTimerRunning())
            {
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
    private long mTimeElapsed;
    private Handler mHandler;
    private Spinner mSpinnerDropdown;
}
