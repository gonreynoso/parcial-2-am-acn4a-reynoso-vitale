package com.example.runtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RunHistoryAdapter extends RecyclerView.Adapter<RunHistoryAdapter.RunViewHolder> {

    private final List<RunItem> items;

    public RunHistoryAdapter(List<RunItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_run, parent, false);
        return new RunViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RunViewHolder holder, int position) {
        RunItem item = items.get(position);
        holder.txtDate.setText(item.getDate());
        holder.txtPace.setText(item.getPace());
        holder.txtDistance.setText(item.getDistance());
        holder.txtDuration.setText(item.getDuration());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RunViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtPace, txtDistance, txtDuration;

        RunViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate     = itemView.findViewById(R.id.txtDate);
            txtPace     = itemView.findViewById(R.id.txtPace);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtDuration = itemView.findViewById(R.id.txtDuration);
        }
    }
}