package com.example.androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class DatabaseController extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tamagotchi";

    public static final String USER = "User";
    public static final String STEP = "Step";
    public static final String TASK = "Task";
    public static final String AVAILABLE_TASK = "AvailableTask";
    public static final String FOOD = "Food";
    public static final String AVAILABLE_FOOD = "AvailableFood";
    public static final String MEDICINE = "Medicine";
    public static final String AVAILABLE_MEDICINE = "AvailableMedicine";
    public static final String PET = "Pet";

    public static final String KEY_ID = "id";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_DAY = "day";
    public static final String KEY_HOUR = "hour";

    private Context context;

    // TODO 2: Add the constructor
    public DatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Load all records in the database
    public static List<String> loadTasks(Context context){
        List<String> tasks = new LinkedList<String>();
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query("Task", null, null, null, null,
                null, null );

        // iterate over returned elements
        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            tasks.add(cursor.getString(1));
            cursor.moveToNext();
        }
        database.close();
        Log.d("STORED TASKS: ", String.valueOf(tasks));

        return tasks;
    }

    //TODO 5: Delete all records from the database
    //public static void deleteRecords(Context context){
    //    StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
    //    SQLiteDatabase database = database = databaseHelper.getWritableDatabase();
    //    int numberDeletedRecords = 0;
//
    //    numberDeletedRecords = database.delete(StepAppOpenHelper.TABLE_NAME, null, null);
    //    database.close();
//
    //    Toast.makeText(context, "Deleted: " + String.valueOf(numberDeletedRecords) + " steps", Toast.LENGTH_LONG).show();
    //}
//
    //// load records from a single day
    //public static Integer loadSingleRecord(Context context, String date){
    //    List<String> steps = new LinkedList<String>();
    //    // Get the readable database
    //    StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
    //    SQLiteDatabase database = databaseHelper.getReadableDatabase();
//
    //    String where = StepAppOpenHelper.KEY_DAY + " = ?";
    //    String [] whereArgs = { date };
//
    //    Cursor cursor = database.query(StepAppOpenHelper.TABLE_NAME, null, where, whereArgs, null,
    //            null, null );
//
    //    // iterate over returned elements
    //    cursor.moveToFirst();
    //    for (int index=0; index < cursor.getCount(); index++){
    //        steps.add(cursor.getString(0));
    //        cursor.moveToNext();
    //    }
    //    database.close();
//
    //    Integer numSteps = steps.size();
    //    Log.d("STORED STEPS TODAY: ", String.valueOf(numSteps));
    //    return numSteps;
    //}


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        String CREATE_USER = "CREATE TABLE User ('key' INTEGER PRIMARY KEY, 'money' INTEGER);";
        String CREATE_STEP = "CREATE TABLE Step " +
                "('key' INTEGER PRIMARY KEY, 'timestamp' TEXT, 'day' TEXT, 'hour' TEXT);";
        String CREATE_TASK = "CREATE TABLE Task " +
                "('key' INTEGER PRIMARY KEY, 'description' TEXT, 'reward' TEXT, 'numSteps' INTEGER, 'location' TEXT);";
        String CREATE_AVAILABLE_TASK = "CREATE TABLE AvailableTask " +
                "('key' INTEGER PRIMARY KEY, FOREIGN KEY ('key') REFERENCES Task('key'));";
        String CREATE_FOOD = "CREATE TABLE Food " +
                "('key' INTEGER PRIMARY KEY, 'happinessLevel' INTEGER, 'price' INTEGER);";
        String CREATE_AVAILABLE_FOOD = "CREATE TABLE AvailableFood " +
                "('key' INTEGER PRIMARY KEY, FOREIGN KEY ('key') REFERENCES Food('key'));";
        String CREATE_MEDICINE = "CREATE TABLE Medicine " +
                "('key' INTEGER PRIMARY KEY, sicknessLevel INTEGER, 'price' INTEGER);";
        String CREATE_AVAILABLE_MEDICINE = "CREATE TABLE AvailableMedicine " +
                "('key' INTEGER PRIMARY KEY, FOREIGN KEY ('key') REFERENCES Medicine('key'));";
        String CREATE_PET = "CREATE TABLE Pet ('key' INTEGER PRIMARY KEY, 'name' TEXT, 'happiness' INTEGER);";

        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_STEP);
        db.execSQL(CREATE_TASK);
        db.execSQL(CREATE_AVAILABLE_TASK);
        db.execSQL(CREATE_FOOD);
        db.execSQL(CREATE_AVAILABLE_FOOD);
        db.execSQL(CREATE_MEDICINE);
        db.execSQL(CREATE_AVAILABLE_MEDICINE);
        db.execSQL(CREATE_PET);

        // Insert data
        db.execSQL("INSERT INTO Task ('key', 'description', 'reward', 'numSteps') " +
                "VALUES (1, 'Do 1000 steps', '25', 1000)");
        db.execSQL("INSERT INTO Task ('key', 'description', 'reward', 'numSteps') " +
                "VALUES (2, 'Do 2000 steps', '50', 2000)");
        db.execSQL("INSERT INTO Task ('key', 'description', 'reward') " +
                "VALUES (3, 'Expose under sun for 30 mins', '25')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
