package com.example.reminderapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import static com.example.reminderapp.R.id.parent;
//import static com.example.reminderapp.R.id.taskListView;

public class MainActivity extends AppCompatActivity {

    private List<Task> taskList = new ArrayList<Task>();
    private clockUpdaterReceiver updaterReceiver = new clockUpdaterReceiver();

    private int ADD_TASK_REQUEST = 1;

    private String PREFS_TASKS = "prefs_tasks";
    private String KEY_TASKS_LIST = "tasks_list";

    private ListView listView = null;
    private TextView timeView = null;
    private TaskUsersAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.taskListView);
        timeView = (TextView) findViewById(R.id.dateTimeTextView);

        adapter = new TaskUsersAdapter(this, taskList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                taskSelected(position);
                //Toast.makeText(SuggestionActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(PREFS_TASKS, Context.MODE_PRIVATE);
        String savedList = preferences.getString(KEY_TASKS_LIST, null);
        //editor.commit();

        //String savedList = getSharedPreferences(PREFS_TASKS, Context.MODE_PRIVATE).getString(KEY_TASKS_LIST, null);
        if (savedList != null) {
            List<Task> saved_list = Task.jsonToTasksList(savedList);
            taskList.addAll(saved_list);
        }

        timeView.setText(getCurrentTimeStamp());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(updaterReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    protected void onPause() {
        // 4
        super.onPause();
        // 5
        unregisterReceiver(updaterReceiver);
        /*
        try {
            unregisterReceiver(tickReceiver)
        } catch (e: IllegalArgumentException) {
            Log.e(MainActivity.LOG_TAG, "Time tick Receiver not registered", e);
        }
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TASK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Object obj = data.getSerializableExtra(TaskDescriptionActivity.EXTRA_TASK_DESCRIPTION);
                Task task = (obj instanceof Task ? (Task)obj : null);
                if (null != task) {
                    taskList.add(task);
                    adapter.notifyDataSetChanged();
                }
                //setTaskNotification(task);
            }
        }
    }

    private void setTaskNotification(Task task) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("Reminder!!");
        builder.setContentText(task.getDescription());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setWhen(System.currentTimeMillis() + 1000000);
        //builder.setDefaults(Notification.DEFAULT_ALL);
        //builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        //builder.setWhen(System.currentTimeMillis() + 5000);
        //RemoteViews headsUp = builder.createHeadsUpContentView();

        Notification notification = builder.build();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.example.reminderapp","com.package.address.MainActivity"));
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        builder.setContentIntent(pendingIntent);

        /*
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long taskDate_milli = task.getDate().getTimeInMillis();
        long now_system_time = System.currentTimeMillis();
        long futureInMillis = taskDate_milli - now_system_time;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, now_system_time + 10000, pendingIntent);
        */
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }

    @Override
    protected void onStop() {
        super.onStop();
        saveTasksToFile();
    }

    private void saveTasksToFile() {
        // Save all data which you want to persist.
        String savedList = Task.tasksListToJson(taskList);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(PREFS_TASKS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_TASKS_LIST, savedList);
        editor.commit();


        //getSharedPreferences(PREFS_TASKS, Context.MODE_PRIVATE).edit().putString(KEY_TASKS_LIST, savedList).apply();//.commit();//
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void addTaskClicked(View view) {
        Intent intent = new Intent(this, TaskDescriptionActivity.class);
        startActivityForResult(intent, ADD_TASK_REQUEST);
    }

    private class clockUpdaterReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // assumes WordService is a registered service
            //Intent intent = new Intent(context, WordService.class);
            //context.startService(intent);
            if (intent != null && intent.getAction() == Intent.ACTION_TIME_TICK) {
                timeView.setText(getCurrentTimeStamp());
            }
        }
    }

    private String getCurrentTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        Date now = new Date();
        return simpleDateFormat.format(now);
    }

    public void taskSelected(final int position) {
        // 1
        //AlertDialog dialog = new AlertDialog(context);
        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.alert_title)
                .setMessage(taskList.get(position).getDescription())
                .setPositiveButton(R.string.delete,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        taskList.remove(position);
                        //taskListDesciptors.removeAt(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.create().show();
    }
}
