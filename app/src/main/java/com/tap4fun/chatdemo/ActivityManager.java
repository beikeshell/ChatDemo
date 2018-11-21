package com.tap4fun.chatdemo;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static Activity getLastActivity() {
        if (!activities.isEmpty()) {
            return activities.get(activities.size() - 1);
        }
        return null;
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }
}
