package com.software.timeline.signup;

/**
 * Created by Prateek on 11/25/2015.
 */
public class TLJobActivities
{
    public TLJobActivities(int jobId, String jobName, boolean isSelected)
    {
        mJobId = jobId;
        mJobName = jobName;
        mIsSelected = isSelected;
    }
    public int getJobId()
    {
        return mJobId;
    }

    public void setJobId(int jobId)
    {
        mJobId = jobId;
    }

    public String getJobName()
    {
        return mJobName;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean isSelected) {
        mIsSelected = isSelected;
    }

    public void setJobName(String jobName)
    {
        mJobName = jobName;
    }

    public static String[] getActivities()
    {
        return items;
    }
    private String mJobName;
    private int mJobId;
    private boolean mIsSelected;
    private static String[] items = new String[]{"Piazza", "Lab Hours", "Office Hours", "Grading", "Discussion Hours", "Email Replies", "Lecture Hours"};

}
