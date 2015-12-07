package com.software.timeline.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.software.timeline.R;
import com.software.timeline.misc.TLApp;
import com.software.timeline.timelogger.TLTimeLoggerActiity;

public class TLNotificationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        Intent intent = getIntent();
        boolean attending = intent.getBooleanExtra("attending", false);
        if (attending)
            Toast.makeText(this, "Attending", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Not attending", Toast.LENGTH_SHORT).show();
        TLApp.alert=1;
        TLApp.aid=1;
        TLApp.aid_count=1;
        Intent timeLoggerIntent = new Intent(this, TLTimeLoggerActiity.class);
        startActivity(timeLoggerIntent);
        finish();
    }
}
