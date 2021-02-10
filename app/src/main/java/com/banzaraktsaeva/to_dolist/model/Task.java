package com.banzaraktsaeva.to_dolist.model;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class Task extends AppCompatActivity {
    public String name;
    public String description;
    public Priority priority;
    public Date deadline;
    public boolean isDone;

    public Task(String name, String description, Priority priority, Date deadline, boolean isDone){
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.isDone = isDone;
    }
}

