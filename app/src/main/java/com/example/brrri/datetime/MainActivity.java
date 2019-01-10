package com.example.brrri.datetime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import android.database.sqlite.SQLiteDatabase;


import java.util.Calendar;



public class MainActivity extends AppCompatActivity {

    TextView currentTime, currentDate, Text;
    Calendar dateAndTime=Calendar.getInstance();
    DatabaseHelper dbHelper;
    String message;
    Long date_time_fromBD;
    String event_fromBD;
    int id;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        Intent intent = getIntent();
        message = intent.getStringExtra("_id");
        id = Integer.parseInt(message);

        date_time_fromBD = intent.getLongExtra("date_time_fromBD", 0);
        event_fromBD = intent.getStringExtra("event_fromBD");

        setContentView(R.layout.activity_main);
        currentDate=(TextView)findViewById(R.id.currentDate);
        currentTime=(TextView)findViewById(R.id.currentTime);
        Text = (TextView)findViewById(R.id.Text);
        setDateTime();
        dbHelper = new DatabaseHelper(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        onBackPressed();
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        DatePickerDialog set = new DatePickerDialog  (MainActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));

        set.getDatePicker().setMinDate(System.currentTimeMillis());
        set.show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        TimePickerDialog set = new TimePickerDialog(MainActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true);
        set.show();
    }
    // установка начальных даты и времени
    private void setDateTime() {

        if (id<0) {
            currentDate.setText(DateUtils.formatDateTime(this,
                    dateAndTime.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

            currentTime.setText(DateUtils.formatDateTime(this,
                    dateAndTime.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_TIME));
        }

        else{
            currentDate.setText(DateUtils.formatDateTime(this,
                    date_time_fromBD,
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

            currentTime.setText(DateUtils.formatDateTime(this,
                    date_time_fromBD,
                    DateUtils.FORMAT_SHOW_TIME));
            Text.setText(event_fromBD);

        }

    }

    private void setInitialDateTime() {

            currentDate.setText(DateUtils.formatDateTime(this,
                    dateAndTime.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

            currentTime.setText(DateUtils.formatDateTime(this,
                    dateAndTime.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_TIME));
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };



    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };


    public void onClick(View view){

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // подготовим данные для вставки в виде пар: наименование столбца - значение

        ContentValues row1 = new ContentValues();
        row1.put("date_time", dateAndTime.getTimeInMillis());
        row1.put("event", Text.getText().toString());

        if (id<0){

            db.insert(dbHelper.TABLE_NAME, null, row1);
        }

        else{
            db.update(dbHelper.TABLE_NAME, row1, dbHelper.COLUMN_ID + "=" +message,null);
        }

        // закрываем подключение к БД
        dbHelper.close();
        onBackPressed();
    }
}