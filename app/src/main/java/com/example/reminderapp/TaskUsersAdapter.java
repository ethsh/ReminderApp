package com.example.reminderapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by etshi on 28-Oct-17.
 */

public class TaskUsersAdapter extends ArrayAdapter<Task> {
    public TaskUsersAdapter(Context context, List<Task> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_layout, parent, false);
        }

        // Get the data item for this position
        Task task = getItem(position);
        // Lookup view for data population
        TextView task_name = (TextView) convertView.findViewById(R.id.textView_taskDescription);
        TextView task_date = (TextView) convertView.findViewById(R.id.textView_date);
        // Populate the data into the template view using the data object
        task_name.setText(task.getDescription());
        task_date.setText(task.getDate().toString());
        // Return the completed view to render on screen
        return convertView;
    }
}
