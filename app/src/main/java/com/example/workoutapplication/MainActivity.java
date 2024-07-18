package com.example.workoutapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private Button nextPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxDataStore<Preferences> dataStoreRx;

        // SEE DataStoreSingleton.java for credits and more information
        DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance(this);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this, dataStoreSingleton.getDataStore());

//       String foo = dataStoreHelper.getStringValue("ExerciseList");
//       dataStoreHelper.setStringValue("ExerciseList", "hello");
//       String bar = dataStoreHelper.getStringValue("ExerciseList");

//       Log.d("MainTest", foo);
//       Log.d("MainTest2", bar);

        // EXERCISE JSON ARRAY
        JSONArray exerciseList = DEBUG_ExerciseList();
        String exerciseListString = exerciseList.toString();

        // TODO: I think we need to wait for the update to complete, something along the lines of runBlocking
        // https://stackoverflow.com/questions/72231438/how-do-i-wait-until-data-from-preferences-datastore-is-loaded

        nextPageButton = (Button) findViewById(R.id.nextPageButton);

        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ExerciseManagementActivity.class));
            }
        });
    }

    public JSONArray DEBUG_ExerciseList() {
        // TEMP DEBUG EXERCISES
        Exercise tempExercise1 = new Exercise("Deadlift", "Lifting bar real cool");
        tempExercise1.addTag("legs");
        tempExercise1.addTag("Biceps");
        tempExercise1.setThumbnailID("001.jpg");

        Exercise tempExercise2 = new Exercise("Rows", "Kind of like a boat!");
        tempExercise2.addTag("Shoulders");
        tempExercise2.setThumbnailID("002.jpg");

        Exercise tempExercise3 = new Exercise("Glute Bridge", "Lift dat butt!");
        tempExercise3.addTag("Glutes");
        tempExercise3.addTag("legs");
        tempExercise3.setThumbnailID("003.jpg");

        Exercise tempExercise4 = new Exercise("Bicep Curls", "Working that iron");
        tempExercise4.addTag("Biceps");
        tempExercise4.setThumbnailID("004.jpg");

        JSONArray resultArray = new JSONArray();
        resultArray.put(tempExercise1.toJsonString());
        resultArray.put(tempExercise2.toJsonString());
        resultArray.put(tempExercise3.toJsonString());
        resultArray.put(tempExercise4.toJsonString());

        return resultArray;
    }
}