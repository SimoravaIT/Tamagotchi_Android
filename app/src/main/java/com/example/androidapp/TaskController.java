package com.example.androidapp;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class TaskController {

    public TaskController(Context context){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        // At the moment new tasks are created every time the user access after 16:00, but it later
        // we should change that behavior by setting a temporal window in which the user can access
        // the app and get new tasks (reminding it by using a notification).
        if (hour >= 16) {
            generateTasks(context);
        }
    }

    public static void generateTasks(Context context) {
        // Take randomly n tasks from the db and put them in the AvailableTask table every day.
        DatabaseController.deleteAvailableTasks(context);

        List<Task> tasks = generateRandomTasks(context);

        for (int i=0; i < tasks.size(); i++) {
            DatabaseController.insertAvailableTask(context, tasks.get(i));
        }
    }

    public void completeTask(Context context) {
        // Check if the user completed the task correctly and delete it from AvailableTask.
    }

    public void collectReward(Context context, Task task) {
        // Allocate money in the user's wallet according to the task.
        User user = DatabaseController.loadUser(context);
        user.setMoney(task.getReward());
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
}
