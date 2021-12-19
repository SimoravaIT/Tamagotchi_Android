package com.example.androidapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class TaskController {

    public TaskController(Context context){
        // New available tasks are generated as soon as one is completed
        DatabaseController databaseHelper = new DatabaseController(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Task> availableTasks = DatabaseController.loadAvailableTasks(context);
        if (availableTasks.size() < 3 && !hasBeenUpdatedToday(context)) {
            generateTasks(context);
        }
    }

    public static void generateTasks(Context context) {
        // Take randomly n tasks from the db and put them in the AvailableTask table.
        DatabaseController.deleteAvailableTasks(context);
        List<Task> tasks = generateRandomTasks(context);
        for (int i=0; i < tasks.size(); i++) {
            DatabaseController.insertAvailableTask(context, tasks.get(i));
        }
        DatabaseController.updateLastTaskGeneration(context);
    }

    public List<Task> completeTasks(Context context) {
        // Check if the user completed any task correctly and delete it from AvailableTask.
        List<Task> atasks = DatabaseController.loadAvailableTasks(context);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = df.format(c);
        int todaySteps = DatabaseController.loadStepsForTheDay(context, today);
        boolean completed = false;

        for (int i = 0; i < atasks.size(); i++) {
            if (atasks.get(i).getNumSteps() > 0) {
                // Task is about completing number of steps
                if (taskIsCompleted(atasks.get(i).getNumSteps(), todaySteps)) {
                    DatabaseController.completeAvailableTask(context, atasks.get(i).getKey());
                    Task temp=atasks.get(i);
                    temp.setCompleted(true);
                   // atasks.set(i,temp);
                }
            } else {
                // Task is about something else
            }
        }
        return atasks;
    }

    private boolean taskIsCompleted(int req, int state) {
        // Input: 'req' is the number of steps/time outside required by the task
        //        'state' is the actual state of steps/time outside performed by the user
        if (state >= req) {
            return true;
        } else {
            return false;
        }
    }

    public static void collectReward(Context context, Task task) {
        // Allocate money in the user's wallet according to the task.
        User user = DatabaseController.loadUser(context);
        int oldMoney = user.getMoney();
        user.setMoney(task.getReward() + oldMoney);
        DatabaseController.updateUser(context, user);
    }

    private static List<Task> generateRandomTasks(Context context) {
        List<Task> tasks = new LinkedList<>();

        // Generate 'toBeGenerated' random numbers which represent the tasks to be retrieved
        // from the database.
        Random rand = new Random();
        Set<Integer> unique = new HashSet<Integer>();
        int upperbound = 3;
        int toBeGenerated = 3;
        int[] arr = new int[toBeGenerated];
        for (int i=0; i<arr.length; i++) {
            int number = rand.nextInt(upperbound);
            while (unique.contains(number)) // 'number' is a duplicate
                number = rand.nextInt(upperbound);
            arr[i] = number;
            unique.add(number); // adding to the set
        }

        for (int j : arr) {
            tasks.add(DatabaseController.loadSingleTask(context, j));
        }

        return tasks;
    }

    private boolean hasBeenUpdatedToday(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date lastUpdate = null;
        Date today = null;
        try {
            lastUpdate = sdf.parse(DatabaseController.loadLastTaskGeneration(context));
            today = sdf.parse(String.valueOf(new Date()));
        } catch (ParseException e) {
            return false;
        }
        long diffInMillies = Math.abs(today.getTime() - lastUpdate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff >= 1) {
            return true;
        }
        return false;
    }
}
