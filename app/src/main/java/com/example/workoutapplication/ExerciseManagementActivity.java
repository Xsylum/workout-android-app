package com.example.workoutapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ExerciseManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_management);

        // TEMP DEBUG EXERCISES
        Exercise tempExercise1 = new Exercise("Deadlift", "Lifting bar real cool");
        Exercise tempExercise2 = new Exercise("Rows", "Kind of like a boat!");
        Exercise tempExercise3 = new Exercise("Glute Bridge", "Lift dat butt!");
        Exercise tempExercise4 = new Exercise("Bicep Curls", "Working that iron");

        Exercise[] tempDataset = {tempExercise1, tempExercise2, tempExercise3, tempExercise4};

        RecyclerView recyclerView = findViewById(R.id.rv_exerciseManagerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ExerciseManagementAdapter(tempDataset));
    }
}