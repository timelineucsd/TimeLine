package com.software.timeline.misc;

import android.app.Application;
import android.content.Context;

import com.software.timeline.database.TLParseDatabase;

/**
 * Created by Prateek on 11/20/2015.
 */
public class TLApp extends Application
{

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        TLParseDatabase.setUpParse();
    }

    public static boolean isLoggedIn()
    {
        return true;
    }

    public static Context getAppContext()
    {
        return mAppContext;
    }

    private static Context mAppContext;
}
