package com.example.runtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationTracker {

    private static final long UPDATE_INTERVAL_MS = 5000;
    private static final float MIN_ACCURACY_METERS = 20f;

    public interface OnDistanceUpdated {
        void onUpdate(float totalMeters);
    }

    private FusedLocationProviderClient client;
    private LocationCallback callback;
    private Location ultimaUbicacion;
    private float totalMetros = 0f;

    @SuppressLint("MissingPermission")
    public void start(Context context, OnDistanceUpdated listener) {
        client = LocationServices.getFusedLocationProviderClient(context);

        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_MS)
                .build();

        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                Location actual = result.getLastLocation();
                if (actual == null || actual.getAccuracy() > MIN_ACCURACY_METERS) {
                    return;
                }
                if (ultimaUbicacion != null) {
                    totalMetros += ultimaUbicacion.distanceTo(actual);
                    listener.onUpdate(totalMetros);
                }
                ultimaUbicacion = actual;
            }
        };

        client.requestLocationUpdates(request, callback, null);
    }

    public void pause() {
        if (client != null && callback != null) {
            client.removeLocationUpdates(callback);
        }
    }

    public void stop() {
        pause();
        ultimaUbicacion = null;
        totalMetros = 0f;
    }
}
