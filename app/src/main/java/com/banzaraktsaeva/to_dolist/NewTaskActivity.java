package com.banzaraktsaeva.to_dolist;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.banzaraktsaeva.to_dolist.model.Priority;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class NewTaskActivity extends AppCompatActivity {
    EditText name, description;
    RadioGroup priority;
    DatePicker deadline;
    Button saveButton;
    private static Priority inputPriority;
    boolean isDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        name = (EditText) findViewById(R.id.newTask_name);
        description = (EditText) findViewById(R.id.newTask_description);
        priority = (RadioGroup) findViewById(R.id.priority);
        deadline = (DatePicker) findViewById(R.id.date_picker);
        saveButton = (Button)findViewById(R.id.button_save);
        priority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { //стандартный метод для работы с RadioGroup
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.priority_high:
                        inputPriority = Priority.High;
                        break;
                    case R.id.priority_medium:
                        inputPriority = Priority.Medium;
                        break;
                    default:
                        inputPriority = Priority.Low;
                        break;
                }
            }
        });
    }

    public void save_click(View view){
        saveButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String inputName = name.getText().toString();
                String inputDescription = Objects.requireNonNull(description.getText()).toString();

                int year = deadline.getYear();
                int month = deadline.getMonth();
                int day = deadline.getDayOfMonth();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Date selectedDate = calendar.getTime();

                /*Log.i("NewTaskActivity", "Work with database, info");
                Log.e("NewTaskActivity", "Work with database, error");
                Log.w("NewTaskActivity", "Work with database, warning");*/

                TaskDB taskDB = new TaskDB(getApplicationContext());

                if (name.getText().toString().equals("")){
                    Toast.makeText(NewTaskActivity.this, "Введите название задачи", Toast.LENGTH_LONG).show();
                } else {
                    setTask(inputName, inputDescription, inputPriority, selectedDate);
                    toCurrentTasks();
                }
            }
        });
    }

    public void setTask(String name, String description, Priority priority, Date deadline){//метод для записи данных в базу
        ContentValues values = new ContentValues();
        values.put(TaskDB.KEY_NAME, name);
        values.put(TaskDB.KEY_DESCRIPTION, description);
        if(priority == null) {
            values.put(TaskDB.KEY_PRIORITY, String.valueOf(Priority.Low));
        } else {
            values.put(TaskDB.KEY_PRIORITY, String.valueOf(priority));
        }

        values.put(TaskDB.KEY_DEADLINE, deadline.toString());

        values.put(TaskDB.KEY_ISDONE, isDone);

        long newRowId = TaskDB.db.insert(TaskDB.TABLE_TASKS, null, values);
        TaskDB.db.close();
    }

    public void toCurrentTasks() { //интент с переходом на активность новой задачи
        Intent intent = new Intent(NewTaskActivity.this, CurrentTasksActivity.class);
        startActivity(intent);
    }
}