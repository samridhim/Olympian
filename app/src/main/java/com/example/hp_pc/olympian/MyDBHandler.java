/*
    Helper class for all Database related operations.
 */

package com.example.hp_pc.olympian;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {
    //Database constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Activities.db";
    private static final String TABLE_NAME = "Activities";
    private static final String TABLE_EXERCISES = "Exercises";

    //For storing the context of the calling activity
    private Context cont;

    //Table constants
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "TITLE";
    private static final String COLUMN_DATE = "DATE";
    private static final String COLUMN_TIME = "TIME";
    private static final String COLUMN_DISTANCE = "DISTANCE";
    private static final String COLUMN_DURATION = "DURATION";
    private static final String COLUMN_PACE = "AVERAGE_PACE";
    private static final String COLUMN_CALORIES = "CALORIES";
    private static final String COLUMN_STEPS = "STEPS";


    //Constructor
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        cont = context;
    }

    //Whenever Database is created
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Creating a table
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_DISTANCE + " TEXT, " +
                COLUMN_DURATION + " TEXT, " +
                COLUMN_PACE + " TEXT, " +
                COLUMN_CALORIES + " TEXT " +
                ");";

        String createTable2 = "CREATE TABLE " + TABLE_EXERCISES + "(" +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_STEPS + " TEXT, " +
                COLUMN_TIME + " TEXT " +
                ");";


        sqLiteDatabase.execSQL(createTable);
        sqLiteDatabase.execSQL(createTable2);

        Toast.makeText(cont,"Database and Table Created Successfully",Toast.LENGTH_SHORT).show();
    }

    //If and when Database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i1>1)
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
            onCreate(sqLiteDatabase);
        }
    }

    //Add an activity to database
    public void addActivityToDatabase(MyActivity activity)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE,activity.getTitle());
        values.put(COLUMN_DATE,activity.getDate());
        values.put(COLUMN_TIME,activity.getTime());
        values.put(COLUMN_DISTANCE,activity.getDistance());
        values.put(COLUMN_DURATION,activity.getDuration());
        values.put(COLUMN_PACE,activity.getPace());
        values.put(COLUMN_CALORIES,activity.getCalories());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
        Toast.makeText(cont, "Activity Added", Toast.LENGTH_SHORT).show();
    }

    //Getting all activities in string format
    public ArrayList<String> getAllActivitiesString()
    {
        ArrayList<String> activityList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        cursor.moveToFirst();
        do {
            String activity = "";
            activity += "Title         : " + cursor.getString(0) + "\n";
            activity += "Date        : " + cursor.getString(1) + "\n";
            activity += "Time        : " + cursor.getString(2) + "\n";
            activity += "Distance : " + cursor.getString(3) + "\n";
            activity += "Duration  : " + cursor.getString(4) + "\n";
            activity += "Pace        : " + cursor.getString(5) + "\n";
            activity += "Calories  : " + cursor.getString(6);

            // Adding string activity to list
            activityList.add(activity);
        } while (cursor.moveToNext());
        // return contact list
        return activityList;
    }

    //Add an exercise to database
    public void addExerciseToDatabase(MyExercise exercise)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE,exercise.getTitle());
        values.put(COLUMN_STEPS,exercise.getSteps());
        values.put(COLUMN_TIME,exercise.getTime());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_EXERCISES,null,values);
        db.close();
/*
        Toast.makeText(cont, "Exercise Added", Toast.LENGTH_SHORT).show();
*/
    }

    //Getting all exercises in string format
    public ArrayList<String> getAllExercisesString()
    {
        ArrayList<String> exerciseList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXERCISES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        cursor.moveToFirst();
        do {
            String activity = "Title : " + cursor.getString(0) + "\n";
            activity += "\nSteps : \n" + cursor.getString(1) + "\n";
            activity += "\nTime : " + cursor.getString(2);

            // Adding string activity to list
            exerciseList.add(activity);
        } while (cursor.moveToNext());
        // return contact list
        return exerciseList;
    }

    //Getting all exercise titles in string format list
    public ArrayList<String> getAllExerciseTitlesString()
    {
        ArrayList<String> exerciseList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXERCISES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        cursor.moveToFirst();
        do {
            String activity = cursor.getString(0);

            // Adding string activity to list
            exerciseList.add(activity);
        } while (cursor.moveToNext());
        // return contact list
        return exerciseList;
    }

    //Getting all exercise titles in string format list
    public String getSpecificExerciseString(int rowID)
    {
        String exercise = "";
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_EXERCISES
                + "WHERE 1'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        cursor.moveToFirst();
        do {
            String activity = "Title : " + cursor.getString(1) + "\n";
            activity += "\nSteps : \n" + cursor.getString(2) + "\n";
            activity += "\nTime : " + cursor.getString(3);

            exercise += activity;
        } while (cursor.moveToNext());
        // return contact list
        return exercise;
    }
}
