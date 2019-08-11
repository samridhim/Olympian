package com.example.hp_pc.olympian;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExercisesList extends Activity {
    //Database Connection
    MyDBHandler dbHandler;

    //UI Elemnts
    ListView listView;

    //ArrayList for storing Exercise Titles that will be displayed in the listView
    ArrayList<String> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_list);

        //Catching the UI elements
        listView = (ListView) findViewById(R.id.exercisesListView);

        //Connecting to the database
        dbHandler = new MyDBHandler(this,null,null,1);

        //Get and display all the exercises in ListView
        exercises = dbHandler.getAllExercisesString();
        displayAllExercises();

        //Whenever a particular exercise is selected from the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                String exercise = exercises.get(i);

                Intent intent = new Intent(ExercisesList.this,Exercise.class);
                intent.putExtra("Exercise",exercise);
                intent.putExtra("Exercise Number",i);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                //based on item add info to intent
            }
        });

    }

    //Do the ListView stuff to display the exercise titles stored in the ArrayList
    private void displayAllExercises() {

        Toast.makeText(this, "Displaying Exercises", Toast.LENGTH_SHORT).show();

        // Reading all activities
        ArrayList<String> ExercisesList = dbHandler.getAllExerciseTitlesString();
        //Doing the ArrayAdapter stuff
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,ExercisesList);

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        /**/
    }

}
