package com.example.brrri.datetime;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DateTime.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE_NAME = "BD"; // название таблицы в бд

    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE_TIME = "date_time";
    public static final String COLUMN_EVENT = "event";

    public DatabaseHelper(Context context) {
        // конструктор суперкласса
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // создаем таблицу с полями
        db.execSQL("CREATE TABLE BD (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DATE_TIME  + " INTEGER, "
                + COLUMN_EVENT + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {

    }

}
