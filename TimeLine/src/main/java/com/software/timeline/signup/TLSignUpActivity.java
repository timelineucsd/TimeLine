package com.software.timeline.signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.software.timeline.R;
import com.software.timeline.misc.TLApp;
import com.software.timeline.schedule_editor.TLScheduleEditorActivity;
import com.software.timeline.schedule_editor.TLScheduledActivityEntry;

import java.util.List;

public class TLSignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        initComponents();
    }

    private void initComponents()
    {
        mUserQuery = new ParseQuery<ParseObject>("UserInfo");
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
        else if(!TextUtils.isEmpty(email) && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mUserQuery.whereEqualTo("pid", PID);
            mUserQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (!isFinishing()) {
                        if (objects.size() == 0) {
                            addNewUser(name, PID, email, mSpinnerJobPercent.getSelectedItem().toString());
                        } else
                            Toast.makeText(TLSignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void addNewUser(final String name, final String PID, final String email, final String taType)
    {
        ParseObject object = new ParseObject("UserInfo");
        object.put("name", name);
        object.put("pid", PID);
        object.put("email", email);
        object.put("tatype", taType);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e)
            {
                if (e == null)
                {
                    Log.d("Prateek:", "Save successful");
                    addUserToSharedPrefs(name, PID, email, taType);
                    Intent intent = new Intent(TLSignUpActivity.this, TLScheduledActivityEntry.class);
                    startActivityForResult(intent, 400);
                }
            }
        });
    }

    private void addUserToSharedPrefs(String name, String pid, String email, String taType)
    {
        SharedPreferences.Editor editor = getSharedPreferences(TLApp.USER_SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.putString("pid", pid);
        editor.putString("email", email);
        editor.putString("tatype", taType);
        editor.putInt("count_lab", 0);
        editor.putInt("count_office", 0);
        editor.putInt("count_discussion", 0);
        editor.putInt("count_lecture", 0);
        editor.putInt("count_piazza", 0);
        editor.putInt("count_email", 0);
        editor.putInt("grading", 0);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        finish();
    }

    private Spinner mSpinnerJobPercent;
    ParseQuery<ParseObject> mUserQuery;
}
