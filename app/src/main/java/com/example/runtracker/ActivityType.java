package com.example.runtracker;

/**
 * Catalog of trackable activities. Each one owns its stable persistence key,
 * display name, icon and simulated speed, so the rest of the app never juggles
 * loose resource ids or magic strings.
 *
 * IMPORTANT: {@link #key} is what gets stored in Firestore. It must stay stable
 * across builds — never persist a resource id, those change between compilations.
 */
public enum ActivityType {

    RUN("run", R.string.nav_run, R.drawable.ic_directions_run, 2.8f),       // ~10 km/h
    HIKE("hike", R.string.actividad_senderismo, R.drawable.ic_directions_walk, 1.4f), // ~5 km/h
    BIKE("bike", R.string.actividad_bicicleta, R.drawable.ic_directions_bike, 6.5f),  // ~23 km/h
    WALK("walk", R.string.actividad_caminar, R.drawable.ic_directions_walk, 1.3f);    // ~4.7 km/h

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

    /** Resolves a stored key back to its type, falling back to {@link #RUN}. */
    public static ActivityType fromKey(String key) {
        if (key != null) {
            for (ActivityType type : values()) {
                if (type.key.equals(key)) return type;
            }
        }
        return RUN;
    }
}
