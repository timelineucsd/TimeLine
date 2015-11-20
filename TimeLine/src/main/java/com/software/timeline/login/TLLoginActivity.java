package com.software.timeline.login;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.software.timeline.R;
import com.software.timeline.timelogger.TLTimeLogger;

public class TLLoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void loginClicked(View view)
    {
        EditText etName = (EditText) findViewById(R.id.editTextName);
        EditText etPID = (EditText) findViewById(R.id.editTextPID);
        String name = etName.getText().toString();
        String PID = etPID.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(PID))
        {
            Toast.makeText(this, "Name or PID can't be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            setResult(RESULT_OK);
            finish();
        }
    }

}
