package com.example.workoutapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private var nextPageButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var dataStoreRx: DataStore<Preferences>

        // SEE DataStoreSingleton.java for credits and more information
        val dataStoreSingleton = DataStoreSingleton.getInstance(this)
        val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)

        // EXERCISE JSON ARRAY
        val exerciseList = DEBUG_ExerciseList()
        val exerciseListString = exerciseList.toString()

        dataStoreHelper.setStringValue("ExerciseList", exerciseListString);
        Log.d("MainTest", dataStoreHelper.getStringValue("ExerciseList"))


        nextPageButton = findViewById<View>(R.id.nextPageButton) as Button
        nextPageButton!!.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    ExerciseManagementActivity::class.java
                )
            )
        }
    }

    private fun DEBUG_ExerciseList(): JSONArray {
        // TEMP DEBUG EXERCISES
        val tempExercise1 = Exercise("Deadlift", "Lifting bar real cool").apply {
            addTag("legs")
            addTag("Biceps")
            thumbnailID = "001.jpg"
        }
        val tempExercise2 = Exercise("Rows", "Kind of like a boat!").apply {
            addTag("Shoulders")
            thumbnailID = "002.jpg"
        }
        val tempExercise3 = Exercise("Glute Bridge", "Lift dat butt!").apply {
            addTag("Glutes")
            addTag("legs")
            thumbnailID = "003.jpg"
        }
        val tempExercise4 = Exercise("Bicep Curls", "Working that iron").apply {
            addTag("Biceps")
            thumbnailID = "004.jpg"
        }
        val resultArray = JSONArray().apply {
            put(tempExercise1.toJsonString())
            put(tempExercise2.toJsonString())
            put(tempExercise3.toJsonString())
            put(tempExercise4.toJsonString())
        }

        return resultArray
    }
}