package com.software.timeline.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.software.timeline.R;
import com.software.timeline.misc.TLApp;

import java.util.List;

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
            loginIfUserExists(name, PID);
        }
    }

    private void addUserToSharedPrefs(String name, String pid, String email, String taType)
    {
        SharedPreferences.Editor editor = getSharedPreferences(TLApp.USER_SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.putString("pid", pid);
        editor.putString("email", email);
        editor.putString("tatype", taType);
        editor.putBoolean("overtimeNotified", false);
        editor.putBoolean("warningNotified", false);
        editor.commit();
    }

    private void loginIfUserExists(final String name, final String pid)
    {
        ParseQuery<ParseObject> mUserQuery = new ParseQuery<ParseObject>("UserInfo");
        mUserQuery.whereEqualTo("pid", pid);
        mUserQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e)
            {
                if (!isFinishing())
                {
                    boolean isFound = false;
                    for (int i=0; i < objects.size(); i++)
                    {
                        if(pid.equals(objects.get(i).getString("pid")) && name.equals(objects.get(i).getString("name")))
                        {
                            isFound = true;
                            setResult(RESULT_OK);
                            addUserToSharedPrefs(name, pid, objects.get(i).getString("email"), objects.get(i).getString("tatype"));
                            TLLoginActivity.this.finish();
                            break;
                        }
                    }
                    if (!isFound)
                        Toast.makeText(TLLoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
