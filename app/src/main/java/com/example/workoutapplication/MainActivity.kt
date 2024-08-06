package com.example.workoutapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private lateinit var exerciseManagementButton: Button
    private lateinit var regimenManagementButton: Button
    private lateinit var workoutLogManagementButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // See DataStoreSingleton.java for credits and more information
        val dataStoreSingleton = DataStoreSingleton.getInstance(this)
        val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)

        exerciseManagementButton = findViewById(R.id.btn_exerciseManagement)
        exerciseManagementButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    ExerciseManagementActivity::class.java
                )
            )
        }

        regimenManagementButton = findViewById(R.id.btn_regimenManagement)
        regimenManagementButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    RegimenManagementActivity::class.java
                )
            )
        }

        workoutLogManagementButton = findViewById(R.id.btn_workoutManagementDebug)
        workoutLogManagementButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    WorkoutLogActivity::class.java
                )
            )
        }
    }
}