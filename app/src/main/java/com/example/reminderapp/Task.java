package com.example.reminderapp;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by etshi on 28-Oct-17.
 */

public class Task implements Serializable {
    private String taskDescription;
    private Date dateTime;

    public static final String taskDescriptionJsonString = "taskDescription";
    public static final String dateTimeJsonString = "dateTime";

    public Task(String description, Date date) {
        taskDescription = description;
        dateTime = date;
    }

    public String getDescription() {
        return taskDescription;
    }
    public Date getDate() {
        return dateTime;
    }

    public static String tasksListToJson(List<Task> list) {
        try {
            JSONArray jsonarray = new JSONArray();
            int counter = 0;
            for (Task task : list) {

                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put(taskDescriptionJsonString, task.getDescription());
                    jsonObj.put(dateTimeJsonString, task.getDate().getTime());
                    jsonarray.put(counter, jsonObj);

                counter++;
            }
            return jsonarray.toString();
        }
        catch (JSONException e) {
            // TODO : what now?
            return "";
        }
    }

    public static List<Task> jsonToTasksList(String jsonStr) {
        ArrayList<Task> out_list = new ArrayList<Task>();
        try {
            JSONArray jsonarray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObj = jsonarray.getJSONObject(i);
                out_list.add(new Task(jsonObj.getString(taskDescriptionJsonString), new Date(jsonObj.getLong(dateTimeJsonString))));
            }
        }
        catch (JSONException e) {
            // TODO : what now?
        }
        return out_list;
    }
}
