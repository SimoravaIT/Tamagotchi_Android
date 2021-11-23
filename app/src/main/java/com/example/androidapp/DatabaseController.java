package com.example.androidapp;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

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
    private static android.widget.Toast Toast;

    private Context context;

    public DatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static List<Task> loadTasks(Context context){
        // Returns all tasks
        List<Task> tasks = new LinkedList<Task>();

        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query("Task", null, null, null, null,
                null, null );

        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            Task task = new Task();
            task.setKey(cursor.getString(0));
            task.setDescription(cursor.getString(1));
            task.setReward(cursor.getInt(2));
            task.setNumSteps(cursor.getInt(3));
            task.setLocation(cursor.getString(4));
            tasks.add(task);
            cursor.moveToNext();
        }
        database.close();
        cursor.close();

        return tasks;
    }

    public static Task loadSingleTask(Context context, int key){
        // Returns a Task with given key
        Task task = new Task();

        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String [] whereArgs = { String.valueOf(key) };

        Cursor cursor = database.query("Task", null, "key=?", whereArgs, null,
                null, null );

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            task.setKey(cursor.getString(0));
            task.setDescription(cursor.getString(1));
            task.setReward(cursor.getInt(2));
            task.setNumSteps(cursor.getInt(3));
            task.setLocation(cursor.getString(4));
        }

        database.close();
        cursor.close();

        return task;
    }

    public static List<Task> loadAvailableTasks(Context context) {
        // Returns all the available tasks of the day
        List<Task> tasks = new LinkedList<Task>();

        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query("AvailableTask", null, null, null, null,
                null, null );

        cursor.moveToFirst();
        for(int i=0; i < cursor.getCount(); i++){
            tasks.add(loadSingleTask(context, cursor.getInt(1)));
            cursor.moveToNext();
        }

        database.close();
        cursor.close();

        return tasks;
    }

    public static void insertAvailableTask(Context context, Task task) {
        // Insert an available task
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        database.execSQL("INSERT INTO AvailableTask ('key', 'task.key') " +
                "VALUES ("+task.getKey()+", (SELECT key FROM Task WHERE key="+task.getKey()+"))");
    }

    public static void deleteAvailableTasks(Context context) {
        // Dump the entire AvailableTask table.
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        int numberDeletedRecords = database.delete("AvailableTask", null, null);
        database.close();

       // Toast.makeText(context, "Deleted: " + String.valueOf(numberDeletedRecords) + " tasks", Toast.LENGTH_LONG).show();
    }

    public static User loadUser(Context context) {
        // Returns the user (or the first user, note that the constrain here is "there can only
        // exists one user in the DB").
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        Cursor cursor = database.query("User", null, null, null, null,
                null, null );

        cursor.moveToFirst();
        User user = new User();
        user.setKey(cursor.getInt(0));
        user.setMoney(cursor.getInt(1));

        cursor.close();
        database.close();

        return user;
    }

    public static void updateUser(Context context, User user) {
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("money", user.getMoney());
        database.update("User", cv,"key=?", new String[]{String.valueOf(user.getKey())});

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        String CREATE_USER = "CREATE TABLE User ('key' INTEGER PRIMARY KEY, 'money' INTEGER);";
        String CREATE_STEP = "CREATE TABLE Step " +
                "('key' INTEGER PRIMARY KEY, 'timestamp' TEXT, 'day' TEXT, 'hour' TEXT);";
        String CREATE_TASK = "CREATE TABLE Task " +
                "('key' INTEGER PRIMARY KEY, 'description' TEXT, 'reward' TEXT, 'numSteps' INTEGER, 'location' TEXT);";
        String CREATE_AVAILABLE_TASK = "CREATE TABLE AvailableTask " +
                "('key' INTEGER PRIMARY KEY, 'task.key' INTEGER, FOREIGN KEY ('key') REFERENCES Task('task.key'));";
        String CREATE_FOOD = "CREATE TABLE Food " +
                "('key' INTEGER PRIMARY KEY, 'happinessLevel' INTEGER, 'price' INTEGER);";
        String CREATE_AVAILABLE_FOOD = "CREATE TABLE AvailableFood " +
                "('key' INTEGER PRIMARY KEY, 'food.key' INTEGER, FOREIGN KEY ('key') REFERENCES Food('food.key'));";
        String CREATE_MEDICINE = "CREATE TABLE Medicine " +
                "('key' INTEGER PRIMARY KEY, sicknessLevel INTEGER, 'price' INTEGER);";
        String CREATE_AVAILABLE_MEDICINE = "CREATE TABLE AvailableMedicine " +
                "('key' INTEGER PRIMARY KEY, 'medicine.key' INTEGER, FOREIGN KEY ('key') REFERENCES Medicine('medicine.key'));";
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
                "VALUES (0, 'Do 1000 steps', '25', 1000)");
        db.execSQL("INSERT INTO Task ('key', 'description', 'reward', 'numSteps') " +
                "VALUES (1, 'Do 2000 steps', '50', 2000)");
        db.execSQL("INSERT INTO Task ('key', 'description', 'reward') " +
                "VALUES (2, 'Expose under sun for 30 mins', '25')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
