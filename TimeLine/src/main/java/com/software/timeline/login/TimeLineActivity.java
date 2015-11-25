package com.software.timeline.login;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.software.timeline.R;
import com.software.timeline.misc.TLApp;
import com.software.timeline.signup.TLSignUpActivity;
import com.software.timeline.timelogger.TLTimeLogger;

public class TimeLineActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_line);
        //Uncomment this is you want to skip the login step and go
        //directly to time logger
//        if (TLApp.isLoggedIn())
//        {
//            Intent intent = new Intent(this, TLTimeLogger.class);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case RESULT_CANCELED:
                break;
            case RESULT_OK:
                Intent intent = new Intent(this, TLTimeLogger.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void buttonSignUpClicked(View view)
    {
        Intent intent = new Intent(this, TLSignUpActivity.class);
        startActivityForResult(intent, 200);
    }

    public void buttonLoginClicked(View view)
    {
        Intent intent = new Intent(this, TLLoginActivity.class);
        startActivityForResult(intent, 100);
    }

}
