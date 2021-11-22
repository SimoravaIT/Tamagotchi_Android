package com.example.androidapp;

public class Task {
    private String description;
    private int reward;
    private int numSteps;
    private String location;

    public Task(String description, int reward, int numSteps, String location){
        this.description = description;
        this.reward = reward;
        this.numSteps = numSteps;
        this.location = location;
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
}
