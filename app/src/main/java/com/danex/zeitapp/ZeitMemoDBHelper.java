package com.danex.zeitapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ZeitMemoDBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = ZeitMemoDBHelper.class.getSimpleName();
    public static final String DB_NAME = "ZeitApp_DB.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_DATES = "all_dates";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME_WORKED = "time_Worked";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_DATES +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_TIME_WORKED + " TEXT NOT NULL);";


    public ZeitMemoDBHelper (Context context){
        //super(context, "PLATZHALTER_DATENBANK", null,1 );
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: "+ getDatabaseName()+  " erzeugt.");

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
