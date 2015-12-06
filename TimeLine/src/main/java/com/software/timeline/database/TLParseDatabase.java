package com.software.timeline.database;

import com.parse.Parse;
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

    private static TLParseDatabase mParseDBHandler;
}
