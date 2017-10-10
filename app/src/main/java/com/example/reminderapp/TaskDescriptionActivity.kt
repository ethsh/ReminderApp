package com.example.reminderapp

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_task_description.*
import java.io.Serializable
import java.util.*



class TaskDescriptionActivity : AppCompatActivity() {

    // 1
    companion object {
        val EXTRA_TASK_DESCRIPTION = "task"
    }

    // 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_description)

        timePicker.setIs24HourView(true)
    }

    // 3
    fun doneClicked(view: View) {
        val taskDescription = descriptionText.text.toString()

        if (!taskDescription.isEmpty()) {
            val task = Task(taskDescription,
                    Date(datePicker.year, datePicker.month, datePicker.dayOfMonth, timePicker.hour, timePicker.minute))
            // 2
            val result = Intent()
            //result.putExtra(EXTRA_TASK_DESCRIPTION, taskDescription)
            result.putExtra(EXTRA_TASK_DESCRIPTION, task)
            setResult(Activity.RESULT_OK, result)
        } else {
            // 3
            setResult(Activity.RESULT_CANCELED)
        }

// 4
        finish()

    }
}

public class Task (description: String, date: Date) : Serializable {
    val taskDescription = description
    val dateTime = date
}