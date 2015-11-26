package com.software.timeline.database;

import com.parse.Parse;
import com.parse.ParseObject;
import com.software.timeline.misc.TLApp;

/**
 * Created by Prateek on 11/25/2015.
 */
public class TLParseDatabase
{
    private TLParseDatabase(){}

    public static TLParseDatabase getHandler()
    {
        if (mParseDBHandler == null)
            mParseDBHandler = new TLParseDatabase();
        return mParseDBHandler;
    }

    public static void setUpParse()
    {
        Parse.enableLocalDatastore(TLApp.getAppContext());
        Parse.initialize(TLApp.getAppContext(), "OSwJFrDPhqMdlU2fRrilaOqkGVDqH4OGb8JPfvRD", "rRGymRfMyNkaI7n8pOLKUHgWkPKEcYmGF6dUe8Lb");
    }

    public void addNewUser(String name, String PID, String email, String taType)
    {
        ParseObject object = new ParseObject("UserInfo");
        object.put("name", name);
        object.put("pid", PID);
        object.put("email", email);
        object.put("tatype", taType);
        object.saveInBackground();
    }

    private static TLParseDatabase mParseDBHandler;
}
