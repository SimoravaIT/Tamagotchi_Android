package com.example.androidapp;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PetController {

    public PetController() { }

    public static void increaseHappiness(Context context, int level) {
        // Increase the happiness of the pet by doing activities like (physically) walking
        // or (virtually) feeding the pet.
        Pet pet = DatabaseController.loadPet(context);
        int newHappiness = pet.getHappiness() + level;

        // Maximum happiness is 100
        if (newHappiness > 100) {
            // newHappiness is greater that 100, so the reminder must be subtracted
            newHappiness = newHappiness - (newHappiness - 100);
        }

        pet.setHappiness(newHappiness);
        DatabaseController.updatePet(context, pet);
    }

    public static void decreaseHappiness(Context context) {
        // Decrease the happiness every 4 hours from the last activity performed (eat, go out, etc)
        Pet pet = DatabaseController.loadPet(context);

        // Author of next 4 lines: Shamim Ahmmed
        // https://stackoverflow.com/questions/17940200/how-to-find-the-duration-of-difference-between-two-dates-in-java
        Date now = Calendar.getInstance().getTime();
        Date lastUpdate = Calendar.getInstance().getTime();
        long duration  = now.getTime() - lastUpdate.getTime();
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

        if (diffInHours >= 4) {
            // Every 4 hours, happiness is decreased by 2.
            // So 50h of inactivity (a little bit more that 2 days) in order to finish the happiness.
            int multiplier = (int) (2 * (diffInHours/4));
            int newHappiness = pet.getHappiness() - multiplier;
            if (newHappiness < 0) {
                newHappiness = 0;   // minimum happiness is 0
            }
            pet.setHappiness(newHappiness);
            DatabaseController.updatePet(context, pet);
        }
    }
}
