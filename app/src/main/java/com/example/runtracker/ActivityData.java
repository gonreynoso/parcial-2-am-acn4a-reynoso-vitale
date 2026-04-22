package com.example.runtracker;

import java.util.Arrays;
import java.util.List;

public class ActivityData {

    public static List<ActivityItem> getRecommended() {
        return Arrays.asList(
                new ActivityItem(
                        R.drawable.ic_directions_run,
                        "Correr 5K",
                        "Carrera continua a ritmo cómodo",
                        "30 min",
                        ActivityItem.Level.BEGINNER
                ),
                new ActivityItem(
                        R.drawable.ic_bolt,
                        "Intervalos HIIT",
                        "Alta intensidad para quemar grasa",
                        "20 min",
                        ActivityItem.Level.INTERMEDIATE
                ),
                new ActivityItem(
                        R.drawable.ic_route,
                        "Carrera larga",
                        "10K a ritmo lento y constante",
                        "60 min",
                        ActivityItem.Level.INTERMEDIATE
                ),
                new ActivityItem(
                        R.drawable.ic_speed,
                        "Tramos de Sprint",
                        "6x400m a velocidad máxima",
                        "25 min",
                        ActivityItem.Level.ADVANCED
                ),
                new ActivityItem(
                        R.drawable.ic_self_improvement,
                        "Trote Recuperativo",
                        "Sesión suave para recuperar",
                        "15 min",
                        ActivityItem.Level.BEGINNER
                ),
                new ActivityItem(
                        R.drawable.ic_directions_walk,
                        "Caminata Activa",
                        "Cardio de baja intensidad",
                        "45 min",
                        ActivityItem.Level.BEGINNER
                )
        );
    }
}