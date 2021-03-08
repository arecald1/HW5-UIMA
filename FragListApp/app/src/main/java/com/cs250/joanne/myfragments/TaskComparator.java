package com.cs250.joanne.myfragments;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {
    public int compare(Task left, Task right) {
        if (left.getCompleted()) {
            return left.getCompleteDate().compareTo(right.getCompleteDate());
        } else {
            return left.getDeadline().compareTo(right.getDeadline());
        }

    }
}
