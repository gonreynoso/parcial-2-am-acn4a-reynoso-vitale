package com.example.runtracker;

import java.util.List;

public class RunStats {

    public final double totalKm;
    public final int count;
    public final double maxDistanceKm;
    
    public final long bestPaceSecondsPerKm;

    private RunStats(double totalKm, int count, double maxDistanceKm, long bestPaceSecondsPerKm) {
        this.totalKm = totalKm;
        this.count = count;
        this.maxDistanceKm = maxDistanceKm;
        this.bestPaceSecondsPerKm = bestPaceSecondsPerKm;
    }

    public static RunStats from(List<Run> runs) {
        double totalKm = 0;
        double maxDistanceKm = 0;
        long bestPace = Long.MAX_VALUE;

        for (Run run : runs) {
            totalKm += run.distanceKm;
            if (run.distanceKm > maxDistanceKm) {
                maxDistanceKm = run.distanceKm;
            }
            if (run.distanceKm >= 0.01 && run.durationSeconds > 0) {
                long pace = Math.round(run.durationSeconds / run.distanceKm);
                if (pace < bestPace) {
                    bestPace = pace;
                }
            }
        }

        return new RunStats(
                totalKm,
                runs.size(),
                maxDistanceKm,
                bestPace == Long.MAX_VALUE ? 0 : bestPace);
    }
}
