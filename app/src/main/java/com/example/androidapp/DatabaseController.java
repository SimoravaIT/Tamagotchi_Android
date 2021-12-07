package com.example.androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        // Returns all the available tasks (the not completed ones)
        List<Task> tasks = new LinkedList<Task>();

        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query("AvailableTask", null, "completed=?", new String[]{"0"}, null,
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

        database.execSQL("INSERT INTO AvailableTask ('key', 'completed', 'task.key') " +
                "VALUES ("+task.getKey()+", 0, (SELECT key FROM Task WHERE key="+task.getKey()+"))");
    }

    public static void completeAvailableTask(Context context, String id) {
        // Set the state of an available task to completed.
        // NOTE: this method doesn't check if the user's state comply with the requirements of the task
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("completed", 1);
        database.update("AvailableTask", cv,"key=?", new String[]{id});
    }

    public static void deleteAvailableTasks(Context context) {
        // Dump the entire AvailableTask table.
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        int numberDeletedRecords = database.delete("AvailableTask", null, null);
        database.close();
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

    public static void insertStep(String s, String day, String hour, Context context) {
        /**
         * Function that insert in the DB the step.
         *
         * @param context: application context
         * @param s: timestamp of that step detection
         * @param day: day of the step detection
         * @param hour: hour of the step detection
         */

        ContentValues values = new ContentValues();
        values.put("timestamp", s);
        values.put("day", day);
        values.put("hour", hour);
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        database.insert("Step",null,values);
    }

    public static Integer loadStepsForTheDay(Context context, String date){
        /**
         * Utility function that return the number of steps done during a specific day passed as input.
         *
         * @param context: application context
         * @param date: the date (YYYY-MM-DD) on which the steps were done
         * @return numstep: the steps done in the day 'date'
         */

        List<String> steps = new LinkedList<String>();
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query("Step", null, "day = ?", new String [] {date}, null,
                null, null );

        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            steps.add(cursor.getString(0));
            cursor.moveToNext();
        }
        database.close();
        Integer numSteps = steps.size();
        return numSteps;
    }

    public static int loadStepsBetweenDates(Context context, String date1, String date2){
        /**
         * Utility function that return the number of steps done between 2 dates in format YYYY-MM-DD
         *
         * @param context: application context
         * @param date1: start date
         * @param date2: finish date
         * @return int: the number steps done between the 2 days
         */

        List<String> dates = new LinkedList<String>();
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String[] args= new String[]{date1,date2};
        Cursor cursor = database.query("Step",  new String[]{"day"},
                "day BETWEEN ? AND ?", args, null,
                null, null );

        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            dates.add(cursor.getString(0));
            cursor.moveToNext();
        }
        database.close();
        Log.d("STORED TIMESTAMPS: ", String.valueOf(dates));
        Log.d("STORED TIMESTAMPS: ", String.valueOf(dates.size()));
        return dates.size();
    }
    public static Map<String, Integer> loadStepsByDay(Context context){
        /**
         * Utility function to obtain a Map<string,Integer>  where the string represent the different
         * days and the integer are the steps done on those day.
         *
         * @param context: application context
         * @retutrn map: the Map with the days and the steps done in those days
         */

        Map<String, Integer>  map = new HashMap<>();

        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor=database.query("Step",new String [] {"day","COUNT(*)"},null, null,"day",null,"day");

        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            String tmpKey = cursor.getString(0);
            Integer tmpValue = Integer.parseInt(cursor.getString(1));

            map.put(tmpKey, tmpValue);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();

        return map;
    }

    public static int loadCountTotalSteps(Context context) {
        /**
         * Utility function to obtain athe total number of steps in the table.
         *
         * @param context: application context
         * @retutrn int: number of values inside the table step
         */
        List<String> dates = new LinkedList<String>();
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.query("Step",  new String[]{"day"},
                null, null, null,
                null, null );

        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            dates.add(cursor.getString(0));
            cursor.moveToNext();
        }
        database.close();
        return dates.size();
    }

    public List<Food> loadFoodList(Context context) {
        // Returns the list of food
        List<Food> foodList = new LinkedList<Food>();

        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query("Food", null, null, null, null,
                null, null );

        cursor.moveToFirst();
        for(int i=0; i < cursor.getCount(); i++){
            foodList.add(new Food(cursor.getInt(0), cursor.getString(1),
                    cursor.getInt(2), cursor.getInt(3)));
            cursor.moveToNext();
        }

        database.close();
        cursor.close();

        return foodList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        String CREATE_USER = "CREATE TABLE User ('key' INTEGER PRIMARY KEY, 'money' INTEGER);";
        String CREATE_STEP = "CREATE TABLE Step " +
                "('key' INTEGER PRIMARY KEY, 'timestamp' TEXT, 'day' TEXT, 'hour' TEXT);";
        String CREATE_TASK = "CREATE TABLE Task " +
                "('key' INTEGER PRIMARY KEY, 'description' TEXT, 'reward' TEXT, 'numSteps' INTEGER, 'location' TEXT);";

        // "completed" here must be of type TEXT because then it cannot be parametrized in the queries
        // https://stackoverflow.com/questions/18746149/android-sqlite-selection-args-with-int-values
        String CREATE_AVAILABLE_TASK = "CREATE TABLE AvailableTask " +
                "('key' INTEGER PRIMARY KEY, 'task.key' INTEGER, 'completed' TEXT, FOREIGN KEY ('key') REFERENCES Task('task.key'));";
        String CREATE_FOOD = "CREATE TABLE Food " +
                "('key' INTEGER PRIMARY KEY, 'name' TEXT, 'happinessLevel' INTEGER, 'price' INTEGER);";
        String CREATE_AVAILABLE_FOOD = "CREATE TABLE AvailableFood " +
                "('key' INTEGER PRIMARY KEY, 'food.key' INTEGER, FOREIGN KEY ('key') REFERENCES Food('food.key'));";
        String CREATE_MEDICINE = "CREATE TABLE Medicine " +
                "('key' INTEGER PRIMARY KEY, 'name' TEXT, sicknessLevel INTEGER, 'price' INTEGER);";
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

        // Insert data about tasks
        db.execSQL("INSERT INTO User ('key', 'money') " +
                "VALUES (0, 10)");

        db.execSQL("INSERT INTO Task ('key', 'description', 'reward', 'numSteps') " +
                "VALUES (0, 'Do 1 step', '1', 1)");
        db.execSQL("INSERT INTO Task ('key', 'description', 'reward', 'numSteps') " +
                "VALUES (1, 'Do 2000 steps', '50', 2000)");
        db.execSQL("INSERT INTO Task ('key', 'description', 'reward', 'numSteps') " +
                "VALUES (2, 'Do 3000 steps', '60', 2000)");
        db.execSQL("INSERT INTO Task ('key', 'description', 'reward') " +
                "VALUES (3, 'Expose under sun for 30 mins', '25')");
        db.execSQL("INSERT INTO Task ('key', 'description', 'reward', 'numSteps') " +
                "VALUES (4, 'Do 100 steps', '10', 100)");

        // Insert data about food
        db.execSQL("INSERT INTO Food ('key', 'name', 'happinessLevel', 'price') " +
                "VALUES (0, 'Banana', '15', '15')");
        db.execSQL("INSERT INTO Food ('key', 'name', 'happinessLevel', 'price') " +
                "VALUES (1, 'Steak', '50', '50')");
        db.execSQL("INSERT INTO Food ('key', 'name', 'happinessLevel', 'price') " +
                "VALUES (2, 'Milk', '10', '10')");
        db.execSQL("INSERT INTO Food ('key', 'name', 'happinessLevel', 'price') " +
                "VALUES (3, 'Salad', '5', '5')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
