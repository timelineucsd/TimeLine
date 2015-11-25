package com.software.timeline.signup;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.software.timeline.R;

public class TLSignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
    }

    public void buttonUserSignUpClicked(View view)
    {
        EditText etName = (EditText) findViewById(R.id.editTextSignUpName);
        EditText etPID = (EditText) findViewById(R.id.editTextSignUpPID);
        EditText etEmail = (EditText) findViewById(R.id.editTextSignUpEmail);
        String name = etName.getText().toString();
        String PID = etPID.getText().toString();
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(PID) || TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Name, PID or Email can't be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(this, TLJobInfoActivity.class);
            startActivityForResult(intent, 300);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case RESULT_OK:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
