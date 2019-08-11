/*
    This user sees this page whenever he completes a 'run activity'.
    It displays the details of the run and provides an option of saving it in the database.
 */

package com.example.hp_pc.olympian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

public class History extends AppCompatActivity{
    //Strings for storing activity details
    int activityDuration;
    String activityDurationString;
    String activityDistance;
    String activityPace;
    String activityDate;
    String activityStartTime;
    String activityCalories;

    EditText activityTitleEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        activityTitleEditText = (EditText) findViewById(R.id.historyTitleEditText);
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int activitiesCompleted = sharedPref.getInt("Activities Completed",-1);
        if(activitiesCompleted>=0)
        {
            activitiesCompleted++;
            editor.putInt("Activities Completed",activitiesCompleted);
            editor.apply();
            activityTitleEditText.setText("Activity " + Integer.toString(activitiesCompleted));
        }
        else
        {
            activityTitleEditText.setText("Activity #");
        }

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        activityDuration = intent.getIntExtra("Time",0);
        activityDistance = intent.getStringExtra("Distance");
        activityPace = intent.getStringExtra("Average Pace");
        activityDate = intent.getStringExtra("ActivityDate");
        activityStartTime = intent.getStringExtra("ActivityTime");
        activityCalories = intent.getStringExtra("Calories");

        activityDurationString = convertSecondsToTimeString(activityDuration);
        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.historyTime);
        textView.setText("Time : " + activityDuration + " seconds");
        TextView textView2 = (TextView) findViewById(R.id.historyDistance);
        textView2.setText("Distance: " + activityDistance + " kilometers");
        TextView textView3 = (TextView) findViewById(R.id.historyPace);
        textView3.setText("Average Pace : " + activityPace + " km/hr.");
        TextView textView4 = (TextView) findViewById(R.id.historyDate);
        textView4.setText("Date : " + activityDate + ". Time : " + activityStartTime);
    }

    //Convert 'timeInSeconds to valid time in String format
    private String convertSecondsToTimeString(int time)
    {
        String timeString = "";
        if(time<60)
        {
            int seconds = time;
            String secondsString = String.format("%02d", seconds);
            timeString =secondsString + " seconds";
        }
        else
        {
            if(time<3600)
            {
                int seconds = time;
                int minutes;
                minutes = seconds/60;
                seconds = seconds%60;
                String secondsString = String.format("%02d", seconds);
                String minutesString = String.format("%02d", minutes);
                timeString =minutesString+":"+secondsString;
            }
            else
            {
                int seconds = time;
                int hours,minutes;
                hours = seconds/3600;
                seconds = seconds%3600;
                minutes = seconds/60;
                seconds = seconds%60;
                String secondsString = String.format("%02d", seconds);
                String minutesString = String.format("%02d", minutes);
                String hoursString = String.format("%02d", hours);
                timeString = hoursString+":"+minutesString+":"+secondsString;
            }
        }

        return timeString;
    }

    //User wants to save the last activity to the database
    public void saveToDatabase(View view)
    {
        EditText activityTitle = (EditText) findViewById(R.id.historyTitleEditText);
        String Title = activityTitle.getText().toString();

        if(Title.equals(""))
        {
            Toast.makeText(this, "Please enter the title", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            String DatabaseExistence = sharedPref.getString("Database Formed","false");
            if(DatabaseExistence.equals("false"))
            {
                editor.putString("Database Formed","true");
                Toast.makeText(this, "Congratulations on your first activity", Toast.LENGTH_SHORT).show();

            }
            editor.commit();

            MyActivity lastActivity = new MyActivity(Title,activityDate,activityStartTime,activityDistance,activityDurationString,activityPace,activityCalories);
            MyDBHandler dbHandler = new MyDBHandler(this,null,null,0);
            dbHandler.addActivityToDatabase(lastActivity);
            Intent intent = new Intent(this, Dashboard.class);
            intent.putExtra("FROM ACTIVITY","B");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
    }

    //User doesn't want to store the details of the last activiy and has pressed the cancel button
    public void cancelSavingAndReturn(View view)
    {
        Intent intent = new Intent(this, Dashboard.class);
        intent.putExtra("FROM ACTIVITY","C");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
