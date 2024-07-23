package com.example.workoutapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class ExerciseManagementActivity : AppCompatActivity(),
    UpdateExerciseFragment.UpdateExerciseDialogListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_management)

        UpdateExerciseFragment().show(supportFragmentManager, "GAME_DIALOG")

        // Getting DataStore to read/write an ExerciseList Preference
        val dataStoreSingleton = DataStoreSingleton.getInstance(this)
        val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)

        val exerciseListJson: String? = dataStoreHelper.getStringValue("ExerciseList");
        val exerciseList = if (exerciseListJson != null)
            JSONArray(exerciseListJson) else JSONArray()

        // Setting up Activity's list of exercises
        val displayList = ArrayList<Exercise>()
        for (i in 0 ..< exerciseList.length()) {
            val exercise = Exercise.fromJsonString(exerciseList[i].toString())
            displayList.add(exercise)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv_exerciseManagerList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ExerciseManagementAdapter(displayList)

        // Modify exercise list buttons
        var addExerciseButton = findViewById<View>(R.id.btn_addExercise) as Button
        addExerciseButton.setOnClickListener {
            val addedExercise = Exercise("test", "test")

            displayList.add(addedExercise)
            recyclerView.adapter!!.notifyItemInserted(displayList.size - 1)

            exerciseList.put(addedExercise.toJsonString())
            dataStoreHelper.setStringValue("ExerciseList", exerciseList.toString())
        }
    }

    // User tapped the positive button of the exercise fragment
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val exerciseName: EditText = dialog.requireDialog().findViewById(R.id.et_exerciseName)
        val exerciseDesc: EditText = dialog.requireDialog().findViewById(R.id.et_exerciseDescription)

        Log.d("MainTest", "${exerciseName.text} & ${exerciseDesc.text}")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

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