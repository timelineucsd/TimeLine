package com.software.timeline.signup;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;

import com.software.timeline.R;

import java.util.ArrayList;

public class TLJobInfoActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_info);
        initComponents();
    }

    private void initComponents()
    {
        initSpinner();
    }

    private void initSpinner()
    {
        mSpinnerJobPercent = (Spinner) findViewById(R.id.spinnerJobPercent);
        String[] items = new String[]{"25%", "50%"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mSpinnerJobPercent.setAdapter(adapter);

    }

    private void signUpCompleted()
    {
        setResult(RESULT_OK);
        finish();
    }

    public void buttonContinuePressed(View view)
    {
        signUpCompleted();
    }

    private Spinner mSpinnerJobPercent;
}
