package com.example.workoutapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.RegimenDataStore
import org.json.JSONArray
import java.util.LinkedList

class RegimenDesignActivity : AppCompatActivity(),
    ExerciseManagementAdapter.ExerciseRecyclerViewListener{

    // The list of data that is displayed by recyclerView
    private var displayList = ArrayList<Exercise>()
    private lateinit var recyclerView: RecyclerView

    // Info regimen currently being modified
    private lateinit var regimen: Regimen
    private var dataStorePosition: Int = -1 // position where regimen is stored in RegimenList

    // DataStore variables
    private val dataStoreSingleton = DataStoreSingleton.getInstance(this)
    private val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regimen_design)

        /** Regimen data setup **/
        dataStorePosition = intent.getIntExtra("RegimenPosition", dataStorePosition)
        val regimenJson = intent.getStringExtra("TargetRegimen")
        val regimenDataStore = RegimenDataStore.fromJsonString(regimenJson)

        // Get list of exercises to set regimen's exercise list via
        // regimenDataStore's list of exerciseIDs
        val exerciseList = getDataStoreExerciseList();

        // Finally, the regimen being designed can be initialized
        regimen = Regimen(regimenDataStore, exerciseList)
        displayList = regimen.exerciseList

        // TODO Check that position != -1 [Try to also JUNIT test this]
        /** /Regimen data setup **/

        // Setting up the recyclerView to display exercises in this regimen
        recyclerView = findViewById(R.id.rv_regimenExerciseList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Reusing ExerciseManagementAdapter for simplicity
        recyclerView.adapter = ExerciseManagementAdapter(displayList, this)

        val addExerciseButton = findViewById<Button>(R.id.btn_regimenAddExercise)
        addExerciseButton.setOnClickListener() {
            Log.d("myTest", "User Clicked add exercise!")
        }

        showAddExerciseDialog(exerciseList)
    }

    override fun onListItemClick(position: Int) {
        TODO("Not yet implemented")
    }

    private fun getDataStoreExerciseList(): ArrayList<Exercise> {
        val exerciseListJsonString = dataStoreHelper.getStringValue("ExerciseList")
        val exerciseJsonArray = if (exerciseListJsonString != null) {
            JSONArray(exerciseListJsonString)
        } else JSONArray()

        // Creating LinkedList<Exercise> from DataStore's JSONArray
        val exerciseList = ArrayList<Exercise>()
        for (i in 0 until exerciseJsonArray.length()) {
            val exerciseJsonString = exerciseJsonArray.get(i).toString()
            exerciseList.add(Exercise.fromJsonString(exerciseJsonString))
        }

        return exerciseList
    }

    private fun showAddExerciseDialog(userExercises: ArrayList<Exercise>) {
        // TODO maybe change this to a Bottom Sheet (pull up screen)
        val fragment = RegimenDesignAddExercisesFragment(userExercises)
        fragment.show(supportFragmentManager, "REGIMEN_ADD_EXERCISE_DIALOG")
    }
}