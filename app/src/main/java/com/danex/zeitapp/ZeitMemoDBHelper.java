package com.danex.zeitapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ZeitMemoDBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = ZeitMemoDBHelper.class.getSimpleName();

    public ZeitMemoDBHelper (Context context){
        super(context, "PLATZHALTER_DATENBANK", null,1 );
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: "+ getDatabaseName()+  " erzeugt.");

    }
    @Override
    public void onCreate(SQLiteDatabase db){

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
