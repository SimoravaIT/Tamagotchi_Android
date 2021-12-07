package com.example.androidapp;

public class Food {
    private int key;
    private String name;
    private int happinessLevel;
    private int price;

    public Food(int key, String name, int happinessLevel, int price) {
        this.key = key;
        this.name = name;
        this.happinessLevel = happinessLevel;
        this.price = price;
    }

    public int getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public int getHappinessLevel() {
        return this.happinessLevel;
    }

    public int getPrice() {
        return this.price;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHappinessLevel(int happinessLevel) {
        this.happinessLevel = happinessLevel;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
