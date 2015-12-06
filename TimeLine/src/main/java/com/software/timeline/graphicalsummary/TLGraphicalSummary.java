package com.software.timeline.graphicalsummary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.software.timeline.R;
import com.software.timeline.misc.TLApp;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TLGraphicalSummary extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphical_summary);
        initSpinner();
    }

    private void initSpinner() {
        mSpinnerDropdown = (Spinner) findViewById(R.id.spinnerGrphical);
        String[] items = new String[]{"Current Week's Summary", "Last Week's Summary"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mSpinnerDropdown.setAdapter(adapter);

        mSpinnerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                getData(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getData(int number){

        SharedPreferences shared = getSharedPreferences(TLApp.USER_SHARED_PREFS, MODE_PRIVATE);
        pid = (shared.getString("pid", ""));
        System.out.println("PID: " + pid);

        Calendar cal = Calendar.getInstance();

        if (number == 1){

            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.add(Calendar.DAY_OF_WEEK, -7);

            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            previous_monday = cal.getTime();
            System.out.println("Last Monday: " + previous_monday);

            // Get the date on Sunday
            cal.add(Calendar.DAY_OF_WEEK, 6);

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE,59);
            cal.set(Calendar.SECOND,59);
            cal.set(Calendar.MILLISECOND,0);
            previous_sunday = cal.getTime();
            System.out.println("Last Sunday: " + previous_sunday);

            getWorkedHours(pid,previous_monday,previous_sunday);
        }
        else{

            cal.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            Calendar dt = Calendar.getInstance();
            dt.clear();
            dt.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            today_day = dt.getTime();
            System.out.println("Today's Date: " + today_day);

            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            current_monday = cal.getTime();
            System.out.println("This Monday: " + current_monday);

            getWorkedHours(pid, current_monday, today_day);
        }
    }


    public void graph_plotting()
    {
        plot = (XYPlot) findViewById(R.id.xyPlot);
        plot.clear();

        System.out.println("aid-1: " + aid_1 + "aid-2: " + aid_2 + "aid-3: " + aid_3 + "aid-4: " + aid_4 + "aid-5: " + aid_5 + "aid-6: " + aid_6 + "aid-7: " + aid_7);

        System.out.println("XYPlot: " + R.id.xyPlot);

        List s1 = getSeries();
        XYSeries series1 = new SimpleXYSeries(s1, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "");

        LineAndPointFormatter s1Format = new LineAndPointFormatter();
        s1Format.setPointLabelFormatter(new PointLabelFormatter());
        s1Format.configure(getApplicationContext(),
                R.xml.lpf1);

        plot.getGraphWidget().setDomainValueFormat(new GraphXLabelFormat());
        plot.addSeries(series1, s1Format);

        plot.setTicksPerRangeLabel(1);
        plot.invalidate();

    }

    public void getWorkedHours(final String PID, final Date begin_date, final Date final_date) {
        aid_1=0;
        aid_2=0;
        aid_3=0;
        aid_4=0;
        aid_5=0;
        aid_6=0;
        aid_7=0;
        System.out.println("Entered get");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TimeLog");
        query.whereEqualTo("pid", PID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    System.out.println("Objects retrieved: " + objects.size());
                    for (int i = 0; i < objects.size(); i++) {

                        System.out.println("inside for");
                        day = objects.get(i).getDate("Day");
                        System.out.println("Object Day: " + day);

                        if (!(day.before(begin_date) || day.after(final_date))) {

                            System.out.println("date in range");
                            start_time = objects.get(i).getInt("startTime");
                            end_time = objects.get(i).getInt("endTime");
                            String each_day_1 = "" + day;
                            String only_day = each_day_1.substring(0, 3);
                            aid = objects.get(i).getInt("activityId");
                            System.out.println("Object startTime: " + start_time + " end time: " + end_time + " day: " + only_day + " aid: " + aid);

                            switch (aid) {
                                case 1:
                                    long seconds_1= TimeUnit.MILLISECONDS.toSeconds(end_time - start_time);
                                    aid_1 += seconds_1;
                                    break;
                                case 2:
                                    long seconds_2= TimeUnit.MILLISECONDS.toSeconds(end_time - start_time);
                                    aid_2 += seconds_2;
                                    break;
                                case 3:
                                    long seconds_3= TimeUnit.MILLISECONDS.toSeconds(end_time - start_time);
                                    aid_3 += seconds_3;
                                    break;
                                case 4:
                                    long seconds_4= TimeUnit.MILLISECONDS.toSeconds(end_time - start_time);
                                    aid_4 += seconds_4;
                                    break;
                                case 5:
                                    long seconds_5= TimeUnit.MILLISECONDS.toSeconds(end_time - start_time);
                                    aid_5 += seconds_5;
                                    break;
                                case 6:
                                    long seconds_6= TimeUnit.MILLISECONDS.toSeconds(end_time - start_time);
                                    aid_6 += seconds_6;
                                    break;
                                case 7:
                                    long seconds_7= TimeUnit.MILLISECONDS.toSeconds(end_time - start_time);
                                    aid_7 += seconds_7;
                                    break;
                            }
                        }
                    }
                    graph_plotting();
                } else {
                    Log.e(SyncStateContract.Constants.DATA, "Failure");
                    System.out.println("data get not done " + e);
                }
            }
        });
    }

    private List getSeries() {
        List series = new ArrayList();
        series.clear();
        long value;
        for (int i = 1; i <= 7; i++) {
            switch (i){
                case 1:
                    value=aid_1;
                    series.add(value);
                    break;
                case 2:
                    value=aid_2;
                    series.add(value);
                    break;
                case 3:
                    value=aid_3;
                    series.add(value);
                    break;
                case 4:
                    value=aid_4;
                    series.add(value);
                    break;
                case 5:
                    value=aid_5;
                    series.add(value);
                    break;
                case 6:
                    value=aid_6;
                    series.add(value);
                    break;
                case 7:
                    value=aid_7;
                    series.add(value);
                    break;
            }
        }
        System.out.println("Series: "+series);
        return series;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private Spinner mSpinnerDropdown;
    private String pid;
    private Date today_day;
    private Date current_monday;
    private Date previous_monday;
    private Date previous_sunday;
    private XYPlot plot;
    public static int aid;
    public static int aid_count;
    private static long start_time;
    private static long end_time;
    private static Date day;
    public static long aid_1=0;
    public static long aid_2=0;
    public static long aid_3=0;
    public static long aid_4=0;
    public static long aid_5=0;
    public static long aid_6=0;
    public static long aid_7=0;
}

class GraphXLabelFormat extends Format {

    final String[] xLabels = {"Piazza", "Lab", "Office", "Grade", "Discuss", "Email", "Lecture"};
    int flag,count=0;
    @Override
    public StringBuffer format(Object arg0, StringBuffer arg1, FieldPosition arg2) {

        int parsedInt = Math.round(Float.parseFloat(arg0.toString()));
        if (count ==0){
            String labelString = xLabels[parsedInt];
            arg1.append(labelString);
            count=1;
            flag=parsedInt;
        }
        Log.d("test", parsedInt + " " + arg1 + " " + arg2);
        if (flag!=parsedInt) {
            String labelString = xLabels[parsedInt];
            arg1.append(labelString);
            flag=parsedInt;
        }
        return arg1;
    }

    @Override
    public Object parseObject(String arg0, ParsePosition arg1)
    {
            return java.util.Arrays.asList(xLabels).indexOf(arg0);
    }
}
