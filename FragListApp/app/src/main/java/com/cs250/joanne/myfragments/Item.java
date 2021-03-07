package com.cs250.joanne.myfragments;

import java.lang.String;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Holds data for one task - straight forward simple little item class
 */
public class Item {
    private String what;
    private String category = "category";
    private boolean completed;
    private Date deadline;
    private Date dateCompleted;


    Item(String what, String category, Date deadline) {
        this.what = what;
        this.category = category;
        this.deadline = deadline;
    }

    public String getWhat() { return what; }
    public String getCategory() { return category; }
    public Boolean getCompleted() { return completed; }
    public Date getDeadline() { return deadline; }
    public Date getCompleteDate() { return dateCompleted; }


}
