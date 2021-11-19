package com.example.androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DB_tamagotchi";


    public static final String TABLE_TASK_NAME = "tasks";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_REWARD = "reward";
    public static final String KEY_STEPS = "numsteps";
    public static final String KEY_LOCATION = "location";

    // Default SQL for creating a table in a database
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_TASK_NAME + " (" +
            KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME  + " TEXT NOT NULL, " + KEY_DESCRIPTION+ " TEXT , "
            + KEY_REWARD + " INTEGER , " + KEY_STEPS + " INTEGER , "
            + KEY_LOCATION + " TEXT );";


    // The constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int NumberOfRows(Context c,String tableName){
        DatabaseHelper databaseHelper = new DatabaseHelper(c);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        int numRows = (int) DatabaseUtils.longForQuery(database, "SELECT COUNT(*) FROM "+tableName, null);
        return numRows;
    }

    public static void DeleteAllTasks(Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int numberDeletedRecords =0;

        numberDeletedRecords = database.delete(DatabaseHelper.TABLE_TASK_NAME, null, null);
        database.close();

        // display the number of deleted records with a Toast message
        Toast.makeText(context,"Deleted " + String.valueOf(numberDeletedRecords) + " tasks ",Toast.LENGTH_LONG).show();

    }


}
