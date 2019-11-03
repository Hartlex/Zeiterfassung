package com.danex.zeitapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ZeitMemoDataSource {
    private static final String LOG_TAG = ZeitMemoDataSource.class.getSimpleName();
    private SQLiteDatabase database;
    private ZeitMemoDBHelper dbHelper;

    private String[] columns = {
            ZeitMemoDBHelper.COLUMN_ID,
            ZeitMemoDBHelper.COLUMN_DATE,
            ZeitMemoDBHelper.COLUMN_TIME_WORKED
    };

    public ZeitMemoDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new ZeitMemoDBHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public ZeitMemo createShoppingMemo(String date, String timeWorked) {
        ContentValues values = new ContentValues();
        values.put(ZeitMemoDBHelper.COLUMN_DATE, date);
        values.put(ZeitMemoDBHelper.COLUMN_TIME_WORKED, timeWorked);
        if(isZeitMemoInDB(date)){
            return updateZeitMemo(getZeitMemoID(date),date,timeWorked);
        }


        long insertId = database.insert(ZeitMemoDBHelper.TABLE_DATES, null, values);

        Cursor cursor = database.query(ZeitMemoDBHelper.TABLE_DATES,
                columns, ZeitMemoDBHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        ZeitMemo zeitMemo = cursorToZeitMemo(cursor);
        cursor.close();

        return zeitMemo;
    }
    public ZeitMemo updateZeitMemo(long id, String newDate, String newTimeWorked) {
        ContentValues values = new ContentValues();
        values.put(ZeitMemoDBHelper.COLUMN_DATE, newDate);
        values.put(ZeitMemoDBHelper.COLUMN_TIME_WORKED, newTimeWorked);

        database.update(ZeitMemoDBHelper.TABLE_DATES,
                values,
                ZeitMemoDBHelper.COLUMN_ID + "=" + id,
                null);

        Cursor cursor = database.query(ZeitMemoDBHelper.TABLE_DATES,
                columns, ZeitMemoDBHelper.COLUMN_ID + "=" + id,
                null, null, null, null);

        cursor.moveToFirst();
        ZeitMemo ZeitMemo = cursorToZeitMemo(cursor);
        cursor.close();

        return ZeitMemo;
    }
    private ZeitMemo cursorToZeitMemo(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ZeitMemoDBHelper.COLUMN_ID);
        int idDate = cursor.getColumnIndex(ZeitMemoDBHelper.COLUMN_DATE);
        int idTimeWorked = cursor.getColumnIndex(ZeitMemoDBHelper.COLUMN_TIME_WORKED);

        String date = cursor.getString(idDate);
        String timeWorked = cursor.getString(idTimeWorked);
        long id = cursor.getLong(idIndex);

        ZeitMemo zeitMemo = new ZeitMemo(date, timeWorked, id);

        return zeitMemo;
    }
    public boolean isZeitMemoInDB(String date){
        Cursor cursor = database.rawQuery("select * FROM TABLE_DATES WHERE COLUMN_DATE like "+date,null);
        if(cursor.getCount()<=0) {
            cursor.close();
            return false;
        }
        else return true;
    }
    public long getZeitMemoID(String date){
        Cursor cursor = database.rawQuery("select COLUMN_ID FROM TABLE_DATES WHERE COLUMN_DATE like "+date,null);
        return cursorToZeitMemo(cursor).getId();
    }
    public List<ZeitMemo> getAllZeitMemos() {
        List<ZeitMemo> shoppingMemoList = new ArrayList<>();

        Cursor cursor = database.query(ZeitMemoDBHelper.TABLE_DATES,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        ZeitMemo shoppingMemo;

        while (!cursor.isAfterLast()) {
            shoppingMemo = cursorToZeitMemo(cursor);
            shoppingMemoList.add(shoppingMemo);
            Log.d(LOG_TAG, "ID: " + shoppingMemo.getId() + ", Inhalt: " + shoppingMemo.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return shoppingMemoList;
    }
}
