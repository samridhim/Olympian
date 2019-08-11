package com.example.hp_pc.olympian;

import java.io.Serializable;

/**
 * Created by hp-pc on 08-Oct-17.
 */

class MyExercise implements Serializable {
    private String title;
    private String steps;
    private String time;

    public MyExercise(String title, String steps, String time) {
        this.title = title;
        this.steps = steps;
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getSteps() {
        return steps;
    }

    public String getTime() {
        return time;
    }
}
