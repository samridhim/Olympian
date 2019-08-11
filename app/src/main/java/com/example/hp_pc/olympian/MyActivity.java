/*
    This is a specific class that corresponds to the activity of the user.
    Here activity means running as an exercise.
 */

package com.example.hp_pc.olympian;

//For passing the object of this class through Android activities.
import java.io.Serializable;

public class MyActivity implements Serializable{
    private String Title;
    private String Date;
    private String Time;
    private String Distance;
    private String Duration;
    private String Pace;
    private String Calories;

    //Empty Constructor
    public MyActivity()
    {

    }

    //Constructor
    public MyActivity(String title, String date, String time, String distance, String duration, String pace, String calories) {
        Title = title;
        Date = date;
        Time = time;
        Distance = distance;
        Duration = duration;
        Pace = pace;
        Calories = calories;
    }

    //Dummy Constructor for testing purposes
    public MyActivity(String title) {
        Title = title;
        Date = "0";
        Time = "0";
        Distance = "0";
        Duration = "0";
        Pace = "0";
        Calories = "0";
    }

    //SETTERS

    public void setTitle(String title) {
        Title = title;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public void setPace(String pace) {
        Pace = pace;
    }

    public void setCalories(String calories) {
        Calories = calories;
    }



    //GETTERS

    public String getTitle() {
        return Title;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getDistance() {
        return Distance;
    }

    public String getDuration() {
        return Duration;
    }

    public String getPace() {
        return Pace;
    }

    public String getCalories() {
        return Calories;
    }
}
