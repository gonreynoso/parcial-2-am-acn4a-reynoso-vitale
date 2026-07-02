package com.example.runtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public final class RunCardFactory {

    private static final Locale ES_AR = new Locale("es", "AR");

    private RunCardFactory() {
    }

    public static View create(LayoutInflater inflater, ViewGroup parent, Run run) {
        View card = inflater.inflate(R.layout.item_run_card, parent, false);

        ImageView ivIcon = card.findViewById(R.id.ivRunIcon);
        TextView tvDate = card.findViewById(R.id.tvRunDate);
        TextView tvDetail = card.findViewById(R.id.tvRunDetail);
        TextView tvDistance = card.findViewById(R.id.tvRunDistance);

        ivIcon.setImageResource(run.type.iconRes);
        ivIcon.setContentDescription(card.getContext().getString(run.type.nameRes));

        if (run.date != null) {
            tvDate.setText(new SimpleDateFormat("dd/MM/yyyy", ES_AR).format(run.date));
        }
        tvDistance.setText(String.format(Locale.getDefault(), "%.2f km", run.distanceKm));

        StringBuilder detail = new StringBuilder();
        if (run.steps > 0) {
            detail.append(run.steps).append(" pasos · ");
        }
        detail.append(formatPace(card, run.distanceKm, run.durationSeconds));
        detail.append(" · ").append(formatDuration(run.durationSeconds));
        tvDetail.setText(detail.toString());

        return card;
    }

    private static String formatPace(View context, double km, long durationSeconds) {
        if (km < 0.01) {
            return context.getContext().getString(R.string.ritmo_formato, "--");
        }
        long secondsPerKm = Math.round(durationSeconds / km);
        String pace = String.format(Locale.getDefault(), "%d:%02d", secondsPerKm / 60, secondsPerKm % 60);
        return context.getContext().getString(R.string.ritmo_formato, pace);
    }

    private static String formatDuration(long durationSeconds) {
        long hours = durationSeconds / 3600;
        long minutes = (durationSeconds % 3600) / 60;
        long seconds = durationSeconds % 60;
        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
}
