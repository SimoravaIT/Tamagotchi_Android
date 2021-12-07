package com.example.androidapp;

import android.content.Context;

public class ShopController {
    public ShopController(Context context) { }

    public Food buy(Context context, Food food) {
        // Check if the user has enough money, update user's wallet and return the food just bought
        // If the user doesn't have enough money, it returns null.
        // NOTE: the return value is useful to understand whether it is the case to trigger
        // the eat animation fragment-side or not.

        User user = DatabaseController.loadUser(context);
        int wallet = user.getMoney();
        int price = food.getPrice();

        if (hasEnoughMoney(wallet, price)) {
            // Update user's wallet
            user.setMoney(wallet - price);
            DatabaseController.updateUser(context, user);
            // TODO: call the 'eat' method
            return food;
        } else {
            return null;
        }
    }

    public void eat(Food food) {
        // Update pet's happiness level according to the food given in input
    }

    private boolean hasEnoughMoney(int wallet, int price) {
        if (wallet >= price) {
            return true;
        } else {
            return false;
        }
    }
}
