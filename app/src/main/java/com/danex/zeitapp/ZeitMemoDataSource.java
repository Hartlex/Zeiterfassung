package com.danex.zeitapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class ZeitMemoDataSource {
    private static final String LOG_TAG = ZeitMemoDataSource.class.getSimpleName();
    private SQLiteDatabase database;
    private ZeitMemoDBHelper dbHelper;

    public ZeitMemoDataSource(Context context){
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new ZeitMemoDBHelper(context);
    }
}
