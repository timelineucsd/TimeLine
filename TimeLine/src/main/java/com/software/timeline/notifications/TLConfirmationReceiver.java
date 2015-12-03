package com.software.timeline.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.software.timeline.R;
import com.software.timeline.timelogger.TLTimeLoggerActiity;

public class TLConfirmationReceiver extends BroadcastReceiver {
    public TLConfirmationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        boolean now = intent.getBooleanExtra("classNow", false);
        String activityName = intent.getStringExtra("activity");
        if (now)
            sendConfirmationNotification(activityName, context);
        else
            sendReminder(activityName, context);
    }

    private void sendReminder(String activityName, Context context)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("TimeLine")
                        .setContentText("You have to attend" + activityName + "in 30 minutes");

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(2, mBuilder.build());
    }

    private void sendConfirmationNotification(String activityName, Context context)
    {
        Intent intentYes = new Intent(context, TLTimeLoggerActiity.class);
        intentYes.putExtra("attedning", true);
        Intent intentNo = new Intent(context, TLTimeLoggerActiity.class);
        intentNo.putExtra("attending", false);
        PendingIntent pYes = PendingIntent.getBroadcast(context, 100, intentYes, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pNo = PendingIntent.getBroadcast(context, 100, intentNo, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("TimeLine")
                        .setContentText("Are you attending" + activityName + "?")
                        .addAction(0, "Yes", pYes)
                        .addAction(0, "No", pNo);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(3, mBuilder.build());
    }
}
