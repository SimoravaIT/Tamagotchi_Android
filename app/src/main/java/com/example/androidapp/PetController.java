package com.example.androidapp;

import android.content.Context;
import android.util.Log;

public class PetController {

    public PetController() { }

    public void increaseHappiness(Context context, int level) {
        // Increase the happiness of the pet by doing activities like (physically) walking
        // or (virtually) feeding the pet.
        Pet pet = DatabaseController.loadPet(context);
        int newHappiness = pet.getHappiness() + level;

        if (newHappiness <= 100) {
            // maximum happiness is 100
            pet.setHappiness(newHappiness);
        } else {
            // newHappiness is greater that 100, so the reminder must be subtracted
            newHappiness = newHappiness - (newHappiness - 100);
            pet.setHappiness(newHappiness);
        }

        DatabaseController.updatePet(context, pet);
    }
}
