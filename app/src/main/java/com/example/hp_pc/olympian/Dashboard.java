/*
    This activity is the first page that a registered user sees.
    It contains a dashboard of all the previous activities of the user.
    It also contains buttons to start new activities/exercises
 */

package com.example.hp_pc.olympian;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class Dashboard extends Activity {
    //Permission
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    //UI Elements
    TextView databaseView;
    MyDBHandler dbHandler;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Connecting code to UI elements
        databaseView = (TextView) findViewById(R.id.databaseTextView);
        listView = (ListView) findViewById(R.id.DatabaseListView);

        dbHandler = new MyDBHandler(this,null,null,1);
        //Show the database if at-least one activity exists
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String DatabaseExistence = sharedPref.getString("Database Formed", "false");
/*
        Toast.makeText(this, DatabaseExistence, Toast.LENGTH_SHORT).show();
*/
        //Print the database
        if (DatabaseExistence.equals("true")) {
            Toast.makeText(this, "Displaying Activities", Toast.LENGTH_SHORT).show();
            displayAllActivities();
        }

        //Checking for permission(LOCATION SERVICES)
        checkForPermissions();
    }

    //Display the entries of the database in ListView
    public void displayAllActivities()
    {
        // Reading all activities
        ArrayList<String> ActivitiesList = dbHandler.getAllActivitiesString();

        //Reversing the order to get the newest first
        Collections.reverse(ActivitiesList);

        //Doing the ArrayAdapter stuff
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,ActivitiesList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
    }

    //Check for permissions
    private void checkForPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    //Get the results of the permission check
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "You may need to restart the Application for changes to take place.", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //User has pressed the 'START ACTIVITY' BUTTON
    public void startActivity(View view)
    {
        //Start the running activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

        //If the user wants to start the activity, be done with the dashboard.
        finish();
    }

    //User has pressed the 'START EXERCISE' BUTTON
    public void startExercise(View view)
    {
        //Start the running activity
        Intent intent = new Intent(this, ExercisesList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

        //As the user can return here from 'Exercises List' there is no need of'finish()'.
    }

}


