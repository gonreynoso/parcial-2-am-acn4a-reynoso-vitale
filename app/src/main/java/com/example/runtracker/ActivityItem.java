package com.example.runtracker;

public class ActivityItem {

    public enum Level { BEGINNER, INTERMEDIATE, ADVANCED }

    private final int iconRes;
    private final String title;
    private final String description;
    private final String duration;
    private final Level level;

    public ActivityItem(int iconRes, String title, String description, String duration, Level level) {
        this.iconRes = iconRes;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.level = level;
    }

    public int getIconRes()         { return iconRes; }
    public String getTitle()        { return title; }
    public String getDescription()  { return description; }
    public String getDuration()     { return duration; }
    public Level getLevel()         { return level; }
}