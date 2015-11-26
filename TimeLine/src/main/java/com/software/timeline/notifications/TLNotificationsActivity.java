package com.software.timeline.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.software.timeline.R;
import com.software.timeline.misc.TLApp;
import com.software.timeline.timelogger.TLTimeLoggerActiity;

public class TLNotificationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.time_logger);
        System.out.println("entered notifications");
        TLApp.alert=1;
        TLApp.aid=1;
        TLApp.aid_count=1;
        Intent intent = new Intent(this, TLTimeLoggerActiity.class);
        startActivity(intent);
    }
}
