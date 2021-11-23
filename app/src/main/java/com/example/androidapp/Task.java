package com.example.androidapp;

public class Task {
    private String key;
    private String description;
    private int reward;
    private int numSteps;
    private String location;
    private boolean completed;

    public Task(){ }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return this.description;
    }

    public int getReward() {
        return this.reward;
    }

    public int getNumSteps() {
        return this.numSteps;
    }

    public String getLocation() {
        return this.location;
    }

    public boolean isCompleted(){return this.completed; }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void taskCompleted(){this.completed=true;}


}
