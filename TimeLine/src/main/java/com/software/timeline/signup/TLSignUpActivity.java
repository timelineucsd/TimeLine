package com.software.timeline.signup;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.software.timeline.R;
import com.software.timeline.database.TLParseDatabase;

public class TLSignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        initComponents();
    }

    private void initComponents()
    {
        mUserQuery = TLUser.getQuery();
        initSpinner();
    }

    private void initSpinner()
    {
        mSpinnerJobPercent = (Spinner) findViewById(R.id.spinnerJobPercent);
        String[] items = new String[]{"25%", "50%"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mSpinnerJobPercent.setAdapter(adapter);
    }

    public void buttonUserSignUpClicked(View view)
    {
        EditText etName = (EditText) findViewById(R.id.editTextSignUpName);
        final EditText etPID = (EditText) findViewById(R.id.editTextSignUpPID);
        EditText etEmail = (EditText) findViewById(R.id.editTextSignUpEmail);
        final String name = etName.getText().toString();
        final String PID = etPID.getText().toString();
        final String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(PID) || TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Name, PID or Email can't be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mUserQuery.fromLocalDatastore();
            mUserQuery.whereEqualTo("pid", PID);
            mUserQuery.getFirstInBackground(new GetCallback<TLUser>() {
                @Override
                public void done(TLUser object, ParseException e)
                {
                    if(!isFinishing())
                    {
                        if (object == null && e.getCode() == ParseException.OBJECT_NOT_FOUND)
                        {
                            TLParseDatabase.getHandler().addNewUser(name, PID, email, mSpinnerJobPercent.getSelectedItem().toString());
                            setResult(RESULT_OK);
                            finish();
                        }
                        else
                            Toast.makeText(TLSignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            setResult(RESULT_OK);
            finish();
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

    private Spinner mSpinnerJobPercent;
    ParseQuery<TLUser> mUserQuery;
}
