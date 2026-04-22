package com.example.runtracker;

public class RunItem {

    private final String date;
    private final String distance;
    private final String duration;
    private final String pace;

    public RunItem(String date, String distance, String duration, String pace) {
        this.date = date;
        this.distance = distance;
        this.duration = duration;
        this.pace = pace;
    }

    public String getDate()     { return date; }
    public String getDistance() { return distance; }
    public String getDuration() { return duration; }
    public String getPace()     { return pace; }
}