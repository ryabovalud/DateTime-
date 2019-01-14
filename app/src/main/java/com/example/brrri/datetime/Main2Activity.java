package com.example.brrri.datetime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;

import java.util.Calendar;


public class Main2Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DatabaseHelper(this);
        ShowDB();

        this.triggerService();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_main2);
        ShowDB();
    }

    public void ShowDB() {
        setContentView(R.layout.activity_main2);

        String[][] myDataset = getDataSet();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        mRecyclerView.setHasFixedSize(true);

        // используем linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // создаем адаптер
        mAdapter = new RecyclerAdapter(myDataset, dbHelper, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private String[][] getDataSet() {

        String[][] mDataSet = new String[100][4];

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor query = db.rawQuery("SELECT * FROM BD ORDER BY date_time;", null);
        int i = 0;
        if (query.moveToFirst()) {
            do {
                Long date_time_fromBD = query.getLong(1);
                String event_fromBD = query.getString(2);


                mDataSet[i][0] = DateUtils.formatDateTime(this, date_time_fromBD,
                        DateUtils.FORMAT_SHOW_DATE);
                mDataSet[i][1] = DateUtils.formatDateTime(this, date_time_fromBD,
                        DateUtils.FORMAT_SHOW_TIME);
                mDataSet[i][2] = event_fromBD;
                mDataSet[i][3] = query.getString(0);

                i++;
            }
            while (query.moveToNext());
        }
        query.close();
        db.close();
        String[][] mDataSet_return = new String[i][4];
        for (int j = 0; j < i; j++) {
            mDataSet_return[j][0] = mDataSet[j][0];
            mDataSet_return[j][1] = mDataSet[j][1];
            mDataSet_return[j][2] = mDataSet[j][2];
            mDataSet_return[j][3] = mDataSet[j][3];
        }
        dbHelper.close();
        return mDataSet_return;
    }

    public void triggerService() {

        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        long time = calendar.getTimeInMillis();

        alarm.setRepeating(AlarmManager.RTC_WAKEUP, time, 1000, pending);

    }



    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("_id",
                "-1");
        startActivity(intent);
    }
}
