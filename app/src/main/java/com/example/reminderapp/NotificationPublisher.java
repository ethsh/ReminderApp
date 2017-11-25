package com.example.reminderapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.reminderapp.TaskDescriptionActivity.EXTRA_TASK_DESCRIPTION;

/**
 * Created by etshi on 28-Oct-17.
 */

public class NotificationPublisher extends BroadcastReceiver {
    // public static String NOTIFICATION_ID = "notification-id";
    // public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        String task_str = intent.getStringExtra(EXTRA_TASK_DESCRIPTION);
        List temp_list = Task.jsonToTasksList(task_str);
        if (0 == temp_list.size()) {
            return;
        }
        Task task = (Task) temp_list.get(0);

        if (null == task) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("Reminder!!");
        builder.setContentText(task.getDescription());
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
