package com.example.runtracker;

import com.google.firebase.firestore.DocumentSnapshot;

public class Workout {

    public static final String GOAL_NONE = "";
    public static final String GOAL_DISTANCE = "distance";
    public static final String GOAL_TIME = "time";

    public final String id;
    public final String name;
    public final ActivityType type;
    
    public final String goalType;
    
    public final double goalValue;

    public Workout(String id, String name, ActivityType type, String goalType, double goalValue) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.goalType = goalType;
        this.goalValue = goalValue;
    }

    public boolean hasGoal() {
        return (GOAL_DISTANCE.equals(goalType) || GOAL_TIME.equals(goalType)) && goalValue > 0;
    }

    public static Workout fromDocument(DocumentSnapshot doc) {
        String name = doc.getString("name");
        String goalType = doc.getString("goalType");
        Double goalValue = doc.getDouble("goalValue");
        return new Workout(
                doc.getId(),
                name != null ? name : "",
                ActivityType.fromKey(doc.getString("type")),
                goalType != null ? goalType : GOAL_NONE,
                goalValue != null ? goalValue : 0);
    }
}
