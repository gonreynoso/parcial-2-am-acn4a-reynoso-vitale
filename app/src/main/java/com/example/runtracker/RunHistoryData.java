package com.example.runtracker;

import java.util.Arrays;
import java.util.List;

public class RunHistoryData {

    public static List<RunItem> getRecent() {
        return Arrays.asList(
                new RunItem("Hoy",          "5.2 km",  "28:14", "5:25 /km"),
                new RunItem("Ayer",         "10.0 km", "58:42", "5:52 /km"),
                new RunItem("Sáb 19 Abr",   "3.5 km",  "18:05", "5:10 /km"),
                new RunItem("Jue 17 Abr",   "7.8 km",  "42:30", "5:27 /km"),
                new RunItem("Mar 15 Abr",   "5.0 km",  "26:48", "5:22 /km"),
                new RunItem("Dom 13 Abr",   "12.3 km", "1:10:15", "5:42 /km")
        );
    }

    public static Stats getStats() {
        return new Stats("43.8 km", "6", "4h 42m");
    }

    public static int[] getWeeklyKm() {
        return new int[] { 0, 5, 0, 8, 3, 7, 5 }; // D, L, M, M, J, V, S
    }

    public static class Stats {
        public final String totalKm, totalRuns, totalTime;
        public Stats(String totalKm, String totalRuns, String totalTime) {
            this.totalKm = totalKm;
            this.totalRuns = totalRuns;
            this.totalTime = totalTime;
        }
    }
}