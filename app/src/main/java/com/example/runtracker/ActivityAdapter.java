package com.example.runtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private final List<ActivityItem> items;

    public ActivityAdapter(List<ActivityItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ActivityItem item = items.get(position);
        holder.imgIcon.setImageResource(item.getIconRes());
        holder.txtTitle.setText(item.getTitle());
        holder.txtDescription.setText(item.getDescription());
        holder.txtDuration.setText(item.getDuration());
        holder.txtLevel.setText("· " + formatLevel(item.getLevel()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String formatLevel(ActivityItem.Level level) {
        switch (level) {
            case BEGINNER:     return "Principiante";
            case INTERMEDIATE: return "Intermedio";
            case ADVANCED:     return "Avanzado";
            default:           return "";
        }
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtTitle, txtDescription, txtDuration, txtLevel;

        ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon        = itemView.findViewById(R.id.imgIcon);
            txtTitle       = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtDuration    = itemView.findViewById(R.id.txtDuration);
            txtLevel       = itemView.findViewById(R.id.txtLevel);
        }
    }
}