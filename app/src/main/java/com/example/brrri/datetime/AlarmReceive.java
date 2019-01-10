package com.example.brrri.datetime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


public class AlarmReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"Alarm Raised",Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        //.setSmallIcon(R.drawable.ic_launcher)
                        .setContentText("--"+"event"+"--" )
                        //.setLargeIcon(BitmapFactory.decodeResource( ,R.drawable.datetime))
                        .setAutoCancel(true);

        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;
        long[] vibrate = new long[] { 1000, 1000, 1000, 1000, 1000 };
        notification.vibrate = vibrate;

        // Show Notification
        notificationManager.notify(0, builder.build());
    }
}
