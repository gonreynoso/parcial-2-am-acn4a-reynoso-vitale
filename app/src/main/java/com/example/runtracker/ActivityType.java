package com.example.runtracker;

public enum ActivityType {

    RUN("run", R.string.nav_run, R.drawable.ic_directions_run, 2.8f),       
    HIKE("hike", R.string.actividad_senderismo, R.drawable.ic_directions_walk, 1.4f), 
    BIKE("bike", R.string.actividad_bicicleta, R.drawable.ic_directions_bike, 6.5f),  
    WALK("walk", R.string.actividad_caminar, R.drawable.ic_directions_walk, 1.3f);    

    public final String key;
    public final int nameRes;
    public final int iconRes;
    public final float speedMetersPerSecond;

    ActivityType(String key, int nameRes, int iconRes, float speedMetersPerSecond) {
        this.key = key;
        this.nameRes = nameRes;
        this.iconRes = iconRes;
        this.speedMetersPerSecond = speedMetersPerSecond;
    }

    
    public static ActivityType fromKey(String key) {
        if (key != null) {
            for (ActivityType type : values()) {
                if (type.key.equals(key)) return type;
            }
        }
        return RUN;
    }
}
