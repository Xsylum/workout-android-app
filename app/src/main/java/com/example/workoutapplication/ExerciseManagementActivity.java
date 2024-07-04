package com.example.workoutapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class ExerciseManagementActivity extends AppCompatActivity {

    private Button addExerciseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_management);

        // TEMP DEBUG EXERCISES
        Exercise tempExercise1 = new Exercise("Deadlift", "Lifting bar real cool");
        Exercise tempExercise2 = new Exercise("Rows", "Kind of like a boat!");
        Exercise tempExercise3 = new Exercise("Glute Bridge", "Lift dat butt!");
        Exercise tempExercise4 = new Exercise("Bicep Curls", "Working that iron");

        ArrayList<Exercise> tempDataset = new ArrayList();
        tempDataset.add(tempExercise1);
        tempDataset.add(tempExercise2);
        tempDataset.add(tempExercise3);
        tempDataset.add(tempExercise4);

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