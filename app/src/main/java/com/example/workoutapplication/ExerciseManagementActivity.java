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

        ArrayList<Exercise> tempDataset = new ArrayList<Exercise>();
        /**
        tempDataset.add(Exercise.fromJsonString(exerciseList.get(0).toString()));
        tempDataset.add(Exercise.fromJsonString(exerciseList.get(1).toString()));
        tempDataset.add(Exercise.fromJsonString(exerciseList.get(2).toString()));
        tempDataset.add(Exercise.fromJsonString(exerciseList.get(3).toString()));
         */


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
}