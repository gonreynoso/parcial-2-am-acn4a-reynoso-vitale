package com.example.runtracker;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

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

    public void start(OnTick listener) {
        this.listener = listener;
        accumulatedMs = 0;
        resume();
    }

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

    public void pause() {
        if (!running) return;
        running = false;
        accumulatedMs += SystemClock.elapsedRealtime() - segmentStartMs;
        if (tick != null) handler.removeCallbacks(tick);
    }

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
