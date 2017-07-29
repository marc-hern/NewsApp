package com.example.marcus.newsapp.data;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Marcus on 7/28/2017.
 */

public class DBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "news.db";
    private static final String TAG = "dbhelper";

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Main method within DBHelper to create a database with the
    // given Strings from Contract.java in data.  Will store the
    // data of the news stories
    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryString = "CREATE TABLE " +
                Contract.OBJECT_TABLES.TABLE_NAME + " ("+
                Contract.OBJECT_TABLES._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.OBJECT_TABLES.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                Contract.OBJECT_TABLES.COLUMN_NAME_DESC + " TEXT NOT NULL, " +
                Contract.OBJECT_TABLES.COLUMN_NAME_URL + " TEXT NOT NULL, " +
                Contract.OBJECT_TABLES.COLUMN_NAME_DATE + " DATE " +
                "); ";



        Log.d(TAG, "Create table SQL: " + queryString);
        db.execSQL(queryString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}