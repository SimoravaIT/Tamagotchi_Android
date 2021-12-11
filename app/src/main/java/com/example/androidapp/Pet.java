package com.example.androidapp;

public class Pet {
    private int key;
    private String name;
    private int happiness;
    
    public Pet(int key, String name, int happiness) {
        this.key = key;
        this.name = name;
        this.happiness = happiness;
    }

    public int getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }
    
    public int getHappiness() {
        return this.happiness;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }
}
