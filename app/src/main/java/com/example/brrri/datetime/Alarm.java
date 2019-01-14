package com.example.brrri.datetime;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;


import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Alarm extends BroadcastReceiver {
    //id Notification
    int id = 1;


    @Override
    public void onReceive(Context context, Intent intent)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        // подключаемся к БД
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor query = db.rawQuery("SELECT * FROM BD WHERE date_time = "+cal.getTimeInMillis()+" ORDER BY _id; ", null);

        if (query.moveToFirst()) {
            do {
                Long id_BD = query.getLong(0);
                String event_fromBD = query.getString(2);

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(event_fromBD)
                                .setAutoCancel(true);

                Notification notification = builder.build();

                notification.defaults = Notification.DEFAULT_SOUND |
                        Notification.DEFAULT_VIBRATE;
                long[] vibrate = new long[] { 1000, 1000, 1000, 1000, 1000 };
                notification.vibrate = vibrate;

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(id, notification);
                id+=1;

                db.delete(dbHelper.TABLE_NAME, dbHelper.COLUMN_ID + "=" +id_BD,null);

            }
            while (query.moveToNext());
        }
        query.close();
        db.close();
        dbHelper.close();
    }
}
