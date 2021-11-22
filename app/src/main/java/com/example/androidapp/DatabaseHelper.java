package com.example.androidapp;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "DB_tamagotchi";

    public static final String TABLE_STEPS_NAME = "num_steps";
    public static final String KEY_STEPS_ID = "id";
    public static final String KEY_STEPS_TIMESTAMP = "timestamp";
    public static final String KEY_STEPS_HOUR = "hour";
    public static final String KEY_STEPS_DAY = "day";



    public static final String TABLE_TASKS_NAME = "tasks";
    public static final String KEY_TASKS_ID = "id";
    public static final String KEY_TASKS_NAME = "title";
    public static final String KEY_TASKS_DESCRIPTION = "description";
    public static final String KEY_TASKS_REWARD = "reward";
    public static final String KEY_TASKS_STEPS = "numsteps";
    public static final String KEY_TASKS_LOCATION = "location";


    // The constructor
    public  DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Default SQL for creating the table task
    public static final String CREATE_TABLE_TASKS_SQL = "CREATE TABLE " + TABLE_TASKS_NAME + " (" +
            KEY_TASKS_ID + " INTEGER PRIMARY KEY, " + KEY_TASKS_NAME + " TEXT NOT NULL, " + KEY_TASKS_DESCRIPTION + " TEXT , "
            + KEY_TASKS_REWARD + " INTEGER , " + KEY_TASKS_STEPS + " INTEGER , "
            + KEY_TASKS_LOCATION + " TEXT );";

    public static final String CREATE_TABLE_STEPS__SQL = "CREATE TABLE " + TABLE_STEPS_NAME + " (" +
            KEY_STEPS_ID + " INTEGER PRIMARY KEY, " + KEY_STEPS_DAY + " TEXT, " + KEY_STEPS_HOUR + " TEXT, "
            + KEY_STEPS_TIMESTAMP + " TEXT);";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASKS_SQL);
        db.execSQL(CREATE_TABLE_STEPS__SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    /*
     * THE FOLLOWING PART IS ABOUT THE METHODS THAT ARE GENERIC
     *
     *
     * */

    /**
     * Utility function to know the number of rows in the gave table
     *
     * @param c: application context
     * @param table_name: the name of the table
     */
    public int NumberOfRows(Context c,String table_name){
        DatabaseHelper databaseHelper = new DatabaseHelper(c);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        int numRows = (int) DatabaseUtils.longForQuery(database, "SELECT COUNT(*) FROM "+ table_name, null);
        return numRows;
    }

    /**
     * Utility function to delete all records from the data base from the giving table
     *
     * @param context: application context
     * @param table_name: the name of the table
     */
    public static void DeleteAllRows(Context context, String table_name){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int numberDeletedRecords =0;
        numberDeletedRecords = database.delete(table_name, null, null);
        database.close();
        // display the number of deleted records with a Toast message
        Toast.makeText(context,"Deleted " + String.valueOf(numberDeletedRecords)+ " ropws from "+table_name,Toast.LENGTH_LONG).show();
    }
    /**
     * Utility function to load all records in the database
     *
     * @param context: application context
     * @param table_name : name of the table
     * @param columns : name of the columns and for the group by the first element
     */
    //Ancora da testare
    public static void loadRecords(Context context,String table_name,String[] columns){
        List<String> tostamp = new LinkedList<String>();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        //group by the first element of the arryay,
        Cursor cursor = database.query(table_name, columns, null, null, columns[0],
                null, null );
        // iterate over returned elements
        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            tostamp.add(cursor.getString(0));
            cursor.moveToNext();
        }
        database.close();
        Log.d("STORED values: ", String.valueOf(tostamp));
    }

    /*
     * THE FOLLOWING PART IS ABOUT THE METHODS THAT ARE FOR THE TASKS
     *
     *
     * */
    /**
     * It return from the DB the list of the names
     *
     * @param context: application context
     */
    public static List<String> ObtainTasksNames(Context context) {
        List<String> temp = new LinkedList<String>();
        String[] col={DatabaseHelper.KEY_TASKS_NAME};
        DatabaseHelper databaseHelper = new DatabaseHelper(context);//not sure getContext, check
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.query(DatabaseHelper.TABLE_TASKS_NAME,
                col, null, null, null,
                null, null );
        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++) {
            temp.add(cursor.getString(0));
            cursor.moveToNext();
        }
        //verification of numbers
        Integer numTask = temp.size();


        database.close();
        return temp;
    }


    public static String ObtainTaskDescription(Context context,String task_name){
        List<String> temp = new LinkedList<String>();
        String[] col={DatabaseHelper.KEY_TASKS_DESCRIPTION};
        DatabaseHelper databaseHelper = new DatabaseHelper(context);//not sure getContext, check
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String selection = KEY_TASKS_NAME+" =?";
        String[] selectionArgs = {task_name};
        Cursor cursor = database.query(DatabaseHelper.TABLE_TASKS_NAME,
                col, selection, selectionArgs, null,
                null, null );
        cursor.moveToFirst();

        for (int index=0; index < cursor.getCount(); index++) {
            temp.add(cursor.getString(0));
            cursor.moveToNext();
        }
        //verification of numbers
        Integer numTask = temp.size();


        database.close();
        return temp.get(0);
    }

}
