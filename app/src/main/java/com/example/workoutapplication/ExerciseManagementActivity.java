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

        // TEMP DEBUG EXERCISES
        Exercise tempExercise1 = new Exercise("Deadlift", "Lifting bar real cool");
        tempExercise1.addTag("legs");
        tempExercise1.addTag("Biceps");
        tempExercise1.setThumbnailID("001.jpg");

        try
        {
            // Creating the JSONObject that will hold this exercise (will need array of these objects!
            JSONObject jsonObject = new JSONObject();

            // Putting exercise's variable values
            jsonObject.put("Name", tempExercise1.getName());
            jsonObject.put("Description", tempExercise1.getDescription());
            jsonObject.put("ThumbnailID", tempExercise1.getThumbnailID());

            // JSONArray to hold exercise's tags
            JSONArray array = new JSONArray(tempExercise1.getTags());
            jsonObject.put("Tags", array);

            // String form of the json for this exercise;
            String exerciseJsonString = jsonObject.toString();
            //Log.d("Json", exerciseJsonString);

            //TODO: add constructor to Exercise which uses the below JSONObject (built from a JSON string)
            //JSONObject newObject = new JSONObject(jsonObject.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }


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