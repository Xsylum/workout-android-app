package com.example.workoutapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExerciseManagementActivity extends AppCompatActivity {

    private Button addExerciseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_management);

        /**
        try {
            FileOutputStream outputStream = getApplicationContext().openFileOutput("Exercises", Context.MODE_PRIVATE);
            Exercise tempExercise1 = new Exercise("Deadlift", "Lifting bar real cool");
            outputStream.write(tempExercise1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
         */ //TODO: probably just use an xml file to save via SharedPreferences?

        JSONArray exerciseList = DEBUG_ExerciseList();

        ArrayList<Exercise> tempDataset = new ArrayList<Exercise>();
        try {
            tempDataset.add(Exercise.fromJsonString(exerciseList.get(0).toString()));
            tempDataset.add(Exercise.fromJsonString(exerciseList.get(1).toString()));
            tempDataset.add(Exercise.fromJsonString(exerciseList.get(2).toString()));
            tempDataset.add(Exercise.fromJsonString(exerciseList.get(3).toString()));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        RecyclerView recyclerView = findViewById(R.id.rv_exerciseManagerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ExerciseManagementAdapter(tempDataset));

        // Modify exercise list buttons
        addExerciseButton = (Button) findViewById(R.id.btn_addExercise);

        addExerciseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tempDataset.add(new Exercise("test", "test"));
                recyclerView.getAdapter().notifyDataSetChanged();
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