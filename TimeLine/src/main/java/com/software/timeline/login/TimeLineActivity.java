package com.software.timeline.login;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.software.timeline.R;
import com.software.timeline.timelogger.TLTimeLogger;

public class TimeLineActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_line);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case RESULT_CANCELED:
                break;
            case RESULT_OK:
                if (requestCode == 100) {
                    Intent intent = new Intent(this, TLTimeLogger.class);
                    startActivity(intent);
                }
                finish();
                break;
        }
    }

    public void buttonSignUpClicked(View view)
    {

    }

    public void buttonLoginClicked(View view)
    {
        Intent intent = new Intent(this, TLLoginActivity.class);
        startActivityForResult(intent, 100);
    }

}
