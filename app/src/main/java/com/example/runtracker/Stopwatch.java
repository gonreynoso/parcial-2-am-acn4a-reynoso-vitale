package com.example.runtracker;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

/**
 * Wall-clock stopwatch with real pause/resume support, ticking once per second.
 * Elapsed time is accumulated across pauses so resuming continues where it left off.
 */
public class Stopwatch {

    public interface OnTick {
        void onTick(long elapsedSeconds);
    }

    private final Handler handler = new Handler(Looper.getMainLooper());
    private OnTick listener;
    private Runnable tick;

    private long accumulatedMs = 0;
    private long segmentStartMs = 0;
    private boolean running = false;

    /** Starts ticking from zero. */
    public void start(OnTick listener) {
        this.listener = listener;
        accumulatedMs = 0;
        resume();
    }

    /** Resumes ticking after a pause. No-op if already running. */
    public void resume() {
        if (running) return;
        running = true;
        segmentStartMs = SystemClock.elapsedRealtime();
        tick = new Runnable() {
            @Override
            public void run() {
                if (listener != null) listener.onTick(getElapsedSeconds());
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(tick);
    }

    /** Freezes elapsed time. No-op if already paused. */
    public void pause() {
        if (!running) return;
        running = false;
        accumulatedMs += SystemClock.elapsedRealtime() - segmentStartMs;
        if (tick != null) handler.removeCallbacks(tick);
    }

    /** Stops and resets to zero. */
    public void stop() {
        pause();
        accumulatedMs = 0;
    }

    public long getElapsedSeconds() {
        long ms = accumulatedMs + (running ? SystemClock.elapsedRealtime() - segmentStartMs : 0);
        return ms / 1000;
    }

    public boolean isRunning() {
        return running;
    }
}
