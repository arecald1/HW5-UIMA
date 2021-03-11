package com.cs250.joanne.myfragments;

import java.lang.String;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Holds data for one task - straight forward simple little item class
 */
//reuploading
public class Task {
    private String what;
    private String category;
    private boolean completed;
    private Date deadline;
    private Date dateCompleted;

    Task(Task orig) {
        this.what = "(Copy) " + orig.what;
        this.category = orig.category;
        this.deadline = orig.deadline;
        this.dateCompleted = orig.dateCompleted;
        this.completed = orig.completed;
    }

    Task(String what, String category, Date deadline) {
        this.what = what;
        this.category = category;
        this.deadline = deadline;
    }

    public String getWhat() { return what; }
    public String getCategory() { return category; }
    public Boolean getCompleted() { return completed; }
    public Date getDeadline() { return deadline; }
    public Date getCompleteDate() { return dateCompleted; }

    public void setComplete() {
        this.completed = true;
        dateCompleted = new Date();
    }

    public String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy");
        return dateFormat.format(date);
    }
}
