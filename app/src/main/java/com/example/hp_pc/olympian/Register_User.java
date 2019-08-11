/*
    New user registration page.
    This page makes use of SharedPreferences to store data locally.
 */

package com.example.hp_pc.olympian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Register_User extends AppCompatActivity {
    //UI elements
    EditText nameInput;
    EditText ageInput;
    EditText weightInput;
    RadioButton maleRBInput;
    RadioButton femaleRBInput;
    char gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //Connecting UI elemnts to variables
        nameInput = (EditText) findViewById(R.id.name);
        ageInput = (EditText) findViewById(R.id.age);
        weightInput = (EditText) findViewById(R.id.weight);
        maleRBInput = (RadioButton) findViewById(R.id.radioButtonMale);
        femaleRBInput = (RadioButton) findViewById(R.id.radioButtonFemale);
    }

    //Radio button male clicked
    public void setGenderMale(View view) {
        //Disable female radio button
        femaleRBInput.setChecked(false);

        //Set gender to male
        gender = 'M';
    }

    //Radio button female clicked
    public void setGenderFemale(View view) {
        //Disable male radio button
        maleRBInput.setChecked(false);

        //Set gender to female
        gender = 'F';
    }


    //User clicked the proceed button
    public void saveInfo(View view)
    {
        //Get user data from UI elements
        String name = nameInput.getText().toString();
        String age_string = ageInput.getText().toString();
        int age = Integer.parseInt(age_string);
        String weight_string = weightInput.getText().toString();
        int weight = Integer.parseInt(weight_string);

        //Check for errors
        Boolean error = false;
        if(name==null || name==null || name.matches(""))
        {
            error = true;
        }
        if(age<0 || age>90 || age_string==null || age_string.matches(""))
        {
            error = true;
        }
        if(weight<20 || weight>150 || weight_string==null || weight_string.matches(""))
        {
            error = true;
        }
        if(gender!='M' && gender!='F')
        {
            error = true;
        }

        //If and Else error is found
        if(error)
        {
            Toast.makeText(this, "Incorrect input. Please try again", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Save data in shared preferences
            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Name",name);
            editor.putInt("Weight",weight);
            editor.putInt("Activities Completed",0);
            editor.apply();

            Toast.makeText(this, "User Info Saved", Toast.LENGTH_SHORT).show();

            //Adding sample exercises
            insertSampleExercises();

            //Go to next page
            Intent intent = new Intent(this, Dashboard.class);
            intent.putExtra("FROM ACTIVITY","A");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

            //End this page. We're done with it.
            finish();
        }
    }

    private void insertSampleExercises() {
        MyDBHandler dbHandler = new MyDBHandler(this,null,null,0);
        MyExercise exercise;
        String title,steps,time;

        title = "Superman";
        steps = "To begin, lie straight and face down on the floor or exercise mat. Your arms should be fully extended in front of you. This is the starting position.\n" +
                "Simultaneously raise your arms, legs, and chest off of the floor and hold this contraction for 2 seconds.\n" +
                "Slowly begin to lower your arms, legs and chest back down to the starting position while inhaling.";
        time = "1 minute";
        exercise= new MyExercise(title,steps,time);
        dbHandler.addExerciseToDatabase(exercise);

        title = "Pushups";
        steps = "Start in a high plank position. Place hands firmly on the ground, directly under shoulders. \n" +
                "Begin to lower your body—keeping back flat and eyes focused about three feet in front of you to maintain a neutral neck—until chest grazes floor.\n" +
                "Keeping core engaged, exhale as you push back to starting position.";
        time = "1 minute";
        exercise= new MyExercise(title,steps,time);
        dbHandler.addExerciseToDatabase(exercise);

        title = "Contralateral Limb Raises";
        steps = "Lie on your stomach on a mat or the floor with your legs outstretched behind you. Your toes are pointing toward the wall behind you. \n" +
                "Exhale. Deepen your abdominal/core muscles to stabilize your spine and slowly float one arm a few inches off the floor.\n" +
                "Gently inhale and lower your arm back towards your starting position without any movement in your low back or hips.";
        time = "1 minute";
        exercise= new MyExercise(title,steps,time);
        dbHandler.addExerciseToDatabase(exercise);

        title = "Reverse Crunch";
        steps = "Lie on your back on a mat with your knees bent, feet flat on the floor and arms spread out to your sides with your palms facing down. Gently exhale. \n" +
                "Exhale, and slowly raise your hips off the mat, rolling your spine up as if trying to bring your knees towards your head.\n" +
                "Gently inhale. With control, lower your spine and hips back to the start position.";
        time = "1 minute";
        exercise= new MyExercise(title,steps,time);
        dbHandler.addExerciseToDatabase(exercise);

        title = "Front Plank";
        steps = "Lie on your stomach on an exercise mat or floor with your elbows close to your sides and directly under your shoulders, palms down and fingers facing forward.\n" +
                "Slowly lift your torso and thighs off the floor or mat. Keep your torso and legs rigid. Do not allow any sagging in your ribcage or low back.\n" +
                "Keep the torso and legs stiff as you slowly and gently lower your body back towards the mat or floor.";
        time = "1 minute";
        exercise= new MyExercise(title,steps,time);
        dbHandler.addExerciseToDatabase(exercise);

        title = "Squat Jumps";
        steps = "Stand with your feet hip-width apart, arms by your sides. Pull your shoulder blades down and engage your abdominal / core muscles to brace your spine.\n" +
                "Shift your hips back and down. This will create a hinge-like movement at your knees. Continue to lower yourself until you feel your heels about to lift off the floor. \n" +
                "With ONLY a very brief pause at the bottom of your downward phase, explode up through your lower body, fully extending your hips, knees and ankles.\n" +
                "The most important components of the landing phase are correct foot position and avoiding excessive forward movement in your lower extremity\n" +
                "Land with your trunk slightly forward, head aligned with your spine and back rigid or flat";
        time = "1 minute";
        exercise= new MyExercise(title,steps,time);
        dbHandler.addExerciseToDatabase(exercise);

        title = "Glute Bridge";
        steps = "Lie on your back on an exercise mat or the floor in a bent-knee position with your feet flat on the floor. \n" +
                "Gently exhale. Keep the abdominals engaged and lift your hips up off the floor. Press your heels into the floor for added stability.\n" +
                "Inhale and slowly lower yourself back to your starting position.\n" +
                "Gradually progress this exercise by starting with both feet together and extending one leg while in the raised position.";
        time = "1 minute";
        exercise= new MyExercise(title,steps,time);
        dbHandler.addExerciseToDatabase(exercise);

        title = "Forward Lunge";
        steps = "Stand with your feet together. Pull your shoulder blades toward your hips.\n" +
                "In preparation to step forward, slowly lift one foot off the floor and find your balance on the standing leg. Try not to move the standing foot and maintain balance without wobbling. Pause.\n" +
                "As you step forward into the lunge, focus on a downward movement of your hips toward the floor. Avoid driving your hips forward.\n" +
                "Firmly push off with the front leg, activating both your thighs and butt muscles to return to your upright, starting position.";
        time = "1 minute";
        exercise= new MyExercise(title,steps,time);
        dbHandler.addExerciseToDatabase(exercise);

        title = "Crunch";
        steps = "Lie on your back on a mat with your knees bent, feet flat on the floor and heels a comfortable distance \n" +
                "Place your hands behind your head. Pull your shoulder blades together and your elbows back without arching your low back or causing your ribs to splay out\n" +
                "Exhale. Engage your abdominal and core muscles. Nod your chin slightly as you slowly curl your head and shoulders off the mat. \n" +
                "Gently inhale and lower your torso back toward the mat slowly and with control. Keep your feet, tailbone and low back in contact with the mat.";
        time = "1 minute";
        exercise= new MyExercise(title,steps,time);
        dbHandler.addExerciseToDatabase(exercise);
    }


}