package com.software.timeline.signup;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.software.timeline.R;

public class TLJobInfoActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_info);
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
}
