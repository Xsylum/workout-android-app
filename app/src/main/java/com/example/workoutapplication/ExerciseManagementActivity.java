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

        String[] tempDataset = {"Joe", "Mama", "Ain't", "Lindsay", "Myself", "I shall",
                                "Shall", "Decree", "that my mama", "Is Jane"};

        RecyclerView recyclerView = findViewById(R.id.rv_exerciseManagerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ExerciseManagementAdapter(tempDataset));
    }
}