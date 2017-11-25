package com.example.reminderapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskDescriptionActivity extends AppCompatActivity {
    private TimePicker timePicker = null;
    private DatePicker datePicker = null;
    private EditText descriptionText = null;

    public static String EXTRA_TASK_DESCRIPTION = "task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        descriptionText = (EditText) findViewById(R.id.descriptionText);

        timePicker.setIs24HourView(true);
    }

    // 3
    public void doneClicked(View view) {
        String taskDescription = descriptionText.getText().toString();

        if (!taskDescription.isEmpty()) {
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            Calendar dateTime = Calendar.getInstance();
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, month);
            dateTime.set(Calendar.DAY_OF_MONTH, day);
            dateTime.set(Calendar.HOUR_OF_DAY, hour);
            dateTime.set(Calendar.MINUTE, minute);

            Task task = new Task(taskDescription, dateTime);

            Intent result = new Intent();
            result.putExtra(EXTRA_TASK_DESCRIPTION, task);
            setResult(Activity.RESULT_OK, result);
        } else {
            // 3
            setResult(Activity.RESULT_CANCELED);
        }
        // 4
        finish();
    }
}

