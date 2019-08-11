package com.example.hp_pc.olympian;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.GifTextView;

public class Exercise extends AppCompatActivity {
    //The data we get from the the previous screen - "Exercises List"
    String exercise;//Exercise details
    int exerciseNumber;//Exercise number

    //Text-To-Speech
    private TextToSpeech tts;

    //Exercise time
    private int timeInSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        //Initialize time to this value so as to give a cooldown period
        timeInSeconds = -12;

        //Create an instance of TextToSpeech
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        //Get data from previous activity
        Intent incomingIntent = getIntent();
        exercise = incomingIntent.getStringExtra("Exercise");
        exerciseNumber = incomingIntent.getIntExtra("Exercise Number",0);

        //Display the exercise details
        TextView exerciseDetails = (TextView) findViewById(R.id.exerciseDescription);
        exerciseDetails.setText(exercise);


        //Get and Display the gif corresponding to the exercise
        setGif();

        //Display the timer for this exercise
        startExerciseTimer();

    }

    //Display the timer for this exercise
    private void startExerciseTimer()
    {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                timeInSeconds++;
                if(timeInSeconds>-6 && timeInSeconds<61)
                {
                    TextView timeView = (TextView) findViewById(R.id.exerciseTimerTextView);
                    timeView.setText(Integer.toString(timeInSeconds));
                }
                if(timeInSeconds == -6)
                {
                    tts.speak("Exercise Begins in Five",TextToSpeech.QUEUE_FLUSH,null);
                }

                if(timeInSeconds==-4)
                {
                    tts.speak("Four",TextToSpeech.QUEUE_FLUSH,null);
                }

                if(timeInSeconds==-3)
                {
                    tts.speak("Three",TextToSpeech.QUEUE_FLUSH,null);
                }

                if(timeInSeconds==-2)
                {
                    tts.speak("Two",TextToSpeech.QUEUE_FLUSH,null);
                }

                if(timeInSeconds==-1)
                {
                    tts.speak("One",TextToSpeech.QUEUE_FLUSH,null);
                }

                if(timeInSeconds==0)
                {
                    tts.speak("Exercise Started",TextToSpeech.QUEUE_FLUSH,null);
                }

                if(timeInSeconds==60)
                {
                    tts.speak("Exercise completed",TextToSpeech.QUEUE_FLUSH,null);
                }
                if(timeInSeconds<61)
                {

                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    //Get and Display the gif corresponding to the exercise
    private void setGif() {
        GifImageView image = (GifImageView) findViewById(R.id.imageView);
        switch(exerciseNumber)
        {
            case 0 :
                image.setImageResource(R.drawable.superman);
                break;
            case 1 :
                image.setImageResource(R.drawable.pushups);
                break;
            case 2 :
                image.setImageResource(R.drawable.cont_limb_raises);
                break;
            case 3 :
                image.setImageResource(R.drawable.rev_crunch);
                break;
            case 4 :
                image.setImageResource(R.drawable.plank);
                break;

            case 5 :
                image.setImageResource(R.drawable.squat_jumps);
                break;

            case 6 :
                image.setImageResource(R.drawable.glute_bridge);
                break;

            case 7 :
                image.setImageResource(R.drawable.forward_lunge);
                break;

            case 8 :
                image.setImageResource(R.drawable.crunch);
                break;

            default:
                image.setImageResource(R.drawable.superman);
                break;
        }
    }

}
