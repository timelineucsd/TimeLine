package com.software.timeline.signup;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Prateek on 11/25/2015.
 */
@ParseClassName("TLUser")
public class TLUser extends ParseObject
{
    public String getUserName()
    {
        return getString("name");
    }

    public void setUserName(String name)
    {
        put("name", name);
    }

    public String getUserPID()
    {
        return getString("pid");
    }

    public void setUserPID(String pid)
    {
        put("pid", pid);
    }

    public String getUserEmail()
    {
        return getString("email");
    }

    public void setUserEmail(String email)
    {
        put("email", email);
    }

    public String getTAType(String taType)
    {
        return getString("tatype");
    }

    public void setTAType(String taType)
    {
        put("tatype", taType);
    }

    public static ParseQuery<TLUser> getQuery()
    {
        return ParseQuery.getQuery(TLUser.class);
    }
}
