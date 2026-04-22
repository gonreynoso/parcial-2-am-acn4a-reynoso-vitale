package com.example.runtracker;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class NavbarHelper {

    public enum Tab { HOME, HISTORY, STATS, PROFILE }

    public static void markActiveTab(Activity activity, Tab activeTab) {
        int active   = ContextCompat.getColor(activity, R.color.neon_green);
        int inactive = ContextCompat.getColor(activity, R.color.icon_inactive);

        setTabColor(activity, R.id.iconHome,    R.id.labelHome,    activeTab == Tab.HOME    ? active : inactive);
        setTabColor(activity, R.id.iconHistory, R.id.labelHistory, activeTab == Tab.HISTORY ? active : inactive);
        setTabColor(activity, R.id.iconStats,   R.id.labelStats,   activeTab == Tab.STATS   ? active : inactive);
        setTabColor(activity, R.id.iconProfile, R.id.labelProfile, activeTab == Tab.PROFILE ? active : inactive);
    }

    private static void setTabColor(Activity activity, int iconId, int labelId, int color) {
        ImageView icon = activity.findViewById(iconId);
        TextView label = activity.findViewById(labelId);
        if (icon != null)  icon.setImageTintList(ColorStateList.valueOf(color));
        if (label != null) label.setTextColor(color);
    }
}