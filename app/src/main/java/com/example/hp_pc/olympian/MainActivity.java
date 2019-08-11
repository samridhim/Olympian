/*
    This activity is displayed during the run.
    It has all the required fields like Distance,Time,Pace,etc.
    It also displays if and when the GPS Signal is acquired and only then does the activity really start.
 */

package com.example.hp_pc.olympian;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    //Service parameters
    private OdometerService odometer;
    private boolean bound = false;
    //Activity Generated Data
    private int userWeight;
    private double weight;
    private String activityDate;
    private String activityStartTime;
    private double distance;
    private double calories;
    private int timeInSeconds;
    private double AveragePace;
    private Location userLocation = null;
    //Check if connection acquired
    private boolean firstLocation = true;
    private TextToSpeech tts;
    //Start a new Service connection
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            OdometerService.OdomterBinder odomterBinder = (OdometerService.OdomterBinder) service;
            odometer = odomterBinder.getOdometer();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            bound = false;
        }
    };

    //OnCreate method to set up the activity at the start
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userWeight = sharedPref.getInt("Weight",0);
        //Initializing the variables
        distance = 0.0;
        AveragePace = 0.0;
        timeInSeconds = 0;
        calories = 0.000;
        weight = userWeight;
        //Call for getting details form Odometer
        getCurrentDetails();
    }

    //Onstart Method for starting(and binding) the connection of the OdometerService
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, OdometerService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

    //OnStop Method for ending(and unbinding) the connection of the OdometerService
    @Override
    protected void onStop() {
        super.onStop();
        if(bound){
            unbindService(connection);
            bound = false;
        }
    }

    //Getting the current details from the OdometerService
    private void getCurrentDetails() {

        //Catching the UI elements
        final TextView distanceview = (TextView) findViewById(R.id.distance);
        final TextView timeview = (TextView) findViewById(R.id.time);
        final TextView gpsview = (TextView) findViewById(R.id.gpsStatus);
        final TextView calorieView = (TextView) findViewById(R.id.calories);
        final TextView currentPaceView = (TextView) findViewById(R.id.currentPace);
        final TextView averagePaceView = (TextView) findViewById(R.id.averagePace);

        //Handler for updating updating the info after every set period of time - here 1 second
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                //Initialize Pace
                double CurrPace = 0.00;
                double AvgPace = 0.00;

                //If odometer is available
                if(odometer != null){
                    //Get distance and location
                    distance = odometer.getKms();
                    userLocation = odometer.getLastLocation();
                }

                //Check if we received a valid location
                if(userLocation!=null)
                {
                    //Check if the location we received wa the first location
                    if(firstLocation==true)
                    {
                        //This is the first time we are receiving a location
                        //If we received a location then we must have acquired GPS Connection!!
                        Toast.makeText(odometer, "GPS Connection Acquired", Toast.LENGTH_SHORT).show();
                        gpsview.setText("GPS : Connected");

                        //Get date
                        DateFormat df_date = new SimpleDateFormat("EEE, dd/MM/yyyy");
                        df_date.setTimeZone(TimeZone.getTimeZone("IST"));
                        activityDate = df_date.format(Calendar.getInstance().getTime());

                        //Get time
                        DateFormat df_time = new SimpleDateFormat("HH:mm:ss");
                        df_time.setTimeZone(TimeZone.getTimeZone("IST"));
                        activityStartTime = df_time.format(Calendar.getInstance().getTime());

                        tts.speak("Activity Started",TextToSpeech.QUEUE_FLUSH,null);
                        //Next time wont be the first time
                        firstLocation = false;
                    }
                }

                //Get  and set Distance
                String distanceStr = String.format("%1$,.3f kms", distance);
                distanceview.setText(distanceStr);

                //Get and set Time Elapsed
                String timeString = convertSecondsToTimeString(timeInSeconds);
                timeview.setText("" + timeString);

                //Update time only if activity has really started
                if(!firstLocation)
                {
                    timeInSeconds++;
                    if(timeInSeconds>1) {
                        //Calculate pace
                        double minutes = timeInSeconds / 60;
                        if(distance>0) {
                            CurrPace = minutes / distance;
                        }
                        else
                        {
                            CurrPace = minutes / 1;
                        }
                        AvgPace = CurrPace;
                        AveragePace = AvgPace;
                    }
                }

                calories = distance * 0.7500 * weight / 1.6;
                String caloriesStr = String.format("%1$,.1f", calories);
                calorieView.setText("Calories : " + caloriesStr);
                //Display pace
                String currentPaceStr = String.format("Current Pace : %1$,.2f minutes/km", CurrPace);
                currentPaceView.setText(currentPaceStr);
                String averagePaceStr = String.format("Average Pace : %1$,.2f minutes/km", AvgPace);
                averagePaceView.setText(averagePaceStr);

                //Update after every 1 second
                handler.postDelayed(this, 1000);
            }
        });
    }


    //Convert 'timeInSeconds to valid time in String format
    private String convertSecondsToTimeString(int time)
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
        String timeString = hoursString+":"+minutesString+":"+secondsString;
        return timeString;
    }

    //User wants to stop activity
    public void stopActivity(View view)
    {
        //Stop services
        odometer.endOdometerService();
        onStop();

        tts.speak("Activity Stopped",TextToSpeech.QUEUE_FLUSH,null);
        Toast.makeText(this, "Activity Stopped", Toast.LENGTH_SHORT).show();

        //Intent to go to next page
        Intent intent = new Intent(this, History.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        //Send data via intent
        intent.putExtra("ActivityDate",activityDate);
        intent.putExtra("ActivityTime",activityStartTime);
        intent.putExtra("Time",timeInSeconds);
        String averagePaceStr = String.format("%1$,.2f", AveragePace);
        intent.putExtra("Average Pace",averagePaceStr);
        String distanceStr = String.format("%1$,.2f", distance);
        intent.putExtra("Distance",distanceStr);
        String caloriesStr = String.format("%1$,.1f", calories);
        intent.putExtra("Calories",caloriesStr);

        //Go to next page
        startActivity(intent);

        //We don't need this page anymore, so end it.
        finish();
    }

}