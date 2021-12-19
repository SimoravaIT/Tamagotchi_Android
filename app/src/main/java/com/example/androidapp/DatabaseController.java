package com.example.androidapp;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
        // Delete the entire AvailableTask entries in the table.
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        int numberDeletedRecords = database.delete("AvailableTask", null, null);
        database.close();
    }

    public static String loadLastTaskGeneration(Context context) {
        // Returns the date of when the available tasks was updated the last time.
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        Cursor cursor = database.query("LastTaskGeneration", null, null, null, null,
                null, null );

        cursor.moveToFirst();
        String lastUpdate = cursor.getString(0);

        cursor.close();
        database.close();

        return lastUpdate;
    }

    public static void updateLastTaskGeneration(Context context) {
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String today = (sdf.format(new Date()));
        Log.d("DATE", today);
        ContentValues cv = new ContentValues();
        cv.put("lastUpdate", today);
        database.update("LastTaskGeneration", cv,"key=?", new String[]{String.valueOf(0)});
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
        // Update the user's wallet
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

    public static Map<String, Integer> loadStepsByDates(Context context,String date1, String date2){
        /**
         * Utility function to obtain a Map<string,Integer>  where the string represent the different
         * days and the integer are the steps done on those day from the 2 date in format YYYY-MM-DD
         * in input.
         * @param date1: start date
         * @param date2: finish date
         * @param context: application context
         * @retutrn map: the Map with the days and the steps done in those days
         */

        Map<String, Integer>  map = new HashMap<>();

        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String[] args= new String[]{date1,date2};
        Cursor cursor=database.query("Step",new String [] {"day","COUNT(*)"},"day BETWEEN ? AND ?", args,"day",null,"day");

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
    public static Map<Integer, Integer> loadStepsByHours(Context context,String date){
        /**
         * Utility function to obtain a Map<string,Integer>  where the string represent the different
         * hours and the integer are the steps done on those hours.
         *
         * @param context: application context
         * @retutrn map: the Map with the hours and the steps done in those hours
         */

        Map<Integer, Integer>  map = new HashMap<>();

        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor=database.query("Step",new String [] {"hour","COUNT(*)"}, "day = ?", new String[]{date},"hour",null,"hour");
        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            Integer tmpKey = Integer.parseInt(cursor.getString(0));
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

    public static List<Food> loadFoodList(Context context) {
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

    public static Pet loadPet(Context context) {
        // Return the pet
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        Cursor cursor = database.query("Pet", null, null, null, null,
                null, null );

        // It is assumed that only one pet can exists in the db
        cursor.moveToFirst();
        Pet pet = null;
        try {
            pet = new Pet(cursor.getInt(0), cursor.getString(1),
                    cursor.getInt(2), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(cursor.getString(3)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cursor.close();
        database.close();

        return pet;
    }

    public static void updatePet(Context context, Pet pet) {
        // Update the pet happiness
        long timeInMillis = System.currentTimeMillis();
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String now = jdf.format(timeInMillis);
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("happiness", pet.getHappiness());
        cv.put("lastUpdate", now);
        database.update("Pet", cv,"key=?", new String[]{String.valueOf(pet.getKey())});
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
        String CREATE_LAST_TASK_GENERATION = "CREATE TABLE LastTaskGeneration ('key' INTEGER PRIMARY KEY, 'lastGeneration' TEXT);";
        String CREATE_FOOD = "CREATE TABLE Food " +
                "('key' INTEGER PRIMARY KEY, 'name' TEXT, 'happinessLevel' INTEGER, 'price' INTEGER);";
        String CREATE_AVAILABLE_FOOD = "CREATE TABLE AvailableFood " +
                "('key' INTEGER PRIMARY KEY, 'food.key' INTEGER, FOREIGN KEY ('key') REFERENCES Food('food.key'));";
        String CREATE_MEDICINE = "CREATE TABLE Medicine " +
                "('key' INTEGER PRIMARY KEY, 'name' TEXT, sicknessLevel INTEGER, 'price' INTEGER);";
        String CREATE_AVAILABLE_MEDICINE = "CREATE TABLE AvailableMedicine " +
                "('key' INTEGER PRIMARY KEY, 'medicine.key' INTEGER, FOREIGN KEY ('key') REFERENCES Medicine('medicine.key'));";
        String CREATE_PET = "CREATE TABLE Pet ('key' INTEGER PRIMARY KEY, 'name' TEXT, 'happiness' INTEGER, 'lastUpdate' TEXT);";

        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_STEP);
        db.execSQL(CREATE_TASK);
        db.execSQL(CREATE_AVAILABLE_TASK);
        db.execSQL(CREATE_LAST_TASK_GENERATION);
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
                "VALUES (0, 'Wheat', '5', '5')");
        db.execSQL("INSERT INTO Food ('key', 'name', 'happinessLevel', 'price') " +
                "VALUES (1, 'worm ', '10', '10')");
        db.execSQL("INSERT INTO Food ('key', 'name', 'happinessLevel', 'price') " +
                "VALUES (2, 'lettuce', '25', '25')");


        // Insert the pet
        // TODO: User must choose the name of the pet!
        long timeInMillis = System.currentTimeMillis();
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String now = jdf.format(timeInMillis);
        db.execSQL("INSERT INTO Pet ('key', 'name', 'happiness', 'lastUpdate') " +
                "VALUES (0, 'Colombo', 70, '"+now+"')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
