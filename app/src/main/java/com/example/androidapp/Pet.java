package com.example.androidapp;

import java.util.Date;

public class Pet {
    private int key;
    private String name;
    private int happiness;
    private Date lastUpdate;
    
    public Pet(int key, String name, int happiness, Date lastUpdate) {
        this.key = key;
        this.name = name;
        this.happiness = happiness;
        this.lastUpdate = lastUpdate;
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

    public Date getLastUpdate() {
        return this.lastUpdate;
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

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
