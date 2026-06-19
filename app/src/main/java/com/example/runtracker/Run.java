package com.example.runtracker;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

/** Immutable view of a stored run, decoupled from Firestore types. */
public class Run {

    public final ActivityType type;
    public final Date date;
    public final double distanceKm;
    public final long steps;
    public final long durationSeconds;

    public Run(ActivityType type, Date date, double distanceKm, long steps, long durationSeconds) {
        this.type = type;
        this.date = date;
        this.distanceKm = distanceKm;
        this.steps = steps;
        this.durationSeconds = durationSeconds;
    }

    public static Run fromDocument(DocumentSnapshot doc) {
        Date date = doc.getTimestamp("timestamp") != null ? doc.getTimestamp("timestamp").toDate() : null;
        Double distanceKm = doc.getDouble("distanciaKm");
        Long steps = doc.getLong("pasos");
        Long durationSeconds = doc.getLong("duracionSegundos");
        return new Run(
                ActivityType.fromKey(doc.getString("tipo")),
                date,
                distanceKm != null ? distanceKm : 0,
                steps != null ? steps : 0,
                durationSeconds != null ? durationSeconds : 0);
    }
}
