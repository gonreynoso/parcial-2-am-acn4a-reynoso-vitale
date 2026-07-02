package com.example.runtracker;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardStats {

    private static final long MILLIS_PER_DAY = 86_400_000L;

    public final double todayKm;
    public final long todayDurationSeconds;
    public final int todayRunCount;
    public final int streakDays;

    private DashboardStats(double todayKm, long todayDurationSeconds, int todayRunCount, int streakDays) {
        this.todayKm = todayKm;
        this.todayDurationSeconds = todayDurationSeconds;
        this.todayRunCount = todayRunCount;
        this.streakDays = streakDays;
    }

    public static DashboardStats from(List<Run> runs) {
        long todayIndex = dayIndex(new Date());
        double todayKm = 0;
        long todayDuration = 0;
        int todayCount = 0;
        Set<Long> daysWithRun = new HashSet<>();

        for (Run run : runs) {
            if (run.date == null) continue;
            long index = dayIndex(run.date);
            daysWithRun.add(index);
            if (index == todayIndex) {
                todayKm += run.distanceKm;
                todayDuration += run.durationSeconds;
                todayCount++;
            }
        }

        return new DashboardStats(todayKm, todayDuration, todayCount, computeStreak(daysWithRun, todayIndex));
    }

    
    private static int computeStreak(Set<Long> daysWithRun, long todayIndex) {
        long cursor = todayIndex;
        if (!daysWithRun.contains(cursor)) {
            cursor--;
        }
        int streak = 0;
        while (daysWithRun.contains(cursor)) {
            streak++;
            cursor--;
        }
        return streak;
    }

    
    private static long dayIndex(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis() / MILLIS_PER_DAY;
    }
}
