package com.software.timeline.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.software.timeline.R;

public class TLConfirmationReceiver extends BroadcastReceiver {
    public TLConfirmationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        boolean now = intent.getExtras().getBoolean("classNow");
        String activityName = intent.getStringExtra("activity");
        Log.d("Prateek", "now=" + now);
        if (intent.hasExtra("classNow"))
        {
            if (now)
                sendConfirmationNotification(activityName, context);
            else
                sendReminder(activityName, context);
        }
        else
        {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(3);
            Intent activityIntent = new Intent(context, TLNotificationsActivity.class);
            activityIntent.putExtra("attending", intent.getBooleanExtra("attending", false));
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }
    }

    private void sendReminder(String activityName, Context context)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("TimeLine")
                        .setContentText("You have to attend " + activityName + " in 30 minutes")
                        .setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(2, mBuilder.build());
    }

    private void sendConfirmationNotification(String activityName, Context context)
    {
        Intent intentYes = new Intent(context, TLConfirmationReceiver.class);
        intentYes.putExtra("attending", true);
        Intent intentNo = new Intent(context, TLConfirmationReceiver.class);
        intentNo.putExtra("attending", false);
        PendingIntent pYes = PendingIntent.getBroadcast(context, 100, intentYes, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pNo = PendingIntent.getBroadcast(context, 101, intentNo, PendingIntent.FLAG_CANCEL_CURRENT);
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
